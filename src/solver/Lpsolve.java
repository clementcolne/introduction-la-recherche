package solver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Lpsolve extends AbstractSolver {

    /**
     * Constructeur de solveur de programme linéaire pour Lp Solve
     * @param filePath chemin du fichier
     * @param options  options du solveur
     */
    public Lpsolve(String filePath, String options) {
        super(filePath, options);
        extension = ".lp";
        this.solver = getClass().getClassLoader().getResource("programmes/lp_solve.exe").getPath();
        solverFile = "output"+File.separatorChar+"user_solution.lp";
    }

    /**
     * Méthode permettant de créer un fichier lp depuis un fichier texte
     */
    public void createSolverFile() {
        try {
            FileWriter myWriter = new FileWriter(solverFile);
            File file = new File(filePath);
            Scanner myReader = new Scanner(file);
            int cpt = 1;

            // On créé un fichier lp grâce aux informations du fichier texte
            while(myReader.hasNext()){
                String data = myReader.nextLine();
                // On regarde si on minimise ou maximis ela fonction de coût
                if (data.equals("min")){
                    myWriter.write("min: ");
                }else if (data.equals("max")){
                    myWriter.write("max: ");
                }

                // On regarde si la ligne correspond à la fonction
                if (data.equals("// fonction")){
                    data = myReader.nextLine();
                    String[] dataTab = data.split(" ");
                    nbVariables = dataTab.length;
                    int i = 0;

                    // On écrit la fonction
                    for (String s : dataTab) {
                        myWriter.write("x"+(i+1));
                        if (i < dataTab.length-1){
                            myWriter.write(" + ");
                        }
                        i++;
                    }
                    myWriter.write(";\n");
                    for (i = 0; i < nbVariables; i++){
                        myWriter.write("c"+cpt+": "+"x"+cpt+" = "+dataTab[i]+";\n");
                        cpt++;
                    }
                }



                // On vérifie les contraintes
                if (data.matches("\\/\\/ c[0-9]*.*")){
                    myWriter.write("c"+cpt+": ");

                    String inequality = choseInequality(data);

                    data = myReader.nextLine();
                    String[] dataTab = data.split(" ");
                    int i = 0;
                    // On écrit la fonction
                    for (String s : dataTab) {
                        myWriter.write(s);
                        if (i < dataTab.length-2){
                            myWriter.write(" + ");
                        }else if (i < dataTab.length-1){
                            myWriter.write(" "+inequality+" ");
                        }
                        i++;
                    }
                    cpt++;
                    myWriter.write(";\n");
                }
            }

            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Méthode permettant de récupérer les valeurs optimales et réalisables du programme linéaire
     */
    public void parseOutput(){
        StringBuilder stringBuilder = new StringBuilder();
        nouvelleFctCout = new float[nbVariables];
        int cpt = 1;
        StringBuilder function = new StringBuilder();
        String optimisation = "";
        boolean infeasible = false, unbounded = false, right = false;
        // On sépare les lignes du fichier en tableau de String
        String[] lpOutput = output.split("\n");

        for (String s : lpOutput) {
            // Le problème n'est pas faisable (la solution n'est pas réalisable ou MRU est incohérent)
            if (s.matches(".*infeasible.*")) {
                setStatutInfeasible();
                infeasible = true;
            } else if (s.matches(".*unbounded.*")) { // On vérifie si le problème est borné
                setStatutUnbounded();
                unbounded = true;
            } else if (s.matches(".*objective.*")) {
                setStatutRight();
                // On sépare les mots de la ligne
                String[] objectiveLine = s.split(" ");
                //On récupère la solution optimale du problème
                valOptimal = Float.parseFloat(objectiveLine[objectiveLine.length-1]);
                right = true;
            } else if (s.matches("^y.*")) {
                setStatutRight();
                String[] objectiveFunction = s.split(" ");
                nouvelleFctCout[cpt] = Float.parseFloat(objectiveFunction[objectiveFunction.length-1]);
                right = true;
            } else { // La solution convient
                setStatutRight();
                right = true;
            }
        }
        if (infeasible) {
            System.out.println("This problem is infeasible");
            System.out.println(("\nTrying to optimize the solution :\n"));

            // On écrit un nouveau fichier lp pour retrouver une fonction de coût optimisée
            try {
                FileWriter myWriter = new FileWriter("./" + getNewSolverFile());
                File file = new File(solverFile);
                Scanner myReader = new Scanner(file);

                // On réécrit la fonction de coût
                myWriter.write(myReader.nextLine());
                myWriter.write("\n");
                cpt = 0;
                // On termine en écrivant les contraintes
                while (myReader.hasNextLine()) {
                    // On passe les contrainte fixant les valeurs des variables
                    if (cpt < nbVariables) {
                        cpt++;
                        myReader.nextLine();
                    } else { // On réécrit le reste du fichier
                        myWriter.write(myReader.nextLine());
                        myWriter.write("\n");
                    }
                }
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            retryLpFile();
        }
        else if (unbounded) { // On vérifie si le problème est borné
            System.out.println("The problem is unbounded");
        }
        else if (right){ // La solution convient
            System.out.println("The solution is in MRU");
        }
    }

    /**
     * Méthode qui exécute de nouveau fichier lp avec le solveur
     */
    public void retryLpFile() {
        solverFile = getNewSolverFile();
        try {
            run();
            display();
            analyzeLpFile();
            solverFile = "output"+File.separatorChar+"user_solution.lp";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode d'analyse du nouveau fichier lp, afin de savoir si le problème d'infaisabilité vient
     * de la fonction de coût ou de la cohérence de MRU
     */
    public void analyzeLpFile(){
        String[] lpOutput = output.split("\n");
        boolean infeasible = false, unbounded = false, right = false;
        for (String s1 : lpOutput) {
            // Le problème n'est pas faisable (MRU est incohérent)
            if (s1.matches(".*infeasible.*")) {
                infeasible = true;
            }else if (s1.matches(".*unbounded.*")) { // On vérifie si le problème est borné
                unbounded = true;
            }else { // La nouvelle solution convient
                right = true;
            }
        }


        if (infeasible){
            System.out.println("MRU incohérent");
        }else if (unbounded){
            System.out.println("Le problème n'est pas borné");
        }else if (right){
            findShortestDistance();
            System.out.println(output);
        }
    }

    /**
     * Méthode permettant de retrouver la plus petite distance entre une fonction de coût et MRU
     */
    private void findShortestDistance() {
        try {
            FileWriter myWriter = new FileWriter("./" + getNewSolverFile());
            File file = new File(filePath);
            Scanner myReader = new Scanner(file);

            if (myReader.hasNextLine()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(myReader.nextLine());
                stringBuilder.append(": ");
                myReader.nextLine();
                String[] dataTab = myReader.nextLine().split(" ");

                // On écrit les contraintes de sorte que les zi soient supérieurs ou égaux à la valeur absolue de yi - xi
                for (int i = 0; i < nbVariables; i++) {
                    stringBuilder.append("z");
                    stringBuilder.append((i + 1));
                    if (i < nbVariables - 1) {
                        stringBuilder.append(" + ");
                    } else {
                        stringBuilder.append(";");
                    }
                }
                stringBuilder.append("\n");
                int cpt = 1;
                for (int i = 0; i < nbVariables; i++) {
                    boolean firstUsed = true;
                    computeAbsoluteValue(stringBuilder, cpt, dataTab, i, firstUsed);
                    firstUsed = false;
                    cpt++;
                    computeAbsoluteValue(stringBuilder, cpt, dataTab, i, firstUsed);
                    cpt++;
                }

                // On réécrit les contraintes de façon à ce que le calcul de la plus petite distance prenne en compte
                // les contraintes de MRU
                while (myReader.hasNext()) {
                    String data = myReader.nextLine();
                    // On vérifie les contraintes
                    if (data.matches("\\/\\/ c[0-9]*.*")) {
                        stringBuilder.append("c" + cpt + ": ");
                        String inequality = choseInequality(data);
                        data = myReader.nextLine();
                        dataTab = data.split(" ");
                        int i = 0;
                        // On écrit la fonction
                        for (String s : dataTab) {
                            if (s.matches("-?[0-9]*(\\.[0-9]*)?x[0-9]*")) {
                                s = s.replace("x", "y");
                            }
                            stringBuilder.append(s);
                            if (i < dataTab.length - 2) {
                                stringBuilder.append(" + ");
                            } else if (i < dataTab.length - 1) {
                                stringBuilder.append(" " + inequality + " ");
                            }
                            i++;
                        }
                        cpt++;
                        stringBuilder.append(";\n");
                    }
                }
                System.out.println(stringBuilder);
                myWriter.write(stringBuilder.toString());
                myWriter.close();
                run();
                display();
                parseOutput();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Écrit les contraintes permettant d'avoir la relation zi >= | xi - yi |
     * @param stringBuilder le String à écrire dans le nouveau fichier lp
     * @param cpt le compteur de contraintes
     * @param dataTab le tableau contenant les valeurs des variables
     * @param i l'index de la boucle permettant d'accéder aux variables dans dataTab
     * @param firstUsed le booléen permettant de savoir quelle contrainte écrire pour avoir la valeur absolue
     */
    private void computeAbsoluteValue(StringBuilder stringBuilder, int cpt, String[] dataTab, int i, boolean firstUsed) {
        stringBuilder.append("c");
        stringBuilder.append(cpt);
        stringBuilder.append(": z");
        stringBuilder.append((i+1));
        if (firstUsed){
            stringBuilder.append(" >= y");
            stringBuilder.append((i+1));
            stringBuilder.append(" - ");
            stringBuilder.append(dataTab[i]);
            stringBuilder.append(";\n");
        }else{
            stringBuilder.append(" >= ");
            stringBuilder.append(dataTab[i]);
            stringBuilder.append(" - y");
            stringBuilder.append((i+1));
            stringBuilder.append(";\n");
        }
    }

    /**
     * Méthode permettant de choisir quelle inégalité doit être écrite dans les contraintes en fonction du fichier texte
     * @param data le String indiquant le type d'inégalité à écrire
     * @return l'inégalité à écrire
     */
    private String choseInequality(String data){
        String inequality = "";
        if (data.matches(".*inf")) {
            inequality = "+ "+epsilon+" <=";
        }
        if (data.matches(".*sup")) {
            inequality = ">= "+epsilon+" +";
        }
        if (data.matches(".*eq")) {
            inequality = "=";
        }
        return inequality;
    }
}
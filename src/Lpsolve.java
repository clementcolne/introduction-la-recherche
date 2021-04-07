import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lpsolve extends AbstractSolver {

    /**
     * Constructeur de solveur de programme linéaire pour Lp Solve
     * @param filePath chemin du fichier
     * @param options  options du solveur
     */
    public Lpsolve(String filePath, String options, String solver) {
        super(filePath, options, solver);
    }

    /**
     * Méthode permettant de créer un fichier lp depuis un fichier texte
     */
    public void createLpFile() {
        try {
            lpFile = "./test.lp";
            FileWriter myWriter = new FileWriter(lpFile);
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
                    String inequality = "";
                    if (data.matches(".*inf")) {
                        inequality = "<=";
                    }
                    if (data.matches(".*sup")) {
                        inequality = ">=";
                    }
                    if (data.matches(".*eq")) {
                        inequality = "=";
                    }
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
        int start = 0;
        int end = 0;
        StringBuilder function = new StringBuilder();
        String optimisation = "";
        boolean infeasible = false, unbounded = false, right = false;
        // On sépare les lignes du fichier en tableau de String
        String[] lpOutput = output.split("\n");

        for (String s : lpOutput) {
            // Le problème n'est pas faisable (la solution n'est pas réalisable ou MRU est incohérent)
            if (s.matches(".*infeasible.*")) {
                infeasible = true;
            } else if (s.matches(".*unbounded.*")) { // On vérifie si le problème est borné
                unbounded = true;
            } else { // La solution convient
                right = true;
            }
        }
        if (infeasible) {
            System.out.println("This problem is infeasible");
            System.out.println(("\nTrying to optimize the solution :\n"));


            // On écrit un nouveau fichier lp pour retrouver une fonction de coût optimisée
            try {
                FileWriter myWriter = new FileWriter("./" + newLpFile);
                File file = new File(lpFile);
                Scanner myReader = new Scanner(file);

                // On réécrit la fonction de coût
                myWriter.write(myReader.nextLine());
                myWriter.write("\n");
                int cpt = 0;
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
        if (unbounded) { // On vérifie si le problème est borné
            System.out.println("The problem is unbounded");
        }
        if (right){ // La solution convient
            System.out.println("The solution is in MRU");
        }
    }

    /**
     * Méthode qui exécute de nouveau fichier lp avec le solveur
     */
    public void retryLpFile() {
        lpFile = newLpFile;
        try {
            run();
            display();
            analyzeLpFile();
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
        for (String s1 : lpOutput) {
            // Le problème n'est pas faisable (MRU est incohérent)
            if (s1.matches(".*infeasible.*")) {
                System.out.println("MRU incohérent");
            }else if (s1.matches(".*unbounded.*")) { // On vérifie si le problème est borné
                System.out.println("Le problème n'est pas borné");
            }else { // La nouvelle solution convient
                System.out.println(s1);
                System.out.println("\n\n");
            }

        }
    }
}

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
     * Méthode permettant de récupérer les valeurs optimales et réalisables du programme linéaire
     */
    public void parseOutput(){
        StringBuilder stringBuilder = new StringBuilder();
        int start = 0;
        int end = 0;
        StringBuilder function = new StringBuilder();
        String optimisation = "";

        // On sépare les lignes du fichier en tableau de String
        String[] lpOutput = output.split("\n");
        for (String s1 : lpOutput){
            // Le problème n'est pas faisable (la solution n'est pas réalisable ou MRU est incohérent)
            if (s1.matches(".*infeasible.*")) {
                stringBuilder.append(s1);
                stringBuilder.append("\nTrying to optimize the solution\n");

                // On lit le fichier lp pour récupérer les valeurs fournies
                try {
                    File file = new File(lpFile);
                    Scanner myReader = new Scanner(file);
                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();
                        // On ne travaille sur la fonction de coût afin de récupérer les variables

                        // On regarde ici si l'optimisation est une recherche de minimum
                        if (data.matches("min.*")) {
                            optimisation = "min";
                            // On sépare le type d'optimisation de la fonction
                            String[] lineOutput = data.split("[ ]*:[ ]*");

                            // On sépare les différentes valeur dans la fonction
                            String[] variables = lineOutput[1].split("[ ]*\\+[ ]*");
                            function.append(optimisation);
                            function.append(": ");
                            int cpt = 0;

                            // On reconstruit la fonction de cout en remlaçant les valeurs par des variables
                            for (String s2 : variables) {
                                if (s2.matches("[0-9]*x[1-9]*")){
                                    start++;
                                    function.append(s2);
                                }
                                if (s2.matches("[0-9]*[;]?")){
                                    end++;
                                    function.append("x");
                                    function.append(start+end);
                                }
                                if (cpt < variables.length-1) {
                                    function.append(" + ");
                                }
                                cpt++;
                            }
                            function.append(";");
                            stringBuilder.append(data);
                        }


                        // On regarde ici si l'optimisation est une recherche de maximum
                        if (data.matches("max.*")) {
                            optimisation = "max";
                            // On sépare le type d'optimisation de la fonction
                            String[] lineOutput = data.split("[ ]*:[ ]*");

                            // On sépare les différentes valeur dans la fonction
                            String[] variables = lineOutput[1].split("[ ]*\\+[ ]*");
                            function.append(optimisation);
                            function.append(": ");
                            int cpt = 0;

                            // On reconstruit la fonction de cout en remplaçant les valeurs par des variables
                            for (String s2 : variables) {
                                System.out.println(s2);
                                if (s2.matches("[0-9]*x[1-9]*")){
                                    start++;
                                    function.append(s2);
                                }
                                if (s2.matches("[0-9]*[;]?")){
                                    end++;
                                    function.append("x");
                                    function.append(start+end);
                                }
                                if (cpt < variables.length-1) {
                                    function.append(" + ");
                                }
                                cpt++;
                            }
                            function.append(";");
                            stringBuilder.append(data);
                        }

                    }
                    myReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }

                // On écrit un nouveau fichier lp pour retrouver une fonction de coût optimisée
                try {
                    FileWriter myWriter = new FileWriter("./"+newLpFile);
                    File file = new File(lpFile);
                    Scanner myReader = new Scanner(file);

                    // On remplace dans le nouveau fichier la fonction de coût composée de valeur,
                    // par la nouvelle fonction de coût composée de variables
                    myReader.nextLine();
                    myWriter.write(function.toString());
                    myWriter.write("\n");

                    // On termine en écrivant les contraintes
                    while (myReader.hasNextLine()) {
                        myWriter.write(myReader.nextLine());
                        myWriter.write("\n");
                    }
                    myWriter.close();
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }

                retryLpFile();

            }else if (s1.matches(".*unbounded.*")) { // On vérifie si le problème est borné
                stringBuilder.append(s1);
            }else { // La solution convient
                //stringBuilder.append("La solution fournie se trouve bien dans MRU");
            }
        }
        System.out.println(stringBuilder.toString());
    }

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

    public void analyzeLpFile(){
        String[] lpOutput = output.split("\n");
        for (String s1 : lpOutput) {
            // Le problème n'est pas faisable (MRU est incohérent)
            if (s1.matches(".*infeasible.*")) {
                System.out.println("MRU incohérent");
            }else if (s1.matches(".*unbounded.*")) { // On vérifie si le problème est borné
                System.out.println("Le problème n'est pas borné");
            }else { // La solution convient
                System.out.println("La nouvelle solution est optimisée");
                System.out.println(s1);
            }

        }
    }
}

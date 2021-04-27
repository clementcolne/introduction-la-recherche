package solver;

import java.io.*;
import java.util.Scanner;

public abstract class AbstractSolver {

    protected final String filePath;
    protected String lpFile;
    protected final String newLpFile;
    protected final String options;
    protected final String solver;
    private Process proc;
    protected String output;
    protected int nbVariables;

    /**
     * Constructeur général de solveur de programme linéaire
     * @param filePath chemin du fichier
     * @param options options du solveur
     */
    public AbstractSolver(String filePath, String options, String solver) {
        this.filePath = filePath;
        this.options = options;
        this.solver = solver;
        this.lpFile = "";
        this.newLpFile = "new_lp_file.lp";
        this.nbVariables = 0;
    }

    /**
     * Lance l'exécutable du solveur pour un fichier et des options spécifiques
     * @throws IOException IOException si le fichier n'est pas trouvé
     */
    public void run() throws IOException {
        Runtime rt = Runtime.getRuntime();
        // command pour exécuter lpsolve
        String[] commands = {solver, options, lpFile};
        // exécution de la commande
        proc = rt.exec(commands);
    }

    /**
     * Permet l'affichage la sortie standard du solveur
     * @throws IOException IOException si le fichier n'est pas trouvé
     */
    public void display() throws IOException {

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        // lecture de la sortie de la commande
        String s;
        this.output = "";
        while ((s = stdInput.readLine()) != null) {
            output += s;
            output += "\n";
        }

        if((s = stdError.readLine()) != null) {
            // lecture de toutes les erreurs relevées par la commande
            System.out.println("Standard error :\n" + s);
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        }
    }

    /**
     * Méthode permettant de récupérer les valeurs optimales et réalisables du programme linéaire
     */
    public abstract void parseOutput();

    /**
     * Méthode permettant de créer un fichier lp depuis un fichier texte
     */
    public abstract void createLpFile();

    public String getFilePath() {
        return filePath;
    }

    public String getLpFile() {
        return lpFile;
    }

    public String getNewLpFile() {
        return newLpFile;
    }

    public String getOptions() {
        return options;
    }

    public String getSolver() {
        return solver;
    }

    public String getOutput() {
        return output;
    }

    public int getNbVariables() {
        return nbVariables;
    }
}

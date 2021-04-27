package solver;

import java.io.*;

public abstract class AbstractSolver {

    protected final String filePath;
    protected String solverFile;
    protected final String newSolverFile;
    protected final String options;
    protected final String solver;
    private Process proc;
    protected String output;
    protected int nbVariables;
    protected String extension;

    /**
     * Constructeur général de solveur de programme linéaire
     * @param filePath chemin du fichier
     * @param options options du solveur
     */
    public AbstractSolver(String filePath, String options, String solver) {
        this.filePath = filePath;
        this.options = options;
        this.solver = getClass().getClassLoader().getResource("programmes/"+solver+".exe").getPath();
        this.solverFile = "";
        this.newSolverFile = "output"+File.separatorChar+"new_solver_file";
        this.nbVariables = 0;
    }

    /**
     * Lance l'exécutable du solveur pour un fichier et des options spécifiques
     * @throws IOException IOException si le fichier n'est pas trouvé
     */
    public void run() {
        Runtime rt = Runtime.getRuntime();
        // command pour exécuter lpsolve
        String[] commands = {solver, options, solverFile};
        // exécution de la commande
        try {
            proc = rt.exec(commands);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public abstract void createSolverFile();

    public String getFilePath() {
        return filePath;
    }

    public String getSolverFile() {
        return solverFile;
    }

    public String getNewSolverFile() {
        return newSolverFile+extension;
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

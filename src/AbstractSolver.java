import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class AbstractSolver {

    protected final String filePath;
    protected final String options;
    protected final String solver;
    private Process proc;
    protected String output;

    /**
     * Constructeur général de solveur de programme linéaire
     * @param filePath chemin du fichier
    // * @param options options du solveur
     */
    public AbstractSolver(String filePath, String options, String solver) {
        this.filePath = filePath;
        this.options = options;
        this.output = "";
        this.solver = solver;
    }

    /**
     * Runs solver for the specified file and options
     * @throws IOException IOException if file not found
     */
    public void run() throws IOException {
        Runtime rt = Runtime.getRuntime();
        // command to run lp solve
        String[] commands = {solver, options, filePath};
        // run command
        proc = rt.exec(commands);
    }

    /**
     * Displays the output of the solve
     * @throws IOException IOException if file not found
     */
    public void display() throws IOException {
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        // read the output from the command
        String s;
        while ((s = stdInput.readLine()) != null) {
            //System.out.println(s);
            output += s;
            output += "\n";
        }

        if((s = stdError.readLine()) != null) {
            // read any errors from the attempted command
            System.out.println("Standard error :\n" + s);
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        }
    }

    /**
     * Méthode permettant de calculer la distance de Manhattan entre deux points donnés
     * @return la distance de Manhattan entre deux points donnés
     */
    public int computeManhattanDistance(){
        return 0;
    }

    /**
     * Méthode permettant de récupérer les valeurs optimales et réalisables du programme linéaire
     */
    public abstract void parseOutput();

}

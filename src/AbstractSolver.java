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

    /**
     * Constructeur général de solveur de programme linéaire
     * @param filePath chemin du fichier
    // * @param options options du solveur
     */
    public AbstractSolver(String filePath, String options, String solver) {
        this.filePath = filePath;
        this.options = options;
        this.solver = solver;
        this.lpFile = "";
        this.newLpFile = "new_lp_file.lp";
    }

    /**
     * Runs solver for the specified file and options
     * @throws IOException IOException if file not found
     */
    public void run() throws IOException {
        Runtime rt = Runtime.getRuntime();
        // command to run lp solve
        String[] commands = {solver, options, lpFile};
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
        this.output = "";
        while ((s = stdInput.readLine()) != null) {
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

    public void createLpFile(){
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
                    int i = 0;
                    // On écrit la fonction
                    for (String s : dataTab) {
                        myWriter.write(s);
                        if (i < dataTab.length-1){
                            myWriter.write(" + ");
                        }
                        i++;
                    }
                    myWriter.write(";\n");
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

    public abstract void retryLpFile();
}

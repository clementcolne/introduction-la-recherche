import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if(args.length == 0 || args.length == 1) {
            System.err.println("Error missing argument : Please, specify the file path and solver executable\n[solver] <file path> [solver options]");
            System.exit(0);
        }
        // get solver executable
        String solveur = args[0];
        // get file path
        String filePath = "./" + args[1];
        // default value if no option specified
        String options = "";
        if(args.length == 3) {
            options = args[2];
        }

        AbstractSolver solver = null;

        switch (solveur) {
            // Lp Solve solver on mac
            case "lp_solve":
                solver = new Lpsolve(filePath, options, solveur);
                break;
            default:
                System.err.println("Error unknown solver : Please");
                System.exit(0);
        }

        // execute solve
        try {
            // run lp solve
            solver.createLpFile();
            solver.run();
            solver.display();
            solver.parseOutput();
        } catch (IOException e) {
            System.err.println("Error file not found : The file " + filePath + " was not found.");
        }
    }

}

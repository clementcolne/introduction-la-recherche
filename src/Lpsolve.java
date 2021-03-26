import java.util.ArrayList;
import java.util.Arrays;
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
        int[] maxX = new int[2];
        int[] minX = new int[2];
        int i = 0;
        int j = 0;
        String[] s = output.split(" ");
        for (String s1 : s){
            if (s1.matches(".*infeasible.*")) {
                stringBuilder.append(s1);
            }else {
                // On cherche la ligne correspondant aux valeurs optmales réalisable des variable dans le résultat du programme linéaire
                if (s1.matches("x[0-9][ ]*[0-9]")) {
                    // On récupère la valeur optimale réalisable de la variable
                    Pattern pattern = Pattern.compile(" [0-9]");
                    Matcher matcher = pattern.matcher(s1);
                    if (matcher.find()) {
                        maxX[i] = Integer.parseInt(matcher.group(0).split(" ")[1]);
                        stringBuilder.append("x");
                        stringBuilder.append((i + 1));
                        stringBuilder.append(" = ");
                        stringBuilder.append(maxX[i]);
                        stringBuilder.append("\n");
                        i++;
                    }
                }
            }
        }
        System.out.println(stringBuilder.toString());
    }

}

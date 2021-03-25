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

    public void parseOutput(){
        StringBuilder stringBuilder = new StringBuilder();
        int[] maxX = new int[2];
        int[] minX = new int[2];
        int i = 0;
        int j = 0;
        String[] s = output.split("\n");
        for (String s1 : s){
            if (s1.matches(".*infeasible.*")) {
                stringBuilder.append(s1);
            }else {
                if (s1.matches("x[0-9][ ]*[0-9]")) {
                    Pattern pattern = Pattern.compile(" [0-9]");
                    Matcher matcher = pattern.matcher(s1);
                    if (matcher.find()) {
                        maxX[i] = Integer.parseInt(matcher.group(0).split(" ")[1]);
                        stringBuilder.append("x");
                        stringBuilder.append((i+1));
                        stringBuilder.append(" = ");
                        stringBuilder.append(maxX[i]);
                        stringBuilder.append("\n");
                        i++;
                    }
                }
                /*if (s1.matches("[c][0-9][ ]*[0-9][ ]*[0-9][ ]*.*")) {
                    Pattern pattern = Pattern.compile("[ ]*[0-9][ ]*[0-9][ ]*");
                    Matcher matcher = pattern.matcher(s1);
                    if (matcher.find()) {
                        System.out.println(matcher.group());
                        /*String[] strings = matcher.group(0).split(" ");
                        for (String s2 : strings){
                            System.out.println(s2);
                        }*/
                        //minX[j] = Integer.parseInt(matcher.group(0).split(" ")[1]);
                        /*stringBuilder.append("x");
                        stringBuilder.append((j+1));
                        stringBuilder.append(" = ");
                        stringBuilder.append(minX[j]);
                        stringBuilder.append("\n");
                        System.out.println(minX[j]);
                        j++;
                    }
                }*/
            }
        }
        System.out.println(stringBuilder.toString());
    }

}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        List<String> histo = new ArrayList<>();
        List<Integer> maxX = new ArrayList<>();
        List<Integer> minX = new ArrayList<>();
        int i = 0;
        int j = 0;
        // On sépare les lignes du fichier en tableau de String
        String[] s = output.split("\n");
        for (String s1 : s){
            if (s1.matches(".*infeasible.*") || s1.matches(".*unbounded.*")) {
                stringBuilder.append(s1);
            }else {
                // On sépare les lignes du tableau en tableau de mots
                String[] ss = s1.split(" ");
                for (String s2 : ss) {
                    // On vérifie qu'on regarde bien une variable, et que c'est la première fois qu'on la voit, afin d'être sur que c'est la valeur max
                    if (s2.matches("x[0-9]") && !histo.contains(s2)) {
                        // On récupère la valeur optimale réalisable de la variable
                        Pattern pattern = Pattern.compile(" [0-9]");
                        Matcher matcher = pattern.matcher(s1);
                        if (matcher.find()) {
                            int number = Integer.parseInt(matcher.group(0).split(" ")[1]);
                            maxX.add(number);
                            stringBuilder.append("x");
                            stringBuilder.append((i + 1));
                            stringBuilder.append(" = ");
                            stringBuilder.append(maxX.get(i));
                            stringBuilder.append("\n");
                            i++;
                            histo.add(s2);
                        }
                    }
                }
            }
            /*System.out.println(s1);
            if (s1.matches(".*infeasible.*")) {
                stringBuilder.append(s1);
            }else {
                // On cherche la ligne correspondant aux valeurs optmales réalisable des variable dans le résultat du programme linéaire
                if (s1.matches("x[0-9]")) {
                    System.out.println("elo le 1");
                    // On récupère la valeur optimale réalisable de la variable
                    Pattern pattern = Pattern.compile(" [0-9]");
                    Matcher matcher = pattern.matcher(s1);
                    if (matcher.find()) {
                        maxX.add(Integer.parseInt(matcher.group(0).split(" ")[1]));
                        stringBuilder.append("x");
                        stringBuilder.append((i + 1));
                        stringBuilder.append(" = ");
                        stringBuilder.append(maxX.get(i));
                        stringBuilder.append("\n");
                        i++;
                    }
                }
            }*/
        }
        System.out.println(stringBuilder.toString());
    }

}

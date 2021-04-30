package evaluation;

import solver.AbstractSolver;
import solver.Lpsolve;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Float.MAX_VALUE;

public class Evaluation {

    private int n;
    private float[] xOptimal;
    private float[] y;
    private float [] x;
    private float [][] MRU;
    private int maxValue = 200;
    private AbstractSolver solver;
    private String evalFile = "src/evaluation/eval.txt";

    public Evaluation(int n) {
        xOptimal = new float[n];
        y = new float[n];
        x = new float[n];
        this.n = n;
        solver = new Lpsolve(evalFile, "");
    }

    /**
     * Démarre l'évaluation de la méthode
     * @param nbIter nombre d'itération sur lesquelles vont être effectuées l'évaluation
     */
    public void evaluer(int nbIter){
        MRU = new float[nbIter][n+1];
        for(int i=0; i<nbIter; i++){
            randomContrainte(i);
            ecrireContrainte(i);
            runSolver();
            while (!solver.getStatut().equals("right")){
                randomContrainte(i);
                runSolver();
            }
            randomCout(i+1);
            reecritureFonction(solver.getNouvelleFctCout());
            System.out.println((i+1)+" contrainte(s) ajoutée(s) : ");
            System.out.println("Résultat solveur : "+solver.getValOptimal());
            System.out.println("Distance avec coût aléatoire : "+distanceManhattan()+"\n");
        }
    }

    /**
     * Génère une nouvelle contrainte aléatoire
     * TODO : vérifier que MRU reste cohérent
     * TODO : faire en sorte que xOptimal soit l'optimal
     * @param numContrainte numéro de la contrainte ajoutée
     */
    private void randomContrainte(int numContrainte){
        Random r = new Random();
        System.out.println("Génération contrainte "+numContrainte);
        for(int i =0; i<=n; i++){
            MRU[numContrainte][i] = r.nextFloat()* (2*maxValue + 1) - maxValue  ;
            //System.out.print(MRU[numContrainte][i]+", ");
        }
        System.out.println("Contrainte "+numContrainte+" générée");
        //System.out.println();
    }

    /**
     * Génère une fonction de coût aléatoire qui satisfait MRU
     * @param nbContraintes nombre de contraintes dans MRU
     */
    private void randomCout(int nbContraintes){
        Random r = new Random();
        boolean correct = false;
        float res;
        System.out.println("Génération coût aléatoire");
        while(!correct) {
            correct = true;
            //On génère une fonction de coût aléatoire
            for (int i = 0; i < n; i++) {
                y[i] = r.nextFloat()* (maxValue + 1);
            }

            //On vérifie que la fonction de coût générée satisfait toute les contraintes
            for(int i = 0; i<nbContraintes && correct; i++){
                res=0;
                for(int j=0; j<n; j++){
                    res =+ y[j]*MRU[i][j];
                    if(res > MRU[i][n]){
                        correct = false;
                    }
                }
            }
        }
        System.out.println("Coût aléatoire généré");
        /*System.out.print("La fonction ");
        for(int i=0; i<n; i++){
            System.out.print(y[i]+" ");
        }
        System.out.println("satisfait MRU");*/
    }

    /**
     * Écrit une contrainte de MRU dans le fichier d'évaluation
     * @param idContrainte numéro de la contrainte à écrire
     */
    private void ecrireContrainte(int idContrainte){
        try {
            FileWriter myWriter = new FileWriter(evalFile, true);
            myWriter.write("\n// c"+(idContrainte+1)+" inf\n");
            for(int i=0; i<n; i++){
                if(MRU[idContrainte][i] != 0){
                    myWriter.write(MRU[idContrainte][i]+"x"+(i+1)+" ");
                }
            }
            myWriter.write(MRU[idContrainte][n]+"");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private float distanceManhattan(){
        float res = 0;
        for(int i=0; i<n; i++){
            res += Math.abs(xOptimal[i]-y[i]);
        }
        return res;
    }

    /**
     * Redéfinit la nouvelle fonction de coût en fonction du résultat du solveur
     * Réécrit cette fonction si besoin dans le fichier d'évaluation
     * @param solucSolveur nombre de contrainte écrites dans le fichier
     */
    private void reecritureFonction(float[] solucSolveur){
        boolean empty = true;
        float[] temp = x;
        for(int i=0; i<n; i++){
            if(0 != solucSolveur[i]){
                empty = false;
            }
            x[i] = solucSolveur[i];
        }
        if(empty){
            x = temp;
        }
        else{

        }
    }

    private void runSolver(){
        solver.createSolverFile();
        solver.run();
        try {
            solver.display();
        } catch (IOException e) {
            e.printStackTrace();
        }
        solver.parseOutput();
    }
}

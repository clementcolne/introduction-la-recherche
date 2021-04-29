package evaluation;

import solver.AbstractSolver;
import solver.Lpsolve;

import java.util.List;
import java.util.Random;

import static java.lang.Float.MAX_VALUE;

public class Evaluation {

    private int n;
    private float[] xOptimal;
    private float[] y;
    private float [] x;
    private float [][] MRU;
    private int maxValue = 200;
    private AbstractSolver solver;

    public Evaluation(int n) {
        xOptimal = new float[n];
        y = new float[n];
        x = new float[n];
        this.n = n;
        solver = new Lpsolve("src/evaluation/eval.txt", "");
    }

    /**
     * Démarre l'évaluation de la méthode
     * @param nbIter nombre d'itération sur lesquelles vont être effectuées l'évaluation
     */
    public void evaluer(int nbIter){
        MRU = new float[nbIter][n+1];
        for(int i=0; i<nbIter; i++){
            randomContrainte(i);
            randomCout(i+1);
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
        for(int i =0; i<=n; i++){
            MRU[numContrainte][i] = r.nextFloat()* (2*maxValue + 1) - maxValue  ;
            //System.out.print(MRU[numContrainte][i]+", ");
        }
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
        /*System.out.print("La fonction ");
        for(int i=0; i<n; i++){
            System.out.print(y[i]+" ");
        }
        System.out.println("satisfait MRU");*/
    }

    /**
     * Redéfinit la nouvelle fonction de coût calculée dans le fichier eval.txt
     * @param nbContrainte nombre de contrainte écrites dans le fichier
     */
    private void reecritureFonction(int nbContrainte){

    }
}

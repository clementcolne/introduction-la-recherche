package evaluation;

import solver.AbstractSolver;
import solver.Lpsolve;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Evaluation {

    private int n;
    private float[] xOptimal;
    private float[] y;
    private float [] x;
    private float [][] MRU;
    private int maxValue = 200;
    private AbstractSolver solver;
    private String evalFile = "src/evaluation/eval.txt";
    private StringBuilder donneesEval;
    private int idLastContrainte;
    private int idStartFonction;
    private int idEndFonction;

    public Evaluation(int n) {
        donneesEval = new StringBuilder();
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
        lectureFichier();
        MRU = new float[nbIter][n+1];
        for(int i=0; i<nbIter; i++){
            randomContrainte(i);
            ecrireContrainte(i);
            runSolver();
            //System.out.println(solver.getStatut());
            while (!solver.getStatut().equals("right")){
                System.out.println("On refait contrainte "+i);
                affichageMRU(i);
                randomContrainte(i);
                reecritureContrainte(i);
                runSolver();
            }
            reecritureFonction(solver.getNouvelleFctCout());
            randomCout(i+1);
            System.out.println((i+1)+" contrainte(s) ajoutée(s) : ");
            System.out.println("Résultat solveur : "+solver.getValOptimal());
            System.out.println("Distance avec coût aléatoire : "+distanceManhattan()+"\n");
            affichageMRU(i);
            System.out.println("_________\n");
        }
    }

    /**
     * Affiche l'ensemble des contraintes déjà générée
     * @param nbContrainte nombre de contraintes générées jusqu'à présent
     */
    private void affichageMRU(int nbContrainte) {
        for(int i=0; i<=nbContrainte; i++){
            for(int j=0; j<n; j++){
                System.out.print(MRU[i][j]+"x"+(j+1)+" ");
            }
            System.out.println("< "+ MRU[i][n]);
        }
    }

    /**
     * Génère une nouvelle contrainte aléatoire
     * TODO : faire en sorte que xOptimal soit l'optimal
     * @param numContrainte numéro de la contrainte ajoutée
     */
    private void randomContrainte(int numContrainte){
        Random r = new Random();
        boolean negatif = false;
        if(r.nextFloat() > 0.5)
            negatif = true;
        System.out.println("Génération contrainte "+numContrainte);
        for(int i =0; i<=n; i++){
            MRU[numContrainte][i] = r.nextFloat()* (maxValue + 1) ;
            if(negatif)
                MRU[numContrainte][i] = -1*MRU[numContrainte][i];
        }
        System.out.println("Contrainte "+numContrainte+" générée");
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
        int cpt=1;
        while(!correct) {
            correct = true;

            //On génère une fonction de coût aléatoire
            for (int i = 0; i < n; i++) {
                y[i] = r.nextFloat()* (maxValue + 1);
                System.out.print(y[i]+" ");
            }
            System.out.println();

            //On vérifie que la fonction de coût générée satisfait toute les contraintes
            for(int i = 0; i<nbContraintes && correct; i++){
                res=0;
                for(int j=0; j<n; j++){
                    res =+ y[j]*MRU[i][j];
                    if(res > MRU[i][n]){
                        correct = false;
                        System.out.println("Ne convient pas avec contrainte "+i);
                    }
                }
            }
            cpt++;
        }
        System.out.println("Coût aléatoire généré au bout de "+cpt+" essais");
    }

    /**
     * Écrit une contrainte de MRU dans le fichier d'évaluation
     * @param idContrainte numéro de la contrainte à écrire
     */
    private void ecrireContrainte(int idContrainte){
        donneesEval.append("\n// c"+(idContrainte+1)+" inf\n");
        idLastContrainte = donneesEval.length();
        for(int i=0; i<n; i++){
            if(MRU[idContrainte][i] != 0){
                donneesEval.append(MRU[idContrainte][i]+"x"+(i+1)+" ");
            }
        }
        donneesEval.append(MRU[idContrainte][n]+"");
        try {
            FileWriter myWriter = new FileWriter(evalFile);
            myWriter.write(donneesEval.toString());
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Réécrit la dernière contrainte générée dans le fichier d'évaluation
     * @param idContrainte numéro de la dernière contrainte générée
     */
    private void reecritureContrainte(int idContrainte){
        StringBuilder contrainte = new StringBuilder();
        for(int i=0; i<n; i++){
            if(MRU[idContrainte][i] != 0){
                contrainte.append(MRU[idContrainte][i]+"x"+(i+1)+" ");
            }
        }
        contrainte.append(MRU[idContrainte][n]+"");
        donneesEval.replace(idLastContrainte, donneesEval.length(), contrainte.toString());

        //On rééécrit le problème dans le fichier d'évaluation
        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(evalFile);
            myWriter.write(donneesEval.toString());
            myWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calcule la distance de Manhattan entre la solution optimal et la fonction de coût aléatoire
     * @return la distance calculée
     */
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
            System.out.println("On réécrit la fonction de cout");
            StringBuilder fonctionCout = new StringBuilder();
            for(int i=0; i<n; i++){
                fonctionCout.append(x[i]+" ");
            }
            fonctionCout.append("#");
            System.out.println(idStartFonction);
            donneesEval.replace(idStartFonction, idEndFonction, fonctionCout.toString());
            idEndFonction = donneesEval.indexOf("#");
            donneesEval.deleteCharAt(idEndFonction);
            FileWriter myWriter = null;
            try {
                myWriter = new FileWriter(evalFile);
                myWriter.write(donneesEval.toString());
                myWriter.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Lance l'éxécution du programme utilisant le solveur
     */
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

    /**
     * Lecture du fichier permettant de récupérer le début du fichier d'évaluation
     */
    private void lectureFichier(){
        File file = new File(evalFile);
        String ligne;
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNext()){
                ligne = myReader.nextLine();
                donneesEval.append(ligne+"\n");
                idStartFonction = donneesEval.length();

                // On regarde si la ligne correspond à la fonction
                if (ligne.equals("// fonction")) {
                    ligne = myReader.nextLine();
                    donneesEval.append(ligne);
                    String[] ligneTab = ligne.split(" ");
                    int i = 0;

                    // On écrit la fonction
                    for (String s : ligneTab) {
                        x[i] = Float.parseFloat(s);
                        i++;
                    }
                    idEndFonction = donneesEval.length();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

package Model.Deplacements;

import Model.Actions.AttaqueMonstre;
import Model.Actions.AttaqueVillageois;
import Model.Case;
import Model.Entity.Joueur;
import Model.Entity.Monstre;
import Model.Grille;
import Utils.ActionVillageois;
import Utils.Direction;

import java.awt.*;
import java.util.ArrayList;

public class DeplacementMonstre implements Runnable{
    //Attribut qui contient l'objet Villageois qui va se déplacer
    private Monstre monstre;
    //Attribut qui contient l'objet Grille sur laquelle les déplacements se font
    private Grille grille;
    private ArrayList<Case> chemin;
    private Thread thread;

    //Constructeur qui initialise les attributs
    public DeplacementMonstre(Monstre m, Grille grille, ArrayList<Case> chemin){
        this.grille = grille;
        this.monstre = m;
        this.chemin = chemin;
    }

    @Override
    public void run() {
        while (!this.chemin.isEmpty() && !this.monstre.estMort() && !this.monstre.getBatiment().estDetruit()) {
            // Récupère la position en X du villageois
            int posX = this.monstre.getPosition().x;
            // Récupère la position en Y du villageois
            int posY = this.monstre.getPosition().y;
            Case cible = this.chemin.get(0);
            int x = cible.getX();
            int y = cible.getY();
            if(posX < x){ //Vers la gauche
                this.monstre.setDirection(Direction.right);
                for (int i1 = posX + Monstre.largeur - 1; i1 >= posX; i1--) {
                    for (int i2 = posY + Monstre.hauteur - 1; i2 >= posY; i2--) {
                        this.grille.getCases()[i1][i2].setMonstres(null);
                        this.grille.getCases()[i1][i2].setOccupee(false);
                        this.grille.getCases()[i1 + 1][i2].setMonstres(this.monstre);
                        this.grille.getCases()[i1 + 1][i2].setOccupee(true);
                    }
                }
                Point tmp = new Point(posX + 1, posY);
                this.monstre.setPosition(tmp);
            }
            else if(posX > x){ //Vers la droite
                this.monstre.setDirection(Direction.left);
                for (int i1 = posX; i1 < posX + Monstre.largeur; i1++) {
                    for (int i2 = posY; i2 < posY + Monstre.hauteur; i2++) {
                        this.grille.getCases()[i1][i2].setMonstres(null);
                        this.grille.getCases()[i1][i2].setOccupee(false);
                        this.grille.getCases()[i1 - 1][i2].setMonstres(this.monstre);
                        this.grille.getCases()[i1 - 1][i2].setOccupee(true);
                    }
                }
                Point tmp = new Point(posX - 1, posY);
                this.monstre.setPosition(tmp);
            }
            else if(posY < y){ //Vers le haut
                this.monstre.setDirection(Direction.up);
                for (int i2 = posY + Monstre.hauteur - 1; i2 >= posY; i2--) {
                    for (int i1 = posX + Monstre.largeur - 1; i1 >= posX; i1--) {
                        this.grille.getCases()[i1][i2].setMonstres(null);
                        this.grille.getCases()[i1][i2].setOccupee(false);
                        this.grille.getCases()[i1][i2 + 1].setMonstres(this.monstre);
                        this.grille.getCases()[i1][i2 + 1].setOccupee(true);
                    }
                }
                Point tmp = new Point(posX, posY + 1);
                this.monstre.setPosition(tmp);
            }
            else if(posY > y){ //Vers le bas
                this.monstre.setDirection(Direction.down);
                for(int i1 = posX; i1 < posX + Monstre.largeur; i1++){
                    for(int i2 = posY; i2 < posY + Monstre.hauteur; i2++){
                        this.grille.getCases()[i1][i2].setMonstres(null);
                        this.grille.getCases()[i1][i2].setOccupee(false);
                        this.grille.getCases()[i1][i2-1].setMonstres(this.monstre);
                        this.grille.getCases()[i1][i2-1].setOccupee(true);
                    }
                }
                Point tmp = new Point(posX, posY-1);
                this.monstre.setPosition(tmp);
            }
            this.monstre.setWalkEtat();
            this.chemin.remove(0);
            this.grille.setChangedAndNotify();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Le monstre doit attaquer le batiment

        if(!this.monstre.getBatiment().estDetruit()) {
            //Demarrer le thread
            AttaqueMonstre attaquer = new AttaqueMonstre(this.monstre, this.monstre.getBatiment());
            if (attaquer.getThread() != null) {
                attaquer.getThread().interrupt();
            }
            Thread thread = new Thread(attaquer);
            thread.start();
            attaquer.setThread(thread);
        }

    }

    //Guetter:

    public Thread getThread() {
        return thread;
    }

    //Setter:

    public void setThread(Thread thread) {
        this.thread = thread;
    }
}

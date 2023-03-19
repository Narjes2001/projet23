package Model;

import Model.Entity.Villageois;
import Utils.Direction;

import javax.swing.text.Position;
import java.awt.*;

public class Argent implements Runnable{
    private Point position;
    private Grille grille;
    private int etat;
    private Thread thread;
    private boolean selected;
    private boolean estRamasse;
    private Direction directionVillageois;
    public static final int largeur = 10;
    public static final int hauteur = 15;

    public Argent(Grille grille, Point position){
        this.position = position;
        this.grille = grille;
        this.etat = 1;
        this.selected = false;
        this.estRamasse = false;
    }

    public Point positionPourVillageois(){
        Point res = null;
        boolean fini = false;
        int x = this.getPosition().x;
        int y = this.getPosition().y;

        while(!fini) {
            int random = (int) Math.floor(Math.random() * 4 + 1);
            if (random == 1) { // Donner une position vers le haut
                res = new Point((x + (largeur/2)) - (Villageois.largeur/2), y - Villageois.hauteur-1);
                this.directionVillageois = Direction.up;
            }
            else if (random == 2) { // Donner une position vers le bas
                res = new Point((x + (largeur/2)) - (Villageois.largeur/2), y+hauteur+1);
                this.directionVillageois = Direction.down;
            }
            else if (random == 3) { // Donner une position vers la gauche
                res = new Point(x-Villageois.largeur-1, y + (Villageois.hauteur /2));
                this.directionVillageois = Direction.right;
            }
            else if (random == 4) { // Donner une position vers la droite
                res = new Point((x+largeur+1), y + (Villageois.hauteur/2));
                this.directionVillageois = Direction.left;
            }
            if(res != null && positionPossible(res.x, res.y)){
                fini = true;
            }
        }
        return res;
    }
    private boolean positionPossible(int x, int y){
        boolean res = true;
        for(int i = x; i < x + Villageois.largeur; i++){
            for(int j = y; j < y + Villageois.hauteur; j++){
                res = res && !this.grille.positionOccupee(i,j);
            }
        }
        return res;
    }

    @Override
    public void run() {
        while(true){

            if(this.etat == 8){
                this.etat = 1;
            }
            else{
                this.etat++;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Guetter;
    public Point getPosition() {
        return position;
    }

    public int getEtat() {
        return etat;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isEstRamasse() {
        return estRamasse;
    }

    public Thread getThread() {
        return thread;
    }

    //Setter:
    public void setPosition(Point position) {
        this.position = position;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public void setEstRamasse(boolean estRamasse) {
        this.estRamasse = estRamasse;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }
}

package Model.Entity;

import Model.Batiment;
import Model.Case;
import Model.Deplacements.AStar;
import Model.Deplacements.DeplacementMonstre;
import Model.Grille;
import Utils.Direction;
import Utils.PairBatP;
import Utils.Vie;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Monstre extends Joueur {
    private int degats;
    private int hp;
    private boolean getAttacked;
    private Point cible;
    private Batiment batiment;
    private Grille grille;
    private Direction directionVillageois;
    private boolean afficheHeathBar;

    public static final int largeur = 12;
    public static final int hauteur = 10;;

    //Constructeur:
    public Monstre(int x, int y, Point cible, Batiment bat){
        super(x, y, "Monstre");
        this.degats = 5;
        this.hp = 100;
        this.cible = cible;
        this.getAttacked = false;
        this.afficheHeathBar = false;
        this.batiment = bat;
    }


    public void deplacer(Grille grille){
        this.grille = grille;
        ArrayList<Case> chemin = new ArrayList<>();
        int i = 0;
        while(chemin.isEmpty() && this.cible != null) {
            if(i > 0){
                PairBatP tmp = grille.EmplacementCible(this.getPosition().x, this.getPosition().y);
                this.cible = tmp.getPoint();
                this.batiment = tmp.getBatiment();
            }
            if(this.estMort() || i == 5){
                break;
            }
            i++;
            AStar astar = new AStar(grille, this.getPosition(), this.cible, 1); // créer un objet AStar
            if (astar.getThread() != null) {
                astar.getThread().interrupt();
            }
            Thread thread = new Thread(astar);
            thread.start();
            astar.setThread(thread);
            try {
                thread.join(); // attendre que le calcul soit terminé
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            chemin = astar.getChemin(); // récupérer le chemin calculé
        }
        if(!this.estMort() && !chemin.isEmpty()) {
            DeplacementMonstre deplacementMonstre = new DeplacementMonstre(this, grille, chemin);
            if (deplacementMonstre.getThread() != null) {
                deplacementMonstre.getThread().interrupt();
            }
            Thread thread = new Thread(deplacementMonstre);
            thread.start();
            deplacementMonstre.setThread(thread);
        }
        else{
            System.out.println("ya un probleme");
        }
    }

    public boolean estMort(){
        return this.hp <= 0;
    }

    public void setWalkEtat(){
        if(this.getEtatWalk() == 2){
            this.setEtatWalk(1);
        }
        else{
            this.setEtatWalk(2);
        }
    }

    public boolean subitAttaque(){
        return this.getAttacked;
    }

    public void subitDegat(int deg) {
        this.afficheHeathBar = true;
        this.hp -= deg;
        this.getAttacked = true;
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

    //Guetters:


    public Point getCible() {
        return cible;
    }

    public Direction getDirectionVillageois() {
        return directionVillageois;
    }

    public Batiment getBatiment() {
        return batiment;
    }

    public boolean getAfficheHeathBar(){
        return this.afficheHeathBar;
    }

    public int getHp() {
        return hp;
    }
    public int getDegats() {
        return degats;
    }

    //Setters:

    public void setBatiment(Batiment batiment) {
        this.batiment = batiment;
    }

    public void setCible(Point cible) {
        this.cible = cible;
    }

    public void setGetAttacked(boolean getAttacked) {
        this.getAttacked = getAttacked;
    }

    public void setAfficheHeathBar(boolean afficheHeathBar) {
        this.afficheHeathBar = afficheHeathBar;
    }
}

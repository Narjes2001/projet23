package Model.Actions;

import Model.Batiment;
import Model.Defense;
import Model.Entity.Monstre;
import Model.Entity.Villageois;
import Utils.ActionVillageois;

public class AttaqueMonstre implements Runnable{
    private Monstre monstre;
    private Batiment batiment;
    private Thread thread;

    public AttaqueMonstre(Monstre monstre, Batiment batiment){
        this.monstre = monstre;
        this.batiment = batiment;
    }

    @Override
    public void run() {
        int tempo = 0;
        while(!this.monstre.estMort() && !this.batiment.estDetruit()){
            if(tempo == 3){
                this.monstre.setWalkEtat();
                this.batiment.subitDegat(this.monstre.getDegats());
                tempo = 0;
            }
            else{
                tempo++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.monstre.setWalkEtat();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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

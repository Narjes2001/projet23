package Utils;

import Model.Batiment;

import java.awt.*;

public class PairBatP {
    private Batiment batiment;
    private Point point;

    public PairBatP(Point result, Batiment bat){
        this.point = result;
        this.batiment = bat;
    }

    //Guetters:
    public Point getPoint() {
        return point;
    }

    public Batiment getBatiment() {
        return batiment;
    }
}

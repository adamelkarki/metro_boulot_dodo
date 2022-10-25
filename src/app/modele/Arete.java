package app.modele;

import java.awt.*;
import java.util.Objects;

public class Arete {
    private int num_sommet1;
    private int num_sommet2;
    private int tps;
    private boolean areteVisitee = false;


    public Arete(int s, int d, int t) {
        this.num_sommet1 = s;
        this.num_sommet2 = d;
        this.tps = t;
    }


    public int getNum_sommet1() {
        return num_sommet1;
    }

    public int getNum_sommet2() {
        return num_sommet2;
    }

    public int getTps() {
        return tps;
    }

    public boolean isAreteVisitee() {
        return areteVisitee;
    }

    public void setAreteVisitee(boolean areteVisitee) {
        this.areteVisitee = areteVisitee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arete arete = (Arete) o;
        return num_sommet1 == arete.num_sommet1 && num_sommet2 == arete.num_sommet2 && tps == arete.tps && areteVisitee == arete.areteVisitee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(num_sommet1, num_sommet2, tps, areteVisitee);
    }

    @Override
    public String toString() {
        return num_sommet1 + " ---> " + num_sommet2 + " : " + tps;
    }
}
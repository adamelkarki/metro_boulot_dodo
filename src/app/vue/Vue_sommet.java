package app.vue;

import app.modele.Sommet;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Vue_sommet {
    public int CoorX, CoorY;
    private String nom;


    public Vue_sommet(String n, int x, int y){
        this.CoorX = x;
        this.CoorY = y;
        this.nom = n;
    }



    public void setListStation(Set<String> listStation) {

        List<String> tmp = new ArrayList<>();
        tmp.addAll(listStation);

    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Vue_sommet)) {
            return false;
        }
        Vue_sommet vue_sommet = (Vue_sommet) o;
        return vue_sommet.nom.equals(this.nom);
    }


    public int getCoorX() {
        return CoorX;
    }

    public void setCoorX(int coorX) {
        CoorX = coorX;
    }

    public int getCoorY() {
        return CoorY;
    }

    public void setCoorY(int coorY) {
        CoorY = coorY;
    }



    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Vue_sommet{" +
                "CoorX=" + CoorX +
                ", CoorY=" + CoorY +
                ", nom='" + nom + '\'' +
                '}';
    }
}

package app.modele;

import app.modele.Sommet;

import java.util.*;

public class Algo {

    private Graphe graphe;
    private List<Sommet> sommets;
    private List<Arete> aretes;

    private List<Sommet> unsettled;
    private List<Sommet> settled;


    public Algo(Graphe g){
        this.sommets = g.getSommets();
        this.aretes = g.getAretes();
        unsettled = new ArrayList<Sommet>();
        settled = new ArrayList<Sommet>();


    }



}

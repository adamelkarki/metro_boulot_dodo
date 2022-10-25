package app.vue;


import app.modele.Graphe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Vue_graphe {
    private List<Vue_sommet> vue_sommets;
    private Graphe graphe;


    public Vue_graphe() throws IOException {
        try {
            this.graphe = new Graphe();

        } catch (IOException e) {
            System.out.println("Error creation graphe in vue_graphe");
        }

        this.vue_sommets = new LinkedList<>();
        init();
    }

    public void init() throws IOException {
        File file = new File("src/pospoints.txt");
        readSommet(file);
    }

    public void readSommet(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line, precedent = "";
        int x;
        int y;
        String nom_station = null;

        while ((line = br.readLine()) != null) {

            String[] regex = line.split(";");
            x = Integer.parseInt(regex[0]);
            y = Integer.parseInt(regex[1]);
            nom_station = regex[2];

            nom_station = nom_station.replace('@', ' ');
            Vue_sommet vue_sommet;


           if (!nom_station.equals(precedent)) {  //enlever les doublons

                vue_sommet = new Vue_sommet(nom_station, x, y);
                this.vue_sommets.add(vue_sommet);


            }

            precedent = nom_station;
        }

    }

    public List<Vue_sommet> getVue_sommets() {
        return vue_sommets;
    }



    public Graphe getGraphe() {
        return graphe;
    }

    public void setGraphe(Graphe graphe) {
        this.graphe = graphe;
    }
}

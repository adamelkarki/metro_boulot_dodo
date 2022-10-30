package app.modele;

import java.io.*;
import java.util.*;

public class Graphe {

    private ArrayList<Sommet> sommets;
    private ArrayList<Arete> aretes;
    private File file;
    private int acpm = 0;


    public Graphe() throws IOException {
        this.sommets = new ArrayList<Sommet>();
        this.aretes = new ArrayList<Arete>();
        this.file = new File("src/metro.txt");
        init();
    }

    public void init() throws IOException {
        readFile();
    }

    public ArrayList<Sommet> getSommets() {
        return sommets;
    }

    public int getAcpm() {
        return acpm;
    }

    public void setAcpm(int acpm) {
        this.acpm = acpm;
    }

    public void setSommets(ArrayList<Sommet> sommets) {
        this.sommets = sommets;
    }

    public ArrayList<Arete> getAretes() {
        return this.aretes;
    }


    public void setAretes(ArrayList<Arete> aretes) {
        this.aretes = aretes;
    }

    //Méthode qui va lire le fichier pour le formater
    public void readFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(this.file));
        String line;

        //Sommets
        int branchement;
        int num_sommet;
        String nom_station = null;
        String numero_ligne = null;
        boolean terminus = false;

        //Arêtes
        int sommetA = 0;
        int sommetB = 0;
        int tps = 0;


        while ((line = br.readLine()) != null) {
            if (line.startsWith("V ")) {
                line = line.substring(2);
                branchement = Integer.parseInt(line.substring(line.length() - 1));
                line = line.substring(0, line.length() - 1);
                num_sommet = Integer.parseInt(line.substring(0, 4));
                line = line.substring(5);

                String[] regex = line.split(";");

                nom_station = regex[0];
                numero_ligne = regex[1].substring(0, regex[1].length() - 1);
                terminus = Boolean.parseBoolean(regex[2]);

                Sommet s = new Sommet(num_sommet, nom_station, numero_ligne, terminus, branchement);
                this.sommets.add(s);
            }
            if (line.startsWith("E ")) {
                String[] regex = line.split(" ");

                sommetA = Integer.parseInt(regex[1]);
                sommetB = Integer.parseInt(regex[2]);
                tps = Integer.parseInt(regex[3]);

                Arete a = new Arete(sommetA, sommetB, tps);
                this.aretes.add(a);

            }
        }
    }

    public ArrayList<Sommet> sommetsAdjacencents(Sommet s) {
        ArrayList<Integer> adjacents = new ArrayList<>();
        ArrayList<Sommet> sommetsAdjacents = new ArrayList<Sommet>();
        for (Arete a : this.getAretes()) {
            if (a.getNum_sommet2() == s.getNum_sommet()) {
                adjacents.add(a.getNum_sommet1());
            }
            if (a.getNum_sommet1() == s.getNum_sommet()) {
                adjacents.add(a.getNum_sommet2());
            }
        }
        for (Sommet sommet : this.getSommets()) {
            for (int i = 0; i < adjacents.size(); i++) {
                if (sommet.getNum_sommet() == adjacents.get(i)) {
                    sommetsAdjacents.add(sommet);
                }
            }
        }
        return sommetsAdjacents;
    }

    public ArrayList<Arete> aretesAdjacentes(Sommet s) {
        ArrayList<Arete> aretesAdjacentes = new ArrayList<Arete>();
        for (Arete a : this.getAretes()) {
            if (a.getNum_sommet2() == s.getNum_sommet() || a.getNum_sommet1() == s.getNum_sommet()) {
                aretesAdjacentes.add(a);
            }
        }
        return aretesAdjacentes;
    }

    public boolean connexite(Sommet s1) {
        s1.setSommetVisite(true);
        ArrayList<Sommet> sommetsAdjacents = sommetsAdjacencents(s1);

        for (Sommet next : sommetsAdjacents) {
            if (next.isSommetVisite() == false) {
                connexite(next);
            }
        }
        return true;
    }

    public Arete distanceSommetsVoisins (Sommet sommet1, Sommet sommet2) {
        int distance = Integer.MAX_VALUE;
        Arete returnArete = null;
        for (Arete arete : this.getAretes()) {
            if((arete.getNum_sommet1() == sommet1.getNum_sommet() && arete.getNum_sommet2() == sommet2.getNum_sommet()) || (arete.getNum_sommet2() == sommet1.getNum_sommet() && arete.getNum_sommet1() == sommet2.getNum_sommet())) {
                returnArete = arete;

            }
        }
        return returnArete;
    }

    public ArrayList<Arete> kruskal() {
        Collections.sort(this.aretes);
        TreeMap<Integer, Integer> sommetsVisites = new TreeMap<>();
        ArrayList<Arete> areteArrayList = new ArrayList<>();
        int poidsDeLarbre = 0;

        for (Arete arete : this.getAretes()) {
            if ((!sommetsVisites.containsKey(arete.getNum_sommet1()) || !sommetsVisites.containsValue(arete.getNum_sommet2())) &&
                    (!sommetsVisites.containsKey(arete.getNum_sommet2()) || !sommetsVisites.containsValue(arete.getNum_sommet1()))) {
                sommetsVisites.put(arete.getNum_sommet1(), arete.getNum_sommet2());
                poidsDeLarbre += arete.getTps();
                areteArrayList.add(arete);

            }
        }
        this.acpm = poidsDeLarbre;
        return areteArrayList;
    }

    public int dijkstra(Sommet s, Sommet s2) {
        TreeMap<Integer, Integer> distance = new TreeMap<>();
        ArrayList<Sommet> visited = new ArrayList<>();
        ArrayList<Sommet> previous = new ArrayList<>();
        TreeMap<Integer, ArrayList> path = new TreeMap<>();
        for (Sommet sommet : this.getSommets()) {
            distance.put(sommet.getNum_sommet(), Integer.MAX_VALUE);
        }
        distance.put(s.getNum_sommet(), 0);
        for (Sommet sommet : this.getSommets()) {
            previous.add(null);
            path.put(sommet.getNum_sommet(), new ArrayList<Sommet>() {
            });
        }
        while (visited.size() != this.getSommets().size()) {
            int min = Integer.MAX_VALUE;
            Sommet sommetMin = null;
            for (Sommet sommet : this.getSommets()) {
                if (distance.get(sommet.getNum_sommet()) < min && !visited.contains(sommet)) {
                    min = distance.get(sommet.getNum_sommet());
                    sommetMin = sommet;
                }
            }
            visited.add(sommetMin);
            for (Sommet sommet : sommetsAdjacencents(sommetMin)) {
                Arete areteDis =  distanceSommetsVoisins(sommetMin, sommet);
                if (distance.get(sommet.getNum_sommet()) > distance.get(sommetMin.getNum_sommet()) + areteDis.getTps()) {
                    distance.put(sommet.getNum_sommet(), distance.get(sommetMin.getNum_sommet()) + areteDis.getTps());
                    previous.set(sommet.getNum_sommet(), sommetMin);
                    sommet.setPrevoius(sommetMin);
                }
            }
        }
        return distance.get(s2.getNum_sommet());
    }

    public ArrayList<Sommet> getPath(Sommet s, Sommet s2){
        ArrayList<Sommet> pathTo = new ArrayList<>();
        while (s2.getPrevoius() != null) {
            pathTo.add(s2);
            s2 = s2.getPrevoius();
        }
        pathTo.add(s);
        Collections.reverse(pathTo);
        return pathTo;
    }

    public ArrayList<Sommet> getShortestPath(Sommet source, Sommet destination, ArrayList<Sommet> previous) {
        ArrayList<Sommet> path = new ArrayList<>();
        Sommet current = destination;
        while (current != source) {
            path.add(current);
            current = previous.get(current.getNum_sommet());
        }
        path.add(source);
        Collections.reverse(path);
        return path;
    }


}
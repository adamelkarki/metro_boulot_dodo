import java.io.*;
import java.util.*;

import static java.util.Collections.sort;

public class Graphe {

    private ArrayList<Sommet> sommets;
    private ArrayList<Arete> aretes;
    private File file;


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

                //System.out.println(branchement);
                //System.out.println(line);
                //System.out.println(line);
                //System.out.println(num_sommet);
                //System.out.println(nom_station);
                //System.out.println(numero_ligne);
                //System.out.println(terminus);

                Sommet s = new Sommet(num_sommet, nom_station, numero_ligne, terminus, branchement);
//                System.out.println(s.toString());
                this.sommets.add(s);
            }
            if (line.startsWith("E ")) {
                String[] regex = line.split(" ");

                sommetA = Integer.parseInt(regex[1]);
                sommetB = Integer.parseInt(regex[2]);
                tps = Integer.parseInt(regex[3]);

                //System.out.println(sommetA);
                //System.out.println(sommetB);
                //System.out.println(tps);

                Arete a = new Arete(sommetA, sommetB, tps);
//                System.out.println(a.toString());
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
//        System.out.println(sommetsAdjacents);
        return sommetsAdjacents;
    }

    public ArrayList<Arete> aretesAdjacentes(Sommet s) {
        ArrayList<Arete> aretesAdjacentes = new ArrayList<Arete>();
        for (Arete a : this.getAretes()) {
            if (a.getNum_sommet2() == s.getNum_sommet() || a.getNum_sommet1() == s.getNum_sommet()) {
                aretesAdjacentes.add(a);
            }
        }
//        System.out.println(aretesAdjacentes);
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
            if ((!sommetsVisites.containsKey(arete.getNum_sommet1()) && !sommetsVisites.containsValue(arete.getNum_sommet2())) ||
                    (!sommetsVisites.containsKey(arete.getNum_sommet2()) && !sommetsVisites.containsValue(arete.getNum_sommet1()))) {
                sommetsVisites.put(arete.getNum_sommet1(), arete.getNum_sommet2());
                poidsDeLarbre += arete.getTps();
                areteArrayList.add(arete);
            }
        }
        System.out.println(poidsDeLarbre);
        return areteArrayList;
    }

    //dikjstra algorithm from a station to another
    public void dijkstra(Sommet s) {
        //create a tree map to store the distance from the source to each vertex
        TreeMap<Integer, Integer> distance = new TreeMap<>();
        //create an array with visited vertices
        ArrayList<Sommet> visited = new ArrayList<>();
        //create a tree map to store the previous vertex of each vertex
        ArrayList<Sommet> previous = new ArrayList<>();
        //create a tree map to store the path from the source to each vertex
        TreeMap<Integer, ArrayList> path = new TreeMap<>();
        //initialize the distance to infinity
        for (Sommet sommet : this.getSommets()) {
            distance.put(sommet.getNum_sommet(), Integer.MAX_VALUE);
        }
        //initialize the distance to 0 for the source
        distance.put(s.getNum_sommet(), 0);
        //initialize the previous vertex to null
        for (Sommet sommet : this.getSommets()) {
            previous.add(null);
            path.put(sommet.getNum_sommet(), new ArrayList<>() {
            });
        }
        //start algorithm
        while (visited.size() != this.getSommets().size()) {
            //find the vertex with the smallest distance
            int min = Integer.MAX_VALUE;
            Sommet sommetMin = null;
            for (Sommet sommet : this.getSommets()) {
                if (distance.get(sommet.getNum_sommet()) < min && !visited.contains(sommet)) {
                    min = distance.get(sommet.getNum_sommet());
                    sommetMin = sommet;
                }
            }
            //add the vertex to the visited array
            visited.add(sommetMin);
            //update the distance of the adjacent vertices
            for (Sommet sommet : sommetsAdjacencents(sommetMin)) {
                Arete areteDis =  distanceSommetsVoisins(sommetMin, sommet);
                if (distance.get(sommet.getNum_sommet()) > distance.get(sommetMin.getNum_sommet()) + areteDis.getTps()) {
                    distance.put(sommet.getNum_sommet(), distance.get(sommetMin.getNum_sommet()) + areteDis.getTps());
                    previous.set(sommet.getNum_sommet(), sommetMin);
                   // ArrayList pathSommet = path.get(sommetMin.getNum_sommet());
                  //  pathSommet.add(areteDis);
                    path.put(sommet.getNum_sommet(),previous);
                }
            }
        }
        //print the distance from the source to each vertex
        for (Sommet sommet : this.getSommets()) {
            System.out.println("Distance from " + s.getNum_sommet() + " to " + sommet.getNum_sommet() + " is " + distance.get(sommet.getNum_sommet()));
        }
        System.out.println(path.get(161));

    }



    // get the shortest path from the source to the destination with a previous array
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


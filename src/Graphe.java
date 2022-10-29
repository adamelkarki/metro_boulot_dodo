import java.io.*;
import java.util.*;

import static java.util.Collections.sort;

public class Graphe {

    private ArrayList<Sommet> sommets;
    private Map<Integer, Integer> result = new HashMap<>();
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
        //System.out.println(sommetsAdjacents);

        for (Sommet next : sommetsAdjacents) {
            if (next.isSommetVisite() == false) {
                connexite(next);
            }
        }
        return true;
    }

    public ArrayList<Arete> kruskal() {
        Collections.sort(this.aretes);
        TreeMap<Integer, Integer> sommetsVisites = new TreeMap<>();
        ArrayList<Arete> areteArrayList = new ArrayList<>();
        int acpm = 0;

        for (Arete arete : this.getAretes()) {
            if ((!sommetsVisites.containsKey(arete.getNum_sommet1()) && !sommetsVisites.containsValue(arete.getNum_sommet2())) || (!sommetsVisites.containsKey(arete.getNum_sommet2()) && !sommetsVisites.containsValue(arete.getNum_sommet1()))) {
                sommetsVisites.put(arete.getNum_sommet1(), arete.getNum_sommet2());
                acpm += arete.getTps();
                areteArrayList.add(arete);
            }
        }
        return areteArrayList;
    }

    public Map<Integer, Integer> dikjstra(Sommet sommetSource, Sommet destination) {
        int sommetAdjacent = 0;
        sommetSource.setSommetVisite(true);
        ArrayList<Sommet> voisins = sommetsAdjacencents(sommetSource);
        ArrayList<Arete> voisines = aretesAdjacentes(sommetSource);

        //On retourne result lorsque les deux sommets sont voisins
        for (Arete arete : voisines) {
            if (arete.getNum_sommet1() == sommetSource.getNum_sommet()) {
                sommetAdjacent = arete.getNum_sommet2();
                this.result.put(sommetAdjacent, arete.getTps());
            }
            if (arete.getNum_sommet2() == sommetSource.getNum_sommet()) {
                sommetAdjacent = arete.getNum_sommet1();
                this.result.put(sommetAdjacent, arete.getTps());
            }
            if ((arete.getNum_sommet1() == destination.getNum_sommet() || arete.getNum_sommet2() == destination.getNum_sommet())) {
                System.out.println("DERNIER TOUR -> ILS SONT VOISINS.");
                this.result.put(destination.getNum_sommet(), arete.getTps());
                return this.result;
            }
        }
        Set<Integer> set_iterator = this.result.keySet();
        ArrayList<Sommet> sommetsFinaux = new ArrayList<>();
        Iterator<Integer> it = set_iterator.iterator();

        if (!this.result.containsKey(destination.getNum_sommet())) {
            while (it.hasNext()) {
                int i = it.next();
                for (Sommet sommet : voisins) {
                    if (sommet.getNum_sommet() == i && !sommet.isSommetVisite()) {
                        sommetsFinaux.add(sommet);
                    }
                }
            }
            for (Sommet sommet : sommetsFinaux) {
                    System.out.println(sommet);
                    dikjstra(sommet, destination);
            }
        }
        return this.result;
    }
}


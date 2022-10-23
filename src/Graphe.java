import java.io.*;
import java.util.ArrayList;

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


        while((line = br.readLine()) != null) {
            if(line.startsWith("V ")) {
                line = line.substring(2);
                branchement = Integer.parseInt(line.substring(line.length()-1));
                line = line.substring(0,line.length()-1);
                num_sommet = Integer.parseInt(line.substring(0,4));
                line = line.substring(5);

                String [] regex = line.split(";");

                nom_station = regex[0];
                numero_ligne = regex[1].substring(0,regex[1].length()-1);
                terminus = Boolean.parseBoolean(regex[2]);

                //System.out.println(branchement);
                //System.out.println(line);
                //System.out.println(line);
                //System.out.println(num_sommet);
                //System.out.println(nom_station);
                //System.out.println(numero_ligne);
                //System.out.println(terminus);

                Sommet s = new Sommet(num_sommet,nom_station, numero_ligne, terminus, branchement);
//                System.out.println(s.toString());
                this.sommets.add(s);
            }
            if(line.startsWith("E ")) {
                String [] regex = line.split(" ");

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

    public ArrayList<Integer> sortAreteByTemps() {
        ArrayList<Integer> areteTrie = new ArrayList<Integer>();
        int cpt = 0;
        for (Arete arete : this.getAretes()) {
            areteTrie.add(arete.getTps());
            cpt++;
        }
        System.out.println(cpt);
        sort(areteTrie);
        System.out.println(areteTrie);
        return areteTrie;
    }

    public ArrayList<Sommet> adjacence(Sommet s) {
        ArrayList<Integer> adjacents = new ArrayList<>();
        ArrayList<Sommet> sommetsAdjacents = new ArrayList<Sommet>();
        for (Arete a : this.getAretes()) {
            if(a.getNum_sommet2() == s.getNum_sommet()) {
                adjacents.add(a.getNum_sommet1());
            }
            if(a.getNum_sommet1() == s.getNum_sommet()) {
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

    public boolean connexite(Sommet s1) {
        s1.setSommetVisite(true);
        ArrayList<Sommet>  sommetsAdjacents = adjacence(s1);
        System.out.println(sommetsAdjacents);


        for(Sommet next : sommetsAdjacents) {
            if(next.isSommetVisite() == false){
                connexite(next);
            }
        }
        return true;
    }

    public int kruskal() {
        ArrayList<Integer> areteByTemps= sortAreteByTemps();
        int cpt = 0;
        int acpm = 0;
        for(Arete arete : this.aretes) {
            if( arete.isAreteVisitee() == false) {
                acpm += areteByTemps.get(cpt);
                cpt++;
                arete.setAreteVisitee(true);
            }
        }
        return acpm;
    }

}

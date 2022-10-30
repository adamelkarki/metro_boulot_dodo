;

import javax.sound.sampled.Port;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {

        Graphe g = new Graphe();

//        Implémentation de la foncton getSommetsAdjacents
        Sommet PierreEtMarieCurie = g.getSommets().get(237);
        Sommet sommetRandom = g.getSommets().get(159);
        Sommet Mairie_DIvry = g.getSommets().get(179);
        Sommet Porte_DIvry = g.getSommets().get(261);
        Sommet PorteDeChoisy = g.getSommets().get(266);
        Sommet PorteDitalie = g.getSommets().get(260);
        Sommet PlaceDItalie = g.getSommets().get(244);
        Sommet MaisonBlanche = g.getSommets().get(184);
//        System.out.println(g.getAretes());
//        System.out.println(g.sommetsAdjacencents(Porte_DIvry));
//        System.out.println(g.aretesAdjacentes(MaisonBlanche));
//        g.connexite(Mairie_DIvry);
        System.out.println(g.kruskal());
//        System.out.println(g.sommetsAdjacencents(PorteDeChoisy));
//        System.out.println(g.sommetsAdjacencents(s1));
//        System.out.println(g.sommetsAdjacencents(PierreEtMarieCurie));
         g.dijkstra(Mairie_DIvry);
//        System.out.println(g.distanceSommetsVoisins(Mairie_DIvry,PierreEtMarieCurie));

    }



}

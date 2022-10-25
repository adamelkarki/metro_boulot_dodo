import javax.sound.sampled.Port;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Graphe g = new Graphe();

//        Implémentation de la foncton getSommetsAdjacents
        Sommet PierreEtMarieCurie = g.getSommets().get(237);
        Sommet sommetRandom = g.getSommets().get(159);
        Sommet Mairie_DIvry = g.getSommets().get(179);
        Sommet Porte_DIvry = g.getSommets().get(261);
        Sommet PorteDeChoisy = g.getSommets().get(266);
//        System.out.println(g.getAretes());
//        System.out.println(g.sommetsAdjacencents(Porte_DIvry));
//        g.aretesAdjacentes(Porte_DIvry);
//        g.connexite(Porte_DIvry);
//        System.out.println(g.kruskal());
        g.dikjstra(Porte_DIvry,PierreEtMarieCurie);



    }



}

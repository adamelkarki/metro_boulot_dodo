package app.vue;

import app.modele.Arete;
import app.modele.Sommet;
import sun.java2d.loops.DrawLine;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class App extends JPanel {
    private Vue_graphe vue_graphe;
    private BufferedImage background;
    private Vue_sommet source;
    private Vue_sommet destination;
    private JTextArea textArea;
    private JToggleButton display_kruskal;
    private Boolean active_kruskal = false;
    private JLabel label_acpm;

    public App() {

        try {
            this.vue_graphe = new Vue_graphe();
            textArea = new JTextArea();
            textArea.setEditable(false);
            label_acpm = new JLabel();

            display_kruskal = new JToggleButton("Kruskal");

            this.background = ImageIO.read(new File("src/metrof_r.png"));

        } catch (IOException e) {
            System.out.println("error in App constructor");

        }
    }


    public void update(Graphics g, Vue_sommet src, Vue_sommet dest) {
        super.update(g);
        textArea.repaint();
        Graphics2D g2 = (Graphics2D) g;

        if(!active_kruskal && display_kruskal.isSelected()) {
            ArrayList<Arete> kruskal_res = new ArrayList<>();
            kruskal_res = this.vue_graphe.getGraphe().kruskal();

            ArrayList<Sommet> sommets = new ArrayList<>();
            label_acpm.setText("ACPM : " +Integer.toString(vue_graphe.getGraphe().getAcpm()));
            System.out.println("ACPM = " + vue_graphe.getGraphe().getAcpm());
            System.out.println(vue_graphe.getGraphe().getSommets().size());
            System.out.println(kruskal_res.size());


            for(Arete arete : kruskal_res){
                Sommet a = vue_graphe.getGraphe().getSommets().get(arete.getNum_sommet1());
                Sommet b = vue_graphe.getGraphe().getSommets().get(arete.getNum_sommet2());
                sommets.add(a);
                sommets.add(b);

                Vue_sommet vueA = vue_graphe.getVue_sommets().stream().filter(s -> (s.getNom() +" ").equals(a.getNom_sommet())).findFirst().get();
                Vue_sommet vueB = vue_graphe.getVue_sommets().stream().filter(s -> (s.getNom() +" ").equals(b.getNom_sommet())).findFirst().get();

                g2.fillOval(vueA.getCoorX() - 4, vueA.getCoorY() - 4, 7, 7);
                g2.fillOval(vueB.getCoorX() - 4, vueB.getCoorY() - 4, 7, 7);
            }
            drawKruskal(g, kruskal_res);
        }else{
            if (src != null) {
                this.source = src;

            }
            if (this.source != null) {
                g2.setColor(Color.BLACK);
                g2.fillOval(this.source.getCoorX() - 4, this.source.getCoorY() - 4, 10, 10);

            }

            if (dest != null) {
                this.destination = dest;
                g2.setColor(Color.BLACK);
                g2.fillOval(this.destination.getCoorX() - 4, this.destination.getCoorY() - 4, 10, 10);
            }

            if (this.destination != null) {
                g2.setColor(Color.BLACK);
                g2.fillOval(this.destination.getCoorX() - 4, this.destination.getCoorY() - 4, 10, 10);
            }

            if (this.source != null && this.destination != null) {
                ArrayList<Sommet> sommets = new ArrayList<>();
                ArrayList<Sommet> chemin = new ArrayList<>();
                float poids = 0;
                String text=textArea.getText();
                String words[]=text.split("\\s");

                Sommet som_source = this.vue_graphe.getGraphe().getSommets().stream().filter(s -> s.getNom_sommet().equals(this.source.getNom() + " ")).findFirst().get();
                Sommet som_dest = this.vue_graphe.getGraphe().getSommets().stream().filter(s -> s.getNom_sommet().equals(this.destination.getNom() + " ")).findFirst().get();
                poids = vue_graphe.getGraphe().dijkstra(som_source, som_dest);
                chemin = vue_graphe.getGraphe().getPath(som_source, som_dest);

                float tpsDeTrajet = (float) poids/(float) 60;

                String numLigne = "";
                for(Sommet s : chemin){
                    Vue_sommet vue = vue_graphe.getVue_sommets().stream().filter(v -> (v.getNom() + " ").equals(s.getNom_sommet())).findFirst().get();
                    if(!s.getNum_ligne().equals(numLigne) && !numLigne.equals("")){
                        numLigne = s.getNum_ligne();
                        textArea.append("Changez de ligne a la station " + s.getNom_sommet() + ", prenez la ligne " + s.getNum_ligne() +"\n");
                        System.out.println("Changez de ligne a la station " + s.getNom_sommet() + ", prenez la ligne " + s.getNum_ligne() +"\n");
                    }else if(!s.getNum_ligne().equals(numLigne) && numLigne.equals("")){
                        numLigne = s.getNum_ligne();
                        textArea.append("Prenez la ligne " + s.getNum_ligne() + " de la station " + s.getNom_sommet() +"\n");
                        System.out.println("Prenez la ligne " + s.getNum_ligne() + " de la station " + s.getNom_sommet() +"\n");
                    }
                    else if(s.getNum_ligne().equals(numLigne)){
                        numLigne = s.getNum_ligne();
                        textArea.append("Station  : "  + s.getNom_sommet() + " (" + s.getNum_ligne() + ")" +"\n");
                        System.out.println("Station  : "  + s.getNom_sommet() + " (" + s.getNum_ligne() + ")" +"\n");
                    }
                    g2.fillOval(vue.getCoorX()-4, vue.getCoorY()-4, 7, 7);
                }

                if(poids < 60){
                    textArea.append("\n" + "Vous attendrez la destination dans " + poids + " secondes.");
                    System.out.println("\n" + "Vous attendrez la destination dans " + poids + " secondes.");
                }else{

                    textArea.append("\n" + "Vous attendrez la destination dans " +  String.format("%.2f", tpsDeTrajet)  + " minutes.");
                    System.out.println("\n" + "Vous attendrez la destination dans " + String.format("%.2f", tpsDeTrajet)  + " minutes.");

                }
                drawArete(g, chemin);
            }
        }


    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3F);
        g2.setComposite(ac);
        g.drawImage(background, 0, 0, 987, 952, this);

        ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F);
        g2.setComposite(ac);

        g2.setStroke(new BasicStroke(2));
    }

    public void drawArete(Graphics g, ArrayList<Sommet> sommets){

        for(Arete arete : this.vue_graphe.getGraphe().getAretes()){
            LinkedHashSet<String> display = new LinkedHashSet<>();

            try {
                Sommet a = sommets.stream().filter(s -> s.getNum_sommet() == arete.getNum_sommet1()).findFirst().get();
                Sommet b = sommets.stream().filter(s -> s.getNum_sommet() == arete.getNum_sommet2()).findFirst().get();
                Vue_sommet vue = this.vue_graphe.getVue_sommets().stream().filter(s -> (s.getNom() + " ").equals(a.getNom_sommet())).findAny().get();
                Vue_sommet vue2 = this.vue_graphe.getVue_sommets().stream().filter(s -> (s.getNom() + " ").equals(b.getNom_sommet())).findFirst().get();
                g.setColor(chooseColor(a));
                g.drawLine(vue.getCoorX(), vue.getCoorY(), vue2.getCoorX(), vue2.getCoorY());

            }catch (Exception e){

            }
        }
    }

    public void drawKruskal(Graphics g, ArrayList<Arete> aretes){
        for(Arete arete : aretes){

            try {
                Sommet a = vue_graphe.getGraphe().getSommets().stream().filter(s -> s.getNum_sommet() == arete.getNum_sommet1()).findFirst().get();
                Sommet b = vue_graphe.getGraphe().getSommets().stream().filter(s -> s.getNum_sommet() == arete.getNum_sommet2()).findFirst().get();
                Vue_sommet vue = this.vue_graphe.getVue_sommets().stream().filter(s -> (s.getNom() + " ").equals(a.getNom_sommet())).findAny().get();
                Vue_sommet vue2 = this.vue_graphe.getVue_sommets().stream().filter(s -> (s.getNom() + " ").equals(b.getNom_sommet())).findFirst().get();
                g.setColor(Color.BLACK);
                g.drawLine(vue.getCoorX(), vue.getCoorY(), vue2.getCoorX(), vue2.getCoorY());

            }catch (Exception e){

            }
        }
    }
    public Color chooseColor(Sommet s){
        switch (s.getNum_ligne()){
            case "1" : return Color.YELLOW;
            case "2" : return Color.BLUE;
            case "3" : return Color.decode("#96892b");
            case "4" : return Color.decode("#b9439b");
            case "5" : return Color.ORANGE;
            case "6" : return Color.GREEN;
            case "7" : return Color.decode("#f59eb3");
            case "7bis" : return Color.decode("#6ebd8c");
            case "8" : return Color.decode("#b89ccb");
            case "9" : return Color.decode("#b6be29");
            case "10" : return Color.decode("#d5a931");
            case "11" : return Color.decode("#8f663b");
            case "12" : return Color.decode("#018b58");
            case "13" : return Color.CYAN;
            case "14" : return Color.MAGENTA;


        }
        return Color.BLACK;
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("Graph");

        JSplitPane splitPane;
        App app;
        JPanel bottomPanel;

        JPanel inputPanel;
        app = new App();

        ArrayList<String> list_combobox = new ArrayList<>();
        for(Vue_sommet s : app.vue_graphe.getVue_sommets()){
            list_combobox.add(s.getNom());
        }

        Set<String> li = new HashSet<>();
        li.addAll(list_combobox);
        list_combobox.clear();
        list_combobox.addAll(li);


        JButton jButton = new JButton("Reset");


        app.display_kruskal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(app.active_kruskal == false) {
                    ////app.repaint();
                    app.update(frame.getGraphics(),null,null);
                    app.active_kruskal = true;
                }else {
                    app.repaint();
                    app.label_acpm.setText("ACPM : " + Integer.toString(app.vue_graphe.getGraphe().getAcpm()));
                    app.active_kruskal = false;
                }
            }
        });

        //trier par odre alphabetique
        Collections.sort(list_combobox);
        String test[] =  list_combobox.toArray(new String[0]);

        JComboBox comboBox_src = new JComboBox<String>(test);
        JComboBox comboBox_dest = new JComboBox<String>(test);

        comboBox_src.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String value = comboBox_src.getSelectedItem().toString();
                Vue_sommet vue_sommet = app.vue_graphe.getVue_sommets().stream().filter(s -> s.getNom().equals(value)).findFirst().get();
                app.update(frame.getGraphics(), vue_sommet, null);
            }
        });



        comboBox_dest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String value = comboBox_dest.getSelectedItem().toString();
                Vue_sommet vue_sommet = app.vue_graphe.getVue_sommets().stream().filter(s -> s.getNom().equals(value)).findFirst().get();
                app.update(frame.getGraphics(), null, vue_sommet);
                comboBox_src.setEnabled(false);
                comboBox_dest.setEnabled(false);

            }
        });

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.repaint();
                app.textArea.setText("");
                comboBox_dest.setEnabled(true);
                comboBox_src.setEnabled(true);

            }
        });

        splitPane = new JSplitPane();

        // app.setBorder(new LineBorder(Color.BLACK));

        bottomPanel = new JPanel();
        //    bottomPanel.setBorder(new LineBorder(Color.blue));

        inputPanel = new JPanel();

        frame.setPreferredSize(new Dimension(1500, 1005));
        frame.getContentPane().setLayout(new GridLayout());
        frame.getContentPane().add(splitPane);

        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(990);
        splitPane.setLeftComponent(app);
        splitPane.setRightComponent(bottomPanel);

        bottomPanel.add(inputPanel);
        bottomPanel.add(inputPanel, BorderLayout.NORTH);

        app.textArea.setPreferredSize(new Dimension(440, 800));
        //   app.textArea.setBorder(new LineBorder(Color.pink));
        bottomPanel.add(app.textArea, BorderLayout.SOUTH);

        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        inputPanel.add(comboBox_src, BorderLayout.NORTH);
        inputPanel.add(comboBox_dest, BorderLayout.SOUTH);

        app.display_kruskal.setPreferredSize(new Dimension(100,20));
        bottomPanel.add(app.display_kruskal, BorderLayout.NORTH);
        jButton.setPreferredSize(new Dimension(100,20));

        bottomPanel.add(jButton);

        bottomPanel.add(app.label_acpm);

        comboBox_src.setPreferredSize(new Dimension(220, 50));
        comboBox_dest.setPreferredSize(new Dimension(220, 50));

        comboBox_src.setMaximumSize(new Dimension(200, 500));
        comboBox_dest.setMaximumSize(new Dimension(200, 500));


        //inputPanel.setBorder(new LineBorder(Color.red));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
    }
}
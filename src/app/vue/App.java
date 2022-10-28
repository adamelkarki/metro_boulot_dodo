package app.vue;

import app.modele.Arete;
import app.modele.Sommet;
import org.testng.ITestListener;
import sun.java2d.loops.DrawLine;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public App() {

        try {
            this.vue_graphe = new Vue_graphe();
            textArea = new JTextArea();
            textArea.setEditable(false);

            this.background = ImageIO.read(new File("src/metrof_r.png"));

        } catch (IOException e) {
            System.out.println("error in App constructor");

        }
    }
/*
    @Override
    public void repaint() {
        super.repaint();
        System.out.println("test");

    }
*/

    public void update(Graphics g, Vue_sommet src, Vue_sommet dest) {
        super.update(g);
        System.out.println("UPDATE");
        Graphics2D g2 = (Graphics2D) g;


        if(src != null){
            this.source = src;

        }
        if(this.source != null){
            g2.setColor(Color.BLACK);
            g2.fillOval(this.source.getCoorX()-4, this.source.getCoorY()-4, 20, 20);
        }

        if(dest != null){
            this.destination = dest;
            g2.setColor(Color.BLACK);
            g2.fillOval(this.destination.getCoorX()-4, this.destination.getCoorY()-4, 20, 20);
        }

        if(this.destination!=null){
            g2.setColor(Color.BLACK);
            g2.fillOval(this.destination.getCoorX()-4, this.destination.getCoorY()-4, 15, 15);
        }

        if(this.source != null && this.destination != null){
            ArrayList<Sommet> sommets = new ArrayList<>();
            Sommet som_source = this.vue_graphe.getGraphe().getSommets().stream().filter(s -> s.getNom_sommet().equals(this.source.getNom() + " ")).findFirst().get();
            Sommet som_dest = this.vue_graphe.getGraphe().getSommets().stream().filter(s -> s.getNom_sommet().equals(this.destination.getNom() + " ")).findFirst().get();

            Map<Integer, Integer> map = this.vue_graphe.getGraphe().dikjstra(som_source, som_dest);
            for(Integer i : map.keySet()){
                Sommet som = this.vue_graphe.getGraphe().getSommets().stream().filter(s -> s.getNum_sommet() == i).findFirst().get();
                sommets.add(som);
                Vue_sommet vue = this.vue_graphe.getVue_sommets().stream().filter(v -> (v.getNom() +" ").equals(som.getNom_sommet())).findFirst().get();
                g2.fillOval(vue.getCoorX()-4, vue.getCoorY()-4, 7, 7);



                //textArea.setText("\n"+ );


                //this.textArea.setText("\n" + this.textArea.getText() + "\n"+vue.getNom() + "\n" + vue2.getNom());

            }

            drawArete(g, sommets);

        }





    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3F);
        g2.setComposite(ac);
        g.drawImage(background,0,0,987,952, this);

         ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F);
        g2.setComposite(ac);

/*
        for (Vue_sommet s : vue_graphe.getVue_sommets()) {

            g2.fillOval(s.getCoorX()-4, s.getCoorY()-4, 7, 7);
        }

 */
        g2.setStroke(new BasicStroke(2));
      //  drawArete(g2);


    }

    public void drawArete(Graphics g, ArrayList<Sommet> sommets){

        /*for(Arete arete : this.vue_graphe.getGraphe().getAretes()){


            Sommet a = this.vue_graphe.getGraphe().getSommets().get(arete.getNum_sommet1());
            Sommet b = this.vue_graphe.getGraphe().getSommets().get(arete.getNum_sommet2());

            Vue_sommet vue = this.vue_graphe.getVue_sommets().stream().filter(s -> (s.getNom()+" ").equals(a.getNom_sommet())).findAny().get();

            Vue_sommet vue2 = this.vue_graphe.getVue_sommets().stream().filter(s -> (s.getNom()+" ").equals(b.getNom_sommet())).findFirst().get();

            g.setColor(chooseColor(a));

            g.drawLine(vue.getCoorX(),vue.getCoorY(),vue2.getCoorX(),vue2.getCoorY());

        }*/

        for(Arete arete : this.vue_graphe.getGraphe().getAretes()){
            LinkedHashSet<String> display = new LinkedHashSet<>();

            try {
                Sommet a = sommets.stream().filter(s -> s.getNum_sommet() == arete.getNum_sommet1()).findFirst().get();
                Sommet b = sommets.stream().filter(s -> s.getNum_sommet() == arete.getNum_sommet2()).findFirst().get();


                display.add(a.getNom_sommet());




                Vue_sommet vue = this.vue_graphe.getVue_sommets().stream().filter(s -> (s.getNom() + " ").equals(a.getNom_sommet())).findAny().get();

                Vue_sommet vue2 = this.vue_graphe.getVue_sommets().stream().filter(s -> (s.getNom() + " ").equals(b.getNom_sommet())).findFirst().get();

                g.setColor(chooseColor(a));


                g.drawLine(vue.getCoorX(), vue.getCoorY(), vue2.getCoorX(), vue2.getCoorY());


            }catch (Exception e){

            }


            String text=textArea.getText();
            String words[]=text.split("\\s");

            for(String str : display){
                textArea.append("       " + str + "\n");
                System.out.println(str);
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
/*
        App app = new App();
        JPanel panel = new JPanel();
        JFrame frame = new JFrame("Graph");
        app.setBorder(new LineBorder(Color.BLACK));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.add(app, BorderLayout.CENTER);
        frame.add(panel);
        panel.setSize(950, 1000);
        frame.pack();
        frame.add(app, BorderLayout.CENTER);
        frame.setSize(1500, 1000);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
*/
        JFrame frame = new JFrame("Graph");

        JSplitPane splitPane;
        App app;
        JPanel bottomPanel;
        JScrollPane scrollPane;
        JTextArea textArea;

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



        //trier par odre alphabetique
        Collections.sort(list_combobox);
        String test[] =  list_combobox.toArray(new String[0]);

        JComboBox comboBox_src = new JComboBox<String>(test);
        JComboBox comboBox_dest = new JComboBox<String>(test);

        comboBox_src.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = comboBox_src.getSelectedItem().toString();
                System.out.println("Station source choose : "+ value);
                Vue_sommet vue_sommet = app.vue_graphe.getVue_sommets().stream().filter(s -> s.getNom().equals(value)).findFirst().get();
                System.out.println(vue_sommet.getNom());
                // set la source
                app.update(frame.getGraphics(), vue_sommet, null);
            }
        });



        comboBox_dest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = comboBox_dest.getSelectedItem().toString();
                System.out.println("Station destination choose : "+ value);
                Vue_sommet vue_sommet = app.vue_graphe.getVue_sommets().stream().filter(s -> s.getNom().equals(value)).findFirst().get();
                System.out.println(vue_sommet.getNom());
                // set la dest
                app.update(frame.getGraphics(), null, vue_sommet);
            }
        });



        splitPane = new JSplitPane();

        app.setBorder(new LineBorder(Color.BLACK));

        bottomPanel = new JPanel();
        bottomPanel.setBorder(new LineBorder(Color.blue));

  //      scrollPane = new JScrollPane();
     //   scrollPane.setBorder(new LineBorder(Color.green));


        inputPanel = new JPanel();

        frame.setPreferredSize(new Dimension(1500, 1005));
        frame.getContentPane().setLayout(new GridLayout());
        frame.getContentPane().add(splitPane);

        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(990);
        splitPane.setLeftComponent(app);
        splitPane.setRightComponent(bottomPanel);

        //bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.add(inputPanel);
        bottomPanel.add(inputPanel, BorderLayout.NORTH);

        app.textArea.setPreferredSize(new Dimension(440, 500));
        app.textArea.setBorder(new LineBorder(Color.pink));
        bottomPanel.add(app.textArea, BorderLayout.SOUTH);

        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        //inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.add(comboBox_src, BorderLayout.NORTH);
        inputPanel.add(comboBox_dest, BorderLayout.SOUTH);


        comboBox_src.setPreferredSize(new Dimension(220, 50));
        comboBox_dest.setPreferredSize(new Dimension(220, 50));

        comboBox_src.setMaximumSize(new Dimension(200, 500));
        comboBox_dest.setMaximumSize(new Dimension(200, 500));


        inputPanel.setBorder(new LineBorder(Color.red));
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

public class Sommet {

    private int num_sommet;
    private String nom_sommet;
    private String num_ligne;
    private boolean terminus;
    private int branchement;
    private boolean sommetVisite = false;

    private Sommet previousSommet = null;


    public boolean isSommetVisite() {
        return sommetVisite;
    }

    public void setSommetVisite(boolean sommetVisite) {
        this.sommetVisite = sommetVisite;
    }

    public Sommet(int ns, String nom, String num, boolean t, int b)
    {
        this.num_sommet = ns;
        this.nom_sommet = nom;
        this.num_ligne = num;
        this.terminus = t;
        this.branchement = b;
    }

    public Sommet(int ns) {
        this.num_sommet = ns;
    }

    public Sommet() { }

    public int getNum_sommet() {
        return this.num_sommet;
    }

    public void setNum_sommet(int num_sommet) {
        this.num_sommet = num_sommet;
    }

    public String getNom_sommet() {
        return this.nom_sommet;
    }

    public void setNom_sommet(String nom_sommet) {
        this.nom_sommet = nom_sommet;
    }

    public String getNum_ligne() {
        return this.num_ligne;
    }

    public void setNum_ligne(String num_ligne) {
        this.num_ligne = num_ligne;
    }

    public boolean isTerminus() {
        return this.terminus;
    }

    public void setTerminus(boolean terminus) {
        this.terminus = terminus;
    }

    public int getBranchement() {
        return this.branchement;
    }

    public void setBranchement(int branchement) {
        this.branchement = branchement;
    }

    @Override
    public String toString() {
        return getNom_sommet() + " " + getNum_sommet() + " " + getNum_ligne() + " " + isTerminus() + " " + getBranchement();
    }

    public void setPrevoius(Sommet sommetMin) {
        this.previousSommet = sommetMin;
    }

    public Sommet getPrevoius() {
        return this.previousSommet;
    }
}

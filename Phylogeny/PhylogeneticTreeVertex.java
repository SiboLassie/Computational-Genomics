/**
 * Computational-Genomics 
 * Part 2 - Phylogeny.
 * @author Avihay D Hemo
 */
 
 
public class PhylogeneticTreeVertex {

    private float distance;
    private int sisI, sisJ;
    private float limbI, limbJ;
    private PhylogeneticTreeVertex parent;
    private boolean isRoot = false;
    private int label;


    public float getDistance() {
        return distance;
    }

    public int getSisI() {
        return sisI;
    }

    public int getSisJ() {
        return sisJ;
    }

    public float getLimbI() {
        return limbI;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public float getLimbJ() {
        return limbJ;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setSisI(int sisI) {
        this.sisI = sisI;
    }

    public void setSisJ(int sisJ) {
        this.sisJ = sisJ;
    }

    public void setLimbI(float limbI) {
        this.limbI = limbI;
    }

    public void setLimbJ(float limbJ) {
        this.limbJ = limbJ;
    }

    public void setParent(PhylogeneticTreeVertex parent) {
        this.parent = parent;
    }

    public PhylogeneticTreeVertex getParent() {
        return parent;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getLabel() {
        return label;
    }

    public void setRoot(){
        this.isRoot = true;
    }

    public PhylogeneticTreeVertex() {

        this.parent = null;
    }
}

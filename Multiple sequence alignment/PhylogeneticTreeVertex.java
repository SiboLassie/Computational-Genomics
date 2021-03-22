/**
 * Computational-Genomics 
 * Part 3 - Multiple sequence alignment.
 * @author Avihay D Hemo
 */
 
public class PhylogeneticTreeVertex {

    private double distance;
    private int sisI, sisJ;
    private double limbI, limbJ;
    private PhylogeneticTreeVertex parent;
    private boolean isRoot = false;
    private int label;


    public double getDistance() {
        return distance;
    }

    public int getSisI() {
        return sisI;
    }

    public int getSisJ() {
        return sisJ;
    }

    public double getLimbI() {
        return limbI;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public double getLimbJ() {
        return limbJ;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setSisI(int sisI) {
        this.sisI = sisI;
    }

    public void setSisJ(int sisJ) {
        this.sisJ = sisJ;
    }

    public void setLimbI(double limbI) {
        this.limbI = limbI;
    }

    public void setLimbJ(double limbJ) {
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

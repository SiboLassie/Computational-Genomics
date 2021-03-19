/**
 * Computational-Genomics 
 * Part 2 - Phylogeny.
 * @author Avihay D Hemo
 */

public class NeighborJoining {

    private float[][] nJmatrix;
    private float[][] distanceMatrix;
    private float[] totalD;
    private float min;
    private int sisI, sisJ;
    private float deltaIJ;
    private float limbI, limbJ;
    private float[][] newMatrix;


    public int getSisI() {
        return sisI;
    }

    public int getSisJ() {
        return sisJ;
    }

    public float getLimbI() {
        return limbI;
    }

    public float getLimbJ() {
        return limbJ;
    }

    public void printV() {
        System.out.print("\n\nd matrix:\n ");
        for(int i=0; i<distanceMatrix.length; i++) {
            for (int j = 0; j<distanceMatrix.length; j++) {
                System.out.print(distanceMatrix[i][j] + "   ");
            }
            System.out.println();
        }
        System.out.println();System.out.println();
        System.out.print("nj matrix:\n ");
        for(int i=0; i<nJmatrix.length; i++) {
            for (int j = 0; j<nJmatrix.length; j++) {
                System.out.print(nJmatrix[i][j] + "   ");
            }
            System.out.println();
        }
        System.out.println();System.out.println();
        System.out.print("array:\n ");
        for (int i=0; i<totalD.length; i++) {
            System.out.print(totalD[i] + "   ");
        }
        System.out.println();System.out.println();
        System.out.print("min:\n " + min);

        System.out.print("\ndelta:\n " + this.deltaIJ);
        System.out.print("\nlimb i:\n " + this.limbI);
        System.out.print("\nlimb j:\n " + this.limbJ);

    }

    public float[][] computeNJMatrix() {
        //init the nj matrix
        for(int i=0; i<distanceMatrix.length; i++) {
            for (int j = 0; j<distanceMatrix.length; j++) {
                nJmatrix[i][j] = (distanceMatrix[i][j]) - totalD[i] - totalD[j];
                if(i==j) {
                    nJmatrix[i][j] = 0;
                }
            }
        }
        //find minimum
        this.min = nJmatrix[nJmatrix.length-1][nJmatrix.length-2];
        this.sisI = nJmatrix.length-1;
        this.sisJ = nJmatrix.length-2;
        for(int i=0; i<nJmatrix.length; i++) {
            for (int j = 0; j<nJmatrix.length; j++) {
                if (this.min != Math.min(min, nJmatrix[i][j])) {
                    this.min = Math.min(min, nJmatrix[i][j]);
                    this.sisI = i;
                    this.sisJ = j;
                }
            }
        }
        //find delta i,j and limbs
        this.deltaIJ = (totalD[sisI] - totalD[sisJ])/(totalD.length - 2);
        this.limbI = ( (distanceMatrix[this.sisI][this.sisJ]/2) + ((totalD[sisI] - totalD[sisJ])/2) );
        this.limbJ = ( (distanceMatrix[this.sisI][this.sisJ]/2) + ((totalD[sisJ] - totalD[sisI])/2) );

        this.newMatrix = new float[this.distanceMatrix.length-1][this.distanceMatrix.length-1];
        this.newMatrix[0][0] = 0;

        for(int i=0, o=0; i<distanceMatrix.length-1; i++, o++) {
            for (int j=0, k=0; j<distanceMatrix.length-1; j++) {
                if (i != this.sisI || j!= this.sisJ) {
                    newMatrix[o][k] = distanceMatrix[i][j];
                    k++;
                }
            }
        }
        //TODO create a new vertex here or connect one

        for(int i=0, j=0; i<=newMatrix.length-1; i++ ) {
            while (j<distanceMatrix.length-2) {
                if ( j != this.sisI && j != this.sisJ ) {
                    newMatrix[newMatrix.length-1][i] = ( distanceMatrix[this.sisI][j] + distanceMatrix[j][this.sisJ] - distanceMatrix[this.sisI][this.sisJ] )/2;
                    newMatrix[i][newMatrix.length-1] = ( distanceMatrix[this.sisI][j] + distanceMatrix[j][this.sisJ] - distanceMatrix[this.sisI][this.sisJ] )/2;
                    i++;
                }
                j++;
            }
        }
        return newMatrix;
    }

    public NeighborJoining(float[][] distanceMatrix) {

        this.distanceMatrix = distanceMatrix;

        this.nJmatrix = new float[distanceMatrix.length][distanceMatrix.length];

        //init totalD array (divergance)
        this.totalD = new float[distanceMatrix.length];
        for(int i=0; i<distanceMatrix.length; i++) {
            for(int j=0; j<distanceMatrix.length; j++) {
                this.totalD[i] = (this.totalD[i] + distanceMatrix[i][j]);
            }
            this.totalD[i] = this.totalD[i]/(distanceMatrix.length-2);
        }
    }
}

/**
 * Computational-Genomics 
 * Part 1 - Pairwise sequence alignment.
 * @author Avihay D Hemo
 */

public class AffineGap {

    private static float[][] g;
    private static float[][] f;
    private static float[][] e;

    /**
     * A method for computing the Affine Gap Penalty model Alignment.
     *
     * @param sequence1 A String representing the first amino acid sequence.
     * @param sequence2 A String representing the second amino acid sequence.
     * @param matchScore A float representing the score assigned to matches.
     * @param mismatchScore A float representing the score assigned to mismatches.
     * @param indelScore A float representing the score assigned to insertions and deletions.
     */
    public static float[][] computeMatrix(String sequence1, String sequence2, float matchScore, float mismatchScore, float indelScore, float gapsPenalty) {
        sequence1 = "-" + sequence1;
        sequence2 = "-" + sequence2;

        float[][] resultMatrix = new float[sequence1.length()][sequence2.length()];
        g = new float[sequence1.length()][sequence2.length()];
        f = new float[sequence1.length()][sequence2.length()];
        e = new float[sequence1.length()][sequence2.length()];


        //TODO base condition is V(i,0)= F(i,0) = Wg + i*Ws     &&    V(0,j) = E(0,j) = Wg + j*Ws
        for (int i = 0; i < sequence1.length(); i++) {
            f[i][0] = gapsPenalty + i*indelScore;
            resultMatrix[i][0] = f[i][0];
        }
        for (int j = 0; j < sequence2.length(); j++) {
            e[0][j] = gapsPenalty + j*indelScore;
            resultMatrix[0][j] = e[0][j];
        }
		
        //TODO matrix V(i,j) = max(G(i,j), E(i,j), F(i,j))
        //TODO G(i,j) = V(i-1, j-1)
        //TODO E(i,j) = max(E(i,j-1)+gapsPenatly, G(i, j-1)+gapsP+indels, F(i, j-1)+gapsP+indels )
        //TODO F(i,j) = max(F(i-1,j)+gapsPenatly, G(i-1, j)+gapsP+indels, E(i-1, j)+gapsP+indels )

        for (int i = 1; i < sequence1.length(); i++) {
            for (int j = 1; j < sequence2.length(); j++) {
                g[i][j] =   resultMatrix[i-1][j-1] + (sequence1.charAt(i) == sequence2.charAt(j) ? matchScore : mismatchScore);
                e[i][j] = Math.max( Math.max( e[i][j-1]+gapsPenalty, g[i][j-1]+gapsPenalty+indelScore),  f[i][j-1]+gapsPenalty+indelScore );
                f[i][j] = Math.max( Math.max( f[i-1][j]+gapsPenalty, g[i-1][j]+gapsPenalty+indelScore),  e[i-1][j]+gapsPenalty+indelScore );

                resultMatrix[i][j] = Math.max( Math.max( g[i][j], e[i][j] ), f[i][j] );
            }
        }
        return resultMatrix;
    }

    /**
     * A utility method for computing the best Affine Gap Penalty model alignment for two sequences.
     *
     * @param similarityMatrix A 2D-array representing the previously computed global max similarity matrix.
     * @param sequence1 A String representing the first amino acid sequence.
     * @param sequence2 A String representing the second amino acid sequence.
     */
    public static OptimalAlignment obtainOptimalAlignmentByDownmostOrder(float similarityMatrix[][], String sequence1, String sequence2) {

        int i = similarityMatrix.length - 1;
        int j = similarityMatrix[0].length - 1;
        StringBuilder alignment1Builder = new StringBuilder();
        StringBuilder alignment2Builder = new StringBuilder();

        sequence1 = "-" + sequence1;
        sequence2 = "-" + sequence2;

        while (i != 0 && j != 0) {
            if (similarityMatrix[i][j] == (e[i][j])) {
                alignment1Builder.insert(0, "-");
                alignment2Builder.insert(0, sequence2.charAt(j));
                j--;
            } else if (similarityMatrix[i][j] == (g[i][j])) {
                alignment1Builder.insert(0, sequence1.charAt(i));
                alignment2Builder.insert(0, sequence2.charAt(j));
                i--;
                j--;
            } else if (similarityMatrix[i][j] == (f[i][j])){
                alignment1Builder.insert(0, sequence1.charAt(i));
                alignment2Builder.insert(0, "-");
                i--;
            }
        }
        System.out.print(alignment1Builder.toString()+"\n");
        System.out.print(alignment2Builder.toString()+"\n");

        return new OptimalAlignment(alignment1Builder.toString(), alignment2Builder.toString());
    }
}

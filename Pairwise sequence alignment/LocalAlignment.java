/**
 * Computational-Genomics 
 * Part 1 - Pairwise sequence alignment.
 * @author Avihay D Hemo
 */

public class LocalAlignment {

    /**
     * A utility method for computing the local max similarity matrix.
     *
     * @param sequence1 A String representing the first amino acid sequence.
     * @param sequence2 A String representing the second amino acid sequence.
     * @param matchScore A float representing the score assigned to matches.
     * @param mismatchScore A float representing the score assigned to mismatches.
     * @param indelScore A float representing the score assigned to insertions and deletions.
     */
    public static float[][] computeMatrix(String sequence1, String sequence2, float matchScore, float mismatchScore, float indelScore) {

        float[][] resultMatrix = new float[sequence1.length()][sequence2.length()];

        for (int i = 0; i < sequence1.length(); i++) {
            resultMatrix[i][0] = 0;
        }
        for (int j = 0; j < sequence2.length(); j++) {
            resultMatrix[0][j] = 0;
        }

        for (int i = 1; i < sequence1.length(); i++) {
            for (int j = 1; j < sequence2.length(); j++) {
                resultMatrix[i][j] = Math.max(resultMatrix[i - 1][j] + indelScore,
                        Math.max(resultMatrix[i][j - 1] + indelScore, resultMatrix[i - 1][j - 1] +
                                (sequence1.charAt(i) == sequence2.charAt(j) ? matchScore : mismatchScore)));
            }
        }
        float maxScore = resultMatrix[sequence1.length()-1][sequence2.length()-1];
        System.out.print(maxScore + "\n");
        int localx = sequence2.length()-1;
        int localy = sequence1.length()-1;

        for (int i = 1; i < sequence1.length(); i++) {
            for (int j = 1; j < sequence2.length(); j++) {
                if (maxScore != Math.max(maxScore, resultMatrix[i][j])) {
                    maxScore = Math.max(maxScore, resultMatrix[i][j]);
                    localx = j;
                    localy = i;
                }
            }
        }
        System.out.print(maxScore + "\n");

        float[][] localMatrix = new float[localy+1][localx+1];
        for (int i = 1; i <= localy; i++) {
            for (int j = 1; j <= localx; j++) {
                localMatrix[i][j] = resultMatrix[i][j];
            }
        }


		/*alternate computing*/
		
        /*for (int i = 0; i <= resultMatrix[0].length; i++) {
            if (i == 0) {
                System.out.print("\t\t");
            } else {
                System.out.print(sequence2.charAt(i-1) + "\t");
            }
        }

        System.out.println();

        for (int i = 0; i < resultMatrix.length; i++) {
            if (i == 0) {
                System.out.print("\t");
            } else {
                System.out.print(sequence1.charAt(i-1) + "\t");
            }
            for (int j = 0; j < resultMatrix[0].length; j++) {
                System.out.print(String.format("%.2f\t", resultMatrix[i][j]));
            }
            System.out.println();
        }
        System.out.println("_________________________________________________________");

        for (int i = 0; i <= localMatrix[0].length; i++) {
            if (i == 0) {
                System.out.print("\t\t");
            } else {
                System.out.print(sequence2.charAt(i-1) + "\t");
            }
        }

        System.out.println();

        for (int i = 0; i < localMatrix.length; i++) {
            if (i == 0) {
                System.out.print("\t");
            } else {
                System.out.print(sequence1.charAt(i-1) + "\t");
            }
            for (int j = 0; j < localMatrix[0].length; j++) {
                System.out.print(String.format("%.2f\t", localMatrix[i][j]));
            }
            System.out.println();
        }*/


        return localMatrix;
    }

    /**
     * A utility method for computing the best local alignment for two sequences.
     *
     * @param similarityMatrix A 2D-array representing the previously computed global max similarity matrix.
     * @param sequence1 A String representing the first amino acid sequence.
     * @param sequence2 A String representing the second amino acid sequence.
     * @param matchScore A float representing the score assigned to matches.
     * @param mismatchScore A float representing the score assigned to mismatches.
     * @param indelScore A float representing the score assigned to insertions and deletions.
     */
    public static OptimalAlignment obtainOptimalAlignmentByDownmostOrder(float similarityMatrix[][],
                                                                         String sequence1, String sequence2, double matchScore, double mismatchScore, double indelScore) {

        int i = similarityMatrix.length - 1;
        int j = similarityMatrix[0].length - 1;
        StringBuilder alignment1Builder = new StringBuilder();
        StringBuilder alignment2Builder = new StringBuilder();

        while (i != 0 && j != 0) {
            if (similarityMatrix[i][j] == (similarityMatrix[i][j - 1] + indelScore)) {
                alignment1Builder.insert(0, "-");
                alignment2Builder.insert(0, sequence2.charAt(j));
                j--;
            } else if (similarityMatrix[i][j] == (similarityMatrix[i - 1][j - 1] + (sequence1.charAt(i) == sequence2.charAt(j) ? matchScore : mismatchScore))) {
                alignment1Builder.insert(0, sequence1.charAt(i));
                alignment2Builder.insert(0, sequence2.charAt(j));
                i--;
                j--;
            } else if (similarityMatrix[i][j] == (similarityMatrix[i - 1][j] + indelScore)){
                alignment1Builder.insert(0, sequence1.charAt(i));
                alignment2Builder.insert(0, "-");
                i--;
            }
        }

        return new OptimalAlignment(alignment1Builder.toString(), alignment2Builder.toString());
    }

}

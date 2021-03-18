# Computational Genomics Software
A Java Project of Computational Genomics that contain: Pairwise sequence alignment, Phylogeny and Multiple sequence alignment.



# Pairwise sequence alignment
  A program that calculates pairwise sequence alignments of two sequences (DNA, RNA, or proteins).

o	Input (a simple GUI for input parameters):

  •	Text field/s for two sequences (allowed characters a-z and A-Z)

  •	Fields with values for substitution matrix. For example:
   - match 2
   - mismatch -1
   - indel -1.5
   - gap 0

  • Radio buttons to choose the alignment algorithm:
    -	Global alignment
    -	End space free alignment
    -	Local alignment
    -	Affine gap penalty model alignment

  • Button to start the algorithm.

o	Output (GUI window with alignment output)
  -	Pairwise alignment showing all matches, subs and indels).
  -	Score of the alignment, number of matches, substitutions and indels.

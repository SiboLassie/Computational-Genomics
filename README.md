# Computational Genomics Software
A Java Project of Computational Genomics that contain 3 parts:

Pairwise sequence alignment, Phylogeny and Multiple sequence alignment.



# Pairwise sequence alignment
  A program that calculates pairwise sequence alignments of two sequences (DNA, RNA, or proteins).

o	Input (a simple GUI for input parameters):
 
    • Text field/s for two sequences (allowed characters a-z and A-Z)

    • Fields with values for substitution matrix. For example:
      o match 2
      o mismatch -1
      o indel -1.5
      o gap 0

    • Radio buttons to choose the alignment algorithm:
      o Global alignment
      o End space free alignment
      o Local alignment
      o Affine gap penalty model alignment

    • Button to start the algorithm.

o	Output (GUI window with alignment output):

     • Pairwise alignment showing all matches, subs and indels.
     • Score of the alignment, number of matches, substitutions and indels.


# Phylogeny
  A program that calculates the phylogenetic tree of k>1 sequences (DNA, RNA or proteins).

o	Input (a simple GUI for input parameters):
 
    • One text field for k sequences of unlimited length with ">" at the beginning of each sequence. For example:
      o >ATTGCCTTC
      o >ATCGTTCT
      o >GTCGTACTAG
      
    • Fields with distance values for substitution matrix. For example:
      o match 0
      o mismatch 1
      o indel 1.5

    • Button to start the algorithm.

o	Output (GUI window with alignment output):

     • Distance matrix – matrix containing distances between each pair of input sequences. 
       
       *Calculate the distance between two sequences useing global pairwise alignment algorithm.
       
     • Phylogenetic tree with distances on edges and labels on leafs, 
       the root is in the middle of the last edge added to the tree.
       
       *For labels display using indexes of sequences in the input. 
       *Using neighbor joining (NJ) clustering algorithm to calculate the tree. 
       *Using divergence to find sister leafs.

*sample-sequences.txt is a general test of multiple sequences and can be run to check the phylogeny works well.

# Multiple sequence alignment
  A program that calculates multiple sequence alignment of k>1 sequences using Progressive Alignment Method.

o	Algorithm used:
 
    • Progressive Alignment Method algorithm (like used in ClustalW), to find an approximation of multiple sequence alignment.
      
    • The algorithm contain the following stages: 
      o Calculate pairwise alignments and distance matrix.
      o Build guide tree - using Neighbor Joining clustering method.
      o Create a progressive multiple sequence alignment, guided by guide tree (using profiles to align alignments).

o	Input (a simple GUI for input parameters):
 
    • One text field for k sequences of unlimited length with ">" at the beginning of each sequence. For example:
      o >ATTGCCTTC
      o >ATCGTTCT
      o >GTCGTACTAG
      
    • Fields with distance values for substitution matrix. For example:
      o match 0
      o mismatch 1
      o indel 1.5

    • Button to start the algorithm.

o	Output (GUI window with alignment output):

     • Distance matrix – matrix containing distances between each pair of input sequences. 
       
       *Calculate the distance between two sequences useing progressive alignment method algorithm.
       
     • Guide tree with distances on edges and labels on leafs, 
       the root is in the middle of the last edge added to the tree.
       
       *For labels display using indexes of sequences in the input. 
       *Using neighbor joining (NJ) clustering algorithm to calculate the tree. 
       *Using divergence to find sister leafs.
       *The output window able to display guide tree of at least 10 sequences and the alignment of at least 10 sequences of size 80 completely without using scroll bars etc.
       *For more sequences, or longer sequences using a scroll bars.
      
      • Multiple sequence alignment found by algorithm.
      
      •	Score of multiple sequence alignment.

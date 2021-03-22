/**
 * Computational-Genomics 
 * Part 3 - Multiple sequence alignment.
 * @author Avihay D Hemo
 */

import clustering.Cluster;
import clustering.visualization.DendrogramPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import static clustering.visualization.DendrogramPanel.createMyCluster;


public class GUI extends JFrame {

    private myPanel mainPanel;
    private outputPanel oputPanel;

    public void goBack() {
        oputPanel.setVisible( false );
        mainPanel.setVisible(true);
    }

    public void nextPanel(double distanceMatrix[][], Cluster cluster, ArrayList<OptimalAlignment> arrAlgn) {
        oputPanel = new outputPanel(this, distanceMatrix, cluster, arrAlgn);
        oputPanel.add(Box.createRigidArea(new Dimension(800, 1000)));
        oputPanel.setVisible(true);
        mainPanel.setVisible(false);
        this.add(oputPanel);
    }

    //Constructor:
    /**
     * This is the GUI constructor, init the main frame.
     * then set the Panels for the controls,
     * and add everything into the main frame.
     *
     * @throws HeadlessException to know if there is an head less exception.
     */
    private GUI() throws HeadlessException {

        super("Multiple alignment calculator");
        this.setResizable(false);

        mainPanel = new myPanel(this);
        mainPanel.add(Box.createRigidArea(new Dimension(800, 800)));

        add(mainPanel);

    }

    /**
     * This is a inner private class to custom our own build JPanel.
     * this class purpose is to help us build our user interface into a panel.
     * this class extends JPanel and implement ActionListener & Runnable
     *
     * the class use:
     * JPanel--> for our interface to have Panel features and behave like a Panel.
     * ActionListener--> to handle events that happens by user input & button clicks.
     */
    private class myPanel extends JPanel implements ActionListener {
        private JFrame theFrame;
        private JLabel sequence1;
        private JTextArea sequence1area;
        private JLabel matchLabel;
        Box inputsBox = Box.createHorizontalBox();
        private JTextField matchTextField;
        private JLabel mismatchLabel;
        private JTextField misMatchTextField;
        private JLabel indelLabel;
        private JTextField indelTextField;
        private JButton calculateButton;
        Box mainBox = Box.createVerticalBox();

        /// action listener stuff
        private double[][] computedMatrix;
        private double[][] distanceMatrix;
        private String sequencesInput[];
        private String matchInput;
        private int match;
        private String mismatchInput;
        private int mismatch;
        private String indelInput;
        private int indel;
        private double score;
        private ArrayList<OptimalAlignment> arrAlgn;

        /**
         * This is an Override function of Interface ActionListener.
         * this function purpose is to handle all of the events,
         * that can happens by user inputs and clicks of buttons,
         * also here we implement the function of the alignment algorithm.
         *
         * @param e is the event that happen.
         */
        @Override
        public synchronized void actionPerformed(ActionEvent e) {
            // if there is a click on 'Calculate Button'
            if (e.getSource() == mainPanel.calculateButton) {
                // we try to get the user inputs
                try {
                    sequencesInput = mainPanel.sequence1area.getText().split(">");
                    ArrayList<String> arrList = new ArrayList<>(Arrays.asList(sequencesInput)) ;
                    if (sequencesInput != null) {
                        matchInput = mainPanel.matchTextField.getText();
                        mismatchInput = mainPanel.misMatchTextField.getText();
                        indelInput = mainPanel.indelTextField.getText();
                        if (matchInput != null && mismatchInput != null && indelInput != null) {
                            try {
                                match = Integer.parseInt(matchInput);
                                mismatch = Integer.parseInt(mismatchInput);
                                indel = Integer.parseInt( indelInput );
                                arrAlgn = new ArrayList<>(arrList.size());

                                int l=0;
                                distanceMatrix = new double[arrList.size()][arrList.size()];
                                for (int i=0; i < arrList.size(); i++) {
                                    distanceMatrix[i][i] = 0;
                                }
                                for (int i=0; i<arrList.size()-1; i++, l++) {
                                    for (int j=i+1; j<arrList.size(); j++) {
                                        computedMatrix = NeedlemanWunsch.computeMatrix(arrList.get(i), arrList.get(j), match, mismatch, indel);
                                        distanceMatrix[i][j] = computedMatrix[computedMatrix.length - 1][computedMatrix[0].length - 1];
                                        if (i%2==0){
                                            arrAlgn.add(NeedlemanWunsch.obtainOptimalAlignmentByDownmostOrder( computedMatrix, arrList.get( i ), arrList.get( j), match,mismatch,indel));

                                        }

                                    }
                                }
                                for (int i=0; i < arrList.size(); i++) {
                                    for (int j =0; j < distanceMatrix.length; j++) {
                                        if (i==j)
                                            break;
                                        distanceMatrix[i][j] = distanceMatrix[j][i];
                                    }
                                }
//                                
                                double[][] test = {{0,13,21,22},
                                                    {13,0,12,13},
                                                    {21,12,0,13},
                                                    {22,13,13,0},};

                                float[][] test3 = {{0,2,7,4,7},
                                                    {2,0,7,4,7},
                                                    {7,7,0,7,6},
                                                    {4,4,7,0,7},
                                                    {7,7,6,7,0},};

                                double[][] test4 = {{0,17,21,27},
                                                   {17,0,12,18},
                                                   {21,12,0,14},
                                                   {27,18,14,0},};

//                                score = distanceMatrix[computedMatrix.length - 1][computedMatrix[0].length - 1];

                                String[] names = new String[distanceMatrix.length];
                                for (int i=0; i<distanceMatrix.length; i++){
                                    names[i] = Integer.toString( i+1 );
                                }


                                JFrame frame = new JFrame();
                                frame.setSize(800, 300);
                                frame.setLocation(400, 300);
                                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                                JPanel content = new JPanel();
                                DendrogramPanel dp = new DendrogramPanel();

                                frame.setContentPane(content);
                                content.setBackground(Color.red);
                                content.setLayout(new BorderLayout());
                                content.add(dp, BorderLayout.CENTER);
                                dp.setBackground(Color.WHITE);
                                dp.setLineColor(Color.BLACK);
                                dp.setScaleValueDecimals(0);
                                dp.setScaleValueInterval(1);
                                dp.setShowDistances(false);

                                Cluster cluster = createMyCluster(distanceMatrix, names);
                                dp.setModel(cluster);
                                frame.setVisible(true);

                               // theTree = constructTree(test, null);

                                nextPanel(distanceMatrix, cluster, arrAlgn);

                            } catch (Exception e1) {
                                // if there is an exception the user get an Error message.
                                JOptionPane.showMessageDialog( null, "The values match / mismatch / indel has to be an integers!" );
                                e1.printStackTrace();
                            }
                        }
                    }
                    else JOptionPane.showMessageDialog(null, "Please Enter at least 2 sequences!");
                } catch (Exception e1) { // might be exceptions.
                    e1.printStackTrace();
                }
            }
        }

        /**
         * This is the class Constructor,
         * here we actually takes care of how's everything gonna look eventually on our control panel.
         *
         * @param mainPanel is a reference of the main Frame of the GUI.
         */
        private myPanel(JFrame mainPanel) {
            // getting a reference of the main Frame,
            theFrame = mainPanel;


            // first we create a rigid arena and add to the vertical box,
            // then set our label/or other controls.
            mainBox.add(Box.createRigidArea(new Dimension(0, 100)));
            sequence1 = new JLabel("Sequences:");
            sequence1.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainBox.add(sequence1);
            mainBox.add(Box.createRigidArea(new Dimension(0, 5)));

            sequence1area = new JTextArea(15,45);
            mainBox.add(new JScrollPane(sequence1area));

            mainBox.add(Box.createRigidArea(new Dimension(0, 15)));

            mainBox.add(Box.createRigidArea(new Dimension(0, 15)));
            mainBox.add(new JSeparator());

            matchLabel = new JLabel("Match: ");
            matchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainBox.add(matchLabel);
            mainBox.add(Box.createRigidArea(new Dimension(5, 10)));
            inputsBox.add(matchLabel);

            matchTextField = new JTextField(1);
            matchTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainBox.add(matchTextField);
            mainBox.add(Box.createRigidArea(new Dimension(15, 0)));
            inputsBox.add(matchTextField);

            inputsBox.add(Box.createRigidArea(new Dimension(15, 0)));
            inputsBox.add(new JSeparator(SwingConstants.VERTICAL));
            inputsBox.add(Box.createRigidArea(new Dimension(15, 0)));

            mismatchLabel = new JLabel("Mismatch: ");
            mismatchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainBox.add(mismatchLabel);
            mainBox.add(Box.createRigidArea(new Dimension(5, 0)));
            inputsBox.add(mismatchLabel);

            misMatchTextField = new JTextField(1);
            misMatchTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainBox.add(misMatchTextField);
            mainBox.add(Box.createRigidArea(new Dimension(15, 0)));
            inputsBox.add(misMatchTextField);

            inputsBox.add(Box.createRigidArea(new Dimension(15, 0)));
            inputsBox.add(new JSeparator(SwingConstants.VERTICAL));
            inputsBox.add(Box.createRigidArea(new Dimension(15, 0)));

            indelLabel = new JLabel("Indel: ");
            indelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainBox.add(indelLabel);
            mainBox.add(Box.createRigidArea(new Dimension(0, 0)));
            inputsBox.add(indelLabel);

            indelTextField = new JTextField(1);
            indelTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainBox.add(indelTextField);
            mainBox.add(Box.createRigidArea(new Dimension(15, 0)));
            inputsBox.add(indelTextField);
            mainBox.add(inputsBox);

            inputsBox.add(Box.createRigidArea(new Dimension(15, 0)));
            inputsBox.add(new JSeparator(SwingConstants.VERTICAL));
            inputsBox.add(Box.createRigidArea(new Dimension(15, 0)));

            mainBox.add(Box.createRigidArea(new Dimension(0, 15)));
            mainBox.add(new JSeparator());

            calculateButton = new JButton("Start Calculate");
            calculateButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainBox.add(Box.createRigidArea(new Dimension(0, 15)));
            mainBox.add(calculateButton);

            // adding to the main frame our sorted control vertical box
            theFrame.add(mainBox);
            add(mainBox);

            /// adding an action listener to anyone that can create an event:
            matchTextField.addActionListener(this);
            misMatchTextField.addActionListener(this);
            indelTextField.addActionListener(this);
            calculateButton.addActionListener(this);
        }
    }

    private class outputPanel extends JPanel implements ActionListener {
        Box outputMainBox = Box.createVerticalBox();
        private JFrame outputFrame;
        private JLabel distanceMatrix;
        private JTextArea outputMatrix;
        private JLabel phylogeneticTree;
        private JTextArea outputTree;
        private JLabel msa;
        private JTextArea outputMSA;
        private JButton goback;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == oputPanel.goback ) {
                goBack();
            }
        }

        public outputPanel(JFrame mainframe, double matrix[][], Cluster cluster, ArrayList<OptimalAlignment> arrAlgn) {
            outputFrame = mainframe;

            distanceMatrix = new JLabel("Distance Matrix:");
            distanceMatrix.setFont( new Font( "Courier New", Font.BOLD, 22 ) );
            distanceMatrix.setAlignmentX(Component.LEFT_ALIGNMENT);
            outputMainBox.add(distanceMatrix);
            outputMainBox.add(Box.createRigidArea(new Dimension(0, 5)));

            outputMatrix = new JTextArea( Math.max(15, matrix.length), 100);
            JScrollPane scrollPane = new JScrollPane(outputMatrix);
            scrollPane.setPreferredSize(new Dimension(750,250));
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            outputMainBox.add(scrollPane);
            outputMatrix.setEnabled(false);
            outputMatrix.setFont( new Font( "Courier New", Font.PLAIN, 20 ) );
            outputMatrix.setDisabledTextColor( Color.GRAY);
            outputMatrix.setAlignmentX(Component.CENTER_ALIGNMENT);

            outputMainBox.add(Box.createRigidArea(new Dimension(0, 10)));


            outputMatrix.append("\n      ");
            for(int k=0; k<matrix.length; k++) {
                outputMatrix.append("S"+k+"     ");
            }
            outputMatrix.append("\n     ");
            for (int i=0; i<matrix.length; i++) {
                outputMatrix.append("\nS"+i+"    ");
                for (int j =0; j < matrix.length; j++) {
                    outputMatrix.append(matrix[i][j]+"   ");
                }
            }

            phylogeneticTree = new JLabel("Phylogenetic Tree:");
            phylogeneticTree.setFont( new Font( "Courier New", Font.BOLD, 22 ) );
            phylogeneticTree.setAlignmentX(Component.LEFT_ALIGNMENT);
            outputMainBox.add(phylogeneticTree);
            outputMainBox.add(Box.createRigidArea(new Dimension(0, 5)));

            outputTree = new JTextArea( Math.max(15, matrix.length), 100);
            JScrollPane scrollPane2 = new JScrollPane(outputTree);
            scrollPane2.setPreferredSize(new Dimension(750,250));
            scrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            outputMainBox.add(scrollPane2);
            outputTree.setEnabled(false);
            outputTree.setFont( new Font( "Courier New", Font.PLAIN, 20 ) );
            outputTree.setDisabledTextColor( Color.GRAY);
            outputTree.setAlignmentX(Component.CENTER_ALIGNMENT);

            outputMainBox.add(Box.createRigidArea(new Dimension(0, 2)));
            outputMainBox.add(Box.createRigidArea(new Dimension(0, 5)));
            outputMainBox.add(new JSeparator());

            displayInfo( outputTree, 0 , cluster);

            msa = new JLabel("Multiple sequence alignment :");
            msa.setFont( new Font( "Courier New", Font.BOLD, 22 ) );
            msa.setAlignmentX(Component.LEFT_ALIGNMENT);
            outputMainBox.add(msa);
            outputMainBox.add(Box.createRigidArea(new Dimension(0, 5)));

            outputMSA = new JTextArea( Math.max(15, matrix.length), 100);
            JScrollPane scrollPane3 = new JScrollPane(outputMSA);
            scrollPane3.setPreferredSize(new Dimension(750,250));
            scrollPane3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            outputMainBox.add(scrollPane3);
            outputMSA.setEnabled(false);
            outputMSA.setFont( new Font( "Courier New", Font.PLAIN, 20 ) );
            outputMSA.setDisabledTextColor( Color.GRAY);
            outputMSA.setAlignmentX(Component.CENTER_ALIGNMENT);

            for(int k=0; k<arrAlgn.size(); k++) {
                outputMSA.append("S"+k+": " + arrAlgn.get( k )+ "\n");
            }

            outputMainBox.add(Box.createRigidArea(new Dimension(0, 5)));
            outputMainBox.add(new JSeparator());
            goback = new JButton("New Alignment");
            goback.setAlignmentX(Component.LEFT_ALIGNMENT);
            outputMainBox.add(Box.createRigidArea(new Dimension(0, 15)));
            outputMainBox.add(goback);
            outputFrame.add(outputMainBox);
            add(outputMainBox);
            goback.addActionListener( this );
        }

    }

    /**
     * This is the main to run the GUI and basically the program.
     * @param args.
     */
    public static void main(String[] args) {
        // creating a GUI object
        GUI myGui = new GUI();
        // swing JFrame.
        myGui.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        myGui.setSize(800, 1000);
        myGui.setVisible(true);
    }

    public PhylogeneticTreeVertex makeNode(NeighborJoining nj ) {
        PhylogeneticTreeVertex newNode = new PhylogeneticTreeVertex();
        newNode.setSisI(nj.getSisI());
        newNode.setSisJ(nj.getSisJ());
        newNode.setLimbI(nj.getLimbI());
        newNode.setLimbJ(nj.getLimbJ());
        newNode.setDistance(-1);
        return newNode;
    }

    public void displayInfo(JTextArea panel ,int indent, Cluster cluster) {
        for (int i = 0; i < indent; i++)
        {
            panel.append( "  " );
        }
        String name = cluster.getName() + (cluster.isLeaf() ? " (leaf)" : "") + (cluster.getDistance() != null ? "  distance: " + cluster.getDistance() : "");
        panel.append( name + "\n" );
        for (Cluster child : cluster.getChildren())
        {
            displayInfo( panel, indent+1,child );
        }
    }

    public PhylogeneticTreeVertex constructTree(double[][] dm, PhylogeneticTreeVertex preNode) {

        double[][] newM;
        NeighborJoining nj = new NeighborJoining(dm);
        newM = nj.computeNJMatrix();
        PhylogeneticTreeVertex newNode = makeNode(nj);

        if (preNode != null) {
            newNode.setLimbJ(dm[0][newNode.getSisJ()] - newNode.getLimbJ());
            newNode.setParent(preNode);
        }
        if (newM.length!=2){
            newNode = constructTree(newM, newNode);
        }
        else {
            newNode.setDistance(dm[0][newNode.getSisJ()] - newNode.getLimbJ());
            newNode.setRoot();
            return newNode;
        }
        return newNode;
    }

    public void displayTree(JTextArea panel ,PhylogeneticTreeVertex node) {
        if (node.getParent()!=null) {
            displayTree(panel ,node.getParent());
        }
        if(node.getDistance()>0) {
            panel.append( "        |" + "\n" + "        |" + "\n" + "        " + String.valueOf(node.getDistance()) + "\n" + "        |" + "\n"  + "        |" + "\n" );
        }
        panel.append( node.getSisI() + " -:");
        for (int i=0; i<8; i++) {
            panel.append("-");
            if (i==4) {
                panel.append(String.valueOf(node.getLimbI()));
            }
        }
        panel.append("\n");
        panel.append("\n");
        panel.append( node.getSisJ() + " -:");
        for (int i=0; i<10; i++) {
            panel.append("-");
            if (i==5) {
                panel.append(String.valueOf(node.getLimbJ()));
            }
        }
        panel.append("\n");
    }
}
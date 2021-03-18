/**
 * Computational-Genomics 
 * Part 1 - Pairwise sequence alignment.
 * @author Avihay D Hemo
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUI extends JFrame {

    private myPanel mainPanel;
    private outputPanel oputPanel;

    public void goBack() {
        mainPanel = new myPanel( this );
        mainPanel.add(Box.createRigidArea(new Dimension(800, 800)));
        add(mainPanel);
        oputPanel.setVisible( false );
    }

    public void nextPanel(String seq1, String seq2, float score, int indels) {
        oputPanel = new outputPanel(this, seq1, seq2, score, indels);
        oputPanel.add(Box.createRigidArea(new Dimension(800, 800)));
        oputPanel.setVisible(true);
        mainPanel.setVisible(false);
        this.add(oputPanel);
    }

    //Constructor:
    /**
     * This is the GUI constructor here we init the main frame.
     * then we set the Panels for the controls,
     * and add everything into our main frame.
     *
     * @throws HeadlessException to know if there is an head less exception.
     */
    private GUI() throws HeadlessException {

        super("Pairwise sequence alignments calculator");
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
        private JLabel sequence2;
        private JTextArea sequence2area;
        private JRadioButton globalAlignment;
        private JRadioButton endSpaceFreeAlignment;
        private JRadioButton localAlignment;
        private JRadioButton affineGapPenaltymodelAlignment;
        private JLabel matchLabel;
        Box inputsBox = Box.createHorizontalBox();
        private JTextField matchTextField;
        private JLabel mismatchLabel;
        private JTextField misMatchTextField;
        private JLabel indelLabel;
        private JTextField indelTextField;
        private JLabel gapPenatlyLabel;
        private JTextField gapPenatlyTextField;
        private JButton calculateButton;
        Box mainBox = Box.createVerticalBox();

        /// action listener stuff
        private float[][] computedMatrix;
        private String sequence1Input;
        private String sequence2Input;
        private int alignment = 1;
        private String matchInput;
        private int match;
        private String mismatchInput;
        private int mismatch;
        private String indelInput;
        private int indel;
        private String gapPenatlyInput;
        private int gapPenatly;
        private float score;
        //create a vertical box to put all of the controls together.

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

            if (e.getSource() == mainPanel.globalAlignment || e.getSource() == localAlignment || e.getSource() == endSpaceFreeAlignment) {
                gapPenatlyTextField.setEditable( false );
            }
            else if (e.getSource() == mainPanel.affineGapPenaltymodelAlignment) {
                gapPenatlyTextField.setEditable( true );
            }

            // if there is a click on 'Calculate Button'
            if (e.getSource() == mainPanel.calculateButton) {
                // we try to get the user inputs
                try {
                    sequence1Input = mainPanel.sequence1area.getText();
                    sequence2Input = mainPanel.sequence2area.getText();
                    if (mainPanel.globalAlignment.isSelected()) {
                        alignment = 1;
                    }
                    if (mainPanel.localAlignment.isSelected()) {
                        alignment = 2;
                    }
                    if (mainPanel.endSpaceFreeAlignment.isSelected()) {
                        alignment = 3;
                    }
                    if (mainPanel.affineGapPenaltymodelAlignment.isSelected()) {
                        alignment = 4;
                    }

                    if (!sequence1Input.isEmpty() &&  !sequence2Input.isEmpty()) {
                        matchInput = mainPanel.matchTextField.getText();
                        mismatchInput = mainPanel.misMatchTextField.getText();
                        indelInput = mainPanel.indelTextField.getText();
                        gapPenatlyInput = mainPanel.gapPenatlyTextField.getText();
                        if (matchInput != null && mismatchInput != null && indelInput != null) {
                            try {
                                match = Integer.parseInt(matchInput);
                                mismatch = Integer.parseInt(mismatchInput);
                                indel = Integer.parseInt( indelInput );
                                /*Global alignment part */
                                if(alignment == 1) {
                                    computedMatrix = NeedlemanWunsch.computeMatrix(sequence1Input, sequence2Input, match, mismatch, indel);
                                    score = computedMatrix[computedMatrix.length - 1][computedMatrix[0].length - 1];
                                    nextPanel(NeedlemanWunsch.obtainOptimalAlignmentByDownmostOrder(computedMatrix, sequence1Input, sequence2Input, match, mismatch, indel).getAlignment1(),
                                            NeedlemanWunsch.obtainOptimalAlignmentByDownmostOrder(computedMatrix, sequence1Input, sequence2Input, match, mismatch, indel).getAlignment2(),
                                            score, indel);
                                    for (int i = 0; i <= sequence2Input.length(); i++) {
                                        if (i == 0) {
                                            System.out.print("\t\t");
                                        } else {
                                            System.out.print(sequence2Input.charAt(i - 1) + "\t");
                                        }
                                    }
                                    System.out.println();
                                    for (int i = 0; i < computedMatrix.length; i++) {
                                        if (i == 0) {
                                            System.out.print("\t");
                                        } else {
                                            System.out.print(sequence1Input.charAt(i - 1) + "\t");
                                        }
                                        for (int j = 0; j < computedMatrix[0].length; j++) {
                                            System.out.print(String.format("%.2f\t", computedMatrix[i][j]));
                                        }
                                        System.out.println();
                                    }
                                }
                                /*Local alignment part */
                                else if (alignment == 2) {
                                    computedMatrix = LocalAlignment.computeMatrix(sequence1Input, sequence2Input, match, mismatch, indel);
                                    score = computedMatrix[computedMatrix.length - 1][computedMatrix[0].length - 1];
                                    nextPanel(LocalAlignment.obtainOptimalAlignmentByDownmostOrder(computedMatrix, sequence1Input, sequence2Input, match, mismatch, indel).getAlignment1(),
                                            LocalAlignment.obtainOptimalAlignmentByDownmostOrder(computedMatrix, sequence1Input, sequence2Input, match, mismatch, indel).getAlignment2(),
                                            score, indel);
                                    for (int i = 0; i <= computedMatrix[0].length; i++) {
                                        if (i == 0) {
                                            System.out.print("\t\t");
                                        } else {
                                            System.out.print(sequence2Input.charAt(i-1) + "\t");
                                        }
                                    }
                                    System.out.println();
                                    for (int i = 0; i < computedMatrix.length; i++) {
                                        if (i == 0) {
                                            System.out.print("\t");
                                        } else {
                                            System.out.print(sequence1Input.charAt(i-1) + "\t");
                                        }
                                        for (int j = 0; j < computedMatrix[0].length; j++) {
                                            System.out.print(String.format("%.2f\t", computedMatrix[i][j]));
                                        }
                                        System.out.println();
                                    }
                                }
                                /*End Space Free alignment part */
                                else if (alignment == 3) {
                                    computedMatrix = EndSpaceFreeAlignment.computeMatrix(sequence1Input, sequence2Input, match, mismatch, indel);
                                    score = computedMatrix[computedMatrix.length - 1][computedMatrix[0].length - 1];
                                    nextPanel(EndSpaceFreeAlignment.obtainOptimalAlignmentByDownmostOrder(computedMatrix, sequence1Input, sequence2Input, match, mismatch, indel).getAlignment1(),
                                            EndSpaceFreeAlignment.obtainOptimalAlignmentByDownmostOrder(computedMatrix, sequence1Input, sequence2Input, match, mismatch, indel).getAlignment2(),
                                            score, indel);
                                    for (int i = 0; i < computedMatrix[0].length; i++) {
                                        if (i == 0) {
                                            System.out.print("\t\t");
                                        } else {
                                            System.out.print(sequence2Input.charAt(i-1) + "\t");
                                        }
                                    }
                                    System.out.println();
                                    for (int i = 0; i < computedMatrix.length; i++) {
                                        if (i == 0) {
                                            System.out.print("\t");
                                        } else {
                                            System.out.print(sequence1Input.charAt(i-1) + "\t");
                                        }
                                        for (int j = 0; j < computedMatrix[0].length; j++) {
                                            System.out.print(String.format("%.2f\t", computedMatrix[i][j]));
                                        }
                                        System.out.println();
                                    }
                                }
                                else if(alignment == 4) {
                                    gapPenatly = Integer.parseInt( gapPenatlyInput );
                                    computedMatrix = AffineGap.computeMatrix(sequence1Input, sequence2Input, match, mismatch, indel, gapPenatly);
                                    score = computedMatrix[computedMatrix.length - 1][computedMatrix[0].length - 1];
                                    nextPanel(AffineGap.obtainOptimalAlignmentByDownmostOrder(computedMatrix, sequence1Input, sequence2Input).getAlignment1(),
                                            AffineGap.obtainOptimalAlignmentByDownmostOrder(computedMatrix, sequence1Input, sequence2Input).getAlignment2(),
                                            score, indel);
                                    for (int i = 0; i <= sequence2Input.length(); i++) {
                                        if (i == 0) {
                                            System.out.print("\t\t");
                                        } else {
                                            System.out.print(sequence2Input.charAt(i - 1) + "\t");
                                        }
                                    }
                                    System.out.println();
                                    for (int i = 0; i < computedMatrix.length; i++) {
                                        if (i == 0) {
                                            System.out.print("\t");
                                        } else {
                                            System.out.print(sequence1Input.charAt(i - 1) + "\t");
                                        }
                                        for (int j = 0; j < computedMatrix[0].length; j++) {
                                            System.out.print(String.format("%.2f\t", computedMatrix[i][j]));
                                        }
                                        System.out.println();
                                    }
                                }
                            } catch (Exception e1) {
                                // if there is an exception the user get an Error message.
                                JOptionPane.showMessageDialog( null, "The values match / mismatch / indel has to be an integers!" );
                                e1.printStackTrace();
                            }
                        }
                    }
                    else JOptionPane.showMessageDialog(null, "Please Enter 2 sequences!");
                } catch (Exception e1) { // there are might be exceptions.
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

            // now honestly everything below is boring, we doing the same again and again,
            // its all a way to organize our control panel, and its the same process:
            // first we create a rigid arena and add to the vertical box,
            // then we set our label/or other control staff.
            mainBox.add(Box.createRigidArea(new Dimension(0, 100)));
            sequence1 = new JLabel("Sequence-1:");
            sequence1.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainBox.add(sequence1);
            mainBox.add(Box.createRigidArea(new Dimension(0, 5)));

            sequence1area = new JTextArea(6,20);
            mainBox.add(new JScrollPane(sequence1area));

            mainBox.add(Box.createRigidArea(new Dimension(0, 15)));

            sequence2 = new JLabel("Sequence-2:");
            sequence2.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainBox.add(sequence2);
            mainBox.add(Box.createRigidArea(new Dimension(0, 5)));

            sequence2area = new JTextArea(6,20);
            mainBox.add(new JScrollPane(sequence2area));

            mainBox.add(Box.createRigidArea(new Dimension(0, 15)));
            mainBox.add(new JSeparator());

            globalAlignment = new JRadioButton("Global Alignment");
            globalAlignment.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainBox.add(globalAlignment);

            localAlignment = new JRadioButton("Local Alignment");
            localAlignment.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainBox.add(localAlignment);

            endSpaceFreeAlignment = new JRadioButton("End Space Free Alignment");
            endSpaceFreeAlignment.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainBox.add(endSpaceFreeAlignment);

            affineGapPenaltymodelAlignment = new JRadioButton("Affine Gap Penalty Model Alignment");
            affineGapPenaltymodelAlignment.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainBox.add(affineGapPenaltymodelAlignment);

            ButtonGroup bg = new ButtonGroup();
            bg.add(globalAlignment);
            bg.add(endSpaceFreeAlignment);
            bg.add(localAlignment);
            bg.add(affineGapPenaltymodelAlignment);

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

            gapPenatlyLabel = new JLabel("Gap Penatly: ");
            gapPenatlyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainBox.add(gapPenatlyLabel);
            mainBox.add(Box.createRigidArea(new Dimension(0, 0)));
            inputsBox.add(gapPenatlyLabel);

            gapPenatlyTextField = new JTextField(1);
            gapPenatlyTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainBox.add(gapPenatlyTextField);
            mainBox.add(Box.createRigidArea(new Dimension(15, 0)));
            inputsBox.add(gapPenatlyTextField);
            gapPenatlyTextField.setEditable( false );
            mainBox.add(inputsBox);

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
            globalAlignment.addActionListener(this);
            endSpaceFreeAlignment.addActionListener(this);
            localAlignment.addActionListener(this);
            affineGapPenaltymodelAlignment.addActionListener(this);
            matchTextField.addActionListener(this);
            misMatchTextField.addActionListener(this);
            indelTextField.addActionListener(this);
            calculateButton.addActionListener(this);
        }
    }

    private class outputPanel extends JPanel implements ActionListener {
        Box outputMainBox = Box.createVerticalBox();
        private JFrame outputFrame;
        private JLabel pairwiseAlignment;
        private JTextArea outputAlignment;
        Box outputBox = Box.createHorizontalBox();
        private JLabel scoreLabel;
        private JTextPane scorePane;
        private JLabel matLabel;
        private JTextPane matPane;
        private JLabel subLabel;
        private JTextPane subPane;
        private JLabel gapLabel;
        private JTextPane gapPane;
        private JButton goback;

        private int matNum = 0;
        private int subsNum = 0;
        private int gapsNum = 0;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == oputPanel.goback ) {
                goBack();
            }
        }

        public outputPanel(JFrame mainframe, String seq1, String seq2, float score, int gaps) {
            outputFrame = mainframe;

            outputMainBox.add(Box.createRigidArea(new Dimension(0, 100)));
            pairwiseAlignment = new JLabel("Pairwise Alignment:");
            pairwiseAlignment.setAlignmentX(Component.CENTER_ALIGNMENT);
            outputMainBox.add(pairwiseAlignment);
            outputMainBox.add(Box.createRigidArea(new Dimension(0, 5)));

            outputAlignment = new JTextArea( 3, Math.max(seq1.length(), seq2.length()));
            JScrollPane scrollPane = new JScrollPane(outputAlignment);
            scrollPane.setPreferredSize(new Dimension(750,100));
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            outputMainBox.add(scrollPane);
            outputAlignment.setEnabled(false);
            outputAlignment.setFont( new Font( "Courier New", Font.PLAIN, 20 ) );
            outputAlignment.setDisabledTextColor( Color.GRAY);
            outputAlignment.setAlignmentX(Component.CENTER_ALIGNMENT);

            outputMainBox.add(Box.createRigidArea(new Dimension(0, 10)));

            for (int i =0; i < seq1.length(); i++){
                outputAlignment.append( String.valueOf( seq1.charAt( i ) ) );

            }
            outputAlignment.append("\n");
            for (int i =0; i < seq1.length(); i++){
                if (seq1.charAt( i ) == '-' || seq2.charAt( i ) == '-') {
                    outputAlignment.append( " " );
                    gapsNum++;
                }
                else if (seq1.charAt( i ) == seq2.charAt( i )) {
                    outputAlignment.append( "|" );
                    matNum++;
                }
                else if (seq1.charAt( i ) != seq2.charAt( i )) {
                    outputAlignment.append( "*" );
                    subsNum++;
                }
            }
            outputAlignment.append("\n");
            for (int i=0; i < seq2.length(); i++){
                outputAlignment.append( String.valueOf( seq2.charAt( i ) ) );
            }

            outputMainBox.add(Box.createRigidArea(new Dimension(0, 5)));
            outputMainBox.add(new JSeparator());

            scoreLabel = new JLabel("Score:");
            scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            outputMainBox.add(scoreLabel);
            outputMainBox.add(Box.createRigidArea(new Dimension(0, 5)));
            outputBox.add(scoreLabel);

            scorePane = new JTextPane();
            scorePane.setPreferredSize(new Dimension(35,1));
            scorePane.setAlignmentX(Component.CENTER_ALIGNMENT);
            scorePane.setEnabled(false);
            scorePane.setDisabledTextColor( Color.BLACK );
            scorePane.setText(String.valueOf(score));

            outputMainBox.add(scorePane);
            outputMainBox.add(Box.createRigidArea(new Dimension(0, 10)));
            outputBox.add(scorePane);

            outputBox.add(Box.createRigidArea(new Dimension(15, 0)));
            outputBox.add(new JSeparator(SwingConstants.VERTICAL));
            outputBox.add(Box.createRigidArea(new Dimension(15, 0)));

            matLabel = new JLabel("Number of Matches: ");
            matLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            outputMainBox.add(matLabel);
            outputMainBox.add(Box.createRigidArea(new Dimension(0, 5)));
            outputBox.add(matLabel);

            matPane = new JTextPane();
            matPane.setPreferredSize(new Dimension(25, 0));
            matPane.setEnabled(false);
            matPane.setDisabledTextColor( Color.BLACK );
            matPane.setAlignmentX(Component.CENTER_ALIGNMENT);
            matPane.setText(String.valueOf(matNum));
            outputMainBox.add(matPane);
            outputMainBox.add(Box.createRigidArea(new Dimension(0, 10)));
            outputBox.add(matPane);

            outputBox.add(Box.createRigidArea(new Dimension(15, 0)));
            outputBox.add(new JSeparator(SwingConstants.VERTICAL));
            outputBox.add(Box.createRigidArea(new Dimension(15, 0)));

            subLabel = new JLabel("Substitutions: ");
            subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            outputMainBox.add(subLabel);
            outputMainBox.add(Box.createRigidArea(new Dimension(0, 5)));
            outputBox.add(subLabel);

            subPane = new JTextPane();
            subPane.setPreferredSize(new Dimension(25, 0));
            subPane.setEnabled(false);
            subPane.setAlignmentX(Component.CENTER_ALIGNMENT);
            subPane.setText(String.valueOf(subsNum));
            subPane.setDisabledTextColor( Color.BLACK );
            outputMainBox.add(subPane);
            outputMainBox.add(Box.createRigidArea(new Dimension(0, 10)));
            outputBox.add(subPane);

            outputBox.add(Box.createRigidArea(new Dimension(15, 0)));
            outputBox.add(new JSeparator(SwingConstants.VERTICAL));
            outputBox.add(Box.createRigidArea(new Dimension(15, 0)));

            gapLabel = new JLabel("Indels:");
            gapLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            outputMainBox.add(gapLabel);
            outputMainBox.add(Box.createRigidArea(new Dimension(0, 5)));
            outputBox.add(gapLabel);

            gapPane = new JTextPane();
            gapPane.setPreferredSize(new Dimension(25, 0));
            gapPane.setEnabled(false);
            gapPane.setAlignmentX(Component.CENTER_ALIGNMENT);
            gapPane.setText(String.valueOf(gapsNum));
            gapPane.setDisabledTextColor( Color.BLACK );
            outputMainBox.add(gapPane);
            outputMainBox.add(Box.createRigidArea(new Dimension(0, 5)));
            outputBox.add(gapPane);

            outputMainBox.add(outputBox);

            outputMainBox.add(Box.createRigidArea(new Dimension(0, 15)));
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
     * This is the main to run the GUI and basically the program we made.
     * @param args whatever...
     */
    public static void main(String[] args) {
        // creating a GUI object
        GUI myGui = new GUI();
        // all of the swing JFrame
        myGui.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        myGui.setSize(800, 800);
        myGui.setVisible(true);
    }
}
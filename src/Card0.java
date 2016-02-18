import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Andrew on 9/22/2015.
 */
public class Card0 {
    private JButton backButton;
    private JButton nextButton;
    private JButton submitButton;
    private JButton openFileButton;
    private JTextField filenameTextField;
    private JTextArea sequenceTextArea;
    private JPanel SEQUENCE;
    private JPanel OpenFilePanel;
    private JPanel SequencePanel;
    private JPanel NavigationPanel;
    private JPanel NavButtonPanel;
    private JPanel SpeGenPanel;
    private JPanel SpeGenButtonPanel;
    private JLabel speciesLabel;
    private JComboBox<String> speciesComboBox;
    private JLabel genLabel;
    private JComboBox<String> genComboBox;
    private JButton settingsButton;
    private JButton GPSettingsButton;

    MainPane mainPane;

    public Card0(final MainPane mainPane) {

        this.mainPane = mainPane;
        mainPane.setTitle("Sequence for Ortholog Search");

        final JFileChooser fileChooser = new JFileChooser();

//        Hide SpeGenPanel for new version of OG
        SpeGenPanel.setVisible(false);

        getGenComboBox().setPrototypeDisplayValue("Select Genome Value");

        try {
            ArrayList<ArrayList<String>> result = OGF
                    .GetFormElements("https://genome.ucsc.edu/cgi-bin/hgBlat?command=start");
            for (int i = 0; i < result.get(1).size(); i++)
                speciesComboBox.addItem(result.get(1).get(i));
            for (int i = 0; i < result.get(2).size(); i++) {
                genComboBox.addItem(result.get(2).get(i));
            }
            sequenceTextArea.setText(mainPane.GP_SEQ);
//            In GenePalette Implementation, uncomment
//            sequenceTextArea.setEditable(false);
//            OpenFilePanel.setVisible(false);
//            backButton.setVisible(false);
//            nextButton.setVisible(false);
//            settingsButton.setVisible(false);
//            In Independent OG Implementation, uncomment
            GPSettingsButton.setVisible(false);

        } catch (Exception ignored) {
        }

        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(openFileButton) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String result = OGF.readFASTA(file);
                    filenameTextField.setText("  " + file.getName());
                    sequenceTextArea.setText(result);
                } else {
                    filenameTextField.setText("  ERROR! Error choosing file. Check Formatting");
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPane.previousView();
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPane.nextView();
            }
        });
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPane.setTitle("Update Settings: Remember to Save!!");
                mainPane.settingsView();
            }
        });
        GPSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPane.settingsView();
            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    int VI = mainPane.getController().getCurrentViewIndex();
                    mainPane.removeView((JPanel) mainPane.getController().getViews().get(VI + 2));
                    mainPane.removeView((JPanel) mainPane.getController().getViews().get(VI + 1));
                } catch (Exception ex) {
                }

                String sequence = "";
                // Get Sequence from JTextArea [SequenceTextArea]
                try {
//                    System.out.println("DNA taken from Sequence Text Area");
                    sequence = sequenceTextArea.getText().replace("\n", "").replace("\r", "");
                } catch (Exception er) {
//                    er.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error processing sequence!\nCheck for invalid characters.");
                }

//                Original BLAT that is no longer necessary...... -_- grrrrrr

//                String base = "https://genome.ucsc.edu/cgi-bin/hgBlat?command=start";
//                String species = getSpeciesComboBox().getSelectedItem().toString();
//                String genome = getGenComboBox().getSelectedItem().toString();
//                String resultSeq = "";
//                String strand = "";
////                Do Initial BLAT using Chosen Species and Genome
//                try {
////                      Get BLAT for the Species + Genome Combo
//                    ArrayList<Object[][]> blat = OGF.getBlatResults(OGF.HGSID, sequence, species, genome, base);
////                      Get First Row of Result
//                    Object[] row = blat.get(2)[0];
//
////                      Check BLAT result Strandedness
//                    if (row[7].toString().contains("+"))
//                        strand = "+";
//                    else
//                        strand = "-";
//
//                    String result = OGF.getDNAQuick(OGF.HGSID, "0", "0", "0", row[0].toString());
//
//                    resultSeq = result.substring(result.indexOf("=none") + 5).replace("\n", "").replace("\r", "");
//                    System.out.println("Result of getDNA from Initial BLAT\n" + resultSeq);
//
//                } catch (Exception ex) {
////                    ex.printStackTrace();
//                    JOptionPane.showMessageDialog(null, "There has been an error connecting to the UCSC Genome Browser");
//                }

//                The original way of making OG
//                Card1 view1 = new Card1(mainPane, resultSeq, strand);

                if (sequence.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid DNA sequence!");
                } else {
//                Stupid new way of making OG
                    Card1 view1 = new Card1(mainPane, sequence, "0");

                    mainPane.addView(view1.getBLAT(), "View1");

//                Enable Next Button
                    nextButton.setEnabled(true);

                    // Switch to Next Panel to display results
                    mainPane.nextView();
                }
            }
        });
        speciesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<ArrayList<String>> result = null;
                try {
                    result = OGF.ChangeOrganism(
                            OGF.HGSID, ((JComboBox<String>) e
                                    .getSource()).getSelectedItem()
                                    .toString(),
                            "https://genome.ucsc.edu/cgi-bin/hgBlat?command=start");

                    genComboBox.removeAllItems();
                    for (int i = 0; i < result.size(); i++)
                        genComboBox.addItem(result.get(1).get(i));
                } catch (Exception e1) {
                }
            }
        });
        genComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public JPanel getSEQUENCE() {
        return SEQUENCE;
    }

    public JComboBox<String> getSpeciesComboBox() {
        return speciesComboBox;
    }

    public JComboBox<String> getGenComboBox() {
        return genComboBox;
    }

    public JTextArea getSeqTextArea() {
        return sequenceTextArea;
    }
}
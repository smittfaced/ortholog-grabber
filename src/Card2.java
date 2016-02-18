import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;

/**
 * Created by Andrew on 11/23/2015.
 */
public class Card2 {
    private JPanel NavigationPanel;
    private JPanel NavButtonPanel;
    private JButton backButton;
    private JButton nextButton;
    private JButton submitButton;
    private JPanel BLATList;
    private JScrollPane BLATSelectionScrollPane;
    private JPanel BLATSelectionPanel;

    MainPane mainPane;
    ArrayList<String> LINKS, SPANS, IDENTITY, SPECIES, STRAND;
    String FIVEPAD = "0", THREEPAD = "0";
    String base = "https://genome.ucsc.edu/cgi-bin/hgBlat?command=start";

    public Card2(MainPane mainpane, ArrayList<String> species, ArrayList<String> links, ArrayList<String> spans, ArrayList<String> identity, ArrayList<String> strand) {

        this.mainPane = mainpane;
        this.SPECIES = species;
        this.LINKS = links;
        this.SPANS = spans;
        this.IDENTITY = identity;
        this.STRAND = strand;

        mainPane.setTitle("Select Matched Sequences for Retrieval");

//        Get Padding of 5' and 3'
        try {
//            Possibly work in JAR form
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(new File("og_settings.txt").getPath())));
//            In-JAR Location of settings file
//            BufferedReader br = new BufferedReader(new FileReader("src/og_settings.txt"));
            try {
//            Possibly work in JAR form
//                br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(new File(System.getProperty("user.home"), "og_settings.txt").getPath())));
//            External location of settings file
                br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(System.getProperty("user.home"), "og_settings.txt"))));
//                br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(new File(System.getProperty("user.dir"), "Resources"), "og_settings.txt"))));
            } catch (Exception ign) {
                ign.printStackTrace();
            }

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
//                Find selected Profile
                if (sCurrentLine.startsWith("%other_settings")) {
                    String fivePad = br.readLine();
                    String threePad = br.readLine();
                    FIVEPAD = fivePad.substring(fivePad.indexOf(":") + 1, fivePad.length());
                    THREEPAD = threePad.substring(threePad.indexOf(":") + 1, threePad.length());
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "There has been an error connecting to the UCSC Genome Browser");
            System.exit(0);
        }

//        In GenePalette Implementation, uncomment
        nextButton.setVisible(false);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String filename = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss'.fasta'").format(new Date());

                for (int i = 0; i < getBLATSelectionPanel().getComponents().length; i++) {
//                Gather Selected Species via Radio Button Status
                    try {
                        JPanel SpeciesSelect = (JPanel) getBLATSelectionPanel().getComponent(i);
                        JPanel SpeciesSelectRow = (JPanel) SpeciesSelect.getComponent(0);
                        JPanel SpeciesButtonRow = (JPanel) SpeciesSelectRow.getComponent(0);
                        JRadioButton SelectButton = (JRadioButton) SpeciesButtonRow.getComponent(0);
                        JComboBox GenomeBox = (JComboBox) SpeciesSelectRow.getComponent(1);
                        JList GenomeList = (JList) SpeciesSelectRow.getComponent(2);

//                        Check if Radio Button is Selected
                        if (SelectButton.isSelected()) {
//                            Get Genome and Species
                            String genome = GenomeList.getModel().getElementAt(GenomeBox.getSelectedIndex()).toString();
                            String specie = ((JLabel) SpeciesButtonRow.getComponent(1)).getText();
//                            Getting Selected Option Strand from ArrayList STRAND
                            List<String> strands = Arrays.asList(STRAND.get(i).split("\\s*,\\s*"));

                            try {
//                                Get BLAT for the Species + Genome Combo
//                                String result = OGF.getDNAQuick(OGF.HGSID, "0", FIVEPAD, THREEPAD, genome);
                                String result = OGF.getDNAQuick(OGF.HGSID, strands.get(GenomeBox.getSelectedIndex()).substring(1, 2), FIVEPAD, THREEPAD, genome);
//                                Print Result for Returning String to GenePalette
                                System.out.println(result);
//                                Save Results to File For Independent OG
                                saveFile(result, filename);

                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "Couldn't get DNA for " + specie);
                            }
                        }
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Something has gone fundamentally wrong...\nPlease notify GenePalette Admin of error and\nprovide information on how to duplicate it!");
                    }
                }
                System.exit(0);
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPane.nextView();
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPane.setTitle("Select Genomes to Search");
                mainPane.previousView();
            }
        });
    }

    public JPanel getBLATList() {
        return BLATList;
    }

    public JScrollPane getBLATSelectionScrollPane() {
        return BLATSelectionScrollPane;
    }

    public void setBLATSelectionScrollPane(JScrollPane BLATSelectionScrollPane) {
        this.BLATSelectionScrollPane = BLATSelectionScrollPane;
    }

    public JPanel getBLATSelectionPanel() {
        return BLATSelectionPanel;
    }

    public void setBLATSelectionPanel(JPanel BLATSelectionPanel) {
        this.BLATSelectionPanel = BLATSelectionPanel;
    }

    public void setBLATSelectionPanelLayout() {
        BLATSelectionPanel.setLayout(new BoxLayout(BLATSelectionPanel, BoxLayout.PAGE_AXIS));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        setBLATSelectionScrollPane(new JScrollPane(new JPanel()));
        setBLATSelectionPanel((JPanel) getBLATSelectionScrollPane().getViewport().getComponent(0));
        setBLATSelectionPanelLayout();

        for (int i = 0; i < LINKS.size(); i++) {
            try {
//                Create BLATResultSelect for each BLAT Result
                getBLATSelectionPanel().add(new BLATResultSelect(SPECIES.get(i), LINKS.get(i), SPANS.get(i), IDENTITY.get(i)).getResultSelect());

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "There has been an error connecting to the UCSC Genome Browser");
                System.exit(0);
            }
        }
    }

    private void saveFile(String result, String filename) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
            out.println(result);
            out.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

//    private String printDNA() {
//        for (int i = 0; i < getVIOGPanel().getComponentCount(); i++) {
//
//            String filename = checkFilename(((JTextField) ((JPanel) getVIOGPanel().getComponent(i)).getComponent(2)).getText());
//            int[] selected = ((JList) ((JScrollPane) ((JPanel) (getVIOGPanel().getComponent(i))).getComponent(1)).getViewport().getComponent(0)).getSelectedIndices();
//            try {
//                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
//                for (int j = 0; j < selected.length; j++) {
//                    if (DNA.get(i).get(selected[j]) == "") { }
//                    else {
//                        out.println(DNA.get(i).get(selected[j]));
//                        System.out.println(DNA.get(i).get(selected[j]));
//                    }
//                }
//                out.close();
//                return "All Good!";
//            } catch (Exception e) {
//            }
//        }
//        return null;
//    }
}

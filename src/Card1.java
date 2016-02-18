import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Andrew on 9/22/2015.
 */
public class Card1 {
    private JPanel BLAT;
    private JButton backButton;
    private JButton submitButton;
    private JButton nextButton;
    private JPanel NavigationPanel;
    private JPanel NavButtonPanel;
    private JScrollPane speciesSelectScrollPane;
    private JPanel speciesSelectPanel;

    MainPane mainPane;
    String sequence;
    String HGSID;
    String STRAND;
    String base = "https://genome.ucsc.edu/cgi-bin/hgBlat?command=start";

    public Card1(MainPane mainpane, String info, String strandInfo) {

//        System.out.println("\nDone Setting mainPane and sequence\n");

        this.mainPane = mainpane;
        this.sequence = info;
        this.STRAND = strandInfo;

        mainPane.setTitle("Select Genomes to Search");

//        In GenePalette Implementation, uncomment
        nextButton.setVisible(false);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String base = "https://genome.ucsc.edu/cgi-bin/hgBlat?command=start";
                ArrayList<String> species = new ArrayList<String>();
                ArrayList<String> links = new ArrayList<String>();
                ArrayList<String> spans = new ArrayList<String>();
                ArrayList<String> identity = new ArrayList<String>();
                ArrayList<String> strand = new ArrayList<String>();


//                Remove any Views after this one to eliminate any inconsistency in Card Traversal
                try {
                    int VI = mainPane.getController().getCurrentViewIndex();
                    mainPane.removeView((JPanel) mainPane.getController().getViews().get(VI + 1));

                } catch (Exception ex) {
                }

//                Setup Error Reporting for Sequences
                String errorMsg = "No Results Found for:";
                boolean error = false;

//                Get All Components of Species within SelectSpeciesPanel
                for (int i = 0; i < getSpeciesSelectPanel().getComponents().length; i++) {
//                Gather Selected Species via Radio Button Status
                    try {
                        JPanel SpeciesSelect = (JPanel) getSpeciesSelectPanel().getComponent(i);
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

                            try {
//                                Get BLAT for the Species + Genome Combo
                                ArrayList<Object[][]> blat = OGF.getBlatResults(OGF.HGSID, sequence, specie, genome, base);
//                                Create Arrays to hold all Items
                                String[] linkArray = new String[blat.get(2).length];
                                String[] spanArray = new String[blat.get(2).length];
                                String[] identityArray = new String[blat.get(2).length];
                                String[] strandArray = new String[blat.get(2).length];
                                for (int j = 0; j < blat.get(2).length; j++) {
                                    Object[] row = blat.get(2)[j];
//                                Add Info to Arrays of Good Results
                                    linkArray[j] = row[0].toString();
                                    spanArray[j] = row[10].toString();
                                    identityArray[j] = row[5].toString();
                                    strandArray[j] = row[7].toString();
                                }
                                links.add(createCommaStringfromArray(linkArray));
                                spans.add(createCommaStringfromArray(spanArray));
                                identity.add(createCommaStringfromArray(identityArray));
                                strand.add(createCommaStringfromArray(strandArray));
                                species.add(specie);

                            } catch (Exception ex) {
                                error = true;
                                errorMsg += "\n    - " + specie + " (" + genome + ")";
                            }
                        }
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Something has gone fundamentally wrong...\nPlease notify GenePalette Admin of error and\nprovide information on how to duplicate it!");
                    }
                }
                if (error) {
                    JOptionPane.showMessageDialog(null, errorMsg);
                }

//                Show next Card with info from Results
                Card2 view2 = new Card2(mainPane, species, links, spans, identity, strand);

                mainPane.addView(view2.getBLATList(), "View2");

//                Enable Next Button
                nextButton.setEnabled(true);

                // Switch to Next Panel to display results
                mainPane.nextView();
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
                mainPane.setTitle("Sequence for Ortholog Search");
                mainPane.previousView();
            }
        });
    }

    public JScrollPane getSpeciesSelectScrollPane() {
        return speciesSelectScrollPane;
    }

    public void setSpeciesSelectScrollPane(JScrollPane speciesSelectScrollPane) {
        this.speciesSelectScrollPane = speciesSelectScrollPane;
    }

    public JPanel getSpeciesSelectPanel() {
        return speciesSelectPanel;
    }

    public void setSpeciesSelectPanel(JPanel speciesSelectPanel) {
        this.speciesSelectPanel = speciesSelectPanel;
    }

    public void setSpeciesSelectPanelLayout() {
        speciesSelectPanel.setLayout(new BoxLayout(speciesSelectPanel, BoxLayout.PAGE_AXIS));
    }

    public JPanel getBLAT() {
        return BLAT;
    }

    private String createCommaStringfromArray(String[] array) {
        StringBuilder sb = new StringBuilder();
        for (String n : array) {
            if (sb.length() > 0) sb.append(',');
            sb.append("'").append(n).append("'");
        }
        return sb.toString();
    }

    private void createUIComponents() {

        setSpeciesSelectScrollPane(new JScrollPane(new JPanel()));
        setSpeciesSelectPanel((JPanel) getSpeciesSelectScrollPane().getViewport().getComponent(0));
        setSpeciesSelectPanelLayout();

//        Get Form Elements from UCSD Website
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        try {
            result = OGF.GetFormElements(base);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "There has been an error connecting to the UCSC Genome Browser");
            System.exit(0);
        }

//        Save HGSID
        HGSID = result.get(0).get(0);

//        Save Organisms
        ArrayList<String> organisms = result.get(1);

//        Get List of Organisms That the User Desires to Be Used Typically
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

            } catch (Exception e) {
                e.printStackTrace();
            }

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
//                Find selected Profile
                if (sCurrentLine.endsWith("*")) {
                    organisms = new ArrayList<>(Arrays.asList(br.readLine().split(",")));
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "There has been an error connecting to the UCSC Genome Browser");
            System.exit(0);
        }

//        For each organism, do ChangeOrganism and get Genome Options
        for (String organism : organisms) {
//            System.out.println("Getting " + organism);
            try {
                ArrayList<ArrayList<String>> genomes = OGF.ChangeOrganism(HGSID, organism, base);
                getSpeciesSelectPanel().add(new BLATSpeciesSelect(organism, genomes).getSpeciesSelect());

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "There has been an error connecting to the UCSC Genome Browser");
                System.exit(0);
            }
        }
    }
}

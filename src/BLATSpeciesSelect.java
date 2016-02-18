import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Andrew on 11/9/2015.
 */
public class BLATSpeciesSelect {
    private JPanel SpeciesSelect;
    private JPanel SpeciesSelectRow;
    private JPanel SpeciesButtonRow;
    private JRadioButton SelectButton;
    private JLabel SpeciesLabel;
    private JComboBox<String> GenomeBox;
    private JList<String> GenomeList;

    ArrayList<String> genomeNames;
    ArrayList<String> genomeCode;
    String species;

    public BLATSpeciesSelect(String species, ArrayList<ArrayList<String>> genomes) {

        this.genomeCode = genomes.get(0);
        this.genomeNames = genomes.get(1);
        this.species = species;

//        Fill Genome ComboBox
        if (genomeNames.size() == 1)
            GenomeBox.setEnabled(false);
        for (String genomeName : genomeNames) {
            GenomeBox.addItem(genomeName);
        }
//        Instantiate Genome Name List
        DefaultListModel<String> dlm = new DefaultListModel<String>();
        for (String s : genomeCode) {
            dlm.addElement(s);
        }
        GenomeList.setModel(dlm);
        GenomeList.setVisible(false);

//        Set Species Label Text
        SpeciesLabel.setText(species);

//        Set RadioButton Selected for Faster Selections by User
        SelectButton.setSelected(true);
    }

    public JRadioButton getSelectButton() {
        return SelectButton;
    }
    public JComboBox<String> getGenomeBox() {
        return GenomeBox;
    }
    public JList<String> getGenomeList() {
        return GenomeList;
    }
    public JPanel getSpeciesSelect() {
        return SpeciesSelect;
    }
}

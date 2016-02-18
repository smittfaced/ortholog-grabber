import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Andrew on 11/24/2015.
 */
public class BLATResultSelect {
    private JPanel ResultSelectRow;
    private JPanel ResultButtonRow;
    private JRadioButton SelectButton;
    private JLabel SpeciesLabel;
    private JComboBox<String> BLATResultBox;
    private JList<String> URLList;
    private JPanel ResultSelect;

    ArrayList<String> LINKS;
    ArrayList<String> SPANS;
    ArrayList<String> IDENTITIES;
    String SPECIES;

    public BLATResultSelect(String species, String links, String spans, String identities) {

        this.SPECIES = species;

//        Split Argument Strings
        this.LINKS = new ArrayList<>(Arrays.asList(links.split(",")));
        this.SPANS = new ArrayList<>(Arrays.asList(spans.split(",")));
        this.IDENTITIES = new ArrayList<>(Arrays.asList(identities.split(",")));

//        Instantiate Genome Name List
        DefaultListModel<String> dlm = new DefaultListModel<String>();
        URLList.setModel(dlm);
        URLList.setVisible(false);

//        Fill Genome ComboBox and JList
        if (LINKS.size() == 1)
            BLATResultBox.setEnabled(false);
        for (int i = 0; i < LINKS.size(); i++) {
            String identity = IDENTITIES.get(i).substring(1, IDENTITIES.get(i).length()-1);
            String span = SPANS.get(i).substring(1, SPANS.get(i).length()-1);
            BLATResultBox.addItem("Identity: " + identity + ",      Span: " + span);
            dlm.addElement(LINKS.get(i).substring(1, LINKS.get(i).length()-1));
        }

//        Set Species Label Text
        SpeciesLabel.setText(species);

//        Set RadioButton Selected for Faster Selections by User
        SelectButton.setSelected(true);
    }

    public JRadioButton getSelectButton() {
        return SelectButton;
    }
    public JComboBox<String> getBLATResultBox() {
        return BLATResultBox;
    }
    public JList<String> getURLList() {
        return URLList;
    }
    public JPanel getResultSelect() {
        return ResultSelect;
    }
}

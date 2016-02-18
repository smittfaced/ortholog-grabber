import javax.swing.*;

/**
 * Created by Andrew on 11/17/2015.
 */
public class OGSelector {
    private JCheckBox SelectButton;
    private JLabel SpeciesLabel;
    private JPanel SpeciesSelect;
    private JPanel SpeciesButtonRow;

    private String Species;
    private boolean choice;

    public OGSelector(String species, int b) {
        this.Species = species;

//        Set Checkbox Selected
        if (b == 0)
            setCheckBoxSelected(true);
        else
            setCheckBoxSelected(false);

//        Set Species Label
        SpeciesLabel.setText(Species);

//        Set RadioButton Selected for Faster Selections by User
//        SelectButton.setSelected(true);
    }

    public JCheckBox getSelectButton() {
        return SelectButton;
    }
    public void setCheckBoxSelected(boolean b) {
        SelectButton.setSelected(b);
    }
    public JPanel getOGSelector() { return SpeciesSelect; }
}

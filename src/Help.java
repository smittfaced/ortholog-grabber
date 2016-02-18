import javax.swing.*;

/**
 * Created by Andrew on 11/25/2015.
 */
public class Help {
    private JTextArea HelpTextArea;
    private JPanel HelpPanel;

    public Help() {
        String help = "OG: Ortholog Grabber Help Documentation\n" +
                "\n" +
                "Welcome to OG!\n" +
                "\n" +
                "Using OG: Ortholog Grabber\n\n" +
                "    1. Input sequence -> Click 'Submit'\n" +
                "        (Sequence taken directly from GenePalette is un-editable!)\n" +
                "    2. Choose desired specie(s) to search for orthologous sequences by selecting buttons left of species name -> 'Submit'\n" +
                "        (If you prefer a specific published genome to search in, select it from the Genome dropdown menu)\n" +
                "    3. Choose desired specie(s) to get orthologous DNA sequence again using buttons to the left\n" +
                "    4. Select from the dropdown menu of 'hits' a sequence with desireable SPAN and original sequence IDENTITY percentage\n" +
                " \n" +
                "    **If at any point you are dissatisfied with the results of a search, return to the previous page using 'back' and resubmit the\n      search after making desired changes.\n" +
                "\n" +
                "Settings:\n\n" +
                "    - Managing Profiles\n" +
                "        OG allows users to easily switch between groups of species for Sequence Retrieval by assigning profiles to them.\n" +
                "        Creating a new profile is as easy as typing a name for the profile in the textfield and clicking 'Add Profile'.\n" +
                "        Once a profile has been added it will be appended to the list of profiles in the top left of the settings page.\n" +
                "        Clicking on a profile tab will allow you to select/deselect species you desire to be included in ortholog searches.\n" +
                "            (Having fewer species in a profile will allow for faster searches!)\n" +
                "        To delete a profile, select the profile tab and click 'Delete Profile'.\n" +
                "        Any changes to profiles will not be saved unless the user selects the 'Save Changes' button in the bottom right!!\n" +
                "\n" +
                "    - Other Settings\n" +
                "        On sequence retrieval, DNA can be padded in the 3' and 5' directions by indicating the number of base pairs desired.\n" +
                "        Again, any changes to settings here will not be saved unless the 'Save Changes' button is clicked!\n" +
                "\n" +
                "    - Helpful Tips\n" +
                "        Whichever profile is selected in the Settings menu will be used to conduct all of the species searches in OG.\n" +
                "        OG: Ortholog Grabber will remember the last selected profile on future instances of the application.\n" +
                "        After making changes to the Settings, remember to save them!";
        HelpTextArea.setText(help);
        HelpTextArea.setOpaque(false);
    }

        public JPanel getHelp() {
                return HelpPanel;
        }
}

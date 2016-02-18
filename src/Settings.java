import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Andrew on 11/17/2015.
 */
public class Settings {
    private JPanel Settings;
    private JPanel NavigationPanel;
    private JPanel NavButtonPanel;
    private JButton backButton;
    private JButton saveButton;

    private JPanel ProfilesPanel;
    private JTabbedPane ProfileTabPanel;

    private JPanel AddNewProfile;
    private JButton AddProfileButton;
    private JTextField AddProfileTextfield;
    private JButton DeleteProfileButton;
    private JPanel OtherSettings;
    private JLabel OtherSettingsLabel;
    private JFormattedTextField padding5Field;
    private JPanel Padding5Panel;
    private JLabel padding5;
    private JPanel Padding3Panel;
    private JLabel padding3;
    private JFormattedTextField padding3Field;
    private JLabel Filename;
    private JTextField filenameTextfield;
    private JLabel basePairs5;
    private JLabel basePairs3;
    private JLabel saveReminder;
    private JButton helpButton;
    private JPanel filenamePanel;
    private JLabel otherSettingsInfo;
    private JPanel ProfileSettings;
    private JTextField ProfileTextfield;
    private JButton AddProfile;
    private JButton DeleteProfile;
    private JPanel helpPanel;
    private JButton submitButtonSettings;

    MainPane mainPane;
    int selectedGuiTab;

    public Settings(final MainPane mainPane) {

        this.mainPane = mainPane;

//        Read in Settings File to get all Profiles
        ArrayList<ArrayList<String>> settings = readSettingFile();

//        For Each Profile Create a Tab
        for (int i = 0; i < settings.get(0).size(); i++) {
            try {
                createProfileTab(settings.get(0).get(i), settings.get(1).get(i));
            } catch (Exception e) {
                createProfileTab(settings.get(0).get(i), "");
            }
        }

//        Set Selected Tab on GUI
        ProfileTabPanel.setSelectedIndex(selectedGuiTab);
        AddNewProfile.setVisible(false);

//        In GenePalette Implementation, uncomment
        filenamePanel.setVisible(false);
        Filename.setVisible(false);

        ProfileTextfield.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (ProfileTextfield.getText().equals("Enter New Profile Name")) {
                    ProfileTextfield.setText("");
//                    repaint();
//                    revalidate();
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPane.setTitle("Sequence for Ortholog Search");
                mainPane.nextView();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSettings();
            }
        });
        AddProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String profileName = AddProfileTextfield.getText();
//                Check validity of Profile Name
                if (profileName.equals("Enter New Profile Name"))
                    JOptionPane.showMessageDialog(null, "Please enter a unique Profile Name");
                else {
//                    Add new Profile Tab to ProfileTabPanel
                    createProfileTab(profileName, "");
//                    Set New Profile Tab as Selected Tab
                    int selected = ProfileTabPanel.getTabCount() - 1;
                    ProfileTabPanel.setSelectedIndex(selected);
                }
                AddProfileTextfield.setText("Enter New Profile Name");
            }
        });
        AddProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String profileName = ProfileTextfield.getText();
//                Check validity of Profile Name
                if (profileName.equals("Enter New Profile Name"))
                    JOptionPane.showMessageDialog(null, "Please enter a unique Profile Name");
                else {
//                    Add new Profile Tab to ProfileTabPanel
                    createProfileTab(profileName, "");
//                    Set New Profile Tab as Selected Tab
                    int selected = ProfileTabPanel.getTabCount() - 1;
                    ProfileTabPanel.setSelectedIndex(selected);
                }
                ProfileTextfield.setText("Enter New Profile Name");
            }
        });
        DeleteProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Get Tab to Remove
                JScrollPane removePanel = (JScrollPane) ProfileTabPanel.getSelectedComponent();
//                Check that Panel is not DEFAULT
                if (ProfileTabPanel.getSelectedIndex() == 0)
                    JOptionPane.showMessageDialog(null, "Unable to delete Default Profile!\n\n   ...What kind of monster are you?");
//                Remove Panel
                else
                    ProfileTabPanel.remove(removePanel);
            }
        });
        DeleteProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Get Tab to Remove
                JScrollPane removePanel = (JScrollPane) ProfileTabPanel.getSelectedComponent();
//                Check that Panel is not DEFAULT
                if (ProfileTabPanel.getSelectedIndex() == 0)
                    JOptionPane.showMessageDialog(null, "Unable to delete Default Profile!\n\n   ...What kind of monster are you?");
//                Remove Panel
                else
                    ProfileTabPanel.remove(removePanel);
            }
        });
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, new Help().getHelp());
            }
        });
        submitButtonSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSettings();
                mainPane.nextView();
            }
        });
    }

    private void createProfileTab(String profileName, String species) {
//        Create Components
        JPanel testPanel = new JPanel();
        testPanel.setLayout(new BoxLayout(testPanel, BoxLayout.PAGE_AXIS));
        JScrollPane testScrollPane = new JScrollPane(testPanel);
        ArrayList<String> Species = new ArrayList<String>(Arrays.asList(species.split(",")));

        try {
            ArrayList<ArrayList<String>> result = OGF.GetFormElements("https://genome.ucsc.edu/cgi-bin/hgBlat?command=start");
            for (int i = 0; i < result.get(1).size(); i++)
//                    Profile has Selected Species previously
                if (Species.contains(result.get(1).get(i)))
                    testPanel.add(new OGSelector(result.get(1).get(i), 0).getOGSelector());
//                    Profile has deselected Species previously
                else
                    testPanel.add(new OGSelector(result.get(1).get(i), 1).getOGSelector());
//              Add Components to Profile Tab Pane
            ProfileTabPanel.add(testScrollPane, profileName);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to Create Profile!");
        }
    }

    private ArrayList<ArrayList<String>> readSettingFile() {
        try {
//            Possibly work in JAR form
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(new File("og_settings.txt").getPath())));
//            In-JAR Location of settings file
//            BufferedReader br = new BufferedReader(new FileReader("src/og_settings.txt"));
            try {
//            Possibly work in JAR form
//                br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(new File(System.getProperty("user.home"), "og_settings.txt").getPath())));
//            External location of settings file

//          In Independent OG Implementation, uncomment
                br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(System.getProperty("user.home"), "og_settings.txt"))));
//          In GenePalette OG Implementation, uncomment
//                br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(new File(System.getProperty("user.dir"), "Resources"), "og_settings.txt"))));
            } catch (Exception ignored) {
            }

            String sCurrentLine;
            int counter = 0;
            ArrayList<String> profileNames = new ArrayList<>();
            ArrayList<String> profileSettings = new ArrayList<>();

            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.startsWith(">")) {
                    if (sCurrentLine.endsWith("*")) {
//                        Set selectedTab from Settings File
                        selectedGuiTab = counter;
                        profileNames.add(sCurrentLine.substring(1, sCurrentLine.length() - 1));
                        counter++;
                    } else {
                        profileNames.add(sCurrentLine.substring(1));
                        counter++;
                    }
                    profileSettings.add(br.readLine());
                } else if (sCurrentLine.startsWith("%other_settings")) {
                    String fivePad = br.readLine();
                    String threePad = br.readLine();
                    try {
                        getPadding5Field().setValue(Integer.parseInt(fivePad.substring(fivePad.indexOf(":") + 1, fivePad.length())));
                        getPadding3Field().setValue(Integer.parseInt(threePad.substring(threePad.indexOf(":") + 1, threePad.length())));
                    } catch (Exception r) {
                        getPadding5Field().setValue(0);
                        getPadding3Field().setValue(0);
                    }
                }
            }

            ArrayList<ArrayList<String>> result = new ArrayList<>();
            result.add(profileNames);
            result.add(profileSettings);

            return result;

        } catch (IOException e) {
            return null;
        }
    }

    public JPanel getSettings() {
        return Settings;
    }

    public void saveSettings() {
//          Create StringBuilder
        StringBuilder sb = new StringBuilder();
//          Get the Tabbed Pane Component
        Component[] comps = ProfileTabPanel.getComponents();
        int selectedTab = ProfileTabPanel.getSelectedIndex();

        for (int i = 0; i < comps.length; i++) {
//              Get The Profile Name
            sb.append(">").append(ProfileTabPanel.getTitleAt(i));
//            If Selected append '*'
            if (i == selectedTab)
                sb.append("*");
//            Append Newline
            sb.append(System.lineSeparator());

//              Get Tab Panel and OGSelectors
            JViewport port = ((JScrollPane) comps[i]).getViewport();
            JPanel panel = (JPanel) port.getComponent(0);
            Component[] comps2 = panel.getComponents();

//              Iterate through each OGSelector and append StringBuilder if Checkbox is selected
            for (int j = 0; j < comps2.length; j++) {
                panel = ((JPanel) ((JPanel) comps2[j]).getComponent(0));
                boolean selected = ((JCheckBox) panel.getComponent(0)).isSelected();
                String species = ((JLabel) panel.getComponent(1)).getText();
                if (selected)
                    sb.append(species).append(",");
            }
            sb.setLength(Math.max(sb.length() - 1, 0));
            sb.append(System.lineSeparator());
        }

//        Append Other Settings Information
        sb.append(System.lineSeparator() + "%other_settings" + System.lineSeparator());

//        Append padding info
        sb.append("5:").append(getPadding5Field().getText()).append(System.lineSeparator());
        sb.append("3:").append(getPadding3Field().getText()).append(System.lineSeparator());

//        Append filename info
//        sb.append("filename:").append().append("\n");

//      Create Settings File using StringBuilder
        try {
//          In Independent OG Implementation, uncomment
            File settingsFile = new File(System.getProperty("user.home"), "og_settings.txt");
//          In GenePalette OG Implementation, uncomment
//            File settingsFile = new File(new File(System.getProperty("user.dir"), "Resources"), "og_settings.txt");

            FileWriter fw = new FileWriter(settingsFile, false);
            fw.write(sb.toString());
            fw.close();
        } catch (Exception fe) {
            JOptionPane.showMessageDialog(null, "Unable to Save Settings!");
        }
    }

    public void submitSearch() {

        // Switch to Next Panel to do Search
        mainPane.nextView();
    }

    private String checkFilename(String str) {
        try {
            String name = str;
            if (!name.equals("Current Date & Time")) {
                if (name.equals("Filename [e.g. GR34.fasta]") || name.equals("")) {
                    Format formatter = new SimpleDateFormat("YYYY-MM-dd_hh-mm-ss");
                    name = formatter.format(new Date()).concat(".fasta");
                } else if (!name.endsWith(".fasta")) {
                    name = name.concat(".fasta");
                }
            } else {
                Format formatter = new SimpleDateFormat("YYYY-MM-dd_hh-mm-ss");
                name = formatter.format(new Date()).concat(".fasta");
            }
            return name;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setPadding5Field(JFormattedTextField padding5Field) {
        this.padding5Field = padding5Field;
    }

    public void setPadding3Field(JFormattedTextField padding3Field) {
        this.padding3Field = padding3Field;
    }

    public JFormattedTextField getPadding5Field() {
        return this.padding5Field;
    }

    public JFormattedTextField getPadding3Field() {
        return this.padding3Field;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.setGroupingUsed(false);

        setPadding3Field(new JFormattedTextField(decimalFormat));
        setPadding5Field(new JFormattedTextField(decimalFormat));

    }
}

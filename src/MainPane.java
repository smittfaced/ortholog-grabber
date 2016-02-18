import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * Created by Andrew on 11/17/2015.
 */
public class MainPane extends Container {

    JFrame mainFrame;
    JPanel mainPane;
    Controller controller;
    CardLayout cardLayout;
    OGF ogf;

    String GP_SEQ;

    public MainPane(String seq, JFrame frame) {
        GP_SEQ = seq;
        this.mainFrame = frame;
        this.ogf = new OGF();
        this.cardLayout = new CardLayout();
        this.mainPane = new JPanel(cardLayout);
        this.controller = new Controller(mainPane, cardLayout);
        this.controller.addView(new Settings(this).getSettings(), "Settings");
        this.controller.addView(new Card0(this).getSEQUENCE(), "View0");
        nextView();
    }

    ///////////////////////
    // Controller Interface
    //
    //

    public void addView(JPanel card, String name) {
        this.controller.addView(card, name);
    }

    public void removeView(JPanel card) {
        this.controller.removeView(card);
    }

    public void nextView() {
        controller.nextView();
//        System.out.println("Current: " + controller.getCurrentViewName() + " : View Index: " + controller.getCurrentViewIndex());
    }

    public void previousView() {
        controller.previousView();
//        System.out.println("Current: " + controller.getCurrentViewName() + " : View Index: " + controller.getCurrentViewIndex());
    }

    public void settingsView() {
        controller.getSettingsView();
    }

    //////////////////////
    // Setters and Getters
    //
    //

    private static void copyFile(File source, File dest)
            throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }

    public Controller getController() {
        return this.controller;
    }

    public JPanel getMainPane() {
        return this.mainPane;
    }


    public void setTitle(String title) {
        mainFrame.setTitle(title);

    }
}


import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 9/22/2015.
 */
public class Controller implements ViewController {

    private Component currentView = null;
    private java.util.List<Component> views;
    private Map<Component, String> mapNames;

    private Container parent;
    private CardLayout cardLayout;

    public Controller(Container parent, CardLayout cardLayout) {
        this.parent = parent;
        this.cardLayout = cardLayout;
        views = new ArrayList<>(25);
        mapNames = new HashMap<>(25);
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public Container getParent() {
        return parent;
    }

    public void addView(Component comp, String name) {
        mapNames.put(comp, name);
        views.add(comp);
        getParent().add(comp, name);
    }

    public void removeView(Component comp) {
        views.remove(comp);
        mapNames.remove(comp);
        getParent().remove(comp);
    }

    @Override
    public void nextView() {
        if (views.size() > 0) {
            String name = null;
            if (currentView == null) {
                currentView = views.get(1);
                name = mapNames.get(currentView);
            } else {
                int index = views.indexOf(currentView);
                index++;
                if (index >= views.size()) {
                    index = 0;
                }
                currentView = views.get(index);
                name = mapNames.get(currentView);
            }
            getCardLayout().show(getParent(), name);
        }
    }

    @Override
    public void previousView() {
        if (views.size() > 0) {
            String name = null;
            if (currentView == null) {
                currentView = views.get(views.size() - 1);
                name = mapNames.get(currentView);
            } else {
                int index = views.indexOf(currentView);
                index--;
                if (index < 0) {
                    index = views.size() - 1;
                }
                currentView = views.get(index);
                name = mapNames.get(currentView);
            }
            getCardLayout().show(getParent(), name);
        }
    }

    @Override
    public void getSettingsView() {
        if (views.size() > 0) {
            String name = null;
            int index = views.indexOf(currentView);
            index--;
            if (index < 0) {
                index = views.size() - 1;
            }
            currentView = views.get(index);
            name = mapNames.get(currentView);

            getCardLayout().show(getParent(), name);
        }
    }

    public int getCurrentViewIndex() {
        return views.indexOf(currentView);
    }

    public java.util.List getViews() {
        return this.views;
    }
}


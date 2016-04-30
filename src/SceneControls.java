import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.event.*;

/**
 * Created by lauren on 29/04/2016.
 */
class SceneControls extends JPanel {
    Scene scene;

    SceneControls(Scene scene) {
        super();
        this.scene = scene;
        setPreferredSize(new Dimension(200, 600));

        addCheckBox("Show centre of mass", e -> this.scene.draw_centre_of_mass = e.getStateChange() == ItemEvent.SELECTED);
        addCheckBox("Show neighbours",     e -> this.scene.draw_neighbours     = e.getStateChange() == ItemEvent.SELECTED);
        addCheckBox("Show view distance",  e -> this.scene.draw_view_distance  = e.getStateChange() == ItemEvent.SELECTED);

        addSlider("Number of Agents", 1, 3000, 100, e -> this.scene.swarm.target_population = ((JSlider)e.getSource()).getValue());
        addSlider("Cohesion", 0, 100, 0,   e -> this.scene.swarm.cohesion   = ((double)((JSlider)e.getSource()).getValue())/100.0);
        addSlider("Separation", 0, 100, 0, e -> this.scene.swarm.separation = ((double)((JSlider)e.getSource()).getValue())/100.0);
        addSlider("Alignment", 0, 100, 0,  e -> this.scene.swarm.alignment  = ((double)((JSlider)e.getSource()).getValue())/100.0);
        addSlider("Time Grain", 1, 100, 10, e -> this.scene.swarm.time_grain = (long)((JSlider)e.getSource()).getValue());
        addSlider("Zoom", 10, 100, 10, e -> this.scene.zoom = (((double)((JSlider)e.getSource()).getValue()))/100.0);

        addLabel("Behaviours");
        for (Behaviour s : scene.swarm.behaviours) {
            addSlider(s.label(), 0, 100, 50, e -> s.setFactor(((double)((JSlider)e.getSource()).getValue())/100.0));
        }
    }

    private void addCheckBox(String label, ItemListener listener) {
        JCheckBox checkbox = new JCheckBox(label);
        checkbox.addItemListener(listener);
        checkbox.setSelected(false);
        add(checkbox);
    }

    private void addLabel(String label) {
        add(new JLabel(label));
    }

    private void addSlider(String label, int min, int max, int value, ChangeListener listener) {
        addLabel(label);
        JSlider slider = new JSlider(min, max, value);
        slider.addChangeListener(listener);
        slider.setValue(value);
        add(slider);
    }
}
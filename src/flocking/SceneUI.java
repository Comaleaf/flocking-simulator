package flocking;

import flocking.behaviours.Behaviour;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

/**
 * Created by Y3761870 on 29/04/2016.
 */
public class SceneUI extends JPanel {
    Scene scene;

    private JSlider slider_to_monitor = null;

    public SceneUI(Scene scene) {
        super();
        this.scene = scene;
        this.setOpaque(false);
        this.setBackground(new Color(0xEE, 0xEE, 0xEE, 0xDD));
        this.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 5));

        addTitle("Simulation");
        addSlider("# Agents", 1,  4000, 100, e -> this.scene.swarm.target_population = monitorSliderValue(e.getSource()) );
        addSlider("Speed",    1,  100,  10,  e -> this.scene.swarm.time_grain = (long)((JSlider)e.getSource()).getValue() );
        addSlider("Zoom",     10, 100,  10,  e -> this.scene.zoom = ((double)((JSlider)e.getSource()).getValue()/100.0) );

        addTitle("Agent Parameters");

        addTitle("Drawing Options");
        addCheckBox("Show centre of mass", e -> this.scene.draw_centre_of_mass = e.getStateChange() == ItemEvent.SELECTED);
        addCheckBox("Show neighbours",     e -> this.scene.draw_neighbours     = e.getStateChange() == ItemEvent.SELECTED);
        addCheckBox("Show view distance",  e -> this.scene.draw_view_distance  = e.getStateChange() == ItemEvent.SELECTED);

        addTitle("Behaviours");
        for (Behaviour b : scene.swarm.behaviours) {
            addSlider(b.toString(), 0, 100, 50, e -> b.setFactor(((double)((JSlider)e.getSource()).getValue())/100.0));
        }
    }

    private int monitorSliderValue(Object slider) {
        if (!(slider instanceof JSlider))
            throw new IllegalArgumentException("Trying to monitor the value of a component that is not a slider");

        this.slider_to_monitor = (JSlider)slider;
        return ((JSlider)slider).getValue();
    }

    public void paintComponent(Graphics g) {
        g.setColor(getBackground());
        Rectangle r = g.getClipBounds();
        g.fillRect(r.x, r.y, r.width, r.height);

        super.paintComponent(g);

        if (this.slider_to_monitor != null && this.slider_to_monitor.getValueIsAdjusting()) {
            g.setColor(Color.black);
            g.setFont(slider_to_monitor.getFont());
            g.drawString(Integer.toString(this.slider_to_monitor.getValue()), this.slider_to_monitor.getX()+15, this.slider_to_monitor.getY()+5);
        }
    }

    private void addCheckBox(String label, ItemListener listener) {
        JCheckBox checkbox = new JCheckBox(label);
        checkbox.addItemListener(listener);
        checkbox.setSelected(false);
        add(checkbox);
    }

    private void addTitle(String text) {
        JLabel label = new JLabel("  " + text);
        Font labelFont = label.getFont();
        label.setFont(new Font(labelFont.getFontName(), Font.BOLD, labelFont.getSize() + 1));
        label.setPreferredSize(new Dimension(170, label.getPreferredSize().height*2));
        add(label);
    }

    private void addLabel(String text) {
        JLabel label = new JLabel(text, JLabel.RIGHT);
        label.setPreferredSize(new Dimension(80, label.getPreferredSize().height));
        add(label);
    }

    private void addSlider(String label, int min, int max, int value, ChangeListener listener) {
        addLabel(label);
        JSlider slider = new JSlider(min, max, value);
        slider.addChangeListener(listener);
        slider.setValue(value);
        slider.setPreferredSize(new Dimension(120, slider.getPreferredSize().height));
        add(slider);
    }
}
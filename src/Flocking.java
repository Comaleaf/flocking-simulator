import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by lauren on 12/03/2016.
 */
public class Flocking {
    // If 60fps is good enough for consoles it's good enough for me.
    static final int framerate = 60;

    public static void main(String[] args) {
        Swarm swarm = new Swarm();
        swarm.repopulate(100);
        swarm.strategies.add(new Cohesion());

        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setTitle("Flocking Simulator");
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Scene scene = new Scene(swarm);
        SceneControls controls = new SceneControls(scene);

        frame.add(controls);
        frame.add(scene);

        scene.revalidate();

        Timer simulation = new Timer(1000/framerate, e -> swarm.update(swarm.time_grain));
        simulation.start();

        Timer gui = new Timer(1000/framerate, e -> scene.repaint());
        gui.start();
    }
}

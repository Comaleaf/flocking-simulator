import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.JComponent;

/**
 * Created by lauren on 28/04/2016.
 */
class Scene extends JComponent {
    Swarm swarm;

    boolean draw_centre_of_mass = false;
    boolean draw_neighbours = false;
    boolean draw_view_distance = false;
    boolean draw_turn_angle = false;

    Scene(Swarm swarm) {
        super();

        this.setPreferredSize(new Dimension(800, 600));
        this.swarm = swarm;
        this.repaint();
        this.revalidate();
    }

    protected void paintComponent(Graphics g_) {
        super.paintComponent(g_);
        Graphics2D g = (Graphics2D)g_;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(3.0F));

        // Draw background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (Agent agent : swarm) {
            // Draw Agent
            Point p = agent.position;
            double y, x;
            y = Math.sin(agent.angle) * 15.0;
            x = Math.cos(agent.angle) * 15.0;
            g.setColor(Color.black);
            g.draw(new Line2D.Double(p.getX(), p.getY(), p.getX()+x, p.getY()+y));

            // Draw related stuff for agent, if enabled
            g.setColor(Color.orange);

            if (this.draw_view_distance)
                g.drawOval((int)p.getX()-(int)agent.view_distance/2, (int)p.getY()-(int)agent.view_distance/2, (int)agent.view_distance, (int)agent.view_distance);

            if (this.draw_turn_angle)
                g.drawString(String.format("(%.0f˚)", Math.floor(Math.toDegrees(agent.angle - agent.centre_of_mass_angle))), (int)p.getX(), (int)p.getY());

            g.setColor(Color.green);

            if (this.draw_neighbours) {
                g.setStroke(new BasicStroke(1.0F));
                for (Agent neighbour : agent.neighbours) {
                    g.draw(new Line2D.Double(p, neighbour.position));
                }
                g.setStroke(new BasicStroke(3.0F));
            }

            g.setColor(Color.cyan);

            if (this.draw_centre_of_mass && agent.centre_of_mass != null)
                g.draw(new Line2D.Double(p, agent.centre_of_mass));
        }
    }

}

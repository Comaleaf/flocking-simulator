package flocking;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.JComponent;
import java.util.List;
import java.util.*;

/**
 * Created by Y3761870 on 28/04/2016.
 */
public class Scene extends JComponent {
    // The swarm that this scene will draw
    Swarm swarm;

    // The zoom factor to draw at
    public double zoom = 1.0;

    // Some drawing options
    boolean draw_centre_of_mass = false;
    boolean draw_neighbours     = false;
    boolean draw_view_distance  = false;

    public Scene(Swarm swarm) {
        super();

        this.swarm = swarm;
        this.repaint();
        this.revalidate();
    }

    /**
     * Gives the bounds of the visible window the Scene will display, in terms of the simulation space. Takes
     * into account the zoom factor.
     * @return Rectangle2D containing offset and size of visible region
     */
    public Rectangle2D getScaledVisibleRegion() {
        Rectangle2D.Double bounds = new Rectangle2D.Double();
        // Essentially the 1/zooms are necessary
        bounds.setRect(
                ((getWidth()/2.0)-(getWidth()*zoom/2.0)) * -1/zoom,
                ((getHeight()/2.0)-(getHeight()*zoom/2.0)) * -1/zoom,
                this.getWidth() * 1/zoom,
                this.getHeight() * 1/zoom);
        return bounds;
    }

    protected void paintComponent(Graphics g_) {
        super.paintComponent(g_);
        // Use Graphics2D instead of Graphics - the Graphics interface does not support rendering hints/etc.
        Graphics2D g = (Graphics2D)g_;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Inverse zoom-factor so you can still see agents when zoomed
        g.setStroke(new BasicStroke(2.0F * 1/(float)this.zoom));

        // Draw white background fill
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Centres the view at any zoom level on what would be the centre of the view at 1.0 zoom. (I.e. this
        // keeps the centre point consistent at all zoom levels)
        g.translate((getWidth()/2.0)-(getWidth()*zoom/2.0), (getHeight()/2.0)-(getHeight()*zoom/2.0));
        g.scale(this.zoom, this.zoom);

        Ellipse2D.Double view_distance = new Ellipse2D.Double(0.0, 0.0, swarm.getViewDistance()*2, swarm.getViewDistance()*2);

        for (Agent agent : swarm) {
            // Skip agents that aren't in the visible region
            if (!g.getClipBounds().contains(agent.position))
                continue;

            // Draw Agent
            Point p = agent.position;
            double y, x;
            y = Math.sin(agent.angle) * 4.0 * this.zoom;
            x = Math.cos(agent.angle) * 4.0 * this.zoom;
            g.setColor(Color.black);

            g.draw(new Line2D.Double(p.x-x, p.y-y, p.x+x, p.y+y));

            // Drawing ovals seems to be really slow - I don't know why this is exactly. Turning off
            // anti-aliasing helps a lot and isn't really necessary for this.
            // It's here because we might as well disable it for all of the accessory display things
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            // Draw related stuff for agent, if enabled
            if (this.draw_view_distance) {
                g.setColor(Color.orange);
                view_distance.x = p.x - swarm.getViewDistance();
                view_distance.y = p.y - swarm.getViewDistance();
                g.draw(view_distance);
            }

            if (this.draw_centre_of_mass && agent.centre_of_mass != null) {
                g.setColor(Color.cyan);
                g.draw(new Line2D.Double(p, agent.centre_of_mass));
            }

            if (this.draw_neighbours) {
                g.setColor(Color.green);
                // This operation is quite expensive at O(n^2) so it can't do more than a few hundred agents
                // without slowing down.
                g.setStroke(new BasicStroke(1.0F * 1/(float)this.zoom));
                agent.neighbours.stream().forEach(n -> g.draw(new Line2D.Double(p, n.position)));
                g.setStroke(new BasicStroke(3.0F * 1/(float)this.zoom)); // Return stroke size back
            }
        }

    }

}

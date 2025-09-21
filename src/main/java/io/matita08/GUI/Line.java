package io.matita08.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * A custom Swing component that draws a horizontal line for visual separation.
 * This component extends JComponent and provides a simple graphical line
 * that can be used to visually separate sections of the GUI interface.
 *
 * <p>The line is drawn in black and is automatically centered horizontally
 * within the component's bounds. The line spans the full width of the component
 * and is positioned at the vertical center.</p>
 *
 * <p>This component is commonly used in the CPU simulator interface to
 * visually separate different functional areas and provide clear boundaries
 * between CPU components.</p>
 *
 * @version 1.0
 * @since 1.0
 */
public class Line extends JComponent {
   /**
    * Paints the line component by drawing a centered horizontal black line.
    * The line extends from the left edge to the right edge of the component
    * and is positioned at the vertical center.
    *
    * @param g the Graphics context used for painting the component
    */
   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Dimension d = this.getSize();
      //draw in black
      g.setColor(Color.BLACK);
      //draw a centered horizontal line
      g.drawLine(0,d.height/2, d.width,d.height/2);
   }
}

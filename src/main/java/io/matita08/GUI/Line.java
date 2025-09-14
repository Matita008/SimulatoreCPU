package io.matita08.GUI;

import javax.swing.*;
import java.awt.*;

public class Line extends JComponent {
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

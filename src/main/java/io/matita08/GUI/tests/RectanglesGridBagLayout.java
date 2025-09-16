package io.matita08.GUI.tests;
import javax.swing.*;
import java.awt.*;

public class RectanglesGridBagLayout extends JFrame {
   
   public RectanglesGridBagLayout() {
      setTitle("Relative Rectangles Layout");
      setSize(900, 600);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      JPanel panel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.fill = GridBagConstraints.BOTH;
      gbc.insets = new Insets(5, 5, 5, 5);
      
      // top-left small square
      addRectangle(panel, gbc, 0, 0, 1, 1);
      
      // top-right small square
      addRectangle(panel, gbc, 3, 0, 1, 1);
      
      // middle-left small square
      addRectangle(panel, gbc, 1, 1, 1, 1);
      
      // left-mid bigger square
      addRectangle(panel, gbc, 0, 2, 2, 2);
      
      // lower-left small square
      addRectangle(panel, gbc, 1, 4, 1, 1);
      
      // bottom-left small square
      addRectangle(panel, gbc, 1, 5, 1, 1);
      
      // bottom-center wide rectangle
      addRectangle(panel, gbc, 2, 5, 2, 1);
      
      // bottom small square (under bottom-center)
      addRectangle(panel, gbc, 4, 5, 1, 1);
      
      // big right square
      addRectangle(panel, gbc, 5, 2, 3, 3);
      
      setContentPane(panel);
      setVisible(true);
   }
   
   private void addRectangle(JPanel panel, GridBagConstraints gbc,
                             int gridx, int gridy, int gridw, int gridh) {
      JLabel rect = new JLabel(" 8 ");
      rect.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
      gbc.gridx = gridx;
      gbc.gridy = gridy;
      gbc.gridwidth = gridw;
      gbc.gridheight = gridh;
      panel.add(rect, gbc);
   }
   
   public static void main(String[] args) {
      SwingUtilities.invokeLater(RectanglesGridBagLayout::new);
   }
}

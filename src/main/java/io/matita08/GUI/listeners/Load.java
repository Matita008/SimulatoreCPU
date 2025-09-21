package io.matita08.GUI.listeners;

import io.matita08.GUI.Display;
import io.matita08.Utils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.*;
import java.io.*;

/**
 * Action listener implementation for handling file loading operations in the CPU simulator.
 * This class manages the file selection dialog and coordinates the loading of program files
 * into the simulator's central memory through the Utils.loadMC() method.
 *
 * <p>The Load listener provides a file chooser interface that filters for supported
 * file types (.txt, .bat, .sim) and integrates with the simulator's memory loading
 * system. When a file is selected, it is processed and loaded into the central memory
 * for program execution.</p>
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>File type filtering for simulator-compatible formats</li>
 *   <li>Cross-platform look-and-feel support</li>
 *   <li>Integration with simulator memory system</li>
 *   <li>Proper window management and user interaction</li>
 * </ul>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 */
public class Load implements ActionListener {
   
   /**
    * File chooser component for selecting program files to load.
    */
   JFileChooser fc;
   
   /**
    * Frame window containing the file chooser dialog.
    */
   JFrame f;
   
   /**
    * Constructs a new Load action listener with configured file chooser.
    * Sets up file filtering for supported formats and initializes the
    * file chooser with the current working directory.
    */
   public Load() {
      fc = new JFileChooser(System.getProperty("user.dir"));
      fc.setFileFilter(new FileFilter() {//TODO Low priority: can be more clean?
         /** {@inheritDoc} */
         @Override
         public boolean accept(File f) {
            return f.isDirectory() || f.getName().endsWith(".txt") || f.getName().endsWith(".md") || f.getName().endsWith(".csv") || f.getName().endsWith(".sim") || f.getName().endsWith(".dat") || f.getName().endsWith(".asm") || f.getName().endsWith(".bin");
         }
         
         /** {@inheritDoc} */
         @Override
         public String getDescription() {
            return "Text file (.txt, .sim)";
         }
      });
      fc.addActionListener(this::load);
   }
   
   /**
    * Handles the action event when the load button is clicked.
    * Creates and displays the file chooser dialog window, or brings
    * an existing dialog to the front if already open.
    *
    * @param e the action event that triggered this method
    */
   @Override
   public void actionPerformed(ActionEvent e) {
      if(f != null) {
         f.setVisible(true);
         f.toFront();
         return;
      }
      f = new JFrame("Open file");
      f.add(fc);
      f.setVisible(true);
      try {
         UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException ex) {
         //noinspection CallToPrintStackTrace
         ex.printStackTrace();
         throw new RuntimeException(ex);
      }
      f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      f.pack();
      f.setVisible(true);
      Display.instance.enableInputMethods(false);
      fc.setMultiSelectionEnabled(false);
   }
   
   /**
    * Handles the file selection action from the file chooser.
    * Processes the user's file selection and loads the chosen file
    * into the simulator's central memory if approved.
    *
    * @param e the action event from the file chooser dialog
    */
   public void load(ActionEvent e) {
      if(f == null) return;
      Display.instance.enableInputMethods(true);
      //System.out.println(e);
      f.setVisible(false);
      f.dispose();
      f = null;
      if (e.getActionCommand().equals("CancelSelection")) return;
      if (!e.getActionCommand().equals("ApproveSelection")) return;
      //System.out.println("Opened file: " + fc.getSelectedFile().toString());
      Utils.loadMC(fc.getSelectedFile());
   }
}

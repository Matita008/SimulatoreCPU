package io.matita08.GUI.listeners;

import io.matita08.GUI.Display;
import io.matita08.GUI.Registers;
import io.matita08.Utils;
import io.matita08.value.Value;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class Load implements ActionListener {
   JFileChooser fc;
   JFrame f;
   
   public Load() {
      fc = new JFileChooser(System.getProperty("user.dir"));
      fc.setFileFilter(new FileFilter() {//TODO Low priority: can be more clean?
         @Override
         public boolean accept(File f) {
            return f.isDirectory() || f.getName().endsWith(".txt") || f.getName().endsWith(".bat") || f.getName().endsWith(".sim");
         }
         
         @Override
         public String getDescription() {
            return "Text file (.txt, .sim)";
         }
      });
      fc.addActionListener(this::load);
   }
   @Override
   public void actionPerformed(ActionEvent e) {
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
   
   public void load(ActionEvent e) {
      if(f == null) return;
      Display.instance.enableInputMethods(true);
      System.out.println(e);
      f.setVisible(false);
      f.dispose();
      f = null;
      if (e.getActionCommand().equals("CancelSelection")) return;
      if (!e.getActionCommand().equals("ApproveSelection")) return;
      System.out.println("Opened file: " + fc.getSelectedFile().toString());
      Utils.loadMC(fc.getSelectedFile());
   }
}

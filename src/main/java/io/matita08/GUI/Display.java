package io.matita08.GUI;

import io.matita08.Constants;
import io.matita08.GUI.listeners.Load;
import io.matita08.logic.Execution;
import io.matita08.value.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Display extends JFrame {
   public static Display instance;
   
   private static Thread swingThread;
   
   JPanel main;
   JPanel MC;
   JPanel busLabels;
   JPanel interfaces;
   JPanel controlPanel;
   JPanel CPU;
   
   JButton load;
   
   ArrayList<JLabel> MCData = new ArrayList<>(Constants.MC_Size);
   
   private Display() {
      super("Simulator");
      main = new JPanel(new GridBagLayout());
      createMainComponents();
      this.add(main);
      this.pack();
      this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      this.setVisible(true);
      swingThread = Thread.currentThread();
   }
   
   private void createMainComponents() {
      GridBagConstraints cfg = new GridBagConstraints();
      Border b;
      TitledBorder tb;
      
      CPU = new JPanel();
      cfg.gridx = 0;      //start column
      cfg.gridy = 0;      //start row
      cfg.gridheight = 3; //row span
      cfg.gridwidth = 5;  //column span
      cfg.ipadx = 2;      //padding (x-axis)
      cfg.ipady = 3;      //padding (y-axis)
      cfg.weighty = 0.6;  //distribution of extra space (y-axis)
      createCPUComponents();
      main.add(CPU, cfg);
      
      busLabels = new JPanel();
      cfg.gridx = 5;      //start column
      cfg.gridy = 0;      //start row
      cfg.gridheight = 3; //row span
      cfg.gridwidth = 1;  //column span
      cfg.ipadx = 2;      //padding (x-axis)
      cfg.ipady = 2;      //padding (y-axis)
      cfg.weighty = 0;    //distribution of extra space (y-axis)
      createBusComponents();
      main.add(busLabels, cfg);
      
      tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Central Memory");
      tb.setTitleJustification(TitledBorder.LEFT);
      tb.setTitlePosition(TitledBorder.TOP);
      b = BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5), tb);
      JPanel tmp = new JPanel();
      tmp.setBorder(b);
      MC = new JPanel();
      MC.setLayout(new GridLayout(0, 2, 2, 0));//DONT FREAKING TOUCH ME, 0 IS ANY NUMBER OF ROWS (ENTRIES) YOU FREAAKY MORON DUMB!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      MC.setBorder(BorderFactory.createMatteBorder(0,0,0,0,Color.lightGray));
      cfg.gridx = 6;      //start column
      cfg.gridy = 0;      //start row
      cfg.gridheight = 6; //row span
      cfg.gridwidth = 2;  //column span
      cfg.ipadx = 5;      //padding (x-axis)
      cfg.ipady = 5;      //padding (y-axis)
      cfg.weighty = 0.6;  //distribution of extra space (y-axis)
      createMCComponents();
      JScrollPane tmpS = new JScrollPane(MC, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      tmpS.setBorder(BorderFactory.createMatteBorder(0,0,0,0,Color.lightGray));
      tmp.add(tmpS);
      main.add(tmp, cfg);
      
      controlPanel = new JPanel();
      cfg.gridx = 0;      //start column
      cfg.gridy = 3;      //start row
      cfg.gridheight = 3; //row span
      cfg.gridwidth = 3;  //column span
      cfg.ipadx = 2;      //padding (x-axis)
      cfg.ipady = 3;      //padding (y-axis)
      cfg.weighty = 0.6;  //distribution of extra space (y-axis)
      createControlPanelComponents();
      main.add(controlPanel, cfg);
      
      interfaces = new JPanel();
      cfg.gridx = 3;      //start column
      cfg.gridy = 3;      //start row
      cfg.gridheight = 3; //row span
      cfg.gridwidth = 3;  //column span
      cfg.ipadx = 2;      //padding (x-axis)
      cfg.ipady = 3;      //padding (y-axis)
      cfg.weighty = 0.6;  //distribution of extra space (y-axis)
      createInterfacesComponents();
      main.add(interfaces, cfg);
   }
   
   private void createCPUComponents() {
      CPU.add(new JLabel("CPU"));
   }
   
   private void createBusComponents() {
      busLabels.add(new JLabel("bus"));
   }
   
   private void createMCComponents() {
      ((JLabel)MC.add(new JLabel("Addresses  "))).setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
      MC.add(new JLabel(" Values"));
      for (int i = 0; i < Constants.MC_Size; i++) {
         JLabel lb = new JLabel(i + " ", SwingConstants.TRAILING);
         lb.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
         MC.add(lb);
         JLabel l = new JLabel(" " + DoubleValue.unset());
         MC.add(l);
         MCData.add(l);
      }
   }
   
   private void createControlPanelComponents() {
      JButton step = new JButton("Step");
      step.addActionListener(Execution::step);
      controlPanel.add(step);
      load = new JButton("Load file");
      controlPanel.add(load);
      load.addActionListener(new Load());
      JButton update = new JButton("Update");
      update.addActionListener(_ -> Display.update());
      controlPanel.add(update);
   }
   
   private void createInterfacesComponents() {
      interfaces.add(new JLabel("bus thingy"));
   }
   
   public static void update() {
      try {
         if(swingThread == Thread.currentThread()) instance.updateImpl();
         else SwingUtilities.invokeAndWait(()->instance.updateImpl());
      } catch (InterruptedException | InvocationTargetException ex) {
         ex.printStackTrace();
      }
   }
   
   private void updateImpl() {
      if(Execution.stepped) instance.updateCU();
      if((Registers.modFlag & 1) == 1) instance.updateMC();
      if((Registers.modFlag & 2) == 2) instance.updatePR();
      if((Registers.modFlag & 4) == 4) instance.updateALU();
   }
   
   private void updateCU() {
   
   }
   
   private void updateMC() {
      for (int i = 0; i < Constants.MC_Size; i++) {
         Value v = Registers.getMC(i);
         String s;
         if(v instanceof UndefinedValue) s = "??";
         else s = String.valueOf(v.get());
         MCData.get(i).setText(s);
      }
   }
   
   private void updatePR() {//ir, pc, mdr, mar, pointer
   
   }
   
   private void updateALU() {//Acc, regB, flags, result, alu op
   
   }
   
   public static void main(String[] args) {
      System.out.println("Hello world!");
      SwingUtilities.invokeLater(Display::init);
   }
   
   public static void init() {
      instance = new Display();
   }
}

/* java.awt.event.ActionEvent[ACTION_PERFORMED,cmd=ApproveSelection,when=1748275134527,modifiers=Button1] on javax.swing.JFileChooser[,0,0,500x326,invalid,
   layout=java.awt.BorderLayout,alignmentX=0.0,alignmentY=0.0,border=javax.swing.border.EmptyBorder@731fa788,flags=320,maximumSize=,minimumSize=,preferredSize=,
   approveButtonText=,currentDirectory=C:\Users\matti\Documents\!other,dialogTitle=,dialogType=OPEN_DIALOG,fileSelectionMode=FILES_ONLY,returnValue=APPROVE_OPTION,
   selectedFile=C:\Users\matti\Documents\!other\Digital-Logic-Sim-Windows.zip,useFileHiding=false] */
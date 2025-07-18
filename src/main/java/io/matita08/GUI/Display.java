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
   
   //JPanels attached directly to the main panel
   JPanel main;
   JPanel MC;
   JPanel busLabels;
   JPanel interfaces;
   JPanel controlPanel;
   JPanel CPU;
   
   //CPU related components
   JLabel PC;
   JLabel IR;
   JLabel MAR;
   JLabel MDR;
   JLabel Pointer;
   JLabel Acc;
   JLabel RegB;
   JLabel PSW;//Program status word //Contains the various flags as a bitmask value
   
   //Control panel related components
   JButton load;
   
   //MC related components
   ArrayList<JLabel> MCData = new ArrayList<>(Constants.getMC_Size());
   
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
      cfg.fill = GridBagConstraints.BOTH;
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
      MC = new JPanel();
      MC.setLayout(new GridLayout(0, 2, 2, 0));//DONT FREAKING TOUCH ME, 0 IS ANY NUMBER OF ROWS (ENTRIES) YOU FREAAKY MORON DUMB!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      MC.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.lightGray));
      //MC.setPreferredSize(new Dimension(150,400));
      JScrollPane scroll = new JScrollPane(MC, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      scroll.setBorder(b);
      scroll.setPreferredSize(new Dimension(150, 250));
      scroll.setMinimumSize(new Dimension(75, 50));
      cfg.gridx = 6;      //start column
      cfg.gridy = 0;      //start row
      cfg.gridheight = 6; //row span
      cfg.gridwidth = 2;  //column span
      cfg.ipadx = 5;      //padding (x-axis)
      cfg.ipady = 5;      //padding (y-axis)
      cfg.weighty = 0.6;  //distribution of extra space (y-axis)
      createMCComponents();
      main.add(scroll, cfg);
      
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
      createCPUAdrComponents();
      createALUComponents();
      createCUComponents();
   }
   
   private void createCPUAdrComponents() {
      CPU.add(new JLabel("CPUAddr"));
   }
   
   private void createALUComponents() {
      CPU.add(new JLabel("ALU"));
   }
   
   private void createCUComponents() {
      CPU.add(new JLabel("CU"));
   }
   
   private void createBusComponents() {
      busLabels.add(new JLabel("bus"));
   }
   
   private void createMCComponents() {
      JLabel adL = (JLabel)MC.add(new JLabel("Addresses  ", SwingConstants.RIGHT));
      //adL.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
      MC.add(new JLabel(" Values", SwingConstants.LEFT));
      for (int i = 0; i < Constants.getMC_Size(); i++) {
         JLabel lb = new JLabel(i + " -> ", SwingConstants.TRAILING);
         //lb.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
         MC.add(lb);
         JLabel l = new JLabel(DoubleValue.unset(), SwingConstants.LEFT);
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
      update.addActionListener(_->Display.update());
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
      for (int i = 0; i < Constants.getMC_Size(); i++) {
         Value v = Registers.getMC(i);
         String s;
         if(v instanceof UndefinedValue) s = UndefinedValue.unset();
         else {
            int n = v.get();
            if(n < 10) s = " ";
            //else if(n < 100) s = " ";//if decommented the string in the line above shall have 2 spaces, else only one
            else s = "";
            s += String.valueOf(n);
         }
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
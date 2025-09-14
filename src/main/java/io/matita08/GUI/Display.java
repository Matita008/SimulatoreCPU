package io.matita08.GUI;

import io.matita08.Constants;
import io.matita08.GUI.listeners.Load;
import io.matita08.data.*;
import io.matita08.logic.*;
import io.matita08.value.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Display extends JFrame {
   public static Display instance;
   
   /**
    * The thread where all swing operation are executed
    */
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
   
   JLabel instruction;
   JLabel phase;
   JLabel nextPhase;
   JLabel cycle;
   JLabel remaining;
   
   //Control panel related components
   JButton load;
   
   //MC related components
   ArrayList<JLabel> MCData = new ArrayList<>(Constants.getMCSize());
   
   private Display() {
      super("Simulator");
      main = new JPanel(new GridBagLayout());
      createMainComponents();
      this.add(main);
      this.pack();
      this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      this.setVisible(true);
      swingThread = Thread.currentThread();
      //Arrays.stream(main.getComponents()).forEach(e ->{System.out.println(" x: " + e.getX() + ", " + e.getWidth() + ", y: " + e.getY() + ", " + e.getHeight());});
   }
   
   /**
    * Create a black line border with a title in the top-left and a padding of 5px
    *
    * @param title The border title
    * @return a new border
    */
   private Border titleBorder(String title) {
      TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), title);
      tb.setTitleJustification(TitledBorder.LEFT);
      tb.setTitlePosition(TitledBorder.TOP);
      return BorderFactory.createCompoundBorder(tb, new EmptyBorder(5, 5, 5, 5));
   }
   
   private Border displayBorder() {
      Border b = BorderFactory.createLineBorder(new Color(0.15f, 0.15f,0.15f));
      return BorderFactory.createCompoundBorder(b, new EmptyBorder(3, 1, 2, 5));
   }
   
   private JLabel createDisplayBox(){
      return createDisplayBox(displayBorder());
   }
   
   private GridBagConstraints set(GridBagConstraints gbc, int x, int y, int width, int height){
      gbc.gridx = x;
      gbc.gridy = y;
      gbc.gridwidth = width;
      gbc.gridheight = height;
      return gbc;
   }
   
   private void addLine(GridBagConstraints gbc, Container container){
      container.add(new Line(), gbc);
   }
   
   private void addSpace(GridBagConstraints gbc, Container container){
      container.add(new JSeparator(), gbc);
   }
   /*
   private JLabel createTitledDisplayBox(String title) {
     return createDisplayBox(titleBorder(title));
     }//*/
   
   private JLabel createDisplayBox(Border b) {
      JLabel l = new JLabel();
      l.setBackground(Color.white);
      l.setOpaque(true);
      l.setBorder(b);
      return l;
   }
   
   private void createMainComponents() {
      GridBagConstraints gridPosition = new GridBagConstraints();
      gridPosition.weightx = 0.5;
      Border border;
      
      border = titleBorder("CPU");
      CPU = new JPanel(new GridBagLayout());
      CPU.setBorder(border);
      gridPosition.gridx = 0;      //start column
      gridPosition.gridy = 0;      //start row
      gridPosition.gridheight = 4; //row span
      gridPosition.gridwidth = 5;  //column span
      gridPosition.ipadx = 2;      //padding (x-axis)
      gridPosition.ipady = 3;      //padding (y-axis)
      gridPosition.weighty = 0.6;  //distribution of extra space (y-axis)
      gridPosition.fill = GridBagConstraints.BOTH;
      createCPUComponents(CPU);
      main.add(CPU, gridPosition);
      
      busLabels = new JPanel();
      gridPosition.gridx = 5;      //start column
      gridPosition.gridy = 0;      //start row
      gridPosition.gridheight = 3; //row span
      gridPosition.gridwidth = 1;  //column span
      gridPosition.ipadx = 2;      //padding (x-axis)
      gridPosition.ipady = 2;      //padding (y-axis)
      gridPosition.weighty = 0;    //distribution of extra space (y-axis)
      createBusComponents();
      main.add(busLabels, gridPosition);
      
      border = titleBorder("Central Memory");
      MC = new JPanel();
      MC.setLayout(new GridLayout(0, 2, 2, 0));//DONT FREAKING TOUCH ME, 0 IS ANY NUMBER OF ROWS (ENTRIES) YOU FREAAKY MORON DUMB!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      MC.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.lightGray));
      //MC.setPreferredSize(new Dimension(150,400));
      JScrollPane scroll = new JScrollPane(MC, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      scroll.setBorder(border);
      scroll.setPreferredSize(new Dimension(150, 250));
      scroll.setMinimumSize(new Dimension(75, 50));
      gridPosition.gridx = 6;      //start column
      gridPosition.gridy = 0;      //start row
      gridPosition.gridheight = 6; //row span
      gridPosition.gridwidth = 2;  //column span
      gridPosition.ipadx = 5;      //padding (x-axis)
      gridPosition.ipady = 5;      //padding (y-axis)
      gridPosition.weighty = 0.6;  //distribution of extra space (y-axis)
      createMCComponents();
      main.add(scroll, gridPosition);
      
      border = titleBorder("Control Panel");
      controlPanel = new JPanel();
      controlPanel.setBorder(border);
      gridPosition.gridx = 0;      //start column
      gridPosition.gridy = 5;      //start row
      gridPosition.gridheight = 1; //row span
      gridPosition.gridwidth = 3;  //column span
      gridPosition.ipadx = 1;      //padding (x-axis)
      gridPosition.ipady = 3;      //padding (y-axis)
      gridPosition.weighty = 0.6;  //distribution of extra space (y-axis)
      createControlPanelComponents();
      main.add(controlPanel, gridPosition);
      
      interfaces = new JPanel();
      gridPosition.gridx = 3;      //start column
      gridPosition.gridy = 3;      //start row
      gridPosition.gridheight = 3; //row span
      gridPosition.gridwidth = 3;  //column span
      gridPosition.ipadx = 2;      //padding (x-axis)
      gridPosition.ipady = 3;      //padding (y-axis)
      gridPosition.weighty = 0.6;  //distribution of extra space (y-axis)
      createInterfacesComponents();
      main.add(interfaces, gridPosition);
   }
   
   private void createCPUComponents(Container CPU) {
      GridBagConstraints gridPosition = new GridBagConstraints();
      gridPosition.fill = GridBagConstraints.BOTH;
      gridPosition.ipadx = 2;
      gridPosition.ipady = 2;
      gridPosition.weightx = 0.1;
      gridPosition.weighty = 0.1;
      gridPosition.insets = new Insets(2,2,2,2);
      
      /*
      *2(+1 x Labels)
         |  0  |  1  |  2  |  3  |  4  |  5  | 6...10 |
       0 | PC  |     |     |     |     | MAR |        |
       1 |     | Pnt |     |     |     |     |        |
       2 | IR  |     |     |     |     | MDR |        |
       3 |     |     |     |     |     |     |        |
       4 |     |     |     |     |     |     |--------|
       5 |     |     |     |     |     |     |        |
       6 |     |     |     |     |     |     |        |
       7 |     |     |     |     |     |     |   CU   |
       8 |     |     |     |     |     |     |        |
       9 |     |     |     |     |     |     |        |
      10 |     |     |     |     |     |     |--------|
       */
      
      set(gridPosition,0,0,1,1);
      CPU.add(new JLabel("PC"), gridPosition);
      gridPosition.gridy = 1;
      CPU.add((PC = createDisplayBox()), gridPosition);
      
      set(gridPosition, 7,0,1,1);
      CPU.add(new JLabel("MAR"), gridPosition);
      gridPosition.gridy = 1;
      CPU.add((MAR = createDisplayBox()), gridPosition);
      
      addLine(set(gridPosition,1,1,6,1), CPU);
      
      //This is here to make the 6th column be filled with the line, not the MAR/MDR textboxes
      CPU.add(new JLabel(), set(gridPosition, 6,2,1,1));
      
      set(gridPosition,2,2,1,1);
      CPU.add(new JLabel("Pointer"), gridPosition);
      gridPosition.gridy = 3;
      CPU.add((Pointer = createDisplayBox()), gridPosition);
      Pointer.setMinimumSize(new Dimension(16, 8));
      
      set(gridPosition,0,4,1,1);
      CPU.add(new JLabel("IR"), gridPosition);
      gridPosition.gridy = 5;
      CPU.add((IR = createDisplayBox()), gridPosition);
      
      set(gridPosition,7,4,1,1);
      CPU.add(new JLabel("MDR"), gridPosition);
      gridPosition.gridy = 5;
      CPU.add((MDR = createDisplayBox()), gridPosition);
      
      addLine(set(gridPosition,1,5,6,1), CPU);
      
      addLine(set(gridPosition, 8, 1, 2,1), CPU);
      addLine(set(gridPosition, 8, 5,1,1), CPU);
      
      updatePR();
      
      CPU.add(new JLabel("Acc"), set(gridPosition,0,8,1,1));
      gridPosition.gridy = 9;
      CPU.add((Acc = createDisplayBox()), gridPosition);
      
      //Don't make Acc and RegB too tall hack
      CPU.add(new JLabel(), set(gridPosition, 0,6,1,1));
      CPU.add(new JLabel(), set(gridPosition, 0,7,1,1));
      CPU.add(new JLabel(), set(gridPosition, 0,10,1,1));
      CPU.add(new JLabel(), set(gridPosition, 0,13,1,1));
      CPU.add(new JLabel(), set(gridPosition, 0,14,1,1));
      
      CPU.add(new JLabel("Reg B"), set(gridPosition,0,11,1,1));
      gridPosition.gridy = 12;
      CPU.add((RegB = createDisplayBox()), gridPosition);
   /*
   JLabel Acc;
   JLabel RegB;
   JLabel PSW;
    */
      
      CPU.add(createCUComponents(), set(gridPosition,6,8,4,6));
   }
   
   private Component createCUComponents() {
      JPanel CU = new JPanel(new GridLayout(6,2,4,2));
      CU.setBorder(titleBorder("Control Unit"));
      
      CU.add(new JLabel("Instruction"));
      CU.add(new JLabel(" "));//spacer
      
      instruction = createDisplayBox();
      CU.add(instruction);
      
      CU.add(new JLabel(" "));//spacer
      
      CU.add(new JLabel("Current Phase"));
      CU.add(new JLabel("Cycles Total"));
      
      phase = createDisplayBox();
      CU.add(phase);
      cycle = createDisplayBox();
      CU.add(cycle);
      
      CU.add(new JLabel("Next Phase"));
      CU.add(new JLabel("Remaining Cycles"));
      
      nextPhase = createDisplayBox();
      CU.add(nextPhase);
      remaining = createDisplayBox();
      CU.add(remaining);
      
      updateCU();
      return CU;
   }
   
   private void createBusComponents() {
      busLabels.add(new JLabel("bus"));
   }
   
   private void createMCComponents() {
      MC.add(new JLabel("Addresses  ", SwingConstants.RIGHT));
      MC.add(new JLabel(" Values", SwingConstants.LEFT));
      for (int i = 0; i < Constants.getMCSize(); i++) {
         JLabel lb = new JLabel(i + " -> ", SwingConstants.TRAILING);
         MC.add(lb);
         JLabel l = new JLabel(SingleValue.unset(), SwingConstants.LEFT);
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
      update.addActionListener((e) -> Display.update());
      controlPanel.add(update);
   }
   
   
   
   private void createInterfacesComponents() {
      interfaces.add(new JLabel("bus thingy stuff"));
   }
   
   /**
    * Update the GUI
    * If the current thread is the swing event thread it will get run immediately, else it will wait for the swing thread to execute the update
    * @see #swingThread
    */
   public static void update() {
      try {
         if(swingThread == Thread.currentThread()) instance.updateImpl();
         else SwingUtilities.invokeAndWait(()->instance.updateImpl());
      } catch (InterruptedException | InvocationTargetException ex) {
         //noinspection CallToPrintStackTrace TODO: ?
         ex.printStackTrace();
      }
   }
   
   private void updateImpl() {
      if(Execution.stepped) instance.updateCU();
      if((Registers.modFlag & 1) == 1) instance.updateMC();
      if((Registers.modFlag & 2) == 2) instance.updatePR();
      if((Registers.modFlag & 4) == 4) instance.updateALU();
   }
   
   /**
    * Function to update the visual part of the Control Unit
    */
   private void updateCU() {
      if(ControlUnit.opcode == Operation.Unknown) instruction.setText("");
      else instruction.setText(ControlUnit.opcode.name.toUpperCase(Locale.ROOT));
      
      if(ControlUnit.current == Phase.None) phase.setText("");
      else phase.setText(ControlUnit.current.name());
      
      nextPhase.setText(ControlUnit.next.name());
      
      if(ControlUnit.totalCycles == -1) cycle.setText("");
      else cycle.setText(String.valueOf(ControlUnit.totalCycles));
      
      if(ControlUnit.currentCycle == -1) remaining.setText("");
      else remaining.setText(String.valueOf(ControlUnit.currentCycle));
   }
   
   /**
    * Function to update the visual part of the Central Memory
    */
   private void updateMC() {
      for (int i = 0; i < Constants.getMCSize(); i++) {
         Value v = Registers.getMC(i);
         String s;
         if(v instanceof UndefinedSingleValue) s = DoubleValue.unset();
         else {   //I don't like the else case being larger than the if one, but inverting them looks so bad, so I'll stick with this abomination
            int n = v.get();
            if(n < 10) s = " ";  //Bad padding logic, ik.
            //else if(n < 100) s = " ";//if uncommented the string in the line above shall have 2 spaces, else only one
            else s = "";
            s += String.valueOf(n);
         }
         MCData.get(i).setText(s);
      }
   }
   
   /**
    * Function to update a visual part of the CPU
    */
   private void updatePR() {//ir, pc, mdr, mar, pointer
      PC.setText(Registers.pc().toString());
      MAR.setText(Registers.getMAR().toString());
      Pointer.setText(Registers.getPointer().toString());
      MDR.setText(Registers.getMDR().toString());
      IR.setText(Registers.getIr().toString());
   
   }
   
   /**
    * Function to update the visual part of the CPU that include all ALU-related components
    */
   private void updateALU() {//Acc, regB, flags, result, alu op
      Acc.setText(Registers.getAcc().toString());
      RegB.setText(Registers.getRegB().toString());
      Acc.setText(Registers.getAcc().toString());
   }
   
   /**
    * Execute this only to test the GUI, the real program starts in {@link io.matita08.Main#main(String[])}
    */
   public static void main(String[] args) throws InterruptedException, InvocationTargetException {
      System.out.println("Hello world!");
      for(String s : args) System.out.println("arg = " + s);
      SwingUtilities.invokeAndWait(Display::init);
      int i = 0;
      for (Component c : instance.CPU.getComponents()) {
         System.out.println("c" + (i++) + " = " + c);
      }
   }
   
   /**
    * Initialize the GUI
    */
   public static void init() {
      if(instance != null) return;  //Ensure only a GUI can be created
      instance = new Display();
   }
}

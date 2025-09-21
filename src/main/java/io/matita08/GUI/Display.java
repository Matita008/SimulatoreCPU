package io.matita08.GUI;

import io.matita08.*;
import io.matita08.GUI.listeners.Load;
import io.matita08.data.*;
import io.matita08.logic.*;
import io.matita08.value.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Main graphical user interface for the CPU Simulator application.
 * This class provides a comprehensive visual representation of a simulated CPU,
 * including CPU registers, central memory, control unit, ALU, and I/O interfaces.
 *
 * <p>The Display class extends JFrame and creates a detailed simulation environment
 * with the following major components:</p>
 * <ul>
 *   <li><strong>CPU Section:</strong> Shows registers (PC, IR, MAR, MDR, Pointer, Acc, RegB),
 *       ALU operations, and Program Status Word (PSW)</li>
 *   <li><strong>Central Memory:</strong> Scrollable view of memory addresses and their values</li>
 *   <li><strong>Control Unit:</strong> Displays current instruction, execution phases, and cycle information</li>
 *   <li><strong>I/O Interfaces:</strong> Input/output buffers, display, and numeric keypad for user interaction</li>
 *   <li><strong>Control Panel:</strong> Buttons for stepping through execution and loading files</li>
 * </ul>
 *
 * <p>This class follows the Singleton pattern to ensure only one display instance exists.
 * All GUI updates are thread-safe and executed on the Swing Event Dispatch Thread.</p>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 */
public class Display extends JFrame {
   /**
    * Singleton instance of the Display window.
    */
   public static Display instance;
   
   /**
    * The thread where all swing operation are executed
    */
   private static Thread swingThread;
   
   //JPanels attached directly to the main panel
   /**
    * Main container panel using GridBagLayout for component organization.
    */
   JPanel main;
   
   /**
    * Panel containing the central memory display with scrollable memory addresses.
    */
   JPanel MC;
   
   /**
    * Panel containing bus labels for address and data bus indicators.
    */
   JPanel busLabels;
   
   /**
    * Panel containing I/O interfaces including buffers, display, and numpad.
    */
   JPanel interfaces;
   
   /**
    * Panel containing control buttons for simulation operations.
    */
   JPanel controlPanel;
   
   /**
    * Panel containing all CPU-related components and registers.
    */
   JPanel CPU;
   
   //CPU related components
   /**
    * Label displaying the Program Counter (PC) register value.
    */
   JLabel PC;
   
   /**
    * Label displaying the Instruction Register (IR) value.
    */
   JLabel IR;
   
   /**
    * Label displaying the Memory Address Register (MAR) value.
    */
   JLabel MAR;
   
   /**
    * Label displaying the Memory Data Register (MDR) value.
    */
   JLabel MDR;
   
   /**
    * Label displaying the Pointer register value.
    */
   JLabel Pointer;
   
   /**
    * Label displaying the Accumulator (Acc) register value.
    */
   JLabel Acc;
   
   /**
    * Label displaying the Register B (RegB) value.
    */
   JLabel RegB;
   
   /**
    * Label displaying the current ALU operation state.
    */
   JLabel ALU;   //Current ALU state
   
   /**
    * Label displaying the Program Status Word containing CPU flags as a bitmask value.
    */
   JLabel PSW;   //Program status word //Contains the various flags as a bitmask value
   
   /**
    * Label displaying the current instruction being executed.
    */
   JLabel instruction;
   
   /**
    * Label displaying the current execution phase.
    */
   JLabel phase;
   
   /**
    * Label displaying the next execution phase.
    */
   JLabel nextPhase;
   
   /**
    * Label displaying the total number of execution cycles.
    */
   JLabel cycle;
   
   /**
    * Label displaying the remaining execution cycles.
    */
   JLabel remaining;
   
   /**
    * Label displaying the input buffer value.
    */
   JLabel bufIn;
   
   /**
    * Label displaying the output buffer value.
    */
   JLabel bufOut;
   
   /**
    * Label displaying the current display output value.
    */
   JLabel display;
   
   //Control panel related components
   /**
    * Button for loading program files into the simulator.
    */
   JButton load;
   
   //MC related components
   /**
    * List of labels representing memory data values, indexed by memory address.
    * Each label corresponds to a memory location in the central memory.
    */
   ArrayList<JLabel> MCData = new ArrayList<>(Constants.getMCSize());
   
   /**
    * Private constructor implementing the Singleton pattern.
    * Initializes the main window, creates all GUI components, and sets up the layout.
    * The window is configured with proper title, close operation, and visibility.
    */
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
   
   /**
    * Creates a border suitable for display components with subtle styling.
    *
    * @return a compound border with line border and padding
    */
   private Border displayBorder() {
      Border b = BorderFactory.createLineBorder(new Color(0.15f, 0.15f,0.15f));
      return BorderFactory.createCompoundBorder(b, new EmptyBorder(3, 1, 2, 5));
   }
   
   /**
    * Creates a display box with the default display border.
    *
    * @return a new JLabel configured as a display box
    */
   private JLabel createDisplayBox(){
      return createDisplayBox(displayBorder());
   }
   
   /**
    * Helper method to set GridBagConstraints values in a single call.
    *
    * @param gbc the GridBagConstraints object to modify
    * @param x the gridx value
    * @param y the gridy value
    * @param width the gridwidth value
    * @param height the gridheight value
    * @return the modified GridBagConstraints object for method chaining
    */
   private GridBagConstraints set(GridBagConstraints gbc, int x, int y, int width, int height){
      gbc.gridx = x;
      gbc.gridy = y;
      gbc.gridwidth = width;
      gbc.gridheight = height;
      return gbc;
   }
   
   /**
    * Adds a graphical Line component to the specified container at the given grid position.
    *
    * @param gbc the grid bag constraints specifying the position
    * @param container the container to add the line to
    */
   private void addLine(GridBagConstraints gbc, Container container){
      container.add(new Line(), gbc);
   }
   
   /**
    * Creates a display box (JLabel) with the specified border.
    * The label is configured with white background and opaque setting.
    *
    * @param b the border to apply to the display box
    * @return a configured JLabel suitable for displaying values
    */
   private JLabel createDisplayBox(Border b) {
      JLabel l = new JLabel();
      l.setBackground(Color.white);
      l.setOpaque(true);
      l.setBorder(b);
      return l;
   }
   
   /**
    * Creates and arranges all main GUI components using GridBagLayout.
    * This method sets up the overall structure of the simulator interface,
    * including CPU, memory, control panels, and I/O interfaces.
    */
   //Create the GUI components
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
      
      busLabels = new JPanel(new GridLayout(8,1));
      gridPosition.gridx = 5;      //start column
      gridPosition.gridy = 1;      //start row
      gridPosition.gridheight = 1; //row span
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
      gridPosition.gridwidth = 1;  //column span
      gridPosition.ipadx = 1;      //padding (x-axis)
      gridPosition.ipady = 3;      //padding (y-axis)
      gridPosition.weighty = 0.6;  //distribution of extra space (y-axis)
      createControlPanelComponents();
      main.add(controlPanel, gridPosition);
      
      interfaces = new JPanel(new GridBagLayout());
      gridPosition.gridx = 2;      //start column
      gridPosition.gridy = 5;      //start row
      gridPosition.gridheight = 3; //row span
      gridPosition.gridwidth = 3;  //column span
      gridPosition.ipadx = 2;      //padding (x-axis)
      gridPosition.ipady = 3;      //padding (y-axis)
      gridPosition.weighty = 0.6;  //distribution of extra space (y-axis)
      createInterfaceComponents();
      main.add(interfaces, gridPosition);
   }
   
   /**
    * Creates all CPU-related GUI components including registers, ALU, and control unit.
    * This method arranges the CPU components in a complex grid layout that visually
    * represents the internal structure of the simulated processor.
    *
    * @param CPU the container panel to add CPU components to
    */
   //Create the CPU GUI
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
         |   0  |  1  |  2  |  3  |  4  |  5  | 6...10 |
       0 |  PC  |     |     |     |     | MAR |        |
       1 |      | Pnt |     |     |     |     |        |
       2 |  IR  |     |     |     |     | MDR |        |
       3 |      |     |     |     |     |     |        |
       4 |      |-----------------------|     |--------|
       5 |  Acc |                       |     |        |
       6 |      |                       |     |        |
       7 |      |         ALU           |     |   CU   |
       8 | RegB |                       |     |        |
       9 |      |                       | PSW |        |
      10 |      |-----------------------|     |--------|
       */
      
      set(gridPosition,0,0,1,1);
      CPU.add(new JLabel("PC"), gridPosition);
      gridPosition.gridy = 1;
      CPU.add((PC = createDisplayBox()), gridPosition);
      
      set(gridPosition, 7,0,1,1);
      CPU.add(new JLabel("MAR"), gridPosition);
      gridPosition.gridy = 1;
      CPU.add((MAR = createDisplayBox()), gridPosition);
      
      
      //This is here to make the 6th column be filled with the line, not the MAR/MDR textboxes
      CPU.add(new JLabel(), set(gridPosition, 6,2,1,1));
      
      CPU.add(new JLabel(), set(gridPosition, 3, 1, 1, 1));
      
      set(gridPosition,2,2,1,1);
      CPU.add(new JLabel("Pointer"), gridPosition);
      gridPosition.gridy = 3;
      CPU.add((Pointer = createDisplayBox()), gridPosition);
      Pointer.setMinimumSize(new Dimension(15, 8));
      
      CPU.add(new JLabel(), set(gridPosition, 3, 3, 1, 1));
      
      set(gridPosition,0,4,1,1);
      CPU.add(new JLabel("IR"), gridPosition);
      gridPosition.gridy = 5;
      CPU.add((IR = createDisplayBox()), gridPosition);
      
      set(gridPosition,7,4,1,1);
      CPU.add(new JLabel("MDR"), gridPosition);
      gridPosition.gridy = 5;
      CPU.add((MDR = createDisplayBox()), gridPosition);
      
      gridPosition.weightx = 0.5;
      gridPosition.weighty = 0.5;
      addLine(set(gridPosition,1,1,6,1), CPU);
      addLine(set(gridPosition,1,5,6,1), CPU);
      addLine(set(gridPosition, 8, 1, 2,1), CPU);
      addLine(set(gridPosition, 8, 5,1,1), CPU);
      gridPosition.weightx = 0.1;
      gridPosition.weighty = 0.1;
      
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
      
      JPanel ALUB = new JPanel(new GridLayout(4,2));
      //Put the component in the penultimate row and last column
      for(int i = 0; i < 3*2 -1; i++) ALUB.add(new JLabel());
      
      ALUB.setBorder(titleBorder("ALU"));
      ALUB.setMinimumSize(new Dimension(200, 200));
      ALU = createDisplayBox(titleBorder("Op"));
      ALU.setMinimumSize(new Dimension(60,5));
      ALU.setPreferredSize(new Dimension(45,25));
      ALUB.add(ALU);
      
      CPU.add(ALUB, set(gridPosition, 2,7,3,7));
      
      CPU.add(new JLabel(), set(gridPosition, 5,10,1,1));
      
      CPU.add(new JLabel("PSW"), set(gridPosition, 5,11,1,1));
      gridPosition.gridy = 12;
      CPU.add((PSW = createDisplayBox()), gridPosition);
      
      CPU.add(createCUComponents(), set(gridPosition,6,8,4,6));
      
      updateALU();
   }
   
   /**
    * Creates the Control Unit components including instruction display,
    * phase information, and cycle counters.
    *
    * @return a JPanel containing all control unit display components
    */
   //Control unit components
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
   
   /**
    * Creates bus indicator labels showing address and data bus directions.
    * These labels provide visual indication of bus activity in the simulation.
    */
   //Literally 2 labels
   private void createBusComponents() {
      busLabels.add(new JLabel());
      busLabels.add(new JLabel());
      busLabels.add(new JLabel());
      busLabels.add(new JLabel());
      busLabels.add(new JLabel("Address bus ->"));
      busLabels.add(new JLabel());
      busLabels.add(new JLabel("<- Data bus ->"));
      busLabels.add(new JLabel());
   }
   
   /**
    * Creates the Central Memory display showing memory addresses and their values.
    * The memory is displayed in a scrollable format with address->value pairs.
    */
   //Create the Central Memory GUI
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
   
   /**
    * Creates control panel buttons for simulation operations.
    * Includes step execution, file loading, and update buttons.
    */
   //Create the buttons in the bottom left GUI
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
      update.setVisible(false);
   }
   
   /**
    * Creates I/O interface components including buffers, display, and numeric keypad.
    * The keypad allows users to input values into the buffer for program interaction.
    */
   private void createInterfaceComponents() {
      //create numpad
      JPanel numpad = new JPanel(new GridLayout(4, 3));
      JButton key;//Tmp variable to store numpad keys
      for(int i = 2; i >= 0; i--){
         for(int j = 1; j <= 3; j++) {
            int num = i*3+j;
            key = new JButton(String.valueOf(num));
            key.addActionListener(e -> {
               Registers.setBufIn(new SingleValue(num, false));
               updateBuf();
            });
            numpad.add(key);
         }
      }
      key = new JButton("0");
      key.addActionListener(e ->{
         Registers.setBufIn(new SingleValue(0, false));
         updateBuf();
      });
      numpad.add(new JLabel());
      numpad.add(key);
      numpad.setBorder(titleBorder("Numpad"));
      
      JPanel bufInterfaces = new JPanel();
      bufInterfaces.setBorder(titleBorder("I/O Interfaces"));
      
      bufInterfaces.add(new JLabel("Buffer out"));
      bufInterfaces.add((bufOut = createDisplayBox()));
      
      bufInterfaces.add(new JLabel());
      
      bufInterfaces.add(new JLabel("Buffer in"));
      bufInterfaces.add((bufIn = createDisplayBox()));
      
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.weightx = 0.3;
      gbc.weighty = 0.3;
      gbc.fill = GridBagConstraints.BOTH;
      
      interfaces.add(bufInterfaces, set(gbc, 0,0,5,1));
      
      JPanel displayPane = new JPanel(new GridLayout(2,1));
      displayPane.setBorder(displayBorder());
      
      displayPane.add(new JLabel("Display"));
      displayPane.add((display = createDisplayBox()));
      display.setText("      ");
      
      gbc.weightx = 0.1;
      gbc.weighty = 0.1;
      interfaces.add(displayPane, set(gbc, 0,1,1,1));
      
      gbc.weightx = 0.2;
      gbc.weighty = 0.2;
      interfaces.add(new JLabel(), set(gbc, 0,2,1,1));
      interfaces.add(new JLabel(), set(gbc, 0,3,1,1));
      interfaces.add(new JLabel(), set(gbc, 1,1,1,1));
      
      interfaces.add(numpad, set(gbc, 3,1,1,3));
      updateBuf();
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
   
   /**
    * Internal implementation of GUI updates based on modification flags.
    * This method selectively updates different sections of the GUI based on
    * which components have been modified since the last update.
    */
   private void updateImpl() {
      if(Execution.stepped) instance.updateCU();
      if((Registers.modFlag & 1) == 1) instance.updateMC();
      if((Registers.modFlag & 2) == 2) instance.updatePR();
      if((Registers.modFlag & 4) == 4) instance.updateALU();
      if((Registers.modFlag & 8) == 8) instance.updateBuf();
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
      ALU.setText(ControlUnit.ALUOpcode);
      PSW.setText(Flags.get());
   }
   
   /**
    * Function to update the visual part of the Control Unit
    */
   private void updateBuf() {
      bufIn.setText(Registers.getBufIn().toString());
      bufOut.setText(Registers.getBufOut().toString());
      display.setText(Registers.getBufOut().toString());
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
      swingThread.setUncaughtExceptionHandler(new ExceptionHandler(true));
   }
}
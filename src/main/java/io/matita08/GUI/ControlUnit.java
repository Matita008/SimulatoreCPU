package io.matita08.GUI;

import io.matita08.logic.*;

public class ControlUnit {
   public static Operation opcode = Operation.Unknown;
   public static Phase current = Phase.None;
   public static Phase next = Phase.Fetch;
   public static int currentCycle = -1;
   public static int totalCycles = -1;
}

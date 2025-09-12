package io.matita08.logic;

public enum Phase{
   Fetch(Execution::fetch),
   Decode(Execution::decode),
   Execute(Execution::execute),
   None(()->{});
   
   private final Runnable action;
   
   Phase(Runnable action) {
      this.action = action;
   }
   
   public void run() {action.run();}
}

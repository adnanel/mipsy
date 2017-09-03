package mipsy.types;

/**
 * Created by prg01 on 7/6/2017.
 */
public class CycleAction {
    public int cycle;
    public String action;
    public Instruction processedInstruction;


    public CycleAction(int cycle, String action, Instruction processedInstruction) {
        this.cycle = cycle;
        this.action = action;
        this.processedInstruction = processedInstruction;
    }

    public CycleAction(int cycle) {
        this.cycle = cycle;
        this.action = "-";
        this.processedInstruction = null;
    }
}

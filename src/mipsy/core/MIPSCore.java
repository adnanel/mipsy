package mipsy.core;

import mipsy.core.components.ControlComponent;
import mipsy.core.dataphases.*;
import mipsy.types.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * Created on 3/30/2017.
 */
public class MIPSCore {
    public ControlComponent controlComponent = new ControlComponent();

    public IF IF = new IF(this);
    public ID ID = new ID(this);
    public EX EX = new EX(this);
    public MEM MEM = new MEM(this);
    public WB WB = new WB(this);

    public EXMEM EXMEM = new EXMEM();
    public IFID IFID = new IFID();
    public IDEX IDEX = new IDEX();
    public MEMWB MEMWB = new MEMWB();

    private int cycleCounter = 0;

    public HashMap<String, Register> registers = new HashMap<>();

    //Because we can't allocate 2^30 integers, we're using a hashmap and will only allocate
    //those ints that are actually used in our program.
    public HashMap<Integer, MemoryEntry> memory = new HashMap<>();

    public List<Instruction> instructions = new ArrayList<>();


    public ArrayList<ArrayList<CycleAction>> cycleActions = new ArrayList<>();

    public MIPSCore(HashMap<String, Register> registers, HashMap<Integer, MemoryEntry> memory, List<Instruction> instructions) {
        this.registers = registers;
        this.memory = memory;
        this.instructions = instructions;
    }



    public MIPSCore(List<Register> registers, HashMap<Integer, MemoryEntry> memory) {
        for ( Register reg : registers )
            this.registers.put( reg.name, reg );

        this.memory = memory;
    }

    public int getCycleCount() {
        return cycleCounter;
    }

    private static void stepPhase(DataPhase phase, int cycleCounter, Consumer<String> logger, List<CycleAction> actionLogger, String phaseName) throws NoMoreInstructionsException {
        if ( phase.step(logger) )
            actionLogger.add(new CycleAction(cycleCounter, phaseName));
        else {
            actionLogger.add(new CycleAction(cycleCounter));
        }
    }

    public void step(Consumer<String> logger, boolean justOne) throws NoMoreInstructionsException {
        if ( WB.isHalt ) throw new NoMoreInstructionsException();


        logger.accept("---BEGIN---");
        while ( !WB.isHalt ) {
            logger.accept(String.format("--CYCLE %d BEGIN--", ++cycleCounter));
            ArrayList<CycleAction> actions = new ArrayList<>(5);
            cycleActions.add(actions);

            stepPhase(IF, cycleCounter, logger, actions, "IF");
            stepPhase(ID, cycleCounter, logger, actions, "ID");
            stepPhase(EX, cycleCounter, logger, actions, "EX");
            stepPhase(MEM, cycleCounter, logger, actions, "MEM");
            stepPhase(WB, cycleCounter, logger, actions, "WB");


            IF.writeResults(logger);
            ID.writeResults(logger);
            EX.writeResults(logger);
            MEM.writeResults(logger);
            WB.writeResults(logger);
            logger.accept("--CYCLE END--");

            if ( justOne ) break;
        }

        logger.accept("---END---");
    }
}


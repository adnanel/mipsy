package mipsy.core;

import mipsy.core.components.ControlComponent;
import mipsy.core.dataphases.*;
import mipsy.types.Instruction;
import mipsy.types.MemoryEntry;
import mipsy.types.Register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Adnan on 3/30/2017.
 */
public class MIPSCore {
    public ControlComponent controlComponent = new ControlComponent();

    public DataPhase[] dataPhases = new DataPhase[] {
            new IF(this),
            new ID(this),
            new EX(this),
            new MEM(this),
            new WB(this)
    };



    public HashMap<String, Register> registers = new HashMap<>();

    //Because we can't allocate 2^30 integers, we're using a hashmap and will only allocate
    //those ints that are actually used in our program.
    public HashMap<Integer, MemoryEntry> memory = new HashMap<>();

    public List<Instruction> instructions = new ArrayList<>();

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


    public void step(Consumer<String> logger) {
        ((WB)dataPhases[dataPhases.length - 1]).ifPhase = (IF)dataPhases[0];

        logger.accept("----STEP BEGIN----");
        DataPhase prevPhase = null;
        for ( DataPhase phase : dataPhases ) {
            phase.receiveData(prevPhase);
            phase.step(logger);
            prevPhase = phase;
        }
        logger.accept("----STEP END----");
    }
}

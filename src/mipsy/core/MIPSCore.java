package mipsy.core;

import mipsy.core.components.ControlComponent;
import mipsy.core.dataphases.*;
import mipsy.types.Instruction;
import mipsy.types.MemoryEntry;
import mipsy.types.NoMoreInstructionsException;
import mipsy.types.Register;

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

    public void reset() {
        IF.reset();
    }

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


    public void step(Consumer<String> logger) throws NoMoreInstructionsException {
        logger.accept("---BEGIN---");

        IF.step(logger);

        ID.step(logger);

        EX.step(logger);

        MEM.step(logger);

        WB.step(logger);

        logger.accept("---END---");
    }
}

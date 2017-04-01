package mipsy.core.dataphases;

import mipsy.core.MIPSCore;
import mipsy.core.components.ALUComponent;
import mipsy.instructions.InstructionAdd;
import mipsy.types.Instruction;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Adnan on 3/31/2017.
 */
public class IF extends DataPhase {
    public int IF_OUT0;
    public int IF_OUT1;

    // IF se sastoji od: http://prntscr.com/eqrei3
    private ALUComponent alu1 = new ALUComponent("ALU1");
    public int pc = 0;
    // instruction memory uzimamo iz MIPSCore

    public IF(MIPSCore core) {
        super(core);

        alu1.setOpB(4);
        alu1.setControl(ALUComponent.CONTROL_ADD);
    }

    @Override
    public void step(Consumer<String> logger) {
        logger.accept("IF: Sending PC into ALU1(OP1) and InstructionMemory(Address)");
        Instruction currInstruction;

        if ( core.instructions.size() <= pc ) {
            logger.accept("IF: WARNING - No instruction at address " + pc + "! Using null instruction...");
            currInstruction = null; //todo, kada napravim null instrukciju dodaj je ovdje
        } else
            currInstruction = core.instructions.get(pc);

        logger.accept("IF: Current instruction is \"" + currInstruction.toString() + "\", coded as " + currInstruction.getCoded());

        alu1.setOpA(pc);

        logger.accept("IF: ALU1 performs ADD operation, operands are const 4 and PC");
        alu1.execute(logger);

        logger.accept("IF: Sending ALU1 output into IF_OUT0");
        IF_OUT0 = alu1.getResult();

        logger.accept("IF: Sending InstructionMemory output to IF_OUT1");
        IF_OUT1 = currInstruction.getCoded();
    }
}

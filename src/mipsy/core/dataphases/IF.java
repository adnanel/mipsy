package mipsy.core.dataphases;

import mipsy.core.MIPSCore;
import mipsy.core.components.ALUComponent;
import mipsy.types.Instruction;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created on 3/31/2017.
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
        logger.accept(String.format("IF: Sending PC(%s) into ALU1(OP1) and InstructionMemory(Address)", "0x" + Integer.toHexString(pc)));
        Instruction currInstruction;

        if ( core.instructions.size() <= pc / 4 ) {
            logger.accept("IF: WARNING - No instruction at address " + pc + "! Using null instruction...");
            currInstruction = null; //todo, kada napravim null instrukciju dodaj je ovdje
        } else
            currInstruction = core.instructions.get(pc / 4);

        logger.accept("IF: Current instruction is \"" + currInstruction.toString() + "\", coded as 0x" + Integer.toHexString(currInstruction.getCoded()));

        alu1.setOpA(pc);

        logger.accept("IF: ALU1 performs ADD operation, operands are const 4 and PC");
        alu1.execute(logger);


        IF_OUT0 = alu1.getResult();
        logger.accept(String.format("IF: Sending ALU1 output (%s) into IF_OUT0", "0x" + Integer.toHexString(IF_OUT0)));

        IF_OUT1 = currInstruction.getCoded();
        logger.accept(String.format("IF: Sending InstructionMemory output (%s) to IF_OUT1", "0x" + Integer.toHexString(IF_OUT1)));


        logger.accept(String.format("IF: Sending InstructionMemory output (%s) to Control", "0x" + Integer.toHexString(currInstruction.getCoded())));
        core.controlComponent.setCurrInstruction(currInstruction);

    }
}

package mipsy.core.dataphases;

import mipsy.core.MIPSCore;
import mipsy.core.components.ALUComponent;
import mipsy.core.components.MUXComponent;
import mipsy.instructions.InstructionHalt;
import mipsy.types.Instruction;
import mipsy.types.NoMoreInstructionsException;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by adnan on 05.04.2017..
 */
public class IF extends DataPhase {
    private ALUComponent alu1 = new ALUComponent("ALU1");
    private MUXComponent mux1 = new MUXComponent("MUX1");
    private int pc = 0;
    List<Instruction> instructions;
    private Instruction currInstruction;

    //povratne sprege
    public int PCSrc = 0;
    public int EX_MEM_OUT0 = 0;
    public int ALU1_RES = 0;

    public void reset() {
        pc = 0;
    }

    public IF(MIPSCore core) {
        super(core);
    }

    @Override
    public void step(Consumer<String> logger) throws NoMoreInstructionsException {
        instructions = core.instructions;
        logger.accept("IF: Starting IF cycle");

        mux1.setSelector(PCSrc);
        mux1.setA(ALU1_RES);
        mux1.setB(EX_MEM_OUT0);

        pc = mux1.getResult(logger);
        logger.accept(String.format("IF: Current PC is 0x%s", Integer.toHexString(pc)));

        alu1.setControl(logger, ALUComponent.CONTROL_ADD);
        alu1.setOpB(logger, 4);
        alu1.setOpA(logger, pc);

        ALU1_RES = alu1.getResult(logger);


        Instruction currInstruction;
        if ( instructions.size() > pc / 4 )
            currInstruction = instructions.get(pc / 4);
        else {
            logger.accept("IF: No instructoin found at current PC address!");
            throw new NoMoreInstructionsException();
        }

    }

    @Override
    public void writeResults(Consumer<String> logger) {
        if ( currInstruction == null ) return;

        logger.accept(String.format("IF: Fetched instruction \"%s\" coded as 0x%s", currInstruction.toString(), Integer.toHexString(currInstruction.getCoded())));
        core.IFID.OUT0 = ALU1_RES;
        core.IFID.OUT1 = currInstruction;
        core.IFID.isHalt = currInstruction instanceof InstructionHalt;
    }
}

package mipsy.core.dataphases;

import mipsy.Utility;
import mipsy.core.MIPSCore;
import mipsy.core.components.ALUComponent;
import mipsy.core.components.MUXComponent;
import mipsy.instructions.InstructionBeq;
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
    private MUXComponent mux2 = new MUXComponent("MUX2");

    private int pc = 0;
    List<Instruction> instructions;
    private Instruction currInstruction;

    //povratne sprege
    public int PCSrc = 0;
    public int Jump = 0;
    public int EX_MEM_OUT0 = 0;
    public int EX_MEM_OUT5 = 0; //JumpAddr
    public int ALU1_RES = 0;

    public boolean isStalling = false;

    private int backupPC = 0;
    private boolean wasBranching = false;

    public void reset() {
        pc = 0;
        ALU1_RES = 0;
        EX_MEM_OUT0 = 0;
        PCSrc = 0;
    }

    public IF(MIPSCore core) {
        super(core);
    }

    @Override
    public PhaseResult step(Consumer<String> logger) throws NoMoreInstructionsException {
        logger = Utility.appendToLogger("IF - ", logger);

        if ( core.IFID.isHalt ) {
            logger.accept("IS HALTING");
            return PhaseResult.NO_ACTION;
        }

        if ( isStalling ) {
            logger.accept("STALLING");

            if ( wasBranching && pc != backupPC ) {
                wasBranching = false;
                isStalling = false;
            } else
                return PhaseResult.NO_ACTION;
        } else if ( core.IDEX.Branch == 1 || (core.IFID.OUT1 != null && core.IFID.OUT1.canBranch()) ) {
            logger.accept("Current instruction is a branching instruction, stalling!");
            isStalling = true;
            wasBranching = true;
            backupPC = pc;
            currInstruction = null;

            return PhaseResult.NO_ACTION;
        }

        instructions = core.instructions;
        logger.accept("START");

        mux1.setSelector(PCSrc);
        mux1.setA(ALU1_RES, logger);
        mux1.setB(EX_MEM_OUT0, logger);

        mux2.setSelector(Jump);
        mux2.setA(mux1.getResult(logger), logger);
        mux2.setB(EX_MEM_OUT5, logger);


        pc = mux2.getResult(logger);
        logger.accept(String.format("Current PC is 0x%s", Integer.toHexString(pc)));

        alu1.setControl(logger, ALUComponent.CONTROL_ADD);
        alu1.setOpB(logger, 4);
        alu1.setOpA(logger, pc);

        ALU1_RES = alu1.getResult(logger);


        if ( instructions.size() > pc / 4 )
            currInstruction = instructions.get(pc / 4);
        else {
            logger.accept("No instruction found at current PC address!");
            currInstruction = new InstructionHalt(null);
            instructions.add(currInstruction);
        }

        logger.accept("END");
        if ( currInstruction instanceof InstructionHalt ) return PhaseResult.NO_ACTION;
        return new PhaseResult(currInstruction);
    }

    @Override
    public void writeResults(Consumer<String> logger) {
        logger = Utility.appendToLogger("IF - ", logger);

        if (core.IFID.OUT1 != null && core.IFID.OUT1.getClass() == InstructionBeq.class) {
            core.IFID.OUT1 = null;
        }
        if ( currInstruction == null ) return;
        if ( isStalling ) return;

        logger.accept(String.format("Fetched instruction \"%s\" coded as 0x%s", currInstruction.toString(), Integer.toHexString(currInstruction.getCoded())));
        core.IFID.OUT0 = ALU1_RES;
        core.IFID.OUT1 = currInstruction;
        core.IFID.isHalt = currInstruction instanceof InstructionHalt;
    }
}

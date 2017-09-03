package mipsy.core.dataphases;

import mipsy.Utility;
import mipsy.core.MIPSCore;
import mipsy.core.components.ALUComponent;
import mipsy.core.components.ALUControllerComponent;
import mipsy.core.components.MUXComponent;
import mipsy.types.Instruction;
import mipsy.types.NoMoreInstructionsException;
import mipsy.types.Register;

import java.util.function.Consumer;

/**
 * Created by adnan on 05.04.2017..
 */
public class EX extends DataPhase {
    private ALUComponent alu2 = new ALUComponent("ALU2");
    private ALUComponent alu3 = new ALUComponent("ALU3");

    private MUXComponent mux4 = new MUXComponent("MUX4");
    private MUXComponent mux3 = new MUXComponent("MUX3");

    private ALUControllerComponent aluControl = new ALUControllerComponent("ALU_C");

    public EX(MIPSCore core) {
        super(core, PhaseNames.EX);
    }

    private Register idexout2;
    private int Branch;
    private int MemRead;
    private int MemToReg;
    private int MemWrite;
    private int RegWrite;
    private int Jump;
    private boolean isHalt;

    private int idexout6;

    private Instruction currentInstruction;

    @Override
    public PhaseResult step(Consumer<String> logger) throws NoMoreInstructionsException {
        logger = Utility.appendToLogger("EX - ", logger);

        IDEX idex = core.IDEX;
        if ( idex.OUT1 == null || idex.currentInstruction == null ) {
            logger.accept("No work to do, skipping...");
            return PhaseResult.NO_ACTION;
        }

        isHalt = idex.isHalt;

        logger.accept("START");

        mux3.setA(idex.OUT4, logger);
        mux3.setB(idex.OUT5, logger);
        mux3.setSelector(idex.RegDst);

        mux4.setA(idex.OUT2.value, logger);
        mux4.setB(idex.OUT3, logger);
        mux4.setSelector(idex.AluSrc);

        currentInstruction = idex.currentInstruction;

        if ( idex.currentInstruction.getType() == Instruction.Type.RType )
            aluControl.setInstruction(Utility.SubBits(idex.OUT3, 0, 6));
        else
            aluControl.setInstruction(Utility.SubBits(idex.currentInstruction.getCoded(), 32 - 6, 32));

        aluControl.setAluOp(idex.AluOp);

        alu3.setControl(logger, aluControl.getResult(logger));
        alu3.setOpA(logger, idex.OUT1.value);
        alu3.setOpB(logger, mux4.getResult(logger));

        alu2.setOpB(logger, idex.OUT3 << 2);
        alu2.setOpA(logger, idex.OUT0);
        alu2.setControl(logger, ALUComponent.CONTROL_ADD);

        idexout2 = idex.OUT2;
        idexout6 = idex.OUT6;

        Branch = idex.Branch;
        MemRead = idex.MemRead;
        MemToReg = idex.MemToReg;
        MemWrite = idex.MemWrite;
        RegWrite = idex.RegWrite;
        Jump = idex.Jump;
        idex.Jump = 0; // "consume" this jump

        idex.clear();

        logger.accept("END");
        if ( core.IDEX.isHalt ) return PhaseResult.NO_ACTION;
        return new PhaseResult(currentInstruction);
    }

    @Override
    public void writeResults(Consumer<String> logger) {
        logger = Utility.appendToLogger("EX - ", logger);

        EXMEM exmem = core.EXMEM;

        exmem.OUT4 = mux3.getResult(logger);
        exmem.OUT3 = idexout2;
        exmem.OUT2 = alu3.getResult(logger);
        exmem.OUT1 = alu3.getZero();
        exmem.OUT0 = alu2.getResult(logger);
        exmem.Branch = Branch;
        exmem.MemRead = MemRead;
        exmem.MemToReg = MemToReg;
        exmem.MemWrite = MemWrite;
        exmem.RegWrite = RegWrite;
        exmem.Jump = Jump;
        exmem.OUT5 = idexout6;

        exmem.currentInstruction = currentInstruction;
        currentInstruction = null;
    }
}

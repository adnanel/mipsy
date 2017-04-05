package mipsy.core.dataphases;

import mipsy.Utility;
import mipsy.core.MIPSCore;
import mipsy.core.components.ALUComponent;
import mipsy.core.components.ALUControllerComponent;
import mipsy.core.components.MUXComponent;
import mipsy.types.NoMoreInstructionsException;

import java.util.function.Consumer;

/**
 * Created by adnan on 05.04.2017..
 */
public class EX extends DataPhase {
    private ALUComponent alu2 = new ALUComponent("ALU2");
    private ALUComponent alu3 = new ALUComponent("ALU3");

    private MUXComponent mux3 = new MUXComponent("MUX3");
    private MUXComponent mux2 = new MUXComponent("MUX2");

    private ALUControllerComponent aluControl = new ALUControllerComponent("ALU_C");

    public EX(MIPSCore core) {
        super(core);
    }

    @Override
    public void step(Consumer<String> logger) throws NoMoreInstructionsException {
        IDEX idex = core.IDEX;
        EXMEM exmem = core.EXMEM;

        logger.accept("EX: Starting EX cycle, data received from IDEX:");

        mux2.setA(idex.OUT4);
        mux2.setB(idex.OUT5);
        mux2.setSelector(idex.RegDst);

        mux3.setA(idex.OUT2.value);
        mux3.setB(idex.OUT3);
        mux3.setSelector(idex.AluSrc);

        aluControl.setInstruction(Utility.SubBits(idex.OUT3, 0, 6));
        aluControl.setAluOp(idex.AluOp);

        alu3.setControl(logger, aluControl.getResult(logger));
        alu3.setOpA(logger, idex.OUT1.value);
        alu3.setOpB(logger, mux3.getResult(logger));

        alu2.setOpB(logger, idex.OUT3 << 2);
        alu2.setOpA(logger, idex.OUT0);
        alu2.setControl(logger, ALUComponent.CONTROL_ADD);

        exmem.OUT4 = mux2.getResult(logger);
        exmem.OUT3 = idex.OUT2;
        exmem.OUT2 = alu3.getResult(logger);
        exmem.OUT1 = alu3.getZero();
        exmem.OUT0 = alu2.getResult(logger);
        exmem.Branch = idex.Branch;
        exmem.MemRead = idex.MemRead;
        exmem.MemToReg = idex.MemToReg;
        exmem.MemWrite = idex.MemWrite;
        exmem.RegWrite = idex.RegWrite;
    }
}

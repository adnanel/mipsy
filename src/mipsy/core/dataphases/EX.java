package mipsy.core.dataphases;

import mipsy.Utility;
import mipsy.core.MIPSCore;
import mipsy.core.components.ALUComponent;
import mipsy.core.components.ALUControllerComponent;
import mipsy.core.components.MUXComponent;

import java.util.function.Consumer;

/**
 * Created on 3/31/2017.
 */
public class EX extends DataPhase {
    /*
    EX_OUT0 - ID_OUT0
    EX_OUT1 - ALU2 result
    EX_OUT2 - ALU3 zero
    EX_OUT3 - ALU3 result
    EX_OUT4 - Read data 2 (ID_OUT3)
     */
    public int EX_OUT0;
    public int EX_OUT1;
    public int EX_OUT2;
    public int EX_OUT3;
    public int EX_OUT4;

    private MUXComponent mux2 = new MUXComponent("MUX2");
    private ALUComponent alu2 = new ALUComponent("ALU2");
    private ALUComponent alu3 = new ALUComponent("ALU3");
    private ALUControllerComponent aluControl = new ALUControllerComponent("ALU_C");

    public EX(MIPSCore core) {
        super(core);

        //ALU2 je fiksirano na ADD op
        alu2.setControl(ALUComponent.CONTROL_ADD);
    }

    @Override
    public void step(Consumer<String> logger ) {
        ID prev = (ID)prevPhase;

        logger.accept("EX: Sending ID_OUT0 to EX_OUT0");
        EX_OUT0 = prev.ID_OUT0;

        logger.accept("EX: Sending ID_OUT1 to ALU3(OP1)");
        alu3.setOpA(prev.ID_OUT1);

        logger.accept("EX: Sending ID_OUT4[5:0] to ALU_C");
        aluControl.setInstruction(Utility.SubBits(prev.ID_OUT4, 0, 6));

        logger.accept("EX: Sending ALUOp to ALU_C");
        aluControl.setAluOp(core.controlComponent.getAluOp());

        logger.accept("EX: Sending ALU_C output to ALU3 control");
        alu3.setControl(aluControl.getResult(logger));

        logger.accept("EX: Sending ALUSrc to MUX2");
        mux2.setSelector(core.controlComponent.getAluSrc());

        logger.accept("EX: Sending ID_OUT3 to MUX2");
        mux2.setA(prev.ID_OUT3);

        logger.accept("EX: Sending ID_OUT2 to MUX2");
        mux2.setB(prev.ID_OUT2);

        logger.accept("EX: Sending MUX2 output to ALU3(OP2)");
        alu3.setOpB( mux2.getResult(logger) );

        logger.accept(String.format("EX: Sending ID_OUT2 (%d) to LEFT_SHIFT ", prev.ID_OUT2));
        int lshift = prev.ID_OUT2 << 2;

        logger.accept(String.format("EX: Sending LEFT_SHIFT (%d) output to ALU2(OP2)", lshift));
        alu2.setOpB(lshift);

        logger.accept(String.format("EX: Sending ID_OUT0 (%d) to ALU2(OP1)", prev.ID_OUT0));
        alu2.setOpA(prev.ID_OUT0);

        alu2.execute(logger);
        logger.accept("EX: Sending ALU2 result EX_OUT1");
        EX_OUT1 = alu2.getResult();

        alu3.execute(logger);
        logger.accept("EX: Sending ALU3 zero output to EX_OUT2");
        EX_OUT2 = alu3.getZero();

        logger.accept("EX: Sending ALU3 output to EX_OUT3");
        EX_OUT3 = alu3.getResult();

        logger.accept("EX: Sending ID_OUT3 to EX_OUT4");
        EX_OUT4 = prev.ID_OUT3;
    }
}

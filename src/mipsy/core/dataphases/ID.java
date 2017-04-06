package mipsy.core.dataphases;

import mipsy.Utility;
import mipsy.core.MIPSCore;
import mipsy.core.components.ControlComponent;
import mipsy.core.components.RegistersComponent;
import mipsy.core.components.SignExtendComponent;
import mipsy.types.NoMoreInstructionsException;
import mipsy.types.Register;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Created by adnan on 05.04.2017..
 */
public class ID extends DataPhase {
    // http://prntscr.com/esxho2

    RegistersComponent registersComponent = new RegistersComponent();
    private ControlComponent control = new ControlComponent();


    private Register reg1;
    private Register reg2;
    private int currInst;

    public ID(MIPSCore core) {
        super(core);

    }

    @Override
    public void step(Consumer<String> logger) throws NoMoreInstructionsException {
        if ( core.IFID.OUT1 == null ) return;

        currInst = core.IFID.OUT1.getCoded();

        int readReg1 = Utility.SubBits(currInst, 21, 26);
        int readReg2 = Utility.SubBits(currInst, 16, 21);


        registersComponent.setRegisters(core.registers);
        registersComponent.setReadRegister1(readReg1);
        registersComponent.setReadRegister2(readReg2);

        reg1 = registersComponent.getReadData1(logger);
        reg2 = registersComponent.getReadData2(logger);

        control.setCurrInstruction(core.IFID.OUT1);

    }

    @Override
    public void writeResults(Consumer<String> logger) {
        core.IDEX.AluOp = control.getAluOp();
        core.IDEX.AluSrc = control.getAluSrc();
        core.IDEX.MemRead = control.getMemRead();
        core.IDEX.MemToReg = control.getMemToReg();
        core.IDEX.MemWrite = control.getMemWrite();
        core.IDEX.Branch = control.getBranch();
        core.IDEX.RegDst = control.getRegDst();
        core.IDEX.RegWrite = control.getRegWrite();

        core.IDEX.OUT0 = core.IFID.OUT0;
        core.IDEX.OUT1 = reg1;
        core.IDEX.OUT2 = reg2;

        core.IDEX.OUT3 = SignExtendComponent.extend(Utility.SubBits(currInst, 0, 16));
        core.IDEX.OUT4 = Utility.SubBits(currInst,16, 21);
        core.IDEX.OUT5 = Utility.SubBits(currInst,11,16);

        core.IDEX.isHalt = currInst == 0;
    }
}

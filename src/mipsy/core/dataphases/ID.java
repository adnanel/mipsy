package mipsy.core.dataphases;

import mipsy.Utility;
import mipsy.core.MIPSCore;
import mipsy.core.components.ControlComponent;
import mipsy.core.components.RegistersComponent;
import mipsy.core.components.SignExtendComponent;
import mipsy.types.Instruction;
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

    private Instruction currInstruction = null;

    public ID(MIPSCore core) {
        super(core);

    }

    @Override
    public void step(Consumer<String> logger) throws NoMoreInstructionsException {
        logger = Utility.appendToLogger("ID - ", logger);

        if ( core.IFID.OUT1 == null && currInstruction == null ) {
            logger.accept("No work to do, skipping...");
            return;
        }

        logger.accept("START");


        if ( core.IFID.OUT1 != null )
            currInstruction = core.IFID.OUT1;
        currInst = currInstruction.getCoded();

        int readReg1 = Utility.SubBits(currInst, 21, 26);
        int readReg2 = Utility.SubBits(currInst, 16, 21);

        //provjera hazarda
        boolean isStalling = false;
        if ( control.getRegWrite() == 1 ) {
            if ( readReg1 != 0 &&
                    (readReg1 == core.IDEX.OUT5 || core.IDEX.OUT4 == readReg1) ) {
                logger.accept(String.format("Current instruction is accessing register 0x%s, stalling!", Integer.toHexString(readReg1)));
                isStalling = true;
            }

            if ( readReg2 != 0 &&
                    (readReg2 == core.IDEX.OUT5 || core.IDEX.OUT4 == readReg2) ) {
                logger.accept(String.format("Current instruction is accessing register 0x%s, stalling!", Integer.toHexString(readReg2)));
                isStalling = true;
            }
        }

        if (core.EXMEM.RegWrite == 1) {
            if (core.EXMEM.OUT4 == readReg1 || core.EXMEM.OUT4 == readReg2) {
                if (core.EXMEM.OUT4 != 0) {
                    logger.accept(String.format("EXMEM.OUT4 is 0x%s, which is a register I need, stalling!", Integer.toHexString(core.EXMEM.OUT4)));
                    isStalling = true;
                }
            }
        }

        if (core.MEMWB.RegWrite == 1) {
            if (core.MEMWB.OUT2 == readReg1 || core.MEMWB.OUT2 == readReg2) {
                if (core.MEMWB.OUT2 != 0) {
                    logger.accept(String.format("MEMWB.OUT2 is 0x%s, which is a register I need, stalling!", Integer.toHexString(core.MEMWB.OUT2)));
                    isStalling = true;
                }
            }
        }




        if ( isStalling ) {
            //logger.accept("Current instruction is writing to at least 1 register the new instruction needs to write to, stalling!");
            core.IF.isStalling = true;

            logger.accept("END");
            return;
        } else core.IF.isStalling = false;




        registersComponent.setRegisters(core.registers);
        registersComponent.setReadRegister1(readReg1);
        registersComponent.setReadRegister2(readReg2);

        reg1 = registersComponent.getReadData1(logger);
        reg2 = registersComponent.getReadData2(logger);

        control.setCurrInstruction(currInstruction);
        core.IFID.OUT1 = null;

        logger.accept("END");
    }

    @Override
    public void writeResults(Consumer<String> logger) {
        logger = Utility.appendToLogger("ID - ", logger);

        if ( core.IF.isStalling ) {
            control.reset();
            return;
        }

        core.IDEX.AluOp = control.getAluOp();
        core.IDEX.AluSrc = control.getAluSrc();
        core.IDEX.MemRead = control.getMemRead();
        core.IDEX.MemToReg = control.getMemToReg();
        core.IDEX.MemWrite = control.getMemWrite();
        core.IDEX.Branch = control.getBranch();
        core.IDEX.RegDst = control.getRegDst();
        core.IDEX.RegWrite = control.getRegWrite();
        control.reset();

        core.IDEX.OUT0 = core.IFID.OUT0;
        core.IDEX.OUT1 = reg1;
        core.IDEX.OUT2 = reg2;

        core.IDEX.OUT3 = SignExtendComponent.extend(Utility.SubBits(currInst, 0, 16));
        core.IDEX.OUT4 = Utility.SubBits(currInst,16, 21);
        core.IDEX.OUT5 = Utility.SubBits(currInst,11,16);

        core.IDEX.isHalt = currInst == 0;
        core.IDEX.currentInstruction = currInstruction;

        currInst = 0;
        currInstruction = null;
    }
}

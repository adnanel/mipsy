package mipsy.core.dataphases;

import mipsy.Utility;
import mipsy.core.MIPSCore;
import mipsy.core.components.MUXComponent;
import mipsy.core.components.RegistersComponent;
import mipsy.core.components.SignExtendComponent;

import java.util.function.Consumer;

/**
 * Created on 3/31/2017.
 */
public class ID extends DataPhase {

    //http://prntscr.com/er45i5
    /*
    ID_OUT0 - IF_OUT0
    ID_OUT1 - read data 1
    ID_OUT2 - SignExtend output
    ID_OUT3 - read data 2
    ID_OUT4 - IF_OUT1[15:0]
     */
    public int ID_OUT0;
    public int ID_OUT1;
    public int ID_OUT2;
    public int ID_OUT3;
    public int ID_OUT4;

    public RegistersComponent registersComponent = new RegistersComponent();
    private MUXComponent mux1 = new MUXComponent("MUX1");

    public ID(MIPSCore core) {
        super(core);
    }

    @Override
    public void step(Consumer<String> logger) {

        registersComponent.setRegisters( core.registers );

        IF prev = (IF)prevPhase;

        logger.accept(String.format("ID: Sending IF_OUT0 (%s) to ID_OUT0", "0x" + Integer.toHexString(prev.IF_OUT0)));
        ID_OUT0 = prev.IF_OUT0;

        logger.accept(String.format("ID: Sending IF_OUT1[25:21] (%s) to ReadReg1", "0x" + Integer.toHexString(Utility.SubBits(prev.IF_OUT0, 21, 26))));
        registersComponent.setReadRegister1( Utility.SubBits(prev.IF_OUT1, 21, 26) );

        logger.accept(String.format("ID: Sending IF_OUT1[20:16] (%s) to ReadReg2 and MUX1, input 0", "0x" + Integer.toHexString(Utility.SubBits(prev.IF_OUT1, 16, 21))));
        registersComponent.setReadRegister2( Utility.SubBits(prev.IF_OUT1, 16, 21));
        mux1.setA(Utility.SubBits(prev.IF_OUT1, 16, 21));

        int readData1 = registersComponent.getReadData1(logger).value;
        logger.accept(String.format("ID: Sending read data 1 (%s) to ID_OUT1", "0x" + Integer.toHexString(readData1)));
        ID_OUT1 = readData1;

        int readData2 = registersComponent.getReadData2(logger).value;
        logger.accept(String.format("ID: Sending read data 2 (%s) to ID_OUT3", "0x" + Integer.toHexString(readData2)));
        ID_OUT3 = readData2;

        int n = Utility.SubBits(prev.IF_OUT1, 11, 16);
        logger.accept(String.format("ID: Sending IF_OUT1[15:11] (%s) to MUX1, input 1", "0x" + Integer.toHexString(n)));
        mux1.setB( n );

        int regdst = core.controlComponent.getRegDst();
        logger.accept(String.format("ID: Sending RegDst (%s) to MUX1", "0x" + Integer.toHexString(regdst)));
        mux1.setSelector(regdst);

        int regwrite = core.controlComponent.getRegWrite();
        logger.accept(String.format("ID: Sending RegWrite (%s) to RegisterComponent(RegWrite)", "0x" + Integer.toHexString(regwrite)));
        registersComponent.setRegWrite(regwrite);

        logger.accept("ID: Sending MUX1 output to WriteRegister");
        registersComponent.setWriteRegister(mux1.getResult(logger));

        logger.accept(String.format("ID: Sending IF_OUT1[15:0] (%s) to SignExtend and ID_OUT4", "0x" + Integer.toHexString(Utility.SubBits( prev.IF_OUT1, 0, 16 ))));
        ID_OUT4 = Utility.SubBits( prev.IF_OUT1, 0, 16 );

        int seout = SignExtendComponent.extend( ID_OUT4 );
        logger.accept(String.format("ID: Sending SignExtend output (%s) to ID_OUT2", "0x" + Integer.toHexString(seout) ) );
        ID_OUT2 = seout;
    }
}

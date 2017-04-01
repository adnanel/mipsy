package mipsy.core.dataphases;

import mipsy.Utility;
import mipsy.core.MIPSCore;
import mipsy.core.components.MUXComponent;
import mipsy.core.components.RegistersComponent;
import mipsy.core.components.SignExtendComponent;

import java.util.function.Consumer;

/**
 * Created by Adnan on 3/31/2017.
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

    private RegistersComponent registersComponent = new RegistersComponent();
    private MUXComponent mux1 = new MUXComponent("MUX1");

    public ID(MIPSCore core) {
        super(core);
    }

    @Override
    public void step(Consumer<String> logger) {

        registersComponent.setRegisters( core.registers );

        IF prev = (IF)prevPhase;

        logger.accept("ID: Sending IF_OUT0 to ID_OUT0");
        ID_OUT0 = prev.IF_OUT0;

        logger.accept("ID: Sending IF_OUT1[25:21] to ReadReg1");
        registersComponent.setReadRegister1( Utility.SubBits(prev.IF_OUT1, 21, 26) );

        logger.accept("ID: Sending IF_OUT1[20:16] to ReadReg2 and MUX1, input 0");
        registersComponent.setReadRegister2( Utility.SubBits(prev.IF_OUT1, 16, 21));
        mux1.setA(Utility.SubBits(prev.IF_OUT1, 16, 21));

        logger.accept("ID: Sending read data 1 to ID_OUT1");
        ID_OUT1 = registersComponent.getReadData1(logger).value;

        logger.accept("ID: Sending read data 2 to ID_OUT3");
        ID_OUT3 = registersComponent.getReadData2(logger).value;

        logger.accept("ID: Sending IF_OUT1[15:11] to MUX1, input 1");
        mux1.setB( Utility.SubBits(prev.IF_OUT1, 11, 16));

        logger.accept("ID: Sending RegDst to MUX1");
        mux1.setSelector(core.controlComponent.getRegDst());

        logger.accept("ID: Sending RegWrite to RegisterComponent(RegWrite)");
        registersComponent.setRegWrite(core.controlComponent.getRegWrite());

        logger.accept("ID: Sending MUX1 output to WriteRegister");
        registersComponent.setRegWrite(mux1.getResult(logger));

        logger.accept("ID: Sending IF_OUT1[15:0] to SignExtend and ID_OUT4");
        ID_OUT4 = Utility.SubBits( prev.IF_OUT1, 0, 16 );

        logger.accept("ID: Sending SignExtend output to ID_OUT2");
        ID_OUT2 = SignExtendComponent.extend( ID_OUT4 );
    }
}

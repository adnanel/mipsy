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
    public int ID_OUT0;
    public int ID_OUT1;
    public int ID_OUT2;
    public int ID_OUT3;
    public int ID_OUT4;

    private RegistersComponent registersComponent = new RegistersComponent();
    private MUXComponent muxComponent = new MUXComponent();

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
        registersComponent.setReadRegister1( Utility.SubBits(prev.IF_OUT1, 21, 25) );

        logger.accept("ID: Sending IF_OUT1[20:16] to ReadReg2 and MUX1, input 0");
        registersComponent.setReadRegister2( Utility.SubBits(prev.IF_OUT1, 16, 20)) ;

        logger.accept("ID: Sending read data 1 to ID_OUT1");
        ID_OUT1 = registersComponent.getReadData1(logger).value;

        logger.accept("ID: Sending read data 2 to ID_OUT3");
        ID_OUT3 = registersComponent.getReadData2(logger).value;

        logger.accept("ID: Sending IF_OUT1[15:11] to MUX1, input 1");
        muxComponent.setB( Utility.SubBits(prev.IF_OUT1, 11, 15));

        logger.accept("ID: Sending IF_OUT1[15:0] to SignExtend and ID_OUT4");
        ID_OUT4 = prev.IF_OUT1;

        logger.accept("ID: Sending SignExtend output to ID_OUT2");
        ID_OUT2 = SignExtendComponent.extend( Utility.SubBits( prev.IF_OUT1, 0, 15 ));
    }
}

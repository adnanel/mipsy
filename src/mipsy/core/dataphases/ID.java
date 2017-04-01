package mipsy.core.dataphases;

import mipsy.core.MIPSCore;

import java.util.function.Consumer;

/**
 * Created by Adnan on 3/31/2017.
 */
public class ID extends DataPhase {
    public int ID_OUT0;
    public int ID_OUT1;
    public int ID_OUT2;

    public ID(MIPSCore core) {
        super(core);
    }

    @Override
    public void step(Consumer<String> logger) {
        super.step(logger);

        logger.accept("ID: Sending IF_OUT0 to ID_OUT0");
        logger.accept("ID: ");
        logger.accept("ID: Sending IF_OUT1[25:21] to ReadReg1");
        logger.accept("ID: Sending IF_OUT1[20:16] to ReadReg2 and MUX1, input 0");
        logger.accept("ID: Sending IF_OUT1[15:11] to MUX1, input 1");
        logger.accept("ID: Sending IF_OUT1[15:0] to SignExtend and ID_OUT2");
    }
}

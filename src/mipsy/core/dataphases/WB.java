package mipsy.core.dataphases;

import mipsy.core.MIPSCore;
import mipsy.core.components.MUXComponent;

import java.util.function.Consumer;

/**
 * Created on 3/31/2017.
 */
public class WB extends DataPhase {
    public IF ifPhase;

    private MUXComponent mux4 = new MUXComponent("MUX4");

    public WB(MIPSCore core) {
        super(core);
    }

    @Override
    public void step(Consumer<String> logger) {
        MEM prev = (MEM)prevPhase;

        logger.accept(String.format("WB: Sending MEM_OUT1 (%s) to MUX4", Integer.toHexString(prev.MEM_OUT1)));
        mux4.setB(prev.MEM_OUT1);

        logger.accept(String.format("WB: Sending MEM_OUT2 (%s) to MUX4", Integer.toHexString(prev.MEM_OUT2)));
        mux4.setA(prev.MEM_OUT2);

        int res = core.controlComponent.getMemToReg();
        logger.accept(String.format("WB: Sending MemToReg (%s) to MUX4", Integer.toHexString(res)));
        mux4.setSelector(res);

        logger.accept(String.format("WB: Sending MEM_OUT0 (%s) output to PC", Integer.toHexString(prev.MEM_OUT0)));
        ifPhase.pc = prev.MEM_OUT0;

        int mux4Out = mux4.getResult(logger);
        logger.accept(String.format("WB: Sending MUX4 output (%s) to Registers", Integer.toHexString(mux4Out)));
        for ( DataPhase phase : core.dataPhases )
            if ( phase instanceof ID ) {
                ((ID) phase).registersComponent.setWriteData(logger, mux4Out);
            }
    }
}

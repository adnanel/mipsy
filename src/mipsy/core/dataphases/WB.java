package mipsy.core.dataphases;

import mipsy.core.MIPSCore;
import mipsy.core.components.MUXComponent;

import java.util.function.Consumer;

/**
 * Created by Adnan on 3/31/2017.
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

        logger.accept("WB: Sending MEM_OUT1 to MUX4");
        mux4.setA(prev.MEM_OUT1);

        logger.accept("WB: Sending MEM_OUT2 to MUX4");
        mux4.setB(prev.MEM_OUT2);

        logger.accept("WB: Sending MemToReg to MUX4");
        mux4.setSelector(core.controlComponent.getMemToReg());

        logger.accept("WB: Sending MEM_OUT0 to WB_OUT0");
        //WB_OUT0 = prev.MEM_OUT0;

        logger.accept("WB: Sending MUX4 output to PC");
        ifPhase.pc = mux4.getResult(logger);
    }
}

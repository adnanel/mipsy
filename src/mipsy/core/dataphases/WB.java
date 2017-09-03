package mipsy.core.dataphases;

import mipsy.Utility;
import mipsy.core.MIPSCore;
import mipsy.core.components.MUXComponent;
import mipsy.types.NoMoreInstructionsException;

import java.util.function.Consumer;

/**
 * Created by adnan on 05.04.2017..
 */
public class WB extends DataPhase {
    private MUXComponent mux5 = new MUXComponent("MUX5");

    public WB(MIPSCore core) {
        super(core);
    }

    public boolean isHalt = false;

    @Override
    public boolean step(Consumer<String> logger) throws NoMoreInstructionsException {
        logger = Utility.appendToLogger("WB - ", logger);
        MEMWB memwb = core.MEMWB;

        // wb can't occur before the 5th cycle
        if ( core.getCycleCount() < 5 ) {
            logger.accept("No work to do, skipping...");
            return false;
        }

        logger.accept("START");

        mux5.setA(memwb.OUT1, logger);
        mux5.setB(memwb.OUT0, logger);
        mux5.setSelector(memwb.MemToReg);

        core.ID.registersComponent.setRegWrite(memwb.RegWrite);
        core.ID.registersComponent.setWriteRegister(memwb.OUT2);
        core.ID.registersComponent.setWriteData(logger, mux5.getResult(logger));

        isHalt = memwb.isHalt;

        logger.accept("END");

        return !isHalt;
    }

    @Override
    public void writeResults(Consumer<String> logger) {

    }
}

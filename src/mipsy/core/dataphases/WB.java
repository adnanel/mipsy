package mipsy.core.dataphases;

import mipsy.core.MIPSCore;
import mipsy.core.components.MUXComponent;
import mipsy.types.NoMoreInstructionsException;

import java.util.function.Consumer;

/**
 * Created by adnan on 05.04.2017..
 */
public class WB extends DataPhase {
    private MUXComponent mux4 = new MUXComponent("MUX4");

    public WB(MIPSCore core) {
        super(core);
    }

    public boolean isHalt = false;

    @Override
    public void step(Consumer<String> logger) throws NoMoreInstructionsException {
        MEMWB memwb = core.MEMWB;

        mux4.setA(memwb.OUT1);
        mux4.setB(memwb.OUT0);
        mux4.setSelector(memwb.MemToReg);

        core.ID.registersComponent.setRegWrite(memwb.RegWrite);
        core.ID.registersComponent.setWriteRegister(memwb.OUT2);
        core.ID.registersComponent.setWriteData(logger, mux4.getResult(logger));

        isHalt = memwb.isHalt;
    }

    @Override
    public void writeResults(Consumer<String> logger) {

    }
}

package mipsy.core.dataphases;

import mipsy.core.MIPSCore;

import java.util.function.Consumer;

/**
 * Created by Adnan on 3/31/2017.
 */
public class WB extends DataPhase {
    public WB(MIPSCore core) {
        super(core);
    }

    @Override
    public void step(Consumer<String> logger) {

    }
}

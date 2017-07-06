package mipsy.core.dataphases;

import mipsy.core.MIPSCore;
import mipsy.types.NoMoreInstructionsException;

import java.util.function.Consumer;

/**
 * Created on 3/31/2017.
 */
public abstract class DataPhase {
    MIPSCore core;

    public DataPhase(MIPSCore core) {
        this.core = core;
    }

    // return false is step didn't occur (e.g. a stall is in progress)
    public abstract boolean step(Consumer<String> logger) throws NoMoreInstructionsException;
    public abstract void writeResults(Consumer<String> logger);
}

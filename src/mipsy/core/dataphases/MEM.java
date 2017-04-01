package mipsy.core.dataphases;

import mipsy.core.MIPSCore;

import java.util.function.Consumer;

/**
 * Created by Adnan on 3/31/2017.
 */
public class MEM extends DataPhase {
    public int MEM_OUT0;
    public int MEM_OUT1;
    public int MEM_OUT2;

    public MEM(MIPSCore core) {
        super(core);
    }

    @Override
    public void step(Consumer<String> logger) {

    }
}

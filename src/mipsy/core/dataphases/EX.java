package mipsy.core.dataphases;

import mipsy.core.MIPSCore;

import java.util.function.Consumer;

/**
 * Created by Adnan on 3/31/2017.
 */
public class EX extends DataPhase {
    public int EX_OUT0;
    public int EX_OUT1;
    public int EX_OUT2;
    public int EX_OUT3;
    public int EX_OUT4;

    public EX(MIPSCore core) {
        super(core);
    }

    @Override
    public void step(Consumer<String> logger ) {}
}

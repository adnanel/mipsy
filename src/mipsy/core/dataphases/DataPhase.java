package mipsy.core.dataphases;

import mipsy.core.MIPSCore;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Created by Adnan on 3/31/2017.
 */
public class DataPhase {
    MIPSCore core;


    public DataPhase(MIPSCore core) {
        this.core = core;
    }

    public void receiveData(DataPhase src) {}

    public void step(Consumer<String> logger) {
    }
}

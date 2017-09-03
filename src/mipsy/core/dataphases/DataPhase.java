package mipsy.core.dataphases;

import mipsy.core.MIPSCore;
import mipsy.types.Instruction;
import mipsy.types.NoMoreInstructionsException;

import java.util.function.Consumer;

/**
 * Created on 3/31/2017.
 */
public abstract class DataPhase {
    MIPSCore core;

    private String name;

    public DataPhase(MIPSCore core, String name) {
        this.core = core;
        this.name = name;
    }

    public String getName() {return name;}

    public abstract PhaseResult step(Consumer<String> logger) throws NoMoreInstructionsException;
    public abstract void writeResults(Consumer<String> logger);

    public static class PhaseResult {
        public boolean stepOccured;
        public Instruction processedInstruction;

        public PhaseResult(boolean stepOccured, Instruction processedInstruction) {
            this.stepOccured = stepOccured;
            this.processedInstruction = processedInstruction;
        }

        public PhaseResult(Instruction processedInstruction) {
            this.stepOccured = processedInstruction != null;
            this.processedInstruction = processedInstruction;
        }

        public static PhaseResult NO_ACTION = new PhaseResult(false, null);
    }

    public static class PhaseNames {
        public static String IF = "IF";
        public static String ID = "ID";
        public static String EX = "EX";
        public static String MEM = "MEM";
        public static String WB = "WB";
    }
}

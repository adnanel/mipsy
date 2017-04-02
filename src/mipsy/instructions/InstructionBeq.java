package mipsy.instructions;

import mipsy.types.Instruction;

import java.util.List;

/**
 * Created on 4/1/2017.
 */
public class InstructionBeq extends Instruction {
    public InstructionBeq(List<String> args) {
        super(args);
    }

    @Override
    public int getCoded() {
        return 0;
    }
}

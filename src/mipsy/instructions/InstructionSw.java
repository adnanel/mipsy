package mipsy.instructions;

import mipsy.types.Instruction;

import java.util.List;

/**
 * Created by Adnan on 4/1/2017.
 */
//todo
public class InstructionSw extends Instruction {
    protected InstructionSw(List<String> args) {
        super(args);
    }

    @Override
    public int getCoded() {
        return 0;
    }
}

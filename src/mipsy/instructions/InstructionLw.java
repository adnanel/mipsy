package mipsy.instructions;

import mipsy.types.Instruction;

import java.util.List;

/**
 * Created by Adnan on 4/1/2017.
 */

//todo
public class InstructionLw extends Instruction {
    protected InstructionLw(List<String> args) {
        super(args);
    }

    @Override
    public int getCoded() {
        return 0;
    }
}

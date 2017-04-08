package mipsy.instructions;

import mipsy.types.Instruction;

import java.util.List;

/**
 * Created by adnan on 06.04.2017..
 */
public class InstructionHalt extends Instruction {
    public InstructionHalt(List<String> args) {
        super(args);
        this.instruction = "halt";
    }

    @Override
    public int getCoded() {
        return 0;
    }

    @Override
    public Type getType() {
        return Type.IType;
    }

    @Override
    public boolean canBranch() {
        return false;
    }


    @Override
    public String toString() {
        return "halt";
    }
}

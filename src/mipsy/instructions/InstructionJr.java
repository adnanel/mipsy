package mipsy.instructions;

import mipsy.Utility;
import mipsy.types.Instruction;
import mipsy.types.Register;

import java.util.List;

/**
 * Created on 3/30/2017.
 */
public class InstructionJr extends Instruction {
    public String targetReg;

    @Override
    public int getCoded() {
        // http://www.math.unipd.it/~sperduti/ARCHITETTURE-1/mips32.pdf
        // https://www.eg.bucknell.edu/~csci320/mips_web/


        int res = 0;

        res = (res << 5) | Register.getRegisterNumber(targetReg);

        res <<= 15;

        res = (res << 6) | 0b001000;

        return res;
    }

    public InstructionJr(List<String> args) {
        super(args);
        this.instruction = "jr";

        if ( args.size() != 1 )
            throw new IllegalArgumentException("Invalid arguments passed to jr! Expected 1, given " + args.size());

        targetReg = args.get(0);
    }

    @Override
    public Type getType() {
        return Type.RType;
    }

    @Override
    public boolean canBranch() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("j %s", targetReg);
    }
}

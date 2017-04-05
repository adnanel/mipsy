package mipsy.instructions;

import mipsy.Utility;
import mipsy.types.Instruction;
import mipsy.types.Register;

import java.util.List;

/**
 * Created on 3/30/2017.
 */
public class InstructionLui extends Instruction {
    public String dest;
    public int immediate;

    @Override
    public int getCoded() {
        // http://www.math.unipd.it/~sperduti/ARCHITETTURE-1/mips32.pdf
        // https://www.eg.bucknell.edu/~csci320/mips_web/

        int res = 0b001111;

        res = (res << 5);

        res = (res << 5) | Register.getRegisterNumber(dest);

        res = (res << 16) | immediate;

        return res;
    }

    public InstructionLui(List<String> args) {
        super(args);
        this.instruction = "lui";

        if ( args.size() != 2 )
            throw new IllegalArgumentException("Invalid arguments passed to lui! Expected 2, given " + args.size());

        dest = args.get(0);
        immediate = Utility.ParseInt(args.get(1));
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
        return String.format("lui %s, %s", dest, "0x" + Integer.toHexString(immediate));
    }
}

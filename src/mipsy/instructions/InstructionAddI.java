package mipsy.instructions;

import mipsy.Utility;
import mipsy.types.Instruction;
import mipsy.types.Register;

import java.util.List;

/**
 * Created on 3/30/2017.
 */
public class InstructionAddI extends Instruction {
    public String dest;
    public String opA;
    public int opB;

    @Override
    public int getCoded() {
        // http://www.math.unipd.it/~sperduti/ARCHITETTURE-1/mips32.pdf
        // https://www.eg.bucknell.edu/~csci320/mips_web/

        int res = 0b001000;

        res = (res << 5) | Register.getRegisterNumber(opA);

        res = (res << 5) | Register.getRegisterNumber(dest);

        res = (res << 16) | opB;

        return res;
    }

    public InstructionAddI(List<String> args) {
        super(args);
        this.instruction = "addi";

        if ( args.size() != 3 )
            throw new IllegalArgumentException("Invalid arguments passed to addi! Expected 3, given " + args.size());

        dest = args.get(0);
        opA = args.get(1);
        opB = Utility.ParseInt(args.get(2));
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
        return String.format("addi %s, %s, %s", dest, opA, "0x" + Integer.toHexString(opB));
    }
}

package mipsy.instructions;

import mipsy.Utility;
import mipsy.types.Instruction;
import mipsy.types.Register;

import java.util.List;

/**
 * Created on 3/30/2017.
 */
public class InstructionSrl extends Instruction {
    public String dest;
    public String opA;
    public int opB;

    @Override
    public int getCoded() {
        // http://www.math.unipd.it/~sperduti/ARCHITETTURE-1/mips32.pdf
        // https://www.eg.bucknell.edu/~csci320/mips_web/

        int res;

        res = Register.getRegisterNumber(opA);

        res = (res << 5) | Register.getRegisterNumber(dest);

        res = (res << 5) | opB;

        res = (res << 6) | 0b000010;

        return res;
    }

    public InstructionSrl(List<String> args) {
        super(args);
        this.instruction = "srl";

        if ( args.size() != 3 )
            throw new IllegalArgumentException("Invalid arguments passed to srl! Expected 3, given " + args.size());

        dest = args.get(0);
        opA = args.get(1);
        opB = Utility.ParseInt(args.get(2));
    }

    @Override
    public String toString() {
        return String.format("srl %s, %s, %d", dest, opA, opB);
    }
}

package mipsy.instructions;

import mipsy.Utility;
import mipsy.types.Instruction;
import mipsy.types.Register;

import java.util.List;

/**
 * Created on 3/30/2017.
 */
public class InstructionLh extends Instruction {
    public String dest;
    public String base;
    public int offset;

    @Override
    public int getCoded() {
        // http://www.math.unipd.it/~sperduti/ARCHITETTURE-1/mips32.pdf
        // https://www.eg.bucknell.edu/~csci320/mips_web/

        int res = 0b100001;

        res = (res << 5) | Register.getRegisterNumber(base);

        res = (res << 5) | Register.getRegisterNumber(dest);

        res = (res << 16) | offset;

        return res;
    }

    public InstructionLh(List<String> args) {
        super(args);
        this.instruction = "lh";

        if ( args.size() != 2 )
            throw new IllegalArgumentException("Invalid arguments passed to lh! Expected 2, given " + args.size());

        dest = args.get(0);
        offset = Utility.ParseInt(args.get(1).substring(0, args.get(1).indexOf('(')));

        String fin = args.get(1);
        fin = fin.substring(fin.indexOf('(') + 1);
        fin = fin.substring(0, fin.length() - 1);

        base = fin;
    }

    @Override
    public String toString() {
        return String.format("lh %s, %s(%s)", dest, "0x" + Integer.toHexString(offset), base);
    }
}

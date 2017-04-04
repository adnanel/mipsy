package mipsy.instructions;

import mipsy.Utility;
import mipsy.types.Instruction;

import java.util.List;

/**
 * Created on 3/30/2017.
 */
public class InstructionJal extends Instruction {
    public int target;

    @Override
    public int getCoded() {
        // http://www.math.unipd.it/~sperduti/ARCHITETTURE-1/mips32.pdf
        // https://www.eg.bucknell.edu/~csci320/mips_web/


        int res = 0b000011;

        res = (res<<26) | target;

        return res;
    }

    public InstructionJal(List<String> args) {
        super(args);
        this.instruction = "jal";

        if ( args.size() != 1 )
            throw new IllegalArgumentException("Invalid arguments passed to jal! Expected 1, given " + args.size());

        target = Utility.ParseInt(args.get(0));
    }

    @Override
    public String toString() {
        return String.format("jal %s", Integer.toHexString(target));
    }
}

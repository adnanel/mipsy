package mipsy.instructions;

import mipsy.Utility;
import mipsy.types.Instruction;
import mipsy.types.Register;

import java.util.List;

/**
 * Created on 4/1/2017.
 */

public class InstructionLw extends Instruction {
    // example: lw  $s2, 0($a2)
    //          lw  rt, offset(base)

    private String destReg;
    private int offset;
    private String baseReg;

    public InstructionLw(List<String> args) {
        super(args);

        this.instruction = "lw";

        if ( args.size() != 2 )
            throw new IllegalArgumentException("Invalid arguments passed to lw! Expected 2, given " + args.size());

        destReg = args.get(0);

        //drugi arg se sastoji od offset(base)

        offset = Utility.ParseInt(args.get(1).substring(0, args.get(1).indexOf('(')));

        String fin = args.get(1);
        fin = fin.substring(fin.indexOf('(') + 1);
        fin = fin.substring(0, fin.length() - 1);

        baseReg = fin;
    }

    @Override
    public int getCoded() {
        int res = 0b100011;

        res = (res << 5) | Register.getRegisterNumber(baseReg);

        res = (res << 5) | Register.getRegisterNumber(destReg);

        res = (res << 16) | offset;

        return res;
    }

    @Override
    public String toString() {
        return String.format("lw %s, %d(%s)", destReg, offset, baseReg);
    }
}

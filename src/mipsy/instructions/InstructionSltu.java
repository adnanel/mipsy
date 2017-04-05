package mipsy.instructions;

import mipsy.types.Instruction;
import mipsy.types.Register;

import java.util.List;

/**
 * Created on 4/1/2017.
 */
public class InstructionSltu extends Instruction {
    private String opA;
    private String opB;
    private String dest;

    public InstructionSltu(List<String> args) {
        super(args);
        this.instruction = "sltu";

        if ( args.size() != 3 )
            throw new IllegalArgumentException("Invalid arguments passed to sltu! Expected 3, given " + args.size());

        opA = args.get(0);
        opB = args.get(1);
        dest = args.get(2);
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
    public int getCoded() {
        int res = 0;

        res = (res << 5) | Register.getRegisterNumber(opA);
        res = (res << 5) | Register.getRegisterNumber(opB);
        res = (res << 5) | Register.getRegisterNumber(dest);

        res = (res << 5);

        res = (res << 6) | 0b101011;

        return res;
    }


    @Override
    public String toString() {
        return String.format("sltu %s, %s, %s", opA, opB, dest);
    }
}

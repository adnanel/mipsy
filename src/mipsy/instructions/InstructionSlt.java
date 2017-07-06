package mipsy.instructions;

import mipsy.Utility;
import mipsy.types.Instruction;
import mipsy.types.Register;

import java.util.List;

/**
 * Created on 4/1/2017.
 */
public class InstructionSlt extends Instruction {
    private String opA;
    private String opB;
    private String dest;

    public InstructionSlt(List<String> args) {
        super(args);
        this.instruction = "slt";

        if ( args.size() != 3 )
            throw new IllegalArgumentException("Invalid arguments passed to slt! Expected 3, given " + args.size());


        dest = args.get(0);
        opA = args.get(1);
        opB = args.get(2);
    }

    @Override
    public Type getType() {
        return Type.RType;
    }

    @Override
    public boolean canBranch() {
        return false;
    }

    @Override
    public int getCoded() {
        int res = 0;

        res = (res << 5) | Register.getRegisterNumber(opA);
        res = (res << 5) | Register.getRegisterNumber(opB);
        res = (res << 5) | Register.getRegisterNumber(dest);

        res = (res << 5);

        res = (res << 6) | 0b101010;

        return res;
    }


    @Override
    public String toString() {
        return String.format("slt %s, %s, %s", dest, opA, opB);
    }
}

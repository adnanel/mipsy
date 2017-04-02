package mipsy.instructions;

import mipsy.Utility;
import mipsy.types.Instruction;
import mipsy.types.Register;

import java.util.List;

/**
 * Created on 4/1/2017.
 */
public class InstructionBeq extends Instruction {
    private String opA;
    private String opB;

    private int offset;

    public InstructionBeq(List<String> args) {
        super(args);
        this.instruction = "beq";

        if ( args.size() != 3 )
            throw new IllegalArgumentException("Invalid arguments passed to add! Expected 3, given " + args.size());

        opA = args.get(0);
        opB = args.get(1);
        offset = Utility.ParseInt(args.get(2));
    }

    @Override
    public int getCoded() {
        int res = 0b000100;

        res = (res << 5) | Register.getRegisterNumber(opA);
        res = (res << 5) | Register.getRegisterNumber(opB);
        res = (res << 16) | offset;

        return res;
    }


    @Override
    public String toString() {
        return String.format("beq %s, %s, %d", opA, opB, offset);
    }
}

package mipsy.instructions;

import mipsy.types.Instruction;

import java.util.List;

/**
 * Created by Adnan on 3/30/2017.
 */
public class InstructionAdd extends Instruction {
    public String dest;
    public String opA;
    public String opB;

    @Override
    public int getCoded() {
        //todo
        return 0;
    }

    public InstructionAdd(List<String> args) {
        super(args);
        this.instruction = "add";

        if ( args.size() != 3 )
            throw new IllegalArgumentException("Invalid arguments passed to add! Expected 3, given " + args.size());

        dest = args.get(0);
        opA = args.get(1);
        opB = args.get(2);
    }

    @Override
    public String toString() {
        return String.format("add %s, %s, %s", dest, opA, opB);
    }
}

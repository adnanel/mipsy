package mipsy.instructions;

import mipsy.types.Instruction;
import mipsy.types.Register;

import java.util.List;

/**
 * Created on 3/30/2017.
 */
public class InstructionNor extends Instruction {
    public String dest;
    public String opA;
    public String opB;

    @Override
    public int getCoded() {
        // http://www.math.unipd.it/~sperduti/ARCHITETTURE-1/mips32.pdf
        // https://www.eg.bucknell.edu/~csci320/mips_web/


        // najvisih 6 bitova su nula
        int res = 0;

        //iducih 5 su opA register
        res = Register.getRegisterNumber(opA);

        //iducih 5 su opB
        res = (res << 5) | Register.getRegisterNumber(opB);

        //iducih 5 su dest
        res = (res << 5) | Register.getRegisterNumber(dest);

        //iducih 5 bita se puni nulama
        res = res << 5;

        res = (res << 6) | 0b100111;

        return res;
    }

    public InstructionNor(List<String> args) {
        super(args);
        this.instruction = "nor";

        if ( args.size() != 3 )
            throw new IllegalArgumentException("Invalid arguments passed to nor! Expected 3, given " + args.size());

        dest = args.get(0);
        opA = args.get(1);
        opB = args.get(2);
    }

    @Override
    public String toString() {
        return String.format("nor %s, %s, %s", dest, opA, opB);
    }
}

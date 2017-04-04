package mipsy.core.components;

import mipsy.Utility;

import java.util.function.Consumer;

/**
 * Created on 4/1/2017.
 */
public class ALUControllerComponent {
    // http://prntscr.com/erb20h

    private int aluOp; //2 bit
    private int instruction; // instruction[5:0]

    private String name;

    public ALUControllerComponent(String name) {
        this.name = name;
    }

    public void setAluOp(int aluOp) {
        this.aluOp = aluOp;
    }


    public int getResult(Consumer<String> logger) {
        int res = _getResult(logger);

        logger.accept(String.format("%s: AluOP = %s, Instruction[5:0] = %s, output is %s", name, "0x" + Integer.toHexString(aluOp), "0x" + Integer.toHexString(instruction), "0x" + Integer.toHexString(res)));

        return res;
    }

    private int _getResult(Consumer<String> logger) {
        aluOp = Utility.SubBits(aluOp, 0, 2);

        if ( aluOp == 0 )       return 0b0010;
        if ( aluOp % 2 == 1 )   return 0b0110;

        if ( (aluOp >> 1) > 0 ) {
            //prema tabeli, gledamo samo bitove [F3:F0]
            instruction = Utility.SubBits(instruction, 0, 4);

            if ( instruction == 0 ) return 0b0010;
            if ( instruction == 2 ) return 0b0110;
            if ( instruction == 4 ) return 0b0000;
            if ( instruction == 5 ) return 0b0001;
            if ( instruction == 10) return 0b0111;

            //todo, NOR ne prepoznaje, [F3:F3] je 0111 ( dec. 7 )
            //todo sto ovi gore IFovi ne prepoznaju...

            //sad za sad je nor hardkodiran...
            if ( instruction == 7 ) return 0b1100;

            logger.accept(String.format("%s: Undefined ALUOp combination!", name));
        } else {
            logger.accept(String.format("%s: Undefined ALUOp combination!", name));
        }
        return 0; //todo
    }

    public void setInstruction(int i) {
        this.instruction = i;
    }

}

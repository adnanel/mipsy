package mipsy.core.components;

import mipsy.instructions.InstructionBeq;
import mipsy.instructions.InstructionLw;
import mipsy.instructions.InstructionSw;
import mipsy.types.Instruction;

/**
 * Created by Adnan on 3/31/2017.
 */
public class ControlComponent {
    private int currInstruction;
    private Class currInstructionClass;

    // http://prntscr.com/erbcwf

    public void setCurrInstruction(int currInstruction) {
        this.currInstruction = currInstruction;
        this.currInstructionClass = Instruction.DetectInstruction(currInstruction);
    }

    public int getRegWrite() {
        if ( currInstructionClass == InstructionLw.class )
            return 1;
        if ( currInstructionClass == InstructionSw.class )
            return 0;
        if ( currInstructionClass == InstructionBeq.class )
            return 1;
        return 0;
    }

    public int getRegDst() {
        if ( currInstructionClass == InstructionLw.class ) return 0;
        return 1;
    }

    public int getAluSrc() {
        if ( currInstructionClass == InstructionLw.class ) return 1;
        if ( currInstructionClass == InstructionSw.class ) return 1;
        return 0;
    }

    public int getAluOp() {
        int op1 = 0;
        int op0 = 0;

        if ( currInstructionClass == InstructionSw.class ) {
            op1 = op0 = 0;
        } else if ( currInstructionClass == InstructionLw.class ) {
            op1 = op0 = 0;
        } else if ( currInstructionClass == InstructionBeq.class ) {
            op0 = 1;
            op1 = 0;
        }

        return (op1 << 1) | op0;
    }

    public int getMemWrite() {
        if ( currInstructionClass == InstructionSw.class ) return 1;
        return 0;
    }

    public int getMemRead() {
        if ( currInstructionClass == InstructionLw.class ) return 1;
        return 0;
    }

    public int getPcSrc() {
        if ( currInstructionClass == InstructionBeq.class ) return 1;
        return 0; //todo
    }

    public int getMemToReg() {
        if ( currInstructionClass == InstructionLw.class )
            return 1;
        return 0;
    }
}

package mipsy.core.components;

import mipsy.instructions.InstructionAddI;
import mipsy.instructions.InstructionBeq;
import mipsy.instructions.InstructionLw;
import mipsy.instructions.InstructionSw;
import mipsy.types.Instruction;

/**
 * Created on 3/31/2017.
 */
public class ControlComponent {
    private Instruction currInstruction;

    // http://prntscr.com/erbcwf

    public void setCurrInstruction(Instruction currInstruction) {
        this.currInstruction = currInstruction;
    }

    public void reset() {
        currInstruction = null;
    }

    public int getRegWrite() {
        if ( currInstruction != null ) {
            if (currInstruction.getClass() == InstructionLw.class)
                return 1;
            if (currInstruction.getClass() == InstructionSw.class)
                return 0;
            if (currInstruction.getClass() == InstructionBeq.class)
                return 0;
            if (currInstruction.getClass() == InstructionAddI.class)
                return 1;
        }

        return 1;
    }

    public int getRegDst() {
        if ( currInstruction != null ) {
            if (currInstruction.getClass() == InstructionLw.class) return 0;

            //I tip instrukcija mora na MUX2 pustiti operand a
            if (currInstruction.getType() == Instruction.Type.IType) return 0;
        }

        return 1;
    }

    public int getAluSrc() {
        if ( currInstruction != null ) {
            if (currInstruction.getClass() == InstructionLw.class) return 1;
            if (currInstruction.getClass() == InstructionSw.class) return 1;

            if ( currInstruction.getClass() == InstructionBeq.class ) return 0;

            // Ostale I tip instrukcije traze da se na MUX3 pusti Operand B
            if (currInstruction.getType() == Instruction.Type.IType) return 1;
        }

        return 0;
    }

    public int getAluOp() {
        int op1 = 1;
        int op0 = 0;


        if ( currInstruction != null ) {
            if (currInstruction.getClass() == InstructionSw.class) {
                op1 = op0 = 0;
            } else if (currInstruction.getClass() == InstructionLw.class) {
                op1 = op0 = 0;
            } else if (currInstruction.getClass() == InstructionAddI.class) {
                op1 = op0 = 0;
            } else if (currInstruction.getClass() == InstructionBeq.class) {
                op0 = 1;
                op1 = 0;
            }
        }


        return (op1 << 1) | op0;
    }

    public int getMemWrite() {
        if ( currInstruction != null ) {
            if (currInstruction.getClass() == InstructionSw.class) return 1;
        }

        return 0;
    }

    public int getMemRead() {
        if ( currInstruction != null ) {
            if (currInstruction.getClass() == InstructionLw.class) return 1;
        }
        return 0;
    }

    public int getBranch() {
        if ( currInstruction != null ) {
            if ( currInstruction.getClass() == InstructionBeq.class ) return 1;
        }
        return 0;
    }

    public int getMemToReg() {
        if ( currInstruction != null ) {
            if (currInstruction.getClass() == InstructionLw.class) return 1;
        }

        return 0;
    }
}

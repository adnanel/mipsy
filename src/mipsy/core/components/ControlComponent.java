package mipsy.core.components;

/**
 * Created by Adnan on 3/31/2017.
 */
public class ControlComponent {
    private int currInstruction;

    public void setCurrInstruction(int currInstruction) {
        this.currInstruction = currInstruction;
    }

    public int getRegWrite() {
        return 0; //todo
    }

    public int getRegDst() {
        return 0; //todo
    }

    public int getAluSrc() {
        return 0; //todo
    }

    public int getAluOp() {
        return 0; //todo
    }

    public int getMemWrite() {
        return 0; //todo
    }

    public int getMemRead() {
        return 0; //todo
    }

    public int getPcSrc() {
        return 0; //todo
    }

    public int getMemToReg() {
        return 0; //todo
    }
}

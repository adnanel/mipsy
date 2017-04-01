package mipsy.core.components;

/**
 * Created by Adnan on 4/1/2017.
 */
public class ALUControllerComponent {
    private int aluOp;
    private int instruction; // instruction[5:0]

    private String name;

    public ALUControllerComponent(String name) {
        this.name = name;
    }

    public void setAluOp(int aluOp) {
        this.aluOp = aluOp;
    }

    public int getResult() {
        return 0; //todo
    }

    public void setInstruction(int i) {
        this.instruction = i;
    }

}

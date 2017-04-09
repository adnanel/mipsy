package mipsy.core.dataphases;

import mipsy.types.Instruction;
import mipsy.types.Register;

/**
 * Created by adnan on 05.04.2017..
 */
public class IDEX {
    public int RegWrite;
    public int RegDst;
    public int AluSrc;
    public int AluOp;
    public int MemWrite;
    public int MemRead;
    public int Branch;
    public int MemToReg;


    public int OUT0;
    public Register OUT1;
    public Register OUT2;
    public int OUT3;
    public int OUT4;
    public int OUT5;

    public boolean isHalt;

    public Instruction currentInstruction;

    public void clear() {
        RegWrite = RegDst = AluSrc = AluOp = MemWrite = MemRead = Branch = MemToReg
                 = OUT0 = OUT3 = OUT4 = OUT5 = 0;
    }
}

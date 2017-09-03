package mipsy.core.dataphases;

import mipsy.types.Instruction;
import mipsy.types.Register;

/**
 * Created by adnan on 05.04.2017..
 */
public class EXMEM {
    public int Branch;
    public int MemWrite;
    public int MemRead;
    public int MemToReg;
    public int RegWrite;
    public int Jump;

    public int OUT0;
    public int OUT1;
    public int OUT2;
    public Register OUT3;
    public int OUT4;
    public int OUT5; //JumpAddr

    public Instruction currentInstruction;
}

package mipsy.core.components;

import mipsy.types.Register;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Created by Adnan on 3/31/2017.
 */
public class RegistersComponent {
    //input pins
    private int readRegister1;
    private int readRegister2;
    private int writeData;
    private int regWrite;

    private String name = "Registers";

    HashMap<String, Register> registers;

    public void setReadRegister1(int readRegister1) {
        this.readRegister1 = readRegister1;
    }

    public void setReadRegister2(int readRegister2) {
        this.readRegister2 = readRegister2;
    }

    public void setWriteData(int writeData) {
        this.writeData = writeData;
    }

    public void setRegWrite(int regWrite) {
        this.regWrite = regWrite;
    }

    public Register getReadData1(Consumer<String> logger) {
        String regName = Register.getMipsRegisterNames()[readRegister1];

        logger.accept(name + ": Reading register " + regName + " and sending to output read data 1");
        return registers.get( regName );
    }

    public Register getReadData2(Consumer<String> logger) {
        String regName = Register.getMipsRegisterNames()[readRegister2];

        logger.accept(name + ": Reading register " + regName + " and sending to output read data 2");
        return registers.get( regName );
    }

    public void setRegisters(HashMap<String, Register> registers) {
        this.registers = registers;
    }
}

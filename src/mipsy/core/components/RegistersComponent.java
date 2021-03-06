package mipsy.core.components;

import mipsy.types.Register;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Created on 3/31/2017.
 */
public class RegistersComponent {
    //input pins
    private int readRegister1;
    private int readRegister2;
    private int regWrite;
    private int writeRegister;

    private String name = "Registers";

    HashMap<String, Register> registers;

    public void setReadRegister1(int readRegister1) {
        this.readRegister1 = readRegister1;
    }

    public void setWriteRegister(int writeRegister) {
        this.writeRegister = writeRegister;
    }

    public void setReadRegister2(int readRegister2) {
        this.readRegister2 = readRegister2;
    }

    public void setWriteData(Consumer<String> logger, int writeData) {
        if ( regWrite == 0 ) {
            logger.accept(String.format("%s: Not writing data because RegWrite = 0", name));
            return;
        }
        String destRegister = Register.getMipsRegisterNames()[writeRegister];

        if ( destRegister.equalsIgnoreCase("$zero") ) {
            logger.accept(String.format("%s: Attempt to write into $zero register, action has no effect.", name));
        } else {
            logger.accept(String.format("%s: Writing %s into register %s", name, "0x" + Integer.toHexString(writeData), destRegister));

            Register reg = registers.computeIfAbsent(destRegister, r -> new Register(r, 0));
            reg.value = writeData;
        }
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

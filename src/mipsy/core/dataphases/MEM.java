package mipsy.core.dataphases;

import mipsy.core.MIPSCore;
import mipsy.core.components.MUXComponent;
import mipsy.core.components.MemoryComponent;

import java.util.function.Consumer;

/**
 * Created on 3/31/2017.
 */
public class MEM extends DataPhase {
    /*
    MEM_OUT0 - mux3 result
    MEM_OUT1 - read data
    MEM_OUT2 - EX_OUT3
     */
    public int MEM_OUT0;
    public int MEM_OUT1;
    public int MEM_OUT2;

    private MUXComponent mux3 = new MUXComponent("MUX3");
    private MemoryComponent dataMemory = new MemoryComponent("DataMem");

    public MEM(MIPSCore core) {
        super(core);
    }


    @Override
    public void step(Consumer<String> logger) {
        EX prev = (EX)prevPhase;

        dataMemory.setMemory(core.memory);

        logger.accept("MEM: Sending EX_OUT3 to DataMem(Address)");
        dataMemory.setAddress(prev.EX_OUT3);

        logger.accept("MEM: Sending EX_OUT4 to DataMem(WriteData)");
        dataMemory.setWriteData(prev.EX_OUT4);

        logger.accept("MEM: Sending ALU2 result to MUX3");
        mux3.setB(prev.EX_OUT1);

        logger.accept("MEM: Sending EX_OUT0 to MUX3");
        mux3.setA(prev.EX_OUT0);

        logger.accept("MEM: Sending PCSrc to MUX3(PCSrc)");
        mux3.setSelector(core.controlComponent.getPcSrc());

        logger.accept("MEM: Sending MemWrite to DataMem(MemWrite)");
        dataMemory.setMemWrite(core.controlComponent.getMemWrite());

        logger.accept("MEM: Sending MemRead to DataMem(MemRead)");
        dataMemory.setMemRead(core.controlComponent.getMemRead());

        logger.accept("MEM: Sending MUX3 output to MEM_OUT0");
        MEM_OUT0 = mux3.getResult(logger);

        logger.accept("MEM: Sending DataMem(ReadData) to MEM_OUT1");
        MEM_OUT1 = dataMemory.getReadData(logger);

        dataMemory.execute(logger);

        logger.accept("MEM: Sending EX_OUT3 to MEM_OUT2");
        MEM_OUT2 = prev.EX_OUT3;
    }
}

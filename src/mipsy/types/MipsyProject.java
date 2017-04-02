package mipsy.types;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created on 3/30/2017.
 */
public class MipsyProject {
    public HashMap<Integer, MemoryEntry> memory = new HashMap<>();
    public HashMap<String, Register> registers = new HashMap<>();
    public List<Instruction> instructions = new ArrayList<>();


    public static MipsyProject loadFile(String file, Consumer<String> logger) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            final MipsyProject project = new MipsyProject();

            reader.lines().forEach(new Consumer<String>() {
                private static final int MODE_DATA = 1;
                private static final int MODE_REGS = 2;
                private static final int MODE_CODE = 3;
                private static final int MODE_META = 4;

                private int mode = 0;
                @Override
                public void accept(String line) {
                    line = line.trim().toLowerCase();
                    if ( line.length() == 0 ) return;

                    try {
                        if ( line.equalsIgnoreCase(".data")) {
                            mode = MODE_DATA;
                            logger.accept("Parsing data segment...");
                        }
                        else if ( line.equalsIgnoreCase(".code"))
                        {
                            mode = MODE_CODE;
                            logger.accept("Parsing code segment...");
                        }
                        else if ( line.equalsIgnoreCase(".regs")){
                            mode = MODE_REGS;
                            logger.accept("Parsing regs segment...");
                        }
                        else if ( line.equalsIgnoreCase(".meta")){
                            mode = MODE_META;
                            logger.accept("Parsing meta segment...");
                        }
                        else {
                            switch ( mode ) {
                                case MODE_CODE:
                                    Instruction instruction = Instruction.fromString(line);
                                    logger.accept(instruction.toString());
                                    project.instructions.add(instruction);
                                    break;
                                case MODE_DATA:
                                    MemoryEntry entry = MemoryEntry.fromString(line);
                                    logger.accept(String.format("Memory[%s] = %d", entry.address, entry.value));
                                    project.memory.put(entry.address, entry);
                                    break;
                                case MODE_REGS:
                                    Register reg = Register.fromString(line);
                                    logger.accept(String.format("Setting %s to %d", reg.name, reg.value));
                                    project.registers.put(reg.name, reg);
                                    break;
                                case MODE_META:
                                    break;
                                default:
                                    logger.accept("Ignoring line\n" + line);
                            }
                        }
                    } catch ( Exception ex ) {
                        logger.accept("Exception! " + ex.getMessage());
                        ex.printStackTrace();
                    }

                }
            });

            logger.accept("Done!" );
            Register.FillMissing(project.registers);

            return project;
        } catch ( Exception ex ) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

    }
}

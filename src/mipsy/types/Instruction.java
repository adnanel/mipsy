package mipsy.types;


import mipsy.instructions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created on 3/30/2017.
 */
public abstract class Instruction {
    public String instruction;

    public List<String> arguments;

    private static HashMap<String, Class> SupportedInstructions = new HashMap<>();
    static {
        //Initialize our supported instructions here

        //Arithmetic
        SupportedInstructions.put("add", InstructionAdd.class);
        SupportedInstructions.put("sub", InstructionSub.class);
        SupportedInstructions.put("addi", InstructionAddI.class);

        //Data transfer
        SupportedInstructions.put("lw", InstructionLw.class);
        SupportedInstructions.put("sw", InstructionSw.class);
        SupportedInstructions.put("lh", InstructionLh.class);
        SupportedInstructions.put("lhu", InstructionLhu.class);
        SupportedInstructions.put("sh", InstructionSh.class);
        SupportedInstructions.put("lb", InstructionLb.class);
        SupportedInstructions.put("lbu", InstructionLbu.class);
        SupportedInstructions.put("sb", InstructionSb.class);
        SupportedInstructions.put("ll", InstructionLl.class);
        SupportedInstructions.put("lui", InstructionLui.class);

        //Logical
        SupportedInstructions.put("and", InstructionAnd.class);
        SupportedInstructions.put("or", InstructionOr.class);
        SupportedInstructions.put("nor", InstructionNor.class);
        SupportedInstructions.put("andi", InstructionAndI.class);


        //Conditional branch
        SupportedInstructions.put("beq", InstructionBeq.class);
    }

    //daje instrukciju kodirano
    // http://www.math.unipd.it/~sperduti/ARCHITETTURE-1/mips32.pdf
    // https://www.eg.bucknell.edu/~csci320/mips_web/

    public abstract int getCoded();

    public static Class DetectInstruction(int encoded) {
        String bin = Integer.toBinaryString(encoded);
        while ( bin.length() < Integer.SIZE ) bin = "0" + bin;

        String first6 = bin.substring(0, 6);

        if ( first6.equalsIgnoreCase("100011") )
            return InstructionLw.class;

        if ( first6.equalsIgnoreCase("101011") )
            return InstructionSw.class;

        if ( first6.equalsIgnoreCase("000100") )
            return InstructionBeq.class;

        if ( first6.equalsIgnoreCase("000000") ) {
            // r type
            String last11 = bin.substring(21);
            String l5 = last11.substring(0, 5);
            last11 = last11.substring(5);

            //last11 - zadnjih 6 bitova
            //l5 - 5 bitova prije zadnjih 6

            if ( l5.equalsIgnoreCase("00000")
                    && last11.equalsIgnoreCase("100000"))
                return InstructionAdd.class;
        }

        return null;
    }

    protected Instruction(List<String> args) {
        this.arguments = args;
    }

    public static List<Instruction> parseInstructions(String code) {
        List<Instruction> res = new ArrayList<>();
        code = code.replace(System.lineSeparator(), "\n");
        code = code.replace("\r\n", "\n");

        for ( String line : code.split("\n") ) {
            Instruction instruction = Instruction.fromString(line);
            res.add(instruction);
        }

        return res;
    }

    public static Instruction fromString(String s) {
        String instruction;
        List<String> operands = new ArrayList<>();

        s = s.trim();
        while ( s.contains("  ")) s = s.replace("  ", " ");

        StringBuilder sb = new StringBuilder();
        int i;
        for ( i = 0; i < s.length(); ++ i ) {
            if ( s.charAt(i) == ' ' ) break;
            sb.append(s.charAt(i));
        }
        instruction = sb.toString().toLowerCase();
        s = s.substring(i).trim();

        for ( String arg : s.split("," ) )
            operands.add(arg.trim());

        if ( SupportedInstructions.containsKey(instruction) )
            try {
                return (Instruction)SupportedInstructions.get(instruction).getConstructors()[0].newInstance(operands);
            } catch (InstantiationException e) {
                e.printStackTrace();
                throw new RuntimeException("Internal error!");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("Internal error!");
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException("Internal error!");
            }

        throw new IllegalArgumentException("Invalid instruction! " + instruction);
    }
}

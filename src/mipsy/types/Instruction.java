package mipsy.types;


import mipsy.core.MIPSCore;
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

    public static enum Type {
        RType,
        JType,
        IType
    }

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
        SupportedInstructions.put("ori", InstructionOrI.class);
        SupportedInstructions.put("sll", InstructionSll.class);
        SupportedInstructions.put("srl", InstructionSrl.class);


        //Conditional branch
        SupportedInstructions.put("beq", InstructionBeq.class);
        SupportedInstructions.put("bne", InstructionBne.class);
        SupportedInstructions.put("slt", InstructionSlt.class);
        SupportedInstructions.put("sltu", InstructionSltu.class);
        SupportedInstructions.put("slti", InstructionSlti.class);
        SupportedInstructions.put("sltiu", InstructionSltiu.class);

        //Unconditional jumps
        SupportedInstructions.put("j", InstructionJ.class);
        SupportedInstructions.put("jr", InstructionJr.class);
        SupportedInstructions.put("jal", InstructionJal.class);


        SupportedInstructions.put("halt", InstructionHalt.class);
    }

    //daje instrukciju kodirano
    // http://www.math.unipd.it/~sperduti/ARCHITETTURE-1/mips32.pdf
    // https://www.eg.bucknell.edu/~csci320/mips_web/

    public abstract int getCoded();

    protected Instruction(List<String> args) {
        this.arguments = args;
    }

    public static List<Instruction> parseInstructions(String code) throws FailedToParseInstructionException {
        List<Instruction> res = new ArrayList<>();
        code = code.replace(System.lineSeparator(), "\n");
        code = code.replace("\r\n", "\n");

        int i = 1;
        for ( String line : code.split("\n") ) {
            try {
                if ( line.trim().length() == 0 ) continue;
                if ( line.trim().startsWith("#")) continue;

                Instruction instruction = Instruction.fromString(line);
                res.add(instruction);
                ++ i;
            } catch ( Exception ex ) {
                ex.printStackTrace();
                throw new FailedToParseInstructionException(i, line, ex);
            }
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

        String err = "";
        if ( SupportedInstructions.containsKey(instruction) )
            try {
                return (Instruction)SupportedInstructions.get(instruction).getConstructors()[0].newInstance(operands);
            } catch (InstantiationException e) {
                e.printStackTrace();
                err = e.getMessage();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                err = e.getMessage();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                err = e.getMessage();
            }

        throw new IllegalArgumentException("Invalid instruction! " + instruction + " (" + err + ")");
    }

    public abstract Type getType();
    public abstract boolean canBranch();

    public String toShortString() {
        //by default return the substring of toString() until the first space
        String res = toString();
        return res.substring(0, res.indexOf(' '));
    }
}

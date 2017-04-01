package mipsy.types;


import mipsy.instructions.InstructionAdd;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Adnan on 3/30/2017.
 */
public abstract class Instruction {
    public String instruction;

    public List<String> arguments;

    private static HashMap<String, Class> SupportedInstructions = new HashMap<>();
    static {
        //Initialize our supported instructions here
        SupportedInstructions.put("add", InstructionAdd.class);
    }

    //daje instrukciju kodirano
    // http://www.math.unipd.it/~sperduti/ARCHITETTURE-1/mips32.pdf
    // https://www.eg.bucknell.edu/~csci320/mips_web/

    public abstract int getCoded();

    protected Instruction(List<String> args) {
        this.arguments = args;
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

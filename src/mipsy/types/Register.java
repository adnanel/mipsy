package mipsy.types;

import mipsy.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Adnan on 3/30/2017.
 */
public class Register {
    private static String s;
    public int value;
    public String name;

    public Register(String name, int value) {
        this.value = value;
        this.name = name;
    }

    public static void FillMissing(HashMap<String, Register> registers) {
        for ( String s : getMipsRegisterNames() )
            if ( !registers.containsKey(s) )
                registers.put(s, new Register(s, 0));
    }

    public static List<Register> makeEmptyRegisters() {
        List<Register> res = new ArrayList<>();
        for ( String s : getMipsRegisterNames() )
            res.add(new Register(s, 0));
        return res;
    }

    public static String[] getMipsRegisterNames()  {
        return new String[] {
            "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7",
            "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8", "$t9",
            "$zero", "$a0", "$a1", "$a2", "$a3", "$v0", "$v1", "$gp", "$fp", "$sp", "$ra", "$at"
        };
    }

    // Expected format: $regName = VALUE
    // VALUE must be an integer written in decimal or hex. hex numbers must start with "0x"
    public static Register fromString(String s) {
        s = s.trim();
        String rName;
        String rVal;

        if ( s.charAt(0) == '$' ) {
            // "$reg = val" format
            StringBuilder sb = new StringBuilder();
            int i;
            for ( i = 0; i < s.length(); ++ i ) {
                if ( s.charAt(i) == ' ' ) break;
                sb.append(s.charAt(i));
            }
            s = s.substring(i).trim();
            if ( s.length() == 0 ) return null;

            rName = sb.toString().toLowerCase();

            boolean found = false;
            for ( String r : getMipsRegisterNames() )
                if ( r.equalsIgnoreCase(rName) ) {
                    found = true;
                    break;
                }

            if ( !found ) throw new IllegalArgumentException("Invalid register name! " + rName);

            if ( s.charAt(0) != '=' ) return null;

            s = s.substring(1).trim();

            sb = new StringBuilder();
            for ( i = 0; i < s.length(); ++ i ) {
                if ( s.charAt(i) == ' ' ) break;
                sb.append(s.charAt(i));
            }

            rVal = sb.toString();

            return new Register(rName, Utility.ParseInt(rVal));
        } else return null;

    }
}

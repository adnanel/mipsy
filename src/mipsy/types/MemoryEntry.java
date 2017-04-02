package mipsy.types;

import mipsy.Utility;

/**
 * Created on 3/30/2017.
 */
public class MemoryEntry {
    public Integer address;
    public Integer value;

    public MemoryEntry(Integer address, Integer value) {
        this.address = address;
        this.value = value;
    }

    // Expected format: "ADDR = VALUE". e.g. "512 = 0" to set the 512th byte to zero
    // hex is supported, e.g. "0xFF = 0" or "512 = 0xFF" or "0xFF = 0x12"
    public static MemoryEntry fromString(String s) {
        s = s.trim();
        String mAddr;
        String mVal;

        StringBuilder sb = new StringBuilder();
        int i;
        for ( i = 0; i < s.length(); ++ i ) {
            if ( s.charAt(i) == ' ' ) break;
            sb.append(s.charAt(i));
        }
        s = s.substring(i).trim();
        if ( s.length() == 0 ) throw new IllegalArgumentException("Syntax error! Expected '=', found end of line");

        mAddr = sb.toString();

        if ( s.charAt(0) != '=' ) throw new IllegalArgumentException("Syntax error! Expected '=', got " + s.charAt(0));

        s = s.substring(1).trim();

        sb = new StringBuilder();
        for ( i = 0; i < s.length(); ++ i ) {
            if ( s.charAt(i) == ' ' ) break;
            sb.append(s.charAt(i));
        }

        mVal = sb.toString();

        return new MemoryEntry( Utility.ParseInt(mAddr), Utility.ParseInt(mVal) );
    }
}

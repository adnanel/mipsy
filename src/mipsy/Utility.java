package mipsy;

/**
 * Created on 3/30/2017.
 */
public class Utility {
    //parses s as int, but supports hex numbers (when s starts with "0x")
    public static int ParseInt( String s ) {
        s = s.trim();
        if ( s.startsWith("0x") ) {
            s = s.substring(2);
            return Integer.parseInt(s, 16);
        } else return Integer.parseInt(s);
    }

    public static String ReverseString(String s) {
        StringBuilder res = new StringBuilder();
        for ( int i = s.length() - 1; i >= 0; -- i )
            res = res.append(s.charAt(i));
        return res.toString();
    }

    //vraca bitove broja kao novi broj, npr SubBits(001000, 0, 4) ce vratiti 1000, gleda se od desna na lijevo
    public static int SubBits(int n, int from, int to) {
        String s = Integer.toBinaryString(n);
        while ( s.length() < Integer.SIZE ) s = "0" + s;
        s = ReverseString(s);

        s = ReverseString(s.substring( from, to ));

        return Integer.parseInt( s, 2 );
    }
}

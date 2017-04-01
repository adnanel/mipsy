package mipsy;

/**
 * Created by Adnan on 3/30/2017.
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


    //vraca bitove broja kao novi broj, npr SubBits(001000, 0, 4) ce vratiti 1000, gleda se od desna na lijevo
    public static int SubBits(int n, int from, int to) {
        String s = Integer.toBinaryString(n);

        s = s.substring( s.length() - from - 1, s.length() - to - 1 );

        return Integer.parseInt( s, 2 );
    }
}

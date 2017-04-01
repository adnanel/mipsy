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
}

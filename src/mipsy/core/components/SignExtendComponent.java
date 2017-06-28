package mipsy.core.components;

import mipsy.Utility;

/**
 * Created on 4/1/2017.
 */
public class SignExtendComponent {
    public static int extend(int n) {
        // sign extend treba da uzme najvisi bit izvornog broja, i njega kopirati na sva ostala visa polja

        int value = (0x0000FFFF & n);
        int mask = 0x00008000;
        if ((mask & n) != 0) {
            value += 0xFFFF0000;
        }
        return value;
    }
}

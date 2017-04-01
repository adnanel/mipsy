package mipsy.core.components;

/**
 * Created by Adnan on 3/31/2017.
 */
public class ControlComponent {

    //http://prnt.sc/er3zsn

    public static final int CONTROL_AND = 0;  // 0000

    public int CC_OUT0;
    public int CC_OUT1;
    public int CC_OUT2;

    public int selector;

    public void setSelector(int selector) {
        this.selector = selector;
    }


}

package mipsy.core.components;

/**
 * Created by Adnan on 4/1/2017.
 */
public class MUXComponent {
    private int a;
    private int b;

    private int selector;

    public void setSelector(int selector) {
        this.selector = selector;
    }

    public void setA(int a) {
        this.a = a;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getResult() {
        return selector == 0 ? a : b;
    }
}

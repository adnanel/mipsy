package mipsy.core.components;

import java.util.function.Consumer;

/**
 * Created on 4/1/2017.
 */
public class MUXComponent {
    private int a;
    private int b;

    private int selector;

    private String name;
    public MUXComponent(String name) {
        this.name = name;
    }


    public void setSelector(int selector) {
        this.selector = selector;
    }

    public void setA(int a) {
        this.a = a;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getResult(Consumer<String> logger) {
        int target = selector == 0 ? a : b;

        String targetOp = selector == 0 ? "a" : "b";
        logger.accept(String.format("%s: Selector is %s, sending %s(operand %s) to output",
                name, Integer.toHexString(selector), Integer.toHexString(target), targetOp));

        return target;
    }
}

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

    public void setA(int a, Consumer<String> logger) {
        logger.accept(String.format("%s: Setting operand a to 0x%s", name, Integer.toHexString(a)));
        this.a = a;
    }

    public void setB(int b, Consumer<String> logger) {
        logger.accept(String.format("%s: Setting operand b to 0x%s", name, Integer.toHexString(b)));
        this.b = b;
    }

    public int getResult(Consumer<String> logger) {
        int target = selector == 0 ? a : b;

        String targetOp = selector == 0 ? "a" : "b";
        logger.accept(String.format("%s: Selector is %s, sending %s(operand %s) to output",
                name, "0x" + Integer.toHexString(selector), "0x" + Integer.toHexString(target), targetOp));

        return target;
    }
}

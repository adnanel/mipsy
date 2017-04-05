package mipsy.core.components;

import java.util.function.Consumer;

/**
 * Created on 3/31/2017.
 *
 *
 * Pogledaj "4.4 A simple implementation scheme" u Petterson Hennesy, 5th edition.
 */
public class ALUComponent {
    /**
     * Control states:
     *
     * ALU control lines  |   Function
     *
     *   0000             |     AND
     *   0001             |     OR
     *   0010             |     ADD
     *   0110             |     SUBTRACT
     *   0111             |     SET ON LESS THAN
     *   1100             |     NOR
     */

    public static final int CONTROL_AND              = 0;  // 0000
    public static final int CONTROL_OR               = 1;  // 0001
    public static final int CONTROL_ADD              = 2;  // 0010
    public static final int CONTROL_SUBTRACT         = 6;  // 0110
    public static final int CONTROL_SET_ON_LESS_THAN = 7;  // 0111
    public static final int CONTROL_NOR              = 12; // 1100

    private static String getControlName(int control) {
        switch ( control ) {
            case CONTROL_ADD:
                return "ADD";
            case CONTROL_AND:
                return "AND";
            case CONTROL_NOR:
                return "NOR";
            case CONTROL_OR:
                return "OR";
            case CONTROL_SET_ON_LESS_THAN:
                return "SET_ON_LESS_THAN";
            case CONTROL_SUBTRACT:
                return "SUBTRACT";
        }
        return "UNKNOWN";
    }

    private String name;

    public ALUComponent(String name) {
        this.name = name;
    }

    private int control;

    private int opA;
    private int opB;
    private int result;

    public int getControl() {
        return control;
    }

    public void setControl(Consumer<String> logger, int control) {
        logger.accept(String.format("%s: Setting 0x%s as control signal ( operation %s )", name, Integer.toHexString(control), getControlName(control)));

        this.control = control;
    }

    public int getResult(Consumer<String> logger) {
        switch ( control ) {
            case CONTROL_ADD:
                result = opA + opB;
                logger.accept(String.format("%s: Calculating %s + %s, result is %s", name, "0x" + Integer.toHexString(opA), "0x" + Integer.toHexString(opB), "0x" + Integer.toHexString(result)));
                break;
            case CONTROL_AND:
                result = opA & opB;
                logger.accept(String.format("%s: Calculating band(%s,%s), result is %s", name, "0x" + Integer.toHexString(opA), "0x" + Integer.toHexString(opB), "0x" + Integer.toHexString(result)));
                break;
            case CONTROL_OR:
                result = opA | opB;
                logger.accept(String.format("%s: Calculating bor(%s,%s), result is %s", name, "0x" + Integer.toHexString(opA), "0x" + Integer.toHexString(opB), "0x" + Integer.toHexString(result)));
                break;
            case CONTROL_SUBTRACT:
                result = opA - opB;
                logger.accept(String.format("%s: Calculating %s - %s, result is %s", name, "0x" + Integer.toHexString(opA), "0x" + Integer.toHexString(opB), "0x" + Integer.toHexString(result)));
                break;
            case CONTROL_SET_ON_LESS_THAN:
                //todo ovo provjeriti, mislim da postavlja 1 ako je opA < opB, 0 otherwise
                result = opA < opB ? 1 : 0;
                logger.accept(String.format("%s: Calculating slt(%s,%s), result is %s", name, "0x" + Integer.toHexString(opA), "0x" + Integer.toHexString(opB), "0x" + Integer.toHexString(result)));
                break;
            case CONTROL_NOR:
                result = ~(opA | opB);
                logger.accept(String.format("%s: Calculating nor(%s,%s), result is %s", name, "0x" + Integer.toHexString(opA), "0x" + Integer.toHexString(opB), "0x" + Integer.toHexString(result)));
                break;
        }

        return result;
    }

    public int getZero() {
        return result == 0 ? 1 : 0;
    }

    public void setOpA(Consumer<String> logger, int opA) {
        logger.accept(String.format("%s: Setting 0x%s as operand 0", name, Integer.toHexString(opA)));
        this.opA = opA;
    }

    public void setOpB(Consumer<String> logger, int opB) {
        logger.accept(String.format("%s: Setting 0x%s as operand 1", name, Integer.toHexString(opB)));

        this.opB = opB;
    }
}

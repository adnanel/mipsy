package mipsy.core.components;

import java.util.function.Consumer;

/**
 * Created by Adnan on 3/31/2017.
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

    public void setControl(int control) {
        this.control = control;
    }

    public int getResult() {
        return result;
    }

    public void setOpA(int opA) {
        this.opA = opA;
    }

    public void setOpB(int opB) {
        this.opB = opB;
    }

    public void execute(Consumer<String> logger) {
        switch ( control ) {
            case CONTROL_ADD:
                result = opA + opB;
                logger.accept(String.format("%s: Calculating %d + %d, result is %d", name, opA, opB, result));
                break;
            case CONTROL_AND:
                result = opA & opB;
                logger.accept(String.format("%s: Calculating band(%d,%d), result is %d", name, opA, opB, result));
                break;
            case CONTROL_OR:
                result = opA | opB;
                logger.accept(String.format("%s: Calculating bor(%d,%d), result is %d", name, opA, opB, result));
                break;
            case CONTROL_SUBTRACT:
                result = opA - opB;
                logger.accept(String.format("%s: Calculating %d - %d, result is %d", name, opA, opB, result));
                break;
            case CONTROL_SET_ON_LESS_THAN:
                //todo ovo provjeriti, mislim da postavlja 1 ako je opA < opB, 0 otherwise
                result = opA < opB ? 1 : 0;
                logger.accept(String.format("%s: Calculating slt(%d,%d), result is %d", name, opA, opB, result));
                break;
            case CONTROL_NOR:
                result = ~(opA | opB);
                logger.accept(String.format("%s: Calculating nor(%d,%d), result is %d", name, opA, opB, result));
                break;
        }
    }
}

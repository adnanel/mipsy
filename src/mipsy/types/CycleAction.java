package mipsy.types;

/**
 * Created by prg01 on 7/6/2017.
 */
public class CycleAction {
    public int cycle;
    public String action;

    public CycleAction(int cycle, String action) {
        this.cycle = cycle;
        this.action = action;
    }

    public CycleAction(int cycle) {
        this.cycle = cycle;
        this.action = "-";
    }
}

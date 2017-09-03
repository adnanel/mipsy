package mipsy.types;

import mipsy.core.dataphases.DataPhase;

import java.util.ArrayList;

public class InstructionLifeCycle {
    public Instruction instruction;
    public int ifCycle;
    public int idCycle;
    public int exCycle;
    public int memCycle;
    public int wbCycle;

    public InstructionLifeCycle(Instruction instruction, ArrayList<ArrayList<CycleAction>> actions) {
        this.instruction = instruction;
        this.ifCycle = -1;
        this.idCycle = -1;
        this.exCycle = -1;
        this.memCycle = -1;
        this.wbCycle = -1;

        for ( int i = 0; i < actions.size(); ++ i ) {
            if ( ifCycle != -1 && idCycle != -1 && exCycle != -1 && memCycle != -1 && wbCycle != -1 ) break;

            for ( CycleAction action : actions.get(i) ) {
                if ( action.processedInstruction == this.instruction ) {
                    if ( action.action.equals(DataPhase.PhaseNames.IF) ) {
                        ifCycle = i;
                        break;
                    } else if ( action.action.equals(DataPhase.PhaseNames.ID) ) {
                        idCycle = i;
                        break;
                    } else if ( action.action.equals(DataPhase.PhaseNames.EX) ) {
                        exCycle = i;
                        break;
                    } else if ( action.action.equals(DataPhase.PhaseNames.MEM) ) {
                        memCycle = i;
                        break;
                    } else if ( action.action.equals(DataPhase.PhaseNames.WB) ) {
                        wbCycle = i;
                        break;
                    }
                }
            }
        }
    }

    public boolean isInvalid() {
        return this.ifCycle < 0 || this.idCycle < 0 || this.exCycle < 0 || this.memCycle < 0 || this.wbCycle < 0;
    }
}

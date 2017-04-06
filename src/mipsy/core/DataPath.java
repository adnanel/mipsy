package mipsy.core;

import mipsy.core.dataphases.ID;
import mipsy.core.dataphases.IF;
import mipsy.core.dataphases.MEM;
import mipsy.core.dataphases.EX;
import mipsy.core.dataphases.WB;
import mipsy.types.Instruction;
import mipsy.types.NoMoreInstructionsException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * Created by adnan on 06.04.2017..
 */
public class DataPath {
    private IF IF;
    private ID ID;
    private MEM MEM;
    private EX EX;
    private WB WB;

    private MIPSCore core;

    private boolean doCycle = false;
    private boolean finished = false;
    private boolean disposed = false;

    private Lock waitLock = new ReentrantLock();
    private int waiters = 0;

    private Lock finishedLock = new ReentrantLock();
    private int finishedWorkersCount = 5;

    //vraca true ako je interrupted
    private boolean waitForCycle() {
        waitLock.lock();
        waiters++;
        waitLock.unlock();

        boolean res = false;
        while ( !doCycle ) {
            try {
                Thread.sleep(500);
            } catch ( Exception ex ) { res = true; break;}
        }
        waitLock.lock();
        waiters--;
        waitLock.unlock();
        return res;
    }

    Consumer<String> logger;

    public void dispose() {
        disposed = true;
        ifThread.interrupt();
        idThread.interrupt();
        exThread.interrupt();
        memThread.interrupt();
        wbThread.interrupt();
    }

    private Thread ifThread = new Thread() {
        @Override
        public void run() {
            int n = 0;
            this.setPriority(Thread.MIN_PRIORITY);

            while ( !disposed ) {
                if ( waitForCycle() ) break;

                try {
                    IF.step(logger);
                } catch ( NoMoreInstructionsException ex ) {
                    n++;
                    if ( n >= 5 )
                        finished = true;
                }


                finishWorker();
            }
        }
    };

    private Thread idThread = new Thread() {
        @Override
        public void run() {
            while ( !disposed ) {
                if ( waitForCycle() ) break;

                try {
                    ID.step(logger);
                } catch (NoMoreInstructionsException e) {
                    e.printStackTrace();
                }


                finishWorker();
            }
        }
    };


    private Thread exThread = new Thread() {
        @Override
        public void run() {
            while ( !disposed ) {
                if ( waitForCycle() ) break;

                try {
                    EX.step(logger);
                } catch (NoMoreInstructionsException e) {
                    e.printStackTrace();
                }


                finishWorker();
            }
        }
    };


    private Thread memThread = new Thread() {
        @Override
        public void run() {
            while ( !disposed ) {
                if ( waitForCycle() ) break;

                try {
                    MEM.step(logger);
                } catch (NoMoreInstructionsException e) {
                    e.printStackTrace();
                }
                finishWorker();
            }
        }
    };


    private Thread wbThread = new Thread() {
        @Override
        public void run() {
            while ( !disposed ) {
                if ( waitForCycle() ) break;

                try {
                    WB.step(logger);
                } catch (NoMoreInstructionsException e) {
                    e.printStackTrace();
                }

                finishWorker();
            }
        }
    };

    public DataPath(MIPSCore core, mipsy.core.dataphases.IF IF, mipsy.core.dataphases.ID ID, mipsy.core.dataphases.MEM MEM, mipsy.core.dataphases.EX EX, mipsy.core.dataphases.WB WB) {
        this.core = core;
        this.IF = IF;
        this.ID = ID;
        this.MEM = MEM;
        this.EX = EX;
        this.WB = WB;

        ifThread.start();
        idThread.start();
        exThread.start();
        memThread.start();
        wbThread.start();

    }

    private void finishWorker() {
        finishedLock.lock();
        finishedWorkersCount++;
        if ( finishedWorkersCount == 5 ) doCycle = false;
        finishedLock.unlock();
    }

    private void waitForWorkersToFinish() {
        while ( finishedWorkersCount != 5 ) {
            try {
                Thread.sleep(200);
            } catch ( Exception ex ) {

            }
        }
    }

    public void step(Consumer<String> logger) throws NoMoreInstructionsException {
        this.logger = logger;
        if ( finished ) throw new NoMoreInstructionsException();

        waitForWorkersToFinish();
        finishedWorkersCount = 0;

        doCycle = true;

        waitForWorkersToFinish();
    }
}

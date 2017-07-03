package mipsy;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import mipsy.core.MIPSCore;
import mipsy.types.*;
import mipsy.ui.PCLineNumberFactory;
import mipsy.ui.listviewcells.ListViewCellMemory;
import mipsy.ui.listviewcells.ListViewCellRegister;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.IntFunction;

public class MainController implements Initializable {
    private MIPSCore mipsCore;

    @FXML
    public Button btStep;
    @FXML
    public Button btRun;
    @FXML
    public Button btReset;
    @FXML
    public Button btRunTests;

    @FXML
    public Label topText;

    @FXML
    private ListView lvRegisters;

    @FXML
    private TextField tfMemFrom;
    @FXML
    private TextField tfMemTo;


    @FXML
    private ListView lvMem1;
    @FXML
    private ListView lvMem2;
    @FXML
    private ListView lvMem3;

    @FXML
    private TextArea taLog;

    @FXML
    private CodeArea taCode;

    private Consumer<String> logger = new Consumer<String>() {
        Lock lock = new ReentrantLock();

        StringBuilder sb = new StringBuilder();

        @Override
        public void accept(String s) {
            lock.lock();
            try {
                Date date = new Date();
                String timestamp = String.format("%02d:%02d:%02d: ", date.getHours(), date.getMinutes(), date.getSeconds());
                sb.append(timestamp).append(s).append('\n');
                taLog.setText(sb.toString());
                taLog.positionCaret(taLog.getText().length());
            } finally {
                lock.unlock();
            }
        }
    };

    private void fillMemory(int from, int to, HashMap<Integer, MemoryEntry> memory) {
        List<MemoryEntry> mem1 = new ArrayList<>();
        List<MemoryEntry> mem2 = new ArrayList<>();
        List<MemoryEntry> mem3 = new ArrayList<>();
        List<MemoryEntry>[] arr = new List[]{mem1, mem2, mem3};

        for ( int i = from, k = 0; i <= to; ++ i, ++ k ) {
            k = k % 3;

            MemoryEntry entry;
            if ( memory.containsKey(i) )
                entry = memory.get(i);
            else
                entry = new MemoryEntry(i, 0);

            arr[k].add(entry);
        }

        lvMem1.setItems(FXCollections.observableList(mem1));
        lvMem2.setItems(FXCollections.observableList(mem2));
        lvMem3.setItems(FXCollections.observableList(mem3));

        lvMem1.setCellFactory(listView -> new ListViewCellMemory(memory));
        lvMem2.setCellFactory(listView -> new ListViewCellMemory(memory));
        lvMem3.setCellFactory(listView -> new ListViewCellMemory(memory));
    }

    private void fillCode(List<Instruction> code) {
        StringBuilder sb = new StringBuilder();
        for ( Instruction i : code ) {
            sb.append(i).append('\n');
        }

        taCode.replaceText(sb.toString());
    }

    private void fillRegisters(HashMap<String, Register> registers) {
        List<Register> reg = new ArrayList<>(registers.values());
        lvRegisters.setItems(FXCollections.observableList(reg));
        lvRegisters.setCellFactory(listView -> new ListViewCellRegister());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the 32 MIPS registers...
        List<Register> registers = Register.makeEmptyRegisters();

        //Initialize our MIPS...
        mipsCore = new MIPSCore(registers, new HashMap<>());

        fillRegisters(mipsCore.registers);
        taCode.replaceText("add $s1, $s2, $s3\nadd $s4, $s5, $s6\nhalt\n");

        IntFunction<Node> numberFactory = PCLineNumberFactory.get(taCode);
        IntFunction<Node> graphicFactory = line -> {
            HBox hbox = new HBox(
                    numberFactory.apply(line));
            hbox.setAlignment(Pos.CENTER_LEFT);
            return hbox;
        };

        taCode.setParagraphGraphicFactory(graphicFactory);

        initParallelScroll();
    }

    private static ScrollBar getListViewScrollBar(ListView src) {
        Node n = src.lookup(".scroll-bar");
        if (n instanceof ScrollBar) {
            return (ScrollBar)n;
        }
        return null;
    }
    private static void bindScrollBars(ListView... targets) {
        Platform.runLater(() -> {
            ArrayList<ScrollBar> bars = new ArrayList<>(targets.length);
            for ( ListView lv : targets ) bars.add(getListViewScrollBar(lv));

            for ( ScrollBar sb : bars ) {
                for ( ScrollBar other : bars ) {
                    if ( other == sb ) continue;

                    sb.valueProperty().bindBidirectional(other.valueProperty());
                }
            }
        });
    }
    private void initParallelScroll() {
        bindScrollBars(lvMem1, lvMem2, lvMem3);
    }

    private int getMemTo() {
        try {
            return Integer.parseInt( tfMemTo.getText() );
        } catch ( Exception ex ) {
            tfMemTo.setText("0");
            return 0;
        }
    }

    private int getMemFrom() {
        try {
            return Integer.parseInt( tfMemFrom.getText() );
        } catch ( Exception ex ) {
            tfMemFrom.setText("0");
            return 0;
        }
    }

    private void calculateCPI() {
        //todo fix the CPI calculation. Currently halt makes it completely wrong most of the time.
        //logger.accept(String.format("Instructions executed (incl. halt): %d, Total Cycles: %d, CPI: %.2f",
        //        mipsCore.instructions.size(), mipsCore.getCycleCount(), ((float)mipsCore.getCycleCount() / mipsCore.instructions.size())));
    }

    @FXML
    protected void memRangeShow(ActionEvent event) {
        fillMemory(getMemFrom(), getMemTo(), mipsCore.memory);
    }

    @FXML
    protected void menuAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("MIPSy " + AppInfo.VERSION);
        alert.setContentText(
                "a MIPS Simulator\n\n" +
                "Authors:\n\n" +
                "Ena Hajdarević ( ehajdarevi2@etf.unsa.ba )\n" +
                "Adnan Elezović ( aelezovic2@etf.unsa.ba )\n" +
                "Haris Halilović ( hhalilovic1@etf.unsa.ba )");

        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });
    }


    @FXML
    protected boolean toolbarStepMIPS(ActionEvent event) {
        setInputLock(true);
        try {
            mipsCore.instructions = Instruction.parseInstructions(taCode.getText());
        } catch ( FailedToParseInstructionException ex ) {
            logger.accept("Failed to parse at line " + ex.line + " (" + ex.lineText + ")");
            return false;
        }

        boolean result = true;
        try {
            mipsCore.step(logger, true);
        } catch ( NoMoreInstructionsException ex ) {
            logger.accept("No more instructions left to step!");
            result = false;
            btRun.setDisable(true);
            btStep.setDisable(true);
            calculateCPI();
        }

        fillRegisters(mipsCore.registers);
        return result;
    }

    private void setInputLock(boolean lock) {
        taCode.setDisable(lock);
        lvRegisters.setDisable(lock);
        lvMem1.setDisable(lock);
        lvMem2.setDisable(lock);
        lvMem2.setDisable(lock);
    }

    @FXML
    protected void toolbarResetMIPS(ActionEvent event) {
        setInputLock(false);

        mipsCore = new MIPSCore(mipsCore.registers, mipsCore.memory, mipsCore.instructions);

        taLog.setText("");

        logger.accept("MIPSY RESET - PC SET TO 0");

        btRun.setDisable(false);
        btStep.setDisable(false);
    }

    @FXML
    protected void toolbarRunMIPS(ActionEvent event) {
        setInputLock(true);

        btRun.setDisable(true);
        btStep.setDisable(true);

        try {
            mipsCore.instructions = Instruction.parseInstructions(taCode.getText());


            taLog.setText("");
            taLog.setScrollTop(9999999);
            try {
                logger.accept("MIPS - RUN BEGIN");
                mipsCore.step(logger, false);

                calculateCPI();
            } catch ( Exception ex ) {
                logger.accept("MIPS - RUN FINISHED" );

            }
            fillRegisters(mipsCore.registers);

        } catch ( FailedToParseInstructionException ex ) {
            logger.accept("Failed to parse at line " + ex.line + " (" + ex.lineText + ")");
        } finally {

            setInputLock(false);
        }

    }

    @FXML
    protected void memScroll(ScrollEvent event) {

    }

    @FXML
    protected void menuOpenFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open MIPSy project file");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MIPSy project file", "*.mipsy"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File f = fileChooser.showOpenDialog(Main.PrimaryStage);

        if ( f != null && f.exists() ) {
            MipsyProject project = MipsyProject.loadFile(f.getAbsolutePath(), logger );

            mipsCore = new MIPSCore(project.registers, project.memory, project.instructions);

            fillCode(project.instructions);
            fillRegisters(mipsCore.registers);

            toolbarResetMIPS(event);
        }
    }

    private void runTestFile(File srcFile, File resFile) throws FileNotFoundException, TestFailedException {
        if ( srcFile == null || !srcFile.exists() ) throw new FileNotFoundException("SrcFile not found!");
        if ( resFile == null || !resFile.exists() ) throw new FileNotFoundException("ResFile not found!");

        MipsyProject project = MipsyProject.loadFile(srcFile.getAbsolutePath(), logger );

        mipsCore = new MIPSCore(project.registers, project.memory, project.instructions);

        fillCode(project.instructions);
        fillRegisters(mipsCore.registers);

        toolbarResetMIPS(null);
        toolbarRunMIPS(null);


        //read the out file
        MipsyProject out = MipsyProject.loadFile(resFile.getAbsolutePath(), logger);

        for ( Map.Entry<String,Register> r : out.registers.entrySet() ) {
            Register evaluated = mipsCore.registers.get(r.getKey());

            if ( evaluated.value != r.getValue().value ) {
                throw new TestFailedException(String.format("Test failed! Value in register %s is expected to be %s, found %s",
                        r.getKey(), r.getValue().value, evaluated.value));
            }
        }
    }

    @FXML
    protected void menuRunTests(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select the directory containing the tests");

        File f = directoryChooser.showDialog(Main.PrimaryStage);

        if ( f != null && f.exists() && f.isDirectory() ) {
            int i = 1;
            try {
                while ( true ) {
                    File src = new File(f, "in" + i + ".mipsy");
                    File testOut = new File(f, "out" + i + ".txt");

                    if ( src.exists() && testOut.exists() ) runTestFile(src, testOut);
                    else break;

                    ++i;
                }
            } catch ( Exception ex ) {
                ex.printStackTrace();
                logger.accept(String.format("Test %d failed, inner exception: " + ex.getMessage(), i) );
                return;
            }
            toolbarResetMIPS(null);
            logger.accept(String.format("-- ALL %d TESTS PASSED --", i));
        }
    }
}

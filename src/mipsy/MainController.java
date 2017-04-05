package mipsy;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import mipsy.core.MIPSCore;
import mipsy.types.Instruction;
import mipsy.types.MemoryEntry;
import mipsy.types.MipsyProject;
import mipsy.types.Register;
import mipsy.ui.listviewcells.ListViewCellMemory;
import mipsy.ui.listviewcells.ListViewCellRegister;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class MainController implements Initializable {
    private MIPSCore mipsCore;

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
    private TextArea taCode;

    Consumer<String> logger = new Consumer<String>() {
        @Override
        public void accept(String s) {
            Date date = new Date();
            String timestamp = String.format("%02d:%02d:%02d: ", date.getHours(), date.getMinutes(), date.getSeconds());
            taLog.setText(taLog.getText() + timestamp + s + "\n");
            taLog.positionCaret(taLog.getText().length());
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

        taCode.setText(sb.toString());
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
    protected void toolbarStepMIPS(ActionEvent event) {
        setInputLock(true);

        mipsCore.instructions = Instruction.parseInstructions(taCode.getText());

        mipsCore.step(logger);

        fillRegisters(mipsCore.registers);
    }

    protected void setInputLock(boolean lock) {
        taCode.setDisable(lock);
        lvRegisters.setDisable(lock);
        lvMem1.setDisable(lock);
        lvMem2.setDisable(lock);
        lvMem2.setDisable(lock);
    }

    @FXML
    protected void toolbarResetMIPS(ActionEvent event) {
        setInputLock(false);

        mipsCore.reset();
        taLog.setText("");

        logger.accept("MIPSY RESET - PC SET TO 0");
    }

    @FXML
    protected void toolbarRunMIPS(ActionEvent event) {
        setInputLock(true);

        mipsCore.instructions = Instruction.parseInstructions(taCode.getText());

        taLog.setText(taLog.getText() + "\n\n\n\n\n\n\n\n\n");
        taLog.setScrollTop(9999999);
        try {
            logger.accept("MIPS - RUN BEGIN");
            while ( true ) mipsCore.step(logger);
        } catch ( Exception ex ) {
            logger.accept("MIPS - RUN FINISHED" );
        }
        fillRegisters(mipsCore.registers);

        setInputLock(false);
    }

    @FXML
    protected void memScroll(ActionEvent event) {
        // todo probaj nekako da se sva tri LVa skrolaju paralelno...
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
}

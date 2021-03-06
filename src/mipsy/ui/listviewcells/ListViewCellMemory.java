package mipsy.ui.listviewcells;

import javafx.scene.control.ListCell;
import mipsy.types.MemoryEntry;
import mipsy.ui.MemoryCell;

import java.util.HashMap;

/**
 * Created on 3/30/2017.
 */
public class ListViewCellMemory extends ListCell<MemoryEntry>
{

    private HashMap<Integer, MemoryEntry> memory;

    public ListViewCellMemory(HashMap<Integer, MemoryEntry> memory) {
        this.memory = memory;
    }

    @Override
    public void updateItem(MemoryEntry entry, boolean empty)
    {
        super.updateItem(entry,empty);
        if(entry != null)
        {
            MemoryCell data = new MemoryCell(memory, entry.address, false);

            setGraphic(data.getBox());
        }
    }
}
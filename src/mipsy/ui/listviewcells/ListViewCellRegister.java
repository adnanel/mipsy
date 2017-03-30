package mipsy.ui.listviewcells;

import javafx.scene.control.ListCell;
import mipsy.ui.RegisterCell;

/**
 * Created by Adnan on 3/30/2017.
 */
public class ListViewCellRegister extends ListCell<String>
{
    @Override
    public void updateItem(String string, boolean empty)
    {
        super.updateItem(string,empty);
        if(string != null)
        {
            RegisterCell data = new RegisterCell();

            data.setLabel(string);

            setGraphic(data.getBox());
        }
    }
}
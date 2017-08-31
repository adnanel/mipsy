package mipsy.ui.listviewcells;

import javafx.scene.control.ListCell;
import mipsy.types.Register;
import mipsy.ui.RegisterCell;

/**
 * Created on 3/30/2017.
 */
public class ListViewCellRegister extends ListCell<Register>
{
    @Override
    public void updateItem(Register register, boolean empty)
    {
        super.updateItem(register,empty);
        if(register != null)
        {
            boolean readOnly = register.name.equalsIgnoreCase("$zero");

            RegisterCell data = new RegisterCell(register, readOnly);

            setGraphic(data.getBox());
        }
    }
}
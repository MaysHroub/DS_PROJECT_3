package layout;

import data.MDate;
import dataholder.DataHolder;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;

public class ModifyDateLayout extends TabLayout {
	
	private DatePicker datePicker;
	private ComboBox<MDate> datesCB;
	private Button insertBtn, deleteBtn, updateBtn, printBtn, upBtn, downBtn;

	public ModifyDateLayout(DataHolder dataHolder) {
		super("", dataHolder);
	}

	@Override
	protected Pane createLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

}

package layout;

import data.MDate;
import dataholder.DataHolder;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
		Label insertL = new Label("Pick a date: "),
				chooseL = new Label("Choose a date: ");
		return null;
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

}
















package layout;

import data.MDate;
import dataholder.DataHolder;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

public class ModifyDateLayout extends TabLayout {
	
	private DatePicker datePicker;
	private ComboBox<MDate> datesCB;
	private Button insertBtn, deleteBtn, updateBtn, printBtn;
	private TableView<MDate> datesTable;
	private Alert alert;

	public ModifyDateLayout(DataHolder dataHolder) {
		super("", dataHolder);
	}

	@Override
	protected Pane createLayout() {
		Label insertL = new Label("Pick a date: "),
				chooseL = new Label("Choose a date: ");
		insertBtn = new Button("Insert");
		deleteBtn = new Button("Delete");
		updateBtn = new Button("Update");
		printBtn = new Button("Print");
		datesCB = new ComboBox<>();
		datesTable = new TableView<>();
		
		return null;
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

}
















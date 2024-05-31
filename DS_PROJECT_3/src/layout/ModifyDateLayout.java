package layout;

import data.MDate;
import dataholder.DataHolder;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ModifyDateLayout extends TabLayout {
	
	private DatePicker datePicker;
	private ComboBox<MDate> datesCB;
	private Button insertBtn, deleteBtn, updateBtn, printBtn;
	private TableView<MDate> datesTable;
	private Alert alert;
	private Label statusL;

	public ModifyDateLayout(DataHolder dataHolder) {
		super("", dataHolder);
	}

	@Override
	protected Pane createLayout() {
		Label pickNewL = new Label("Pick a new date: "),
				selectL = new Label("Select a date: ");
		statusL = new Label();
		insertBtn = new Button("Insert");
		deleteBtn = new Button("Delete");
		updateBtn = new Button("Update");
		printBtn = new Button("Print");
		datesCB = new ComboBox<>();
		alert = new Alert(AlertType.CONFIRMATION);
		datesTable = new TableView<>();
		
		fillDatesCB();
		
		alert.setTitle("Confirmation required");
		alert.setHeaderText("Are you sure you want to proceed?");
		alert.setContentText("This action cannot be undone :)");
		
		
		
		GridPane upGrid = new GridPane(10, 10);
		upGrid.add(pickNewL, 0, 0);
		upGrid.add(datePicker, 1, 0);
		upGrid.add(selectL, 0, 1);
		upGrid.add(datesCB, 0, 1);
		
		GridPane downGrid = new GridPane(10, 10);
		downGrid.add(insertBtn, 0, 0);
		downGrid.add(updateBtn, 1, 0);
		downGrid.add(deleteBtn, 2, 0);
		downGrid.add(statusL, 1, 1);
		
		VBox rightBox = new VBox(15, datesTable, printBtn);
		
		VBox leftBox = new VBox(20, upGrid, downGrid);
		
		BorderPane layout = new BorderPane();
		layout.setLeft(leftBox);
		layout.setRight(rightBox);
		
		return layout;
	}

	private void fillDatesCB() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

}
















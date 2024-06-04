package layout;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import data.MDate;
import data.MDateStat;
import dataholder.DataHolder;
import hash.Flag;
import hash.HNode;
import hash.QuadraticOHash;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ModifyDateLayout extends TabLayout {
	
	private DatePicker datePicker;
	private ComboBox<MDate> datesCB;
	private Button insertBtn, deleteBtn, updateBtn, printBtn;
	private TableView<HNode<MDate>> datesTable;
	private Alert alert;
	private Label statusL;
	
	ObservableList<HNode<MDate>> dateList;

	public ModifyDateLayout(DataHolder dataHolder) {
		super("Modify Date", dataHolder);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Pane createLayout() {
		Label pickNewL = new Label("Pick a new date: "),
				selectL = new Label("Select a date: ");
		statusL = new Label();
		
		insertBtn = new Button("Insert");
		insertBtn.setOnAction(e -> insert());
		deleteBtn = new Button("Delete");
		deleteBtn.setOnAction(e -> delete());
		updateBtn = new Button("Update");
		updateBtn.setOnAction(e -> update());
		printBtn = new Button("Print");
		printBtn.setOnAction(e -> print());
		
		datesCB = new ComboBox<>();
		fillDatesCB();
		
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation required");
		alert.setHeaderText("Are you sure you want to proceed?");
		alert.setContentText("This action cannot be undone :)");
		
		datePicker = new DatePicker();
		
		datesTable = new TableView<>();
		TableColumn<HNode<MDate>, String> dateColumn = new TableColumn<>("Date");
		TableColumn<HNode<MDate>, String> flagColumn = new TableColumn<>("Flag");
		dateColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getData().toString()));
		flagColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getFlag().toString()));
		datesTable.getColumns().addAll(dateColumn, flagColumn);
		datesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
		
		dateList = FXCollections.observableArrayList();
		
		datesTable.setItems(dateList);
		
		GridPane upGrid = new GridPane(15, 20);
		upGrid.add(pickNewL, 0, 0);
		upGrid.add(datePicker, 1, 0);
		upGrid.add(selectL, 0, 1);
		upGrid.add(datesCB, 1, 1);
		upGrid.setAlignment(Pos.CENTER);
		
		GridPane downGrid = new GridPane(10, 10);
		downGrid.add(insertBtn, 0, 0);
		downGrid.add(updateBtn, 1, 0);
		downGrid.add(deleteBtn, 2, 0);
		downGrid.setAlignment(Pos.CENTER);
		
		VBox rightBox = new VBox(15, datesTable, printBtn);
		rightBox.setAlignment(Pos.CENTER);
		
		VBox leftBox = new VBox(20, upGrid, downGrid, statusL);
		leftBox.setAlignment(Pos.CENTER);
		
		BorderPane layout = new BorderPane();
		layout.setLeft(leftBox);
		layout.setRight(rightBox);
		layout.setPadding(new Insets(20));
		
		return layout;
	}
	
	private void print() {
		dateList.clear();
		QuadraticOHash<MDate> dates = getDataHolder().getDates();
		for (int i = 0; i < dates.getTableSize(); i++)
			if (dates.get(i).getFlag() != Flag.EMPTY)
				dateList.add(dates.get(i));
		datesTable.refresh();
	}

	private void update() {
		MDate selectedDate = datesCB.getValue();
		LocalDate newDate = datePicker.getValue();
		if (selectedDate == null || newDate == null) {
			statusL.setText("No date is selected");
			return;
		}
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			@SuppressWarnings("deprecation")
			Date date = new Date(newDate.getYear() - 1900, newDate.getMonthValue() - 1, newDate.getDayOfMonth());
			getDataHolder().getDates().delete(selectedDate);
			selectedDate.setDate(date);
			getDataHolder().getDates().add(selectedDate);
			statusL.setText("Date is updated to " + selectedDate);
		}
	}

	private void delete() {
		MDate selectedDate = datesCB.getValue();
		if (selectedDate == null) {
			statusL.setText("No date is selected");
			return;
		}
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			getDataHolder().getDates().delete(selectedDate);
			datesCB.getItems().remove(selectedDate);
			statusL.setText("Date " + selectedDate + " is deleted");
		}
	}

	private void insert() {
		LocalDate selectedDate = datePicker.getValue();
		if (selectedDate == null) {
			statusL.setText("No date is selected");
			return;
		}
		@SuppressWarnings("deprecation")
		Date date = new Date(selectedDate.getYear() - 1900, selectedDate.getMonthValue() - 1, selectedDate.getDayOfMonth());
		MDate mDate = new MDate(date);
		if (getDataHolder().getDates().find(mDate) == null) {
			MDateStat stat = new MDateStat(mDate);
			mDate.setStat(stat);
			getDataHolder().getDates().add(mDate);
			fillDatesCB();
			statusL.setText("Date " + selectedDate + " is inserted");
		} else 
			statusL.setText("Date " + selectedDate + " already exists");
	}

	private void fillDatesCB() {
		QuadraticOHash<MDate> dates = getDataHolder().getDates();
		for (int i = 0; i < dates.getTableSize(); i++)
			if (dates.get(i).getFlag() == Flag.FULL)
				datesCB.getItems().add(dates.get(i).getData());
	}
	

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

}
















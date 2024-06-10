package rubbish;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import data.District;
import data.MDate;
import data.MDateStat;
import data.Martyr;
import data_structs.doublylinkedlist.DNode;
import data_structs.doublylinkedlist.DoublyLinkedList;
import data_structs.hash.Flag;
import data_structs.hash.HNode;
import data_structs.hash.QuadraticOHash;
import data_structs.linkedlist.Node;
import data_structs.queue.LinkedListQueue;
import data_structs.tree.TNode;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RestOfClasses {

}






class ModifyDateLayout extends TabLayout {
	
	private DatePicker datePicker;
	private ComboBox<MDate> datesCB;
	private Button insertBtn, deleteBtn, updateBtn, printBtn, printWithEmptyBtn;
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
		printWithEmptyBtn = new Button("Print Including Empty Spots");
		printWithEmptyBtn.setOnAction(e -> printWithEmpty());
		
		datesCB = new ComboBox<>();
		fillDatesCB();
		
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation required");
		alert.setHeaderText("Are you sure you want to proceed?");
		alert.setContentText("This action cannot be undone :)");
		
		datePicker = new DatePicker();
		datePicker.setEditable(false);
		
		datesTable = new TableView<>();
		TableColumn<HNode<MDate>, String> dateColumn = new TableColumn<>("Date");
		TableColumn<HNode<MDate>, String> flagColumn = new TableColumn<>("Flag");
		TableColumn<HNode<MDate>, Integer> indexColumn = new TableColumn<>("Index");
		dateColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().toString()));
		flagColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getFlag().toString()));
		indexColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getIndex()).asObject());
		datesTable.getColumns().addAll(indexColumn, dateColumn, flagColumn);
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
		
		VBox rightBox = new VBox(15, datesTable, printBtn, printWithEmptyBtn);
		rightBox.setAlignment(Pos.CENTER);
		
		VBox leftBox = new VBox(20, upGrid, downGrid, statusL);
		leftBox.setAlignment(Pos.CENTER);
		
		BorderPane layout = new BorderPane();
		layout.setCenter(leftBox);
		layout.setRight(rightBox);
		layout.setPadding(new Insets(20));
		
		return layout;
	}
	
	private void print() {
		dateList.clear();
		QuadraticOHash<MDate> dates = dataHolder.getDates();
		for (int i = 0; i < dates.getTableSize(); i++)
			if (dates.get(i).getFlag() != Flag.EMPTY)
				dateList.add(dates.get(i));
		datesTable.refresh();
	}
	
	private void printWithEmpty() {
		dateList.clear();
		QuadraticOHash<MDate> dates = dataHolder.getDates();
		for (int i = 0; i < dates.getTableSize(); i++)
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
			MDate mDate = new MDate(date);
			if (dataHolder.getDates().find(mDate) != null) {
				statusL.setText("Updating failed. " + mDate + " already exists");
				return;
			}
			mDate.setMartyrs(selectedDate.getMartyrs());
			mDate.setStat(selectedDate.getStat());
			dataHolder.getDates().delete(selectedDate);
			dataHolder.getDates().add(mDate);
			fillDatesCB();
			statusL.setText("Date is updated to " + mDate);
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
			dataHolder.getDates().delete(selectedDate);
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
		if (dataHolder.getDates().find(mDate) == null) {
			MDateStat stat = new MDateStat(mDate);
			mDate.setStat(stat);
			dataHolder.getDates().add(mDate);
			fillDatesCB();
			statusL.setText("Date " + selectedDate + " is inserted");
		} else 
			statusL.setText("Date " + selectedDate + " already exists");
	}

	private void fillDatesCB() {
		datesCB.getItems().clear();
		QuadraticOHash<MDate> dates = dataHolder.getDates();
		for (int i = 0; i < dates.getTableSize(); i++)
			if (dates.get(i).getFlag() == Flag.FULL)
				datesCB.getItems().add(dates.get(i).getData());
	}
	

	@Override
	public void updateContent() {
		statusL.setText("");
	}

}

class ModifyMartyrLayout extends TabLayout {
	
	private Label statusL, currentDateL;
	private TextField nameTF, ageTF, sizeTF, heightTF;
	private Button insertBtn, displaySizeHeightBtn, sortTableBtn, displayTableBtn, deleteBtn;
	private RadioButton maleRB, femaleRB;
	private ComboBox<District> districtsCB;
	private ComboBox<String> locationsCB;
	private TableView<Martyr> martyrsTable;
	private ObservableList<Martyr> martyrs;
	
	private Alert alert;
	
	
	public ModifyMartyrLayout(DataHolder dataHolder) {
		super("Modify Martyrs", dataHolder);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Pane createLayout() {
		martyrsTable = new TableView<>();
		martyrsTable.setEditable(true);
		TableColumn<Martyr, String> nameColumn = new TableColumn<>("Name");
		TableColumn<Martyr, Integer> ageColumn = new TableColumn<>("Age");
		TableColumn<Martyr, String> genderColumn = new TableColumn<>("Gender");
		TableColumn<Martyr, String> districtColumn = new TableColumn<>("District");
		TableColumn<Martyr, String> locationColumn = new TableColumn<>("Location");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Martyr, String>("Name"));
		nameColumn.setCellFactory(TextFieldTableCell.<Martyr> forTableColumn());
		nameColumn.setOnEditCommit((CellEditEvent<Martyr, String> t) -> {
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				Martyr m = ((Martyr) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if (dataHolder.getCurrentDate().getMartyrs().find(
						new Martyr(t.getNewValue(), m.getDistrict(), "", 'M', 0)) != null) {
					statusL.setText("Updating name failed. Martyr with this new name exist in the same district");
					martyrsTable.refresh();
					return;
				}
				dataHolder.getCurrentDate().getMartyrs().delete(m);
				m.setName(t.getNewValue());
				dataHolder.getCurrentDate().getMartyrs().insert(m);
				fillTableInOrder();
				statusL.setText("Martyr's name is updated");
			}
		});
		ageColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getAge()).asObject());
		genderColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getGender() + ""));
		districtColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getDistrict()));
		locationColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getLocation()));
		martyrsTable.getColumns().addAll(nameColumn, ageColumn, genderColumn, districtColumn, locationColumn);
		martyrsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
		
		martyrs = FXCollections.observableArrayList();
		martyrsTable.setItems(martyrs);
		
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation required");
		alert.setHeaderText("Are you sure you want to proceed?");
		alert.setContentText("This action cannot be undone :)");
		
		currentDateL = new Label();
		statusL = new Label();
		
		Label l1 = new Label("Enter martyr's name:"),
				l2 = new Label("Enter martyr's age:"),
				l3 = new Label("Select gender:"),
				l4 = new Label("Select District:"),
				l5 = new Label("Size:"),
				l6 = new Label("Height:"),
				l7 = new Label("Select Location:");
		
		nameTF = new TextField();
		ageTF = new TextField();
		sizeTF = new TextField();
		sizeTF.setPrefColumnCount(5);
		sizeTF.setEditable(false);
		heightTF = new TextField();
		heightTF.setPrefColumnCount(5);
		heightTF.setEditable(false);
		
		insertBtn = new Button("Insert");
		insertBtn.setOnAction(e -> insertMartyr());
		deleteBtn = new Button("Delete selected");
		deleteBtn.setOnAction(e -> deleteMartyr());
		displaySizeHeightBtn = new Button("Display tree's size and height");
		displaySizeHeightBtn.setOnAction(e -> {
			if (dataHolder.getCurrentDate() == null) return;
			sizeTF.setText(dataHolder.getCurrentDate().getMartyrs().size() + "");
			heightTF.setText(dataHolder.getCurrentDate().getMartyrs().height() + "");
		});
		sortTableBtn = new Button("Sort by age");
		sortTableBtn.setOnAction(e -> sortTableByAge());
		displayTableBtn = new Button("Level order");
		displayTableBtn.setOnAction(e -> fillTableLevelOrder());
		
		maleRB = new RadioButton("Male");
		femaleRB = new RadioButton("Female");
		ToggleGroup toggleGroup = new ToggleGroup();
		maleRB.setToggleGroup(toggleGroup);
		femaleRB.setToggleGroup(toggleGroup);
		
		districtsCB = new ComboBox<>();
		districtsCB.setOnAction(e -> {
			if (districtsCB.getValue() == null) return;
			locationsCB.getSelectionModel().clearSelection();
			fillLocationsCB(districtsCB.getValue());
		});
		locationsCB = new ComboBox<>();
		
		GridPane gp = new GridPane(15, 20);
		gp.add(l1, 0, 0);
		gp.add(nameTF, 1, 0);
		gp.add(insertBtn, 2, 0);
		gp.add(l2, 0, 1);
		gp.add(ageTF, 1, 1);
		gp.add(l3, 0, 2);
		gp.add(new HBox(15, maleRB, femaleRB), 1, 2);
		gp.add(l4, 0, 3);
		gp.add(districtsCB, 1, 3);
		gp.add(l7, 0, 4);
		gp.add(locationsCB, 1, 4);
		gp.add(displaySizeHeightBtn, 0, 5);
		gp.add(new HBox(25, l5, sizeTF), 0, 6);
		gp.add(new HBox(10, l6, heightTF), 0, 7);
		gp.setAlignment(Pos.CENTER);
		
		HBox hBox = new HBox(10, sortTableBtn, displayTableBtn, deleteBtn);
		hBox.setAlignment(Pos.CENTER);
		
		VBox vBox = new VBox(15, martyrsTable, hBox);
		vBox.setAlignment(Pos.CENTER);
		
		BorderPane layout = new BorderPane();
		layout.setTop(currentDateL);
		layout.setLeft(gp);
		layout.setCenter(vBox);
		layout.setBottom(statusL);
		layout.setPadding(new Insets(15));
		
		BorderPane.setAlignment(vBox, Pos.CENTER);
		BorderPane.setMargin(vBox, new Insets(0, 0, 0, 15));
		
		return layout;
	}
	
	private void insertMartyr() {
		if (dataHolder.getCurrentDate() == null) return;
		if (nameTF.getText().isEmpty() || ageTF.getText().isEmpty() ||
				districtsCB.getValue() == null || locationsCB.getValue() == null ||
				!maleRB.isSelected() && !femaleRB.isSelected()) {
			statusL.setText("No enough information :/");
			return;
		}
		if (!ageTF.getText().matches("[0-9]+")) {
			statusL.setText("You must enter a number for age text-field");
			return;
		}
		if (ageTF.getText().length() > 5) {
			statusL.setText("Invalid number for age. It must consist of at most 5 digits");
			return;
		}
		String name = nameTF.getText();
		int age = Integer.valueOf(ageTF.getText());
		char gender = (maleRB.isSelected()) ? 'M' : 'F';
		String district = districtsCB.getValue().toString(),
				location = locationsCB.getValue();
		Martyr martyr = new Martyr(name, district, location, gender, age);
		if (dataHolder.getCurrentDate().getMartyrs().find(martyr) != null) {
			statusL.setText("Martyr " + name + " already exists");
			return;
		}
		dataHolder.getCurrentDate().getMartyrs().insert(martyr);
		dataHolder.getCurrentDate().getStat().updateStats();
		martyrs.add(martyr);
		martyrsTable.refresh();
		statusL.setText("Martyr " + name + " is inserted :)");
	}
	
	private void deleteMartyr() {
		Martyr selectedMartyr = martyrsTable.getSelectionModel().getSelectedItem();
		if (selectedMartyr == null) {
			statusL.setText("No martyr is selected");
			return;
		}
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			dataHolder.getCurrentDate().getMartyrs().delete(selectedMartyr);
			martyrs.remove(selectedMartyr);
			martyrsTable.refresh();
			dataHolder.getCurrentDate().getStat().updateStats();
			statusL.setText("Martyr " + selectedMartyr.getName() + " is deleted");
		}
	}
	
	private void fillTableInOrder() {
		if (dataHolder.getCurrentDate() == null)
			return;
		martyrs.clear();
		TNode<Martyr> root = dataHolder.getCurrentDate().getMartyrs().getRoot();
		fillTableInOrder(root);
		martyrsTable.refresh();
	}
	
	private void fillTableInOrder(TNode<Martyr> curr) {
		if (curr == null) return;
		fillTableInOrder(curr.getLeft());
		martyrs.add(curr.getData());
		fillTableInOrder(curr.getRight());
	}
	
	private void fillTableLevelOrder() {
		if (dataHolder.getCurrentDate() == null)
			return;
		martyrs.clear();
		TNode<Martyr> root = dataHolder.getCurrentDate().getMartyrs().getRoot();
		if (root == null) return;
		LinkedListQueue<TNode<Martyr>> queue = new LinkedListQueue<>();
		queue.enqueue(root);
		while (!queue.isEmpty()) {
			TNode<Martyr> curr = queue.dequeue();
			martyrs.add(curr.getData());
			if (curr.hasRight()) queue.enqueue(curr.getRight());
			if (curr.hasLeft()) queue.enqueue(curr.getLeft());
		}
		martyrsTable.refresh();
	}
	
	private void sortTableByAge() {
		if (dataHolder.getCurrentDate() == null || 
				dataHolder.getCurrentDate().getMartyrs().size() == 0)
			return;
		Martyr[] arr = new Martyr[martyrs.size()+1];
		martyrs.toArray(arr);
		System.arraycopy(arr, 0, arr, 1, arr.length - 1);
		Martyr.heapSortAsc(arr);
		martyrs = FXCollections.observableArrayList(arr);
		martyrs.remove(0);
		martyrsTable.setItems(martyrs);
		martyrsTable.refresh();
	}

	@Override
	public void updateContent() {
		statusL.setText("");
		if (dataHolder.getCurrentDate() != null) {
			currentDateL.setText(dataHolder.getCurrentDate().toString());
			fillTableInOrder();
			fillDistrictsCB();
		}
		else {
			currentDateL.setText("");
			martyrs.clear();
			martyrsTable.refresh();
		}
	}
	
	private void fillDistrictsCB() {
		districtsCB.getItems().clear();
		DoublyLinkedList<District> districts = dataHolder.getDistricts();
		DNode<District> curr = districts.getHead();
		while (curr != null) {
			districtsCB.getItems().add(curr.getData());
			curr = curr.getNext();
		}
	} 
	
	private void fillLocationsCB(District district) {
		locationsCB.getItems().clear();
		Node<String> curr = district.getLocations().getHead();
		while (curr != null) {
			locationsCB.getItems().add(curr.getData());
			curr = curr.getNext();
		}
	}

}
class NavigateDateLayout extends TabLayout {
	
	private Label totalL, totalMalesL, totalFemalesL, avgAgesL,
					youngestMartyrL, oldestMartyrL,
					maxMartyrDistrictL, maxMartyrLocationL,
					currentDateL;
	private Button upBtn, downBtn;

	
	public NavigateDateLayout(DataHolder dataHolder) {
		super("Date Navigation", dataHolder);
	}

	@Override
	protected Pane createLayout() {
		Label l1 = new Label("Total number of Martyrs:"),
				l2 = new Label("Total number of Males:"),
				l3 = new Label("Total number of Females:"),
				l4 = new Label("Average ages:"),
				l5 = new Label("Youngest Martyr:"),
				l6 = new Label("Oldest Martyr:"),
				l7 = new Label("District with maximum martyrs:"),
				l8 = new Label("Location wiht maximum martyrs:");
		
		totalL = new Label();
		totalMalesL = new Label();
		totalFemalesL = new Label();
		avgAgesL = new Label();
		youngestMartyrL = new Label();
		oldestMartyrL = new Label();
		maxMartyrDistrictL = new Label();
		maxMartyrLocationL = new Label();
		currentDateL = new Label();
		
		upBtn = new Button("  UP ↑  ");
		upBtn.setOnAction(e -> moveUp());
		downBtn = new Button("DOWN ↓");
		downBtn.setOnAction(e -> moveDown());
		
		GridPane gp = new GridPane(15, 20);
		gp.add(l1, 0, 0);
		gp.add(totalL, 1, 0);
		gp.add(l2, 0, 1);
		gp.add(totalMalesL, 1, 1);
		gp.add(l3, 0, 2);
		gp.add(totalFemalesL, 1, 2);
		gp.add(l4, 0, 3);
		gp.add(avgAgesL, 1, 3);
		gp.add(l5, 0, 4);
		gp.add(youngestMartyrL, 1, 4);
		gp.add(l6, 0, 5);
		gp.add(oldestMartyrL, 1, 5);
		gp.add(l7, 0, 6);
		gp.add(maxMartyrDistrictL, 1, 6);
		gp.add(l8, 0, 7);
		gp.add(maxMartyrLocationL, 1, 7);
		gp.setAlignment(Pos.CENTER);
		
		VBox vBox = new VBox(10, upBtn, downBtn);
		vBox.setAlignment(Pos.CENTER);
		
		BorderPane layout = new BorderPane();
		layout.setTop(currentDateL);
		layout.setCenter(gp);
		layout.setBottom(vBox);
		layout.setPadding(new Insets(20));
		
		BorderPane.setAlignment(currentDateL, Pos.CENTER);
		
		return layout;
	}
	
	private void moveDown() {
		dataHolder.moveDown();
		fillLayoutWithData();
	}

	private void moveUp() {
		dataHolder.moveUp();
		fillLayoutWithData();
	}

	private void fillLayoutWithData() {
		if (dataHolder.getCurrentDate() == null ||
				dataHolder.getCurrentDate().getMartyrs().size() == 0) {
			if (dataHolder.getCurrentDate() != null)
				currentDateL.setText(dataHolder.getCurrentDate().toString());
			else 
				currentDateL.setText("");
			totalL.setText("");
			totalMalesL.setText("");
			totalFemalesL.setText("");
			avgAgesL.setText("");
			youngestMartyrL.setText("");
			oldestMartyrL.setText("");
			maxMartyrDistrictL.setText("");
			maxMartyrLocationL.setText("");
			return;
		}
		MDateStat stat = dataHolder.getCurrentDate().getStat();
		currentDateL.setText(dataHolder.getCurrentDate().toString());
		totalL.setText(stat.getTotalMartyrs() + "");
		totalMalesL.setText(stat.getTotalMales() + "");
		totalFemalesL.setText(stat.getTotalFemales() + "");
		avgAgesL.setText(stat.getAvgAges() + "");
		youngestMartyrL.setText(stat.getYoungest().toString());
		oldestMartyrL.setText(stat.getOldest().toString());
		maxMartyrDistrictL.setText(stat.getDistrictWithMaxMartyr());
		maxMartyrLocationL.setText(stat.getLocationWithMaxMartyr());
	}

	@Override
	public void updateContent() {
		fillLayoutWithData();
	}

}
class SaveDataLayout extends TabLayout {
	
	private TextField fileTF;
	private Button saveBtn;
	private Label saveL;

	public SaveDataLayout(DataHolder dataHolder) {
		super("Save data", dataHolder);
	}

	@Override
	protected Pane createLayout() {
		fileTF = new TextField();
		saveBtn = new Button("Save");
		saveL = new Label();
		
		fileTF.setPromptText("Enter file name/path");
		
		saveBtn.setOnAction(e -> saveDate());
		
		VBox vBox = new VBox(20, fileTF, saveBtn, saveL);
		vBox.setAlignment(Pos.CENTER);
		vBox.setPadding(new Insets(50));
		
		return vBox;
	}

	private void saveDate() {
		if (fileTF.getText() == null || fileTF.getText().length() == 0) return;
		
		try (PrintWriter out = new PrintWriter(fileTF.getText())) {
			writeDates(out);
			saveL.setText("All data is saved");
		} catch (IOException e) {
			System.out.println(e);
			saveL.setText("Couldn't save to file!");
		}
	}
	
	private void writeDates(PrintWriter out) {
		QuadraticOHash<MDate> dates = dataHolder.getDates();
		int m = dates.getTableSize();
		for (int i = 0; i < m; i++) {
			HNode<MDate> date = dates.get(i);
			if (date.getFlag() == Flag.FULL)
				writeMartyrs(date.getData().getMartyrs().getRoot(), date.getData().getDate(), out);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void writeMartyrs(TNode<Martyr> curr, Date date, PrintWriter out) {
		if (curr == null) return;
		// 0.name, 1.event, 2.age, 3.location, 4.district, 5.gender
		Martyr m = curr.getData();
		out.println(String.format("%s,%s,%d,%s,%s,%c",
				m.getName(),
				(date.getMonth() + 1) + "/" + 
				(date.getDate()) + "/" + 
				(date.getYear() + 1900),
				m.getAge(),
				m.getLocation(),
				m.getDistrict(),
				m.getGender()));
		writeMartyrs(curr.getLeft(), date, out);
		writeMartyrs(curr.getRight(), date, out);
	}

	@Override
	public void updateContent() { /* do nth */ }

}
abstract class TabLayout {
	
	private Pane layout;
	private String name;
	protected DataHolder dataHolder;
	
	protected TabLayout(String name, DataHolder dataHolder) {
		this.name = name;
		this.dataHolder = dataHolder;
		layout = createLayout();
	}

	protected abstract Pane createLayout();
	
	public abstract void updateContent();

	public String getName() {
		return name;
	}

	public Pane getLayout() {
		return layout;
	}

	public DataHolder getDataHolder() {
		return dataHolder;
	}
	
}
abstract class TabScene {
	
	private Scene scene;
	private SceneID id;
	private SceneManager manager;
	private TabLayout[] layouts;
	
	public TabScene(SceneManager manager, SceneID id, int width, int height, TabLayout... layouts) {
		this.id = id;
		this.manager = manager;
		this.layouts = layouts;
		scene = createScene(width, height);
		scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
	}
	
	public Scene getScene() {
		return scene;
	}

	public SceneID getId() {
		return id;
	}
	
	protected abstract Scene createScene(int width, int height);
	
	public void updateLayouts() {
		for (int i = 0; i < layouts.length; i++) 
			layouts[i].updateContent();
	}

	public TabLayout[] getLayouts() {
		return layouts;
	}

	public SceneManager getManager() {
		return manager;
	}
	
}
class SceneManager {
	
	private TabScene[] scenes;
	private Stage primaryStage;
	
	public SceneManager() {}
	
	public SceneManager(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public void setScenes(TabScene... scenes) {
		this.scenes = scenes;
	}
	
	public void switchTo(SceneID id) {
		for (int i = 0; i < scenes.length; i++) 
			if (scenes[i].getId() == id) {
				primaryStage.setScene(scenes[i].getScene());
				scenes[i].updateLayouts();
				System.out.println("switching to " + id);
				break;
			}
	}
	
	public Scene getSceneByID(SceneID id) {
		for (int i = 0; i < scenes.length; i++) 
			if (scenes[i].getId() == id) 
				return scenes[i].getScene();
		return null;
	}

}
enum SceneID {
	MARTYR_DATE,
	MARTYR,
	SAVE
}
class SaveScene extends TabScene {

	public SaveScene(SceneManager manager, int width, int height, TabLayout... layouts) {
		super(manager, SceneID.SAVE, width, height, layouts);
	}

	@Override
	protected Scene createScene(int width, int height) {
		TabPane tabPane = new TabPane();
		TabLayout[] layouts = getLayouts();

		for (int i = 0; i < layouts.length; i++) {
			Tab tab = new Tab(layouts[i].getName());
			tab.setContent(layouts[i].getLayout());
			tab.setClosable(false);
			tabPane.getTabs().add(tab);
		}
		tabPane.setSide(Side.BOTTOM);

		MenuItem dateItem = new MenuItem("Date screen");
		dateItem.setOnAction(e -> {
			getManager().switchTo(SceneID.MARTYR_DATE);
		});
		Menu menu = new Menu("Go to");
		menu.getItems().addAll(dateItem);
		MenuBar menuBar = new MenuBar(menu);
		
		BorderPane bp = new BorderPane();
		bp.setTop(menuBar);
		bp.setCenter(tabPane);
		
		return new Scene(bp, width, height);
	}

}
class MartyrScene extends TabScene {

	public MartyrScene(SceneManager manager, int width, int height, TabLayout... layouts) {
		super(manager, SceneID.MARTYR, width, height, layouts);
	}

	@Override
	protected Scene createScene(int width, int height) {
		TabPane tabPane = new TabPane();
		TabLayout[] layouts = getLayouts();

		for (int i = 0; i < layouts.length; i++) {
			Tab tab = new Tab(layouts[i].getName());
			tab.setContent(layouts[i].getLayout());
			tab.setClosable(false);
			tabPane.getTabs().add(tab);
		}
		tabPane.setSide(Side.BOTTOM);

		MenuItem dateItem = new MenuItem("Date screen");
		dateItem.setOnAction(e -> {
			getManager().switchTo(SceneID.MARTYR_DATE);
		});
		Menu menu = new Menu("Go to");
		menu.getItems().addAll(dateItem);
		MenuBar menuBar = new MenuBar(menu);
		
		BorderPane bp = new BorderPane();
		bp.setTop(menuBar);
		bp.setCenter(tabPane);
		
		return new Scene(bp, width, height);
	}

}

class MartyrDateScene extends TabScene {

	public MartyrDateScene(SceneManager manager, int width, int height, TabLayout... layouts) {
		super(manager, SceneID.MARTYR_DATE, width, height, layouts);
	}

	@Override
	protected Scene createScene(int width, int height) {
		TabPane tabPane = new TabPane();
		TabLayout[] layouts = getLayouts();

		for (int i = 0; i < layouts.length; i++) {
			Tab tab = new Tab(layouts[i].getName());
			tab.setContent(layouts[i].getLayout());
			tab.setClosable(false);
			tabPane.getTabs().add(tab);
		}
		tabPane.setSide(Side.BOTTOM);

		MenuItem martyrItem = new MenuItem("Martyr screen");
		MenuItem saveItem = new MenuItem("Save screen");
		
		martyrItem.setOnAction(e -> {
			getManager().switchTo(SceneID.MARTYR);
		});
		saveItem.setOnAction(e -> {
			getManager().switchTo(SceneID.SAVE);
		});
		Menu menu = new Menu("Go to");
		menu.getItems().addAll(martyrItem, saveItem);
		MenuBar menuBar = new MenuBar(menu);
		
		BorderPane bp = new BorderPane();
		bp.setTop(menuBar);
		bp.setCenter(tabPane);
		
		return new Scene(bp, width, height);
	}


}
class DataHolder {
	
	private QuadraticOHash<MDate> dates;
	private MDate currentDate;
	private int currentIdx;
	
	private DoublyLinkedList<District> districts;

	
	public DataHolder() { }
	
	public DataHolder(QuadraticOHash<MDate> dates) {
		this.dates = dates;
		updateDatesStat();
		if (dates.get(0).getFlag() == Flag.FULL) currentDate = dates.get(0).getData();
		else moveDown();
	}
	
	public QuadraticOHash<MDate> getDates() {
		return dates;
	}
	
	public void setDates(QuadraticOHash<MDate> dates) {
		this.dates = dates;
		updateDatesStat();
		if (dates.get(0).getFlag() == Flag.FULL) currentDate = dates.get(0).getData();
		else moveDown();
	}
	
	private void updateDatesStat() {
		int m = dates.getTableSize();
		for (int i = 0; i < m; i++) {
			HNode<MDate> date = dates.get(i);
			if (date.getFlag() == Flag.FULL)
				date.getData().getStat().updateStats();
		}
	}

	public void moveUp() {
		int i = currentIdx;
		for (; --i >= 0 && dates.get(i).getFlag() != Flag.FULL;);
		if (i >= 0 && dates.get(i).getFlag() == Flag.FULL) {
			currentIdx = i;
			currentDate = dates.get(i).getData();
		}
	}
	
	public void moveDown() {
		int i = currentIdx, m = dates.getTableSize();
		for (; ++i < m && dates.get(i).getFlag() != Flag.FULL;);
		if (i < m && dates.get(i).getFlag() == Flag.FULL) {
			currentIdx = i;
			currentDate = dates.get(i).getData();
		}
	}

	public MDate getCurrentDate() {
		return currentDate;
	}

	public DoublyLinkedList<District> getDistricts() {
		return districts;
	}

	public void setDistricts(DoublyLinkedList<District> districts) {
		this.districts = districts;
	}
	
}
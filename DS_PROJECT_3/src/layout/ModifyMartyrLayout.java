package layout;

import java.util.Arrays;
import java.util.Optional;
import data.District;
import data.Martyr;
import data_structs.doublylinkedlist.DNode;
import data_structs.doublylinkedlist.DoublyLinkedList;
import data_structs.linkedlist.Node;
import data_structs.queue.LinkedListQueue;
import data_structs.tree.TNode;
import dataholder.DataHolder;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
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

public class ModifyMartyrLayout extends TabLayout {
	
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
				String oldName = m.getName();
				if (dataHolder.getCurrentDate().getMartyrs().find(
						new Martyr(t.getNewValue(), m.getDistrict(), "", 'M', 0)) != null) {
					m.setName(oldName);
					statusL.setText("Updating name failed. Martyr with this new name exist in the same district");
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
		if (dataHolder.getCurrentDate() == null || 
				dataHolder.getCurrentDate().getMartyrs().size() == 0)
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
		if (dataHolder.getCurrentDate() == null || 
				dataHolder.getCurrentDate().getMartyrs().size() == 0)
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
			if (dataHolder.getCurrentDate().getMartyrs().size() != 0) {
				fillTableInOrder();
				fillDistrictsCB();
			}
			return;
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

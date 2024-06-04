package layout;

import java.util.Arrays;
import java.util.Optional;
import data.District;
import data.Martyr;
import dataholder.DataHolder;
import doublylinkedlist.DNode;
import doublylinkedlist.DoublyLinkedList;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import linkedlist.Node;
import queue.LinkedListQueue;
import tree.TNode;

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
		TableColumn<Martyr, String> nameColumn = new TableColumn<>("Name");
		TableColumn<Martyr, Integer> ageColumn = new TableColumn<>("Age");
		TableColumn<Martyr, String> genderColumn = new TableColumn<>("Gender");
		TableColumn<Martyr, String> districtColumn = new TableColumn<>("District");
		TableColumn<Martyr, String> locationColumn = new TableColumn<>("Location");
		nameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getName()));
		nameColumn.setOnEditCommit((CellEditEvent<Martyr, String> t) -> {
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				Martyr m = ((Martyr) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				getDataHolder().getCurrentDate().getMartyrs().delete(m);
				m.setName(t.getNewValue());
				getDataHolder().getCurrentDate().getMartyrs().insert(m);
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
		
		statusL = currentDateL = new Label();
		
		Label l1 = new Label("Enter martyr's name:"),
				l2 = new Label("Enter martyr's age:"),
				l3 = new Label("Select gender:"),
				l4 = new Label("Select District and Location:"),
				l5 = new Label("Size:"),
				l6 = new Label("Height:");
		
		nameTF = new TextField();
		ageTF = new TextField();
		sizeTF = new TextField();
		sizeTF.setPrefColumnCount(5);
		heightTF = new TextField();
		heightTF.setPrefColumnCount(5);
		
		insertBtn = new Button("Insert");
		insertBtn.setOnAction(e -> insertMartyr());
		deleteBtn = new Button("Delete selected");
		deleteBtn.setOnAction(e -> deleteMartyr());
		displaySizeHeightBtn = new Button("Display tree's size and height");
		displaySizeHeightBtn.setOnAction(e -> {
			sizeTF.setText(getDataHolder().getCurrentDate().getMartyrs().size() + "");
			heightTF.setText(getDataHolder().getCurrentDate().getMartyrs().height() + "");
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
		gp.add(statusL, 2, 1);
		gp.add(l3, 0, 2);
		gp.add(new HBox(15, maleRB, femaleRB), 1, 2);
		gp.add(l4, 0, 3);
		gp.add(districtsCB, 1, 3);
		gp.add(locationsCB, 2, 3);
		gp.add(displaySizeHeightBtn, 0, 4);
		gp.add(new HBox(10, l5, sizeTF), 1, 4);
		gp.add(new HBox(10, l6, heightTF), 2, 4);
		gp.setAlignment(Pos.CENTER);
		
		VBox vBox = new VBox(15, martyrsTable, new HBox(10, sortTableBtn, displayTableBtn, deleteBtn));
		vBox.setAlignment(Pos.CENTER);
		
		BorderPane layout = new BorderPane();
		layout.setTop(currentDateL);
		layout.setCenter(gp);
		layout.setRight(vBox);
		layout.setPadding(new Insets(10));
		
		BorderPane.setAlignment(vBox, Pos.CENTER);
		
		return layout;
	}
	
	private void insertMartyr() {
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
		String name = nameTF.getText();
		int age = Integer.valueOf(ageTF.getText());
		char gender = (maleRB.isSelected()) ? 'M' : 'F';
		String district = districtsCB.getValue().toString(),
				location = locationsCB.getValue();
		getDataHolder().getCurrentDate().getMartyrs().insert(
				new Martyr(name, district, location, gender, age));
		getDataHolder().getCurrentDate().getStat().updateStats();
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
			getDataHolder().getCurrentDate().getMartyrs().delete(selectedMartyr);
			martyrs.remove(selectedMartyr);
			martyrsTable.refresh();
			getDataHolder().getCurrentDate().getStat().updateStats();
			statusL.setText("Martyr " + selectedMartyr.getName() + " is deleted");
		}
	}
	
	private void fillTableInOrder() {
		martyrs.clear();
		TNode<Martyr> root = getDataHolder().getCurrentDate().getMartyrs().getRoot();
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
		martyrs.clear();
		TNode<Martyr> root = getDataHolder().getCurrentDate().getMartyrs().getRoot();
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
		Martyr[] arr = new Martyr[martyrs.size()+1];
		martyrs.toArray(arr);
		System.arraycopy(arr, 0, arr, 1, arr.length);
		Martyr.heapSortAsc(arr);
		martyrs = FXCollections.observableArrayList(arr);
		martyrsTable.setItems(martyrs);
		martyrsTable.refresh();
	}

	@Override
	public void updateContent() {
		fillTableInOrder();
		fillDistrictsCB();
	}
	
	private void fillDistrictsCB() {
		districtsCB.getItems().clear();
		DoublyLinkedList<District> districts = getDataHolder().getDistricts();
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

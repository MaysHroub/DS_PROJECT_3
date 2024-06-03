package layout;

import data.District;
import data.MDate;
import data.Martyr;
import dataholder.DataHolder;
import doublylinkedlist.DNode;
import doublylinkedlist.DoublyLinkedList;
import hash.HNode;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
	
	
	public ModifyMartyrLayout(DataHolder dataHolder) {
		super("Modify Martyrs", dataHolder);
	}

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
			Martyr m = ((Martyr) t.getTableView().getItems().get(t.getTablePosition().getRow()));
			getDataHolder().getCurrentDate().getMartyrs().delete(m);
			m.setName(t.getNewValue());
			getDataHolder().getCurrentDate().getMartyrs().insert(m);
			fillTableLevelOrder();
		});
		ageColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getAge()).asObject());
		genderColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getGender() + ""));
		districtColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getDistrict()));
		locationColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getLocation()));
		martyrsTable.getColumns().addAll(nameColumn, ageColumn, genderColumn, districtColumn, locationColumn);
		martyrsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
		
		martyrs = FXCollections.observableArrayList();
		
		statusL = currentDateL = new Label();
		
		Label l1 = new Label("Enter martyr's name:"),
				l2 = new Label("Enter martyr's age@:"),
				l3 = new Label("Select gender:"),
				l4 = new Label("Select District and Location:"),
				l5 = new Label("Size:"),
				l6 = new Label("Height:");
		
		nameTF = new TextField();
		ageTF = new TextField();
		sizeTF = new TextField();
		heightTF = new TextField();
		
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
		
		GridPane gp = new GridPane(10, 10);
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
		
		VBox vBox = new VBox(15, martyrsTable, new HBox(10, sortTableBtn, displayTableBtn, deleteBtn));
		
		BorderPane layout = new BorderPane();
		layout.setTop(currentDateL);
		layout.setCenter(gp);
		layout.setRight(vBox);
		
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
		statusL.setText("Martyr " + name + " is inserted :)");
	}
	
	private void deleteMartyr() {
		Martyr selectedMartyr = martyrsTable.getSelectionModel().getSelectedItem();
		if (selectedMartyr == null) {
			statusL.setText("No martyr is selected");
			return;
		}
		getDataHolder().getCurrentDate().getMartyrs().delete(selectedMartyr);
		martyrs.remove(selectedMartyr);
		martyrsTable.refresh();
	}
	
	private void fillTableInOrder() {
		TNode<Martyr> root = getDataHolder().getCurrentDate().getMartyrs().getRoot();
		if (root == null) return;
	}
	
	private void fillTableInOrder(TNode<Martyr> curr) {
		
	}
	
	private void fillTableLevelOrder() {
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
		martyrsTable.setItems(martyrs);
		martyrsTable.refresh();
	}
	
	private void sortTableByAge() {
		Martyr[] arr = new Martyr[martyrs.size()];
		martyrs.toArray(arr);
		Martyr.heapSortAsc(arr);
		martyrs = FXCollections.observableArrayList(arr);
		martyrsTable.setItems(martyrs);
		martyrsTable.refresh();
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}
	
	private void fillDistrictsCB() {
		DoublyLinkedList<District> districts = getDataHolder().getDistricts();
		DNode<District> curr = districts.getHead();
		while (curr != null) {
			districtsCB.getItems().add(curr.getData());
			curr = curr.getNext();
		}
	} 
	
	private void fillLocationsCB(District district) {
		Node<String> curr = district.getLocations().getHead();
		while (curr != null) {
			locationsCB.getItems().add(curr.getData());
			curr = curr.getNext();
		}
	}

}

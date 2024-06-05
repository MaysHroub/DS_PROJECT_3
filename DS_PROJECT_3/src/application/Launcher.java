package application;
	
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Scanner;
import data.District;
import data.MDate;
import data.MDateStat;
import data.Martyr;
import data_structs.doublylinkedlist.DNode;
import data_structs.doublylinkedlist.DoublyLinkedList;
import dataholder.DataHolder;
import hash.HNode;
import hash.QuadraticOHash;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import layout.ModifyDateLayout;
import layout.ModifyMartyrLayout;
import layout.NavigateDateLayout;
import layout.SaveDataLayout;
import scenes.MartyrDateScene;
import scenes.MartyrScene;
import scenes.SaveScene;
import scenes.SceneID;
import scenes.SceneManager;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;


public class Launcher extends Application {
	
	final int WIDTH = 1050, HEIGHT = 550;
	
	
	@Override
	public void start(Stage primaryStage) {
		Button loadBtn = new Button("Load data");

		loadBtn.setStyle("-fx-font-size: 13;" + "	-fx-font-weight: bold;" + "	-fx-font-family: 'Cambria';"
				+ "	-fx-background-color: #fca503;" + "	-fx-text-fill: white;");
		loadBtn.setOnAction(e -> {
			DataHolder dataHolder = new DataHolder();
			loadData(primaryStage, dataHolder);
			initPorgram(primaryStage, dataHolder);
		});

		primaryStage.setScene(new Scene(new StackPane(loadBtn), WIDTH, HEIGHT));
		primaryStage.show();
	}
	
	private void initPorgram(Stage stage, DataHolder dataHolder) {
		ModifyDateLayout modifyDateLayout = new ModifyDateLayout(dataHolder);
		ModifyMartyrLayout modifyMartyrLayout = new ModifyMartyrLayout(dataHolder);
		NavigateDateLayout navigateDateLayout = new NavigateDateLayout(dataHolder);
		SaveDataLayout saveDataLayout = new SaveDataLayout(dataHolder);
		
		SceneManager manager = new SceneManager(stage);
		
		MartyrDateScene martyrDateScene = new MartyrDateScene(manager, WIDTH, HEIGHT, modifyDateLayout, navigateDateLayout);
		MartyrScene martyrScene = new MartyrScene(manager, WIDTH, HEIGHT, modifyMartyrLayout);
		SaveScene saveScene = new SaveScene(manager, WIDTH, HEIGHT, saveDataLayout);
		
		manager.setScenes(martyrDateScene, martyrScene, saveScene);
		
		manager.switchTo(SceneID.MARTYR_DATE);
	}
	
	private void loadData(Stage stage, DataHolder dataHolder) {
		QuadraticOHash<MDate> hashTable = new QuadraticOHash<>(5);
		DoublyLinkedList<District> districts = new DoublyLinkedList<>();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select a file");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("csv files", "*.csv"));
		fileChooser.setInitialDirectory(new File("/C:/Users/ismae/Downloads"));
		File selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile == null)
			return;
		try (Scanner in = new Scanner(new FileInputStream(selectedFile))) {
			in.nextLine(); // 0.name, 1.event, 2.age, 3.location, 4.district, 5.gender
			while (in.hasNext()) {
				String[] tokens = in.nextLine().split(",");
				if (tokens[2].length() == 0)  // no age
					continue;
				
				String[] dateInfo = tokens[1].split("/");
				@SuppressWarnings("deprecation")
				Date date = new Date(Integer.parseInt(dateInfo[2]) - 1900, Integer.parseInt(dateInfo[0]) - 1,
						Integer.parseInt(dateInfo[1]));
				MDate martyrDate = new MDate(date);
				HNode<MDate> hashedDate = hashTable.find(martyrDate);
				if (hashedDate != null)
					martyrDate = hashedDate.getData();
				else {
					hashTable.add(martyrDate);
					MDateStat stat = new MDateStat(martyrDate);
					martyrDate.setStat(stat);
				}
				
				Martyr martyr = new Martyr(tokens[0], tokens[4], tokens[3],
						tokens[5].charAt(0), Integer.valueOf(tokens[2]));
				martyrDate.getMartyrs().insert(martyr);
				
				District district = new District(tokens[4]);
				DNode<District> districtNode = districts.find(district);
				if (districtNode != null) 
					district = districtNode.getData();
				else 
					districts.insert(district);
				
				if (district.getLocations().find(tokens[3]) == null)
					district.getLocations().insertSorted(tokens[3]);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		dataHolder.setDates(hashTable);
		dataHolder.setDistricts(districts);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

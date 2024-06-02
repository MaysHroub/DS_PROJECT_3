package layout;

import data.MDateStat;
import dataholder.DataHolder;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class NavigateDateLayout extends TabLayout {
	
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
		
		upBtn = new Button("UP ↑");
		downBtn = new Button("DOWN ↓");
		
		GridPane gp = new GridPane(10, 10);
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
		
		VBox vBox = new VBox(10, upBtn, downBtn);
		
		BorderPane layout = new BorderPane();
		layout.setTop(currentDateL);
		layout.setCenter(gp);
		layout.setBottom(vBox);
		
		return layout;
	}
	
	private void fillLayoutWithData() {
		DataHolder dataHolder = getDataHolder();
		if (dataHolder.getCurrentDate() == null) {
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
		// TODO Auto-generated method stub

	}

}

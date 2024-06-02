package layout;

import dataholder.DataHolder;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class NavigateDateLayout extends TabLayout {
	
	private Label totalL, totalMalesL, totalFemalesL, avgAgesL,
					youngestMartyrL, oldestMartyrL, maxMartyrDistrictL, maxMartyrLocationL;
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
		
		upBtn = new Button("UP ↑");
		downBtn = new Button("DOWN ↓");
		
		return null;
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
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

}

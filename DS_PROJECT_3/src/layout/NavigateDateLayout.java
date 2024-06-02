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
		
		return null;
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

}

package layout;

import dataholder.DataHolder;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class NavigateDateLayout extends TabLayout {
	
	private Label totalL, totalMalesL, totalFemalesL, avgAgesL,
					youngestMartyrL, oldestMartyrL, maxMartyrDistrictL, maxMartyrLocationL;

	public NavigateDateLayout(DataHolder dataHolder) {
		super("Date Navigation", dataHolder);
	}

	@Override
	protected Pane createLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

}

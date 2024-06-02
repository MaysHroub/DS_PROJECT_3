package layout;

import data.District;
import data.Martyr;
import dataholder.DataHolder;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ModifyMartyrLayout extends TabLayout {
	
	private Label statusL;
	private TextField nameTF, ageTF, sizeTF, heightTF;
	private Button insertBtn, displaySizeHeightBtn, sortTableBtn, displayTableBtn;
	private RadioButton maleRB, femaleRB;
	private ComboBox<District> districtsCB;
	private ComboBox<String> locationsCB;
	private TableView<Martyr> martyrsTable;
	
	
	public ModifyMartyrLayout(DataHolder dataHolder) {
		super("Modify Martyrs", dataHolder);
	}

	@Override
	protected Pane createLayout() {
		statusL = new Label();
		return null;
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

}

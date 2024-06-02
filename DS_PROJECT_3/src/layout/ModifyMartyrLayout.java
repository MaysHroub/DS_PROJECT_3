package layout;

import dataholder.DataHolder;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ModifyMartyrLayout extends TabLayout {
	
	private Label statusL;
	private TextField nameTF, ageTF, sizeTF, heightTF;
	private Button insertBtn, displaySizeHeightBtn, sortTableBtn, displayTableBtn;
	private RadioButton maleRB, femaleRB;
	
	
	
	public ModifyMartyrLayout(DataHolder dataHolder) {
		super("Modify Martyrs", dataHolder);
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

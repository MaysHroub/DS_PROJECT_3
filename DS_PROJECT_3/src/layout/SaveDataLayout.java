package layout;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import data.District;
import data.MDate;
import data.Martyr;
import dataholder.DataHolder;
import hash.Flag;
import hash.HNode;
import hash.QuadraticOHash;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import tree.TNode;

public class SaveDataLayout extends TabLayout {
	
	private TextField fileTF;
	private Button saveBtn;
	private Label saveL;

	public SaveDataLayout(DataHolder dataHolder) {
		super("Save data", dataHolder);
	}

	@Override
	protected Pane createLayout() {
		fileTF = new TextField();
		saveBtn = new Button("Save");
		saveL = new Label();
		
		fileTF.setPromptText("Enter file name/path");
		
		saveBtn.setOnAction(e -> saveDate());
		
		VBox vBox = new VBox(20, fileTF, saveBtn, saveL);
		vBox.setAlignment(Pos.CENTER);
		vBox.setPadding(new Insets(50));
		
		return vBox;
	}

	private void saveDate() {
		if (fileTF.getText() == null || fileTF.getText().length() == 0) return;
		
		try (PrintWriter out = new PrintWriter(fileTF.getText())) {
			writeDates(out);
			saveL.setText("All data is saved");
		} catch (IOException e) {
			System.out.println(e);
			saveL.setText("Couldn't save to file!");
		}
	}
	
	private void writeDates(PrintWriter out) {
		QuadraticOHash<MDate> dates = getDataHolder().getDates();
		int m = dates.getTableSize();
		for (int i = 0; i < m; i++) {
			HNode<MDate> date = dates.get(i);
			if (date.getFlag() == Flag.FULL)
				writeMartyrs(date.getData().getMartyrs().getRoot(), date.getData().getDate(), out);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void writeMartyrs(TNode<Martyr> curr, Date date, PrintWriter out) {
		if (curr == null) return;
		// 0.name, 1.event, 2.age, 3.location, 4.district, 5.gender
		Martyr m = curr.getData();
		out.println(String.format("%s,%s,%d,%s,%s,%c",
				m.getName(),
				(date.getMonth() + 1) + "/" + 
				(date.getDate()) + "/" + 
				(date.getYear() + 1900),
				m.getAge(),
				m.getLocation(),
				m.getDistrict(),
				m.getGender()));
		writeMartyrs(curr.getLeft(), date, out);
		writeMartyrs(curr.getRight(), date, out);
	}

	@Override
	public void updateContent() { /* do nth */ }

}










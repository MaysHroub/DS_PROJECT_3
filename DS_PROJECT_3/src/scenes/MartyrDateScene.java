package scenes;

import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import layout.TabLayout;

public class MartyrDateScene extends TabScene {

	public MartyrDateScene(SceneManager manager, int width, int height, TabLayout... layouts) {
		super(manager, SceneID.MARTYR_DATE, width, height, layouts);
	}

	@Override
	protected Scene createScene(int width, int height) {
		TabPane tabPane = new TabPane();
		TabLayout[] layouts = getLayouts();

		for (int i = 0; i < layouts.length; i++) {
			Tab tab = new Tab(layouts[i].getName());
			tab.setContent(layouts[i].getLayout());
			tab.setClosable(false);
			tabPane.getTabs().add(tab);
		}
		tabPane.setSide(Side.BOTTOM);

		MenuItem martyrItem = new MenuItem("Martyr screen");
		MenuItem saveItem = new MenuItem("Save screen");
		
		martyrItem.setOnAction(e -> {
			getManager().switchTo(SceneID.MARTYR);
		});
		saveItem.setOnAction(e -> {
			getManager().switchTo(SceneID.SAVE);
		});
		Menu menu = new Menu("Go to");
		menu.getItems().addAll(martyrItem, saveItem);
		MenuBar menuBar = new MenuBar(menu);
		
		BorderPane bp = new BorderPane();
		bp.setTop(menuBar);
		bp.setCenter(tabPane);
		
		return new Scene(bp, width, height);
	}


}

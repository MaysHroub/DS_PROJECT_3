module DS_PROJECT_3 {
	requires javafx.controls;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;
	opens data to javafx.base;
}

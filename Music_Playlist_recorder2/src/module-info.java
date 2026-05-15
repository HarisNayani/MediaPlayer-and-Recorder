module YourModuleName {
    requires transitive javafx.graphics;
    requires javafx.controls;
	requires javafx.base;
	requires javafx.fxml;
	requires javafx.media;
    
    exports M;
}

package addr.book.controller;


import addr.book.model.Contact;
import addr.book.view.ContactOverview;
import addr.book.view.EditContactController;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddrBookApp extends Application {

	private Stage primaryStage;
	private BorderPane rootBorderPane;

	public ObservableList<Contact> contactList = FXCollections.observableArrayList();
	
	/*
	 * Constructor
	 */
	public AddrBookApp() {
//		contactList.add(new Contact("Matt", "Bradford"));
	}
	
	/*
	 * returns ObservableList of Contacts
	 */
	public ObservableList<Contact> getContactInfo() {
		return contactList;
	}
	
	public void setContactInfo(ObservableList<Contact> list) {
		contactList = list;
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Address Book Application");
		primaryStage.setHeight(450);
		primaryStage.setWidth(560);
		
		rootBorderPane();
		contactOverview();
		
	}
	
	public void rootBorderPane() {
		try {
			
			// Load root BorderPane
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(AddrBookApp.class.getResource("../view/RootBorderPane.fxml"));
			rootBorderPane = (BorderPane) loader.load();
			
			// Show the scene
			Scene scene = new Scene(rootBorderPane);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

    /**
     * Shows the contact overview inside the root border pane.
     */
	public void contactOverview() {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(AddrBookApp.class.getResource("../view/ContactOverview.fxml"));
			AnchorPane contactOverview = (AnchorPane) loader.load();
			rootBorderPane.setCenter(contactOverview);
			
			// Allows ContactOverview class to have access to AddrBookApp
			ContactOverview controller = loader.getController();
			controller.setAddrBookApp(this);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean displayEditWindow(Contact contact) {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(AddrBookApp.class.getResource("../view/EditContact.fxml"));
			AnchorPane editWindow = (AnchorPane) loader.load();
			
			// Create the stage window
			Stage stageWindow = new Stage();
			stageWindow.setTitle("Editing Contact");
			stageWindow.initModality(Modality.WINDOW_MODAL);
			stageWindow.initOwner(primaryStage);
			Scene scene = new Scene(editWindow);
			stageWindow.setScene(scene);
			
			// Places the contact into the controller
			EditContactController controller = loader.getController();
			controller.setEditDialog(stageWindow);
			controller.setContact(contact);
			
			// Shows the stage window until it is closed
			stageWindow.showAndWait();
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
	        return false;
		}
		
		
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

package addr.book.view;

import addr.book.model.Contact;
import addr.book.util.CalendarUtil;
import addr.book.controller.AddrBookApp;
import addr.book.view.SQLConnect;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class ContactOverview {

	@FXML
	private TableView<Contact> contactTable;
	@FXML
	private TableColumn<Contact, String> firstNameColumn;
	@FXML
	private TableColumn<Contact, String> lastNameColumn;
	@FXML
	private TextField searchField = new TextField();
	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField emailField;
	@FXML
	private TextField addrField;
	@FXML
	private TextField dateOBField;

	// Reference to the main application.
	private AddrBookApp addrBookApp = new AddrBookApp();


	/**
	 * Default constructor. Called before the initialize() method.
	 */

	public ContactOverview() {}

	@FXML
	private void load() {
		SQLConnect sqlDB = new SQLConnect();

		// Clear existing contacts from Address Book
		addrBookApp.contactList.clear();
		addrBookApp.contactList = sqlDB.loadAddrBook();
		addrBookApp.contactOverview();
	}
	
	@FXML
	private void save() {
		if (addrBookApp.getContactInfo().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(addrBookApp.getPrimaryStage());
			alert.setTitle("Address Book is Empty");
			alert.setHeaderText("No contacts to save!");
			alert.setContentText("Please add a person to the table before saving.");
			alert.showAndWait();
		} else {
			SQLConnect sqlDB = new SQLConnect();
			sqlDB.saveAddrBook(addrBookApp.getContactInfo());
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(addrBookApp.getPrimaryStage());
			alert.setTitle("Saved");
			alert.setHeaderText("Save Successful!");
			alert.setContentText("Address Book Saved!");
			alert.showAndWait();
		}
	}

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the FXML file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Initialize the Contact table with the two columns.
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
		firstNameField.setVisible(false);
		lastNameField.setVisible(false);
		emailField.setVisible(false);
		addrField.setVisible(false);
		dateOBField.setVisible(false);

		// Resets all contact fields.
		showContact(null);

		// Adds listener to show details once they have changed.
		contactTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showContact(newValue));
				
		
        
		
	} // End of Initialize

	@FXML
	private void search() {
		FilteredList<Contact> filteredList = new FilteredList<>(addrBookApp.getContactInfo(),p->true);
		
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(contact -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                
                if (contact.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (contact.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
		
        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Contact> sortedList = new SortedList<>(filteredList);
        
        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedList.comparatorProperty().bind(contactTable.comparatorProperty());
        
        // 5. Add sorted (and filtered) data to the table.
        contactTable.setItems(sortedList);
	}
	
	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param addrBookApp
	 */
	public void setAddrBookApp(AddrBookApp addrBookApp) {
		this.addrBookApp = addrBookApp;

		// Add fields.
		contactTable.setItems(addrBookApp.getContactInfo());
	}

	public void showContact(Contact contact) {
		if (contact == null) {
			firstNameField.setText("");
			lastNameField.setText("");
			emailField.setText("");
			addrField.setText("");
			dateOBField.setText("");
		} else {
			firstNameField.setVisible(true);
			firstNameField.setText(contact.getFirstName());

			lastNameField.setVisible(true);
			lastNameField.setText(contact.getLastName());

			emailField.setVisible(true);
			emailField.setText(contact.getEmail());

			addrField.setVisible(true);
			addrField.setText(contact.getAddress());

			// Converts LocalDate format to String format.
			dateOBField.setText(CalendarUtil.applyFormat(contact.getDateOB()));
			dateOBField.setVisible(true);
		}
	}

	/**
	 * This function opens the window for the user to edit the new contact.
	 */
	@FXML
	private void newContact() {
		Contact contact = new Contact();
		boolean okClicked = addrBookApp.displayEditWindow(contact);
		if (okClicked && contact.getFirstName() != null) {
			addrBookApp.getContactInfo().add(contact);
			
			// Show alert that the add was a success.
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Add Successful");
			alert.setHeaderText(contact.getFirstName() + " was successfully added!");
			alert.showAndWait();
		}
	}

	/**
	 * This function is called when the edit button is clicked.
	 */
	@FXML
	private void editContact() {
		Contact contact = contactTable.getSelectionModel().getSelectedItem();

		if (contact != null) {
			// If there is a contact selected.
			boolean okClicked = addrBookApp.displayEditWindow(contact);
			if (okClicked) {
				showContact(contact);
			}
			
		} else {
			// If there is no contact selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(addrBookApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Person Selected");
			alert.setContentText("Please select a person in the table.");

			alert.showAndWait();
		}
	}

	/**
	 * This function handles deleting a contact from the address book.
	 */
	@FXML
	private void deleteContact() {
		// Sets the Data Model for the TableView back to the full list
		// in case it was changed during a search. 
		contactTable.setItems(addrBookApp.getContactInfo());
		
		int selectedContact = contactTable.getSelectionModel().getSelectedIndex();

		if (selectedContact >= 0) {
			contactTable.getItems().remove(selectedContact);
			
		} else {
			// If there is no contact selected when delete is pressed.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(addrBookApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Person Selected");
			alert.setContentText("Please select a contact in the table.");

			alert.showAndWait();
		}

	}

}

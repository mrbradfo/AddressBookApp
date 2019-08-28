package addr.book.view;

import addr.book.model.Contact;
import addr.book.util.CalendarUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditContactController {
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
	
	private Stage editDialogStage;
	private Contact contact;
	private boolean okClicked = false;
	
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
	@FXML
	private void initialize() {}
	
	
	 /**
     * Sets the stage of the edit dialog.
     * 
     * @param editDialogStage
     */
	public void setEditDialog(Stage editDialogStage) {
		this.editDialogStage = editDialogStage;
	}
	
	
	/**
     * Sets the contact to be edited in the dialog.
     * 
     * @param contact
     */
    public void setContact(Contact contact) {
        this.contact = contact;

        firstNameField.setText(contact.getFirstName());
        lastNameField.setText(contact.getLastName());
        emailField.setText(contact.getEmail());
        addrField.setText(contact.getAddress());
        dateOBField.setText(CalendarUtil.applyFormat(contact.getDateOB()));
        dateOBField.setPromptText("MM.dd.yyyy");	
    }
	
    /**
     * @return okClicked boolean 
     * true if Ok was clicked.
     * false if Ok has not been clicked.  
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when Ok is clicked
     */
    @FXML
    private void okClicked() {
        if (usrInputCheck()) {
            contact.setFirstName(firstNameField.getText());
            contact.setLastName(lastNameField.getText());
            contact.setEmail(emailField.getText());
            contact.setAddress(addrField.getText());
            contact.setDateOB(CalendarUtil.parse(dateOBField.getText()));

            okClicked = true;
            editDialogStage.close();
        }
    }

    /**
     * Called when cancel is clicked.
     */
    @FXML
    private void cancelClicked() {
        editDialogStage.close();
    }
    

    /**
     * User input validation.
     * 
     * @return 
     */
    private boolean usrInputCheck() {
        String errorMsg = "";
        boolean errorDetected = false;

        if (firstNameField.getText() == null || firstNameField.getText().length() == 0)
            errorMsg += "No valid first name!\n"; 
        
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0)
            errorMsg += "No valid last name!\n"; 
        
        if (emailField.getText() == null || emailField.getText().length() == 0) 
            errorMsg+= "No valid postal code!\n"; 
        
        if (addrField.getText() == null || addrField.getText().length() == 0) 
            errorMsg += "No valid street!\n";

        if (dateOBField.getText() == null || dateOBField.getText().length() == 0) { 
            errorMsg += "No valid birthday!\n";
        } else {
            if (!CalendarUtil.dateIsValid(dateOBField.getText())) {
                errorMsg += "Date of birht is not valid. Use the format MM.dd.yyyy!\n";
            }
        }

        if (errorMsg.length() == 0) {
            errorDetected = true;
        } else {
        	errorDetected = false;
            // Show the error message
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(editDialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMsg);
            
            alert.showAndWait();
        }
        return errorDetected;
    }
	
}


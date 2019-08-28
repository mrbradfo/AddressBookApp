package addr.book.model;

import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
 * Matt Bradford
 */

public class Contact {
	
	private final StringProperty firstName;
	private final StringProperty lastName;
	private final StringProperty email;
	private final StringProperty address;
	private final ObjectProperty<LocalDate> dateOB;

	
	public Contact() {
		this(null,null);
	}
	
	
	public Contact(String firstName, String lastName) {
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.email = new SimpleStringProperty("");
		this.address = new SimpleStringProperty("");;
		this.dateOB = new SimpleObjectProperty<LocalDate>(LocalDate.now());
		
	}
	
	public String getFirstName() {
		return firstName.get();
	}
	
	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}
	
	public StringProperty firstNameProperty() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName.get();
	}
	
	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}
	
	public StringProperty lastNameProperty() {
		return lastName;
	}
	
	public String getEmail() {
		return email.get();
	}
	
	public void setEmail(String email) {
		this.email.set(email);
	}
	
	public StringProperty emailProperty() {
		return email;
	}
	
	public String getAddress() {
		return address.get();
	}
	
	public void setAddress(String address) {
		this.address.set(address);
	}
	
	public StringProperty addressProperty() {
		return address;
	}
	
	public void setDateOB(LocalDate dateOB) {
	 this.dateOB.set(dateOB);	
	}
	
	public LocalDate getDateOB() {
		return dateOB.get();
	}
	
	public ObjectProperty<LocalDate> dateOBProperty() {
		return dateOB;
	}
	
	
}

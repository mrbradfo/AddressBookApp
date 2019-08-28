package addr.book.view;

import java.sql.*;

import addr.book.model.Contact;
import addr.book.util.CalendarUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SQLConnect {
	
	// Default constructor.
	public SQLConnect() {}

	String url = "jdbc:mysql://localhost:3306/addrbookdb";
	String user = "User1";
	String pass = "pass";
	String clearTable = "DELETE FROM contactinfo";
	String insert = "insert into contactinfo (firstName, lastName, email, address, dateOB)";
	
	/** Fix duplicates
	 *  When saving check if info is already in the database
	 */
	public void saveAddrBook(ObservableList<Contact> contactList) {
		try {
			
			// Get connection
			Connection connect = DriverManager.getConnection(url, user, pass);
			// Create statement
			Statement statement = connect.createStatement();
			// Clear existing contacts
			statement.executeUpdate(clearTable);
			//contactsToSave.get(0).getFirstName()
			
			String insertContact = insert + " values (?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = connect.prepareStatement(insertContact);
			for(int index=0; index<contactList.size(); index++) {
				preparedStmt.setString(1, contactList.get(index).getFirstName());
				preparedStmt.setString(2, contactList.get(index).getLastName());
				preparedStmt.setString(3, contactList.get(index).getEmail());
				preparedStmt.setString(4, contactList.get(index).getAddress());
				preparedStmt.setString(5, CalendarUtil.applyFormat(contactList.get(index).getDateOB()));
				preparedStmt.execute();
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * When loading check if data has already been loaded into the addrBook. 
	 * Do not allow duplicates into the addrBook.
	 * @return
	 */
	public ObservableList<Contact> loadAddrBook() {

		ObservableList<Contact> contactList = FXCollections.observableArrayList();
		try {
			// Get connection
			Connection connect = DriverManager.getConnection(url, user, pass);

			// Create statement
			Statement statement = connect.createStatement();

			// SQL Query
			ResultSet result = statement.executeQuery("select * from contactinfo");

			// Process the result
			while (result.next()) {
				Contact contact = new Contact();
				contact.setFirstName(result.getString("firstName"));
				contact.setLastName(result.getString("lastName"));
				contact.setEmail(result.getString("email"));
				contact.setAddress(result.getString("address"));
				contact.setDateOB(CalendarUtil.parse(result.getString("dateOB")));
				contactList.add(contact);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return contactList;
	}

}

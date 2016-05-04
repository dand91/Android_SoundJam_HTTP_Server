package Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import Send.SendClassList;
import Send.SendClass;

/**
 * Database is a class that specifies the interface to the database. Uses
 * JDBC and the MySQL Connector/J driver.
 */
public class Database {
	/**
	 * The database connection.
	 */
	private Connection conn;
	protected boolean connected;
    private static Database db;
    private static String PASSWORD;
    private static String USERNAME;
    private static String DATABASE;

    public static void initiate() {
    	
        db = new Database();
        
        try {

        @SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader("info.txt"));
		PASSWORD = br.readLine();
		USERNAME = br.readLine();
		DATABASE = br.readLine();

		} catch (IOException e) {

			System.out.println("Unable to read info file");
		}
    }

    public static Database getInstance() throws Exception {
        if (db != null) {
            return db;
        } else {
            throw new Exception("Must initiate Database first");
        }
    }

	/**
	 * Open a connection to the database, using the specified user name and
	 * password.

	 * @return true if the connection succeeded, false if the supplied user name
	 *         and password were not recognized. Returns false also if the JDBC
	 *         driver isn't found.
	 */
    
	public boolean openConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/" + DATABASE, USERNAME, PASSWORD);

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		return isConnected();
	}


	/**
	 * Close the connection to the database.
	 */
	public void closeConnection() {

		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
		}
		conn = null;
	}

	/**
	 * Check if the connection to the database has been established
	 *
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		
		try {
			
			return conn != null && !conn.isClosed();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Database getDatabase(){
		
		return this;
	}
	
	
	public SendClassList getData(String group) {

    	  SendClassList sendList = new SendClassList();
    	  sendList.setSendClassList(new ArrayList<SendClass>());
    	  
    	  
		try {

			checkStatus();
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM dataTable WHERE groupName = ?");
			ps.setString(1, group);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {

				  SendClass temp = new SendClass();
				  temp.setGroupName(group);
				  temp.setInstrumentName(rs.getString("instrumentName") );
				  temp.setData(rs.getString("data") );
				  temp.setVolume(rs.getString("volume"));
				
		    	  sendList.getSendClassList().add(temp);

			}
				return sendList;

		} catch (SQLException e) {

			System.out.println("Unable to fetch group info: ");
			System.out.println(e.getMessage());
			
		}

		return null;

	}
	public boolean saveData(String groupName,String instrumentName,String data,String volume) {
		
		try {

			checkStatus();
			PreparedStatement ps = conn
					.prepareStatement("INSERT INTO dataTable (groupName, instrumentName, data, volume) values(?, ?, ?, ?)"
							+ " ON DUPLICATE KEY UPDATE data = ? , volume = ?;");

			ps.setString(1, groupName);
			ps.setString(2, instrumentName);
			ps.setString(3, data);
			ps.setString(4, volume);			
			ps.setString(5, data);
			ps.setString(6, volume);

			ps.execute();
			
			return true;

		} catch (SQLException e) {
			
			System.out.println("Unable to save group info: ");
			System.out.println(e.getMessage());
		}

		return false;

	}
	
	
	public boolean isValidGroup(String group){
		
		try {

			checkStatus();
			
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM dataTable WHERE groupName = ?");
			ps.setString(1, group);

			ResultSet rs = ps.executeQuery();

			return rs.next();
			
		} catch (SQLException e) {

			System.out.println("Unable to validate group: ");
			System.out.println(e.getMessage());
			}
		
		return false;
		
	}
	private void checkStatus(){
		
		if(!isConnected()){
			
			openConnection();
		}
		
	}
}
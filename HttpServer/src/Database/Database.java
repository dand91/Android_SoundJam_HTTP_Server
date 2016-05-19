package Database;

import Send.SendClass;
import Send.SendClassList;
import Log.Log;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Database is a class that specifies the interface to the database. Uses
 * JDBC and the MySQL Connector/J driver.
 */
public class Database {
    private static Database db;
    private static String PASSWORD;
    private static String USERNAME;
    private static String DATABASE;
    /**
     * The database connection.
     */
    private Connection conn;

    public static void initiate() {

        db = new Database();

        try {

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
     *
     * @return true if the connection succeeded, false if the supplied user name
     * and password were not recognized. Returns false also if the JDBC
     * driver isn't found.
     */

    public boolean openConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/" + DATABASE, USERNAME, PASSWORD);

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("Database", "Unable to start database, SQL exception");
            System.exit(0);
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("Database","Unable to start database, Class Not Found");
            System.exit(0);
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


    public SendClassList getData(String group) {

        SendClassList sendList = new SendClassList();
        sendList.setSendClassList(new ArrayList<SendClass>());


        try {

            checkStatus();

            PreparedStatement ps = conn
                    .prepareStatement("SELECT * FROM BPMTable WHERE groupName = ?");
            ps.setString(1, group);

            ResultSet rs = ps.executeQuery();

            int BPM = 120;

            if(rs.next()) {

                 BPM = rs.getInt("BPM");

            }

            ps = conn
                    .prepareStatement("SELECT * FROM dataTable WHERE groupName = ?");
            ps.setString(1, group);

            rs = ps.executeQuery();


            while (rs.next()) {

                SendClass temp = new SendClass();
                temp.setInstrumentName(rs.getString("instrumentName"));
                temp.setData(rs.getString("data"));
                temp.setVolume(rs.getString("volume"));
                temp.setBars(rs.getInt("bars"));
                temp.setHasData(true);

                sendList.getSendClassList().add(temp);
                sendList.setGroupName(group);
                sendList.setBPM(BPM);

            }

            return sendList;

        } catch (SQLException e) {

            Log.e("Database", "Unable to fetch group info: " + e.getMessage());

        }

        return null;

    }

    public boolean saveData(String groupName, String instrumentName, String data, String volume, int bars, int BPM) {

        try {

            checkStatus();
            PreparedStatement ps = conn
                    .prepareStatement("INSERT INTO dataTable (groupName, instrumentName, data, volume,bars) values(?, ?, ?, ?, ?)"
                            + " ON DUPLICATE KEY UPDATE data = ? , volume = ?, bars = ?;");

            ps.setString(1, groupName);
            ps.setString(2, instrumentName);
            ps.setString(3, data);
            ps.setString(4, volume);
            ps.setInt(5, bars);
            ps.setString(6, data);
            ps.setString(7, volume);
            ps.setInt(8, bars);

            ps.execute();


            ps = conn
                    .prepareStatement("INSERT INTO BPMTable (groupName, BPM) values(?, ?)"
                            + " ON DUPLICATE KEY UPDATE BPM = ?;");

            ps.setString(1, groupName);
            ps.setInt(2, BPM);
            ps.setInt(3, BPM);

            ps.execute();

            return true;

        } catch (SQLException e) {

            Log.e("Database", "Unable to save group info: " + e.getMessage());

        }

        return false;

    }


    public boolean isValidGroup(String group) {

        try {

            checkStatus();

            PreparedStatement ps = conn
                    .prepareStatement("SELECT * FROM dataTable WHERE groupName = ?");
            ps.setString(1, group);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {

            Log.e("Database", "Unable to validate group: " + e.getMessage());

        }

        return false;

    }

    private void checkStatus() {

        if (!isConnected()) {

            openConnection();
        }

    }
}
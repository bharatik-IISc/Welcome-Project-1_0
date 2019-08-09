import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DBOperations {
	
	/* This function will return a connection to the database server */
	public Connection connectToDB()
	{
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(Server.db_url,Server.db_username,Server.db_password);	
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Failure");
			e.printStackTrace();
		} 
		finally {
			return conn;
		}
	}
	
	/* This function will try to close the connection to the database server */
	public void closeDBConnection(Connection conn)
	{
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to close the connection");
			e.printStackTrace();
		}
		
	}
	
	/* This function will authenticate the user credentials */
	public boolean authenticateUser(Connection conn,String username,String password)
	{
		boolean authStatus = false;
		String queryString = "SELECT * FROM user_details WHERE username=? AND password=?";
		PreparedStatement ps1;
		try {
			ps1 = conn.prepareStatement(queryString);
			ps1.setString(1, username);
			ps1.setString(2,password);
			ResultSet rs = ps1.executeQuery();
			
			if(rs.next())
			{
				authStatus = true;
			}
			
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return authStatus;
		}
		
		
	}

	/* This function will create a new user */
	public boolean createUser(Connection conn,String username,String password)
	{
		boolean creationStatus = false;
		String queryString = "INSERT INTO user_details VALUES(?,?)";
		PreparedStatement ps1;
		try {
			ps1 = conn.prepareStatement(queryString);
			ps1.setString(1, username);
			ps1.setString(2,password);
			int i = ps1.executeUpdate();
			
			if(i>0)
			{
				creationStatus = true;
			}
			
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return creationStatus;
		}
		
	}
	
	/* This function will create a new group */
	public boolean createGroup(Connection conn, String groupname, String creator)
	{
		boolean creationStatus = false;
		String queryString = "INSERT INTO group_members VALUES(?,?,?)";
		PreparedStatement ps1;
		try {
			ps1 = conn.prepareStatement(queryString);
			ps1.setString(1, groupname);
			ps1.setString(2,creator);
			ps1.setString(3,creator);
			int i = ps1.executeUpdate();
			
			if(i>0)
			{
				creationStatus = true;
			}
			
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return creationStatus;
		}
	
	}

	/* This function will add a member in the group */
	public boolean addGroupMember(Connection conn, String groupname, String member)
	{
		boolean creationStatus = false;
		String queryString = "INSERT INTO group_members VALUES(?,?,NULL)";
		PreparedStatement ps1;
		try {
			ps1 = conn.prepareStatement(queryString);
			ps1.setString(1, groupname);
			ps1.setString(2,member);
			
			int i = ps1.executeUpdate();
			
			if(i>0)
			{
				creationStatus = true;
			}
			
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return creationStatus;
		}
	

	}
	
	/* This function will store message in DB for retrieval and return the message id 
	 * for the given message if it is stored successfully, else it will return 0 */
	public int storeToSendMessage(Connection conn, String sender, String receiver, String message, String attachmentPath)
	{
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		int messageId = 0;
		//boolean storeStatus = false;
		String queryString = "INSERT INTO message_details(sender,receiver,msg,attachmentpath,senttimestamp) "
				+ "VALUES(?,?,?,?,?) RETURNING mid";
		PreparedStatement ps1;
		try {
			ps1 = conn.prepareStatement(queryString);
			ps1.setString(1,sender);
			ps1.setString(2,receiver);
			ps1.setString(3,message);
			ps1.setString(4,attachmentPath);
			ps1.setTimestamp(5, currentTime);
			ResultSet rs = ps1.executeQuery();
			
			if(rs.next())
			{
				messageId = rs.getInt("mid");
			}
			
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return messageId;
		}
	

		
	}

	/*This function will retrieve the messages from the DB*/
	public ResultSet retrieveMessages(Connection conn, String receiver)
	{
		ResultSet rs = null;
		String queryString = "SELECT * FROM message_details "
				+ "WHERE receiver = ? OR sender = ? OR receiver IN "
				+ " ( SELECT groupname FROM group_members where username = ?) ORDER BY senttimestamp DESC";
		PreparedStatement ps1;
		try {
			ps1 = conn.prepareStatement(queryString);
			ps1.setString(1, receiver);
			ps1.setString(2, receiver);
			ps1.setString(3, receiver);
			 rs = ps1.executeQuery();
		
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return rs;
		}
		
		
	}
}

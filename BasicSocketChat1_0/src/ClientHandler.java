import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClientHandler extends Thread{

	final Socket s;
	final DataInputStream in;
	final DataOutputStream out;
	
	
	public ClientHandler(Socket s, DataInputStream in, DataOutputStream out)
	{
		this.s = s;
		this.in = in;
		this.out = out;
		
		synchronized (Server.activeClients) {
			Server.activeClients++;
			System.out.println("Client Added : Number of active clients : "+Server.activeClients);
		}
		
	}
	
	@Override
	public void run()
	{
		String rcvd = "";
		String toreturn;
		
		//Initiate a connection with the database - 
		DBOperations dbOps = new DBOperations();
		Connection conn = dbOps.connectToDB();
		
		
			String queryString = null;
			

			try {
				
			
				
				while(!rcvd.equals("exit"))
				{
					//Chat server options
					toreturn = "CHAT MESSENGER \n"
							+ "1) Login\n"
							+ "2) Signup\n"
							+ "Enter option : ";
					this.out.writeUTF(toreturn); 
					
					//Client Response
					rcvd = this.in.readUTF();
					
					if(rcvd.equals("1"))
					{
						//Authenticate User
						this.out.writeUTF("Username : ");
						String userName = this.in.readUTF();
						this.out.writeUTF("Password : ");
						String pass = this.in.readUTF();
						
						
						if(dbOps.authenticateUser(conn, userName, pass))
						{
							//User Authenticated Successfully - Chat Options active for user
							toreturn = "To know more about supported functions type help";
							while(!rcvd.equals("exit"))
							{
								toreturn+="\n=> ";
								this.out.writeUTF(toreturn);
								toreturn = "";
								rcvd = this.in.readUTF();
								String[] args = rcvd.split("--",-1);
								
								String option = args[0];
								
								if(option.equals("SendMsg"))
								{
									// Store the message in Data base
									String sendTo = args[1];
									String msg = args[2];
									
									if(dbOps.storeToSendMessage(conn, userName, sendTo, msg, "")<1)
										toreturn = "Unable to send message";										
									
								}
								else if(option.equals("RcvdMsg"))
								{
									//Retrieve Data from database
									ResultSet rs = dbOps.retrieveMessages(conn, userName);
									String str = "";
									try {
										System.out.println("MessageId \t Sender \t Receiver \t Message \t Sent Time");
										str+=("MessageId \t Sender \t Receiver \t Sent Time \t\t Attachment \t\t Message\n");
										while(rs.next())
										{
											System.out.println(rs.getInt("mid") + " \t\t "
													+ rs.getString("sender") + " \t\t "
													+ rs.getString("receiver") + " \t "
													+ rs.getString("msg") + " \t "
													+ rs.getTimestamp("senttimestamp") + " \t ");
											
											
											str+=(rs.getInt("mid") + " \t\t "
													+ rs.getString("sender") + " \t\t "
													+ rs.getString("receiver") + " \t "
													+ rs.getTimestamp("senttimestamp") + " \t "
													+ rs.getString("attachmentpath") + " \t "
													+ rs.getString("msg") + " \n");
											
											
										}
										
										if(str.equals(""))
											toreturn="No messages";
										else
											toreturn = str;
										
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										System.out.println("Unable to retrieve results");
										toreturn = "Unable to retrieve results";
									}
									
								}
								else if(option.equals("SendAttach"))
								{
									//Save attachment log in db and file in filesystem
									String sendTo = args[1];
									this.out.writeUTF("Upload File Path : ");
									String fileClientPath = this.in.readUTF();
									String[] vals = fileClientPath.split(".",-1);
									String clientFileName = "Attachment."+vals[vals.length-1];
									
									int msgId = dbOps.storeToSendMessage(conn, userName, sendTo, "", clientFileName);
									
									String directoryPath = "C:\\Users\\Bharati\\Desktop\\"+"serverFiles"+File.separator+msgId;
									FileOperations fOps = new FileOperations();
									if(!fOps.downloadFile(directoryPath, clientFileName, in))
										toreturn = "Unable to send attachment";
									else
										toreturn = "Sent File";
									
								}
								else if(option.equals("DwnldAttach"))
								{
									//Send attachment to client
									String msgId = args[1];
									this.out.writeUTF("Download File Path : ");
									String clientFileDownloadPath = this.in.readUTF();
									FileOperations fOps = new FileOperations();
									String filePath = "serverFiles"+File.separator+msgId;
									fOps.uploadFile(filePath, out);
								}
								else
								{
									//Invalid Option
									toreturn = "Invalid Option";
									
								}
							}
							
						}
						else
						{
							//User Authentication failed
							toreturn = "Invalid username/password";
							this.out.writeUTF(toreturn);
						}
						
					}
					else if(rcvd.equals("2"))
					{
						//Create account/user
						this.out.writeUTF("Enter new Username : ");
						String userName = this.in.readUTF();
						this.out.writeUTF("Enter new Password : ");
						String pass = this.in.readUTF();
						if(dbOps.createUser(conn, userName, pass))						
							this.out.writeUTF("Successfully created a new user");
						else
							this.out.writeUTF("Unable to create a new user");
					}
					else if(!rcvd.equals("exit"))
					{
						// Invalid option
						this.out.writeUTF("Invalid Option.");
					}
					
				}
				
				
				
				
				
				System.out.println("CLosing client Thread");
				
				synchronized (Server.activeClients) {
					Server.activeClients--;
					System.out.println("Client Deleted : Number of active clients : "+Server.activeClients);
				}
				
				
				
				dbOps.closeDBConnection(conn);
				this.in.close();
				this.out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		
			
			
		
				
		
	}
}

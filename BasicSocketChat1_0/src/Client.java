import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	//initialize socket and io streams
	private Socket socket = null;
	private BufferedReader input = null;
	
	
	//constructor to initialize IP and port
	public Client(String ip, int port)
	{
		DataOutputStream out = null;
		DataInputStream in = null;
		
		//establish a connection
		System.out.println("establish a connection");
		try {
			socket = new Socket(ip,port);
			System.out.println("Client Initialized Successfully");
			
			//Take input from terminal
			input = new BufferedReader(new InputStreamReader(System.in));
						
			out = new DataOutputStream(socket.getOutputStream()); // Sends output to the socket
			in = new DataInputStream(socket.getInputStream()); // Gets input from the socket
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to initialize client : Unknown Host Exception");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to initialize client : IO Exception");
			e.printStackTrace();
		}
		
		//String to read message from input
		String line = "";
		
		try {
			//Limitation introduced : read message just till 1st \r, \n or EOF 
			System.out.println("Wait for client input and write to socket");
			while(!line.equals("exit"))
			{
				String serverMsg = in.readUTF();
				System.out.print(serverMsg);
				
				line = input.readLine();
				out.writeUTF(line);
				
				//Upload the file if an attachment is sent
				if(serverMsg.equals("Upload File Path : "))
				{
					//Code to upload the file
					FileInputStream fis = new FileInputStream(line);
					byte[] fileBuffer = new byte[4096];
					
					while(fis.read(fileBuffer)>0)
					{
						out.write(fileBuffer);
					}
					 
					fis.close();
				}
				else if(serverMsg.equals("Download File Path : "))
				{
					//Code to download file 
					FileOutputStream fos = new FileOutputStream(line);
					byte[] fileBuffer = new byte[4096];
					
					while(in.read(fileBuffer,0,fileBuffer.length)>0)
					{
						fos.write(fileBuffer);
					}
					
				}
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Close the connection
		try {
			System.out.println("Close the io streams and socket  at client");
			input.close();
			out.close();
			socket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		
		
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Client c = new Client("127.0.0.1",5000);

	}

}

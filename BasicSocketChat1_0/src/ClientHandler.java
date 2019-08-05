import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread{

	final Socket s;
	final DataInputStream in;
	final DataOutputStream out;
	
	
	public ClientHandler(Socket s, DataInputStream in, DataOutputStream out)
	{
		this.s = s;
		this.in = in;
		this.out = out;
	}
	
	@Override
	public void run()
	{
		String rcvd;
		String toreturn;
		//while(true)
		{
			rcvd = in.readUTF();
			
			if(rcvd.substring(0, 4).equals("SENDMSG"))
			{
				
			}
			else if(rcvd.substring(0, 4).equals("RECEIVEDMSGS"))
			{
				
			}	
		}
	}
}

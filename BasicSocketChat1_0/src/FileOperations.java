import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOperations {
	
	/* This function will upload the file present at filePath with 
	 * the help of out parameter of type DataOutputStream */
	public boolean uploadFile(String filePath,DataOutputStream out)
	{
		boolean uploadStatus = true;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			uploadStatus = false;
			System.out.println("File not found.");
			e.printStackTrace();
		}
		byte[] fileBuffer = new byte[4096];
		
		try {
			while(fis.read(fileBuffer)>0)
			{
				out.write(fileBuffer);
			}
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			uploadStatus = false;
			System.out.println("Unable to upload the file.");
			e.printStackTrace();
		}
		
		return uploadStatus;
	}

	/*This function will help download the file from DataInputStream in,
	 *  at downloadDir with name fileName*/
	public boolean downloadFile(String downloadDir, String fileName, DataInputStream in)
	{
		boolean downloadStatus = true;
		
		File newDirectory = new File(downloadDir);
		boolean isCreated = newDirectory.mkdirs();
		if(!(isCreated || newDirectory.exists()))
		{
			downloadStatus = false;
		}
		else
		{
			String filePath = downloadDir + File.separator + fileName;
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(filePath);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				downloadStatus = false;
				System.out.println("File not found");
				e.printStackTrace();
			}
			byte[] fileBuffer = new byte[4096];
			
			try {
				while(in.read(fileBuffer,0,fileBuffer.length)>0)
				{
					fos.write(fileBuffer);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				downloadStatus = false;
				System.out.println("Unable to download the file");
				e.printStackTrace();
			}
		}
		
		return downloadStatus;
	}
	
}

package com.simple.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* TODO:
 * -Better handling of client disconnection on the client side
 */

public class ThinSocketClient {
	private String clientName;;

	// TCP Components
	private Socket socket = null;
	private BufferedReader inStream = null;
	private PrintWriter outStream = null;
	private int portNum = -1;
	private String hostName = null;
	private int timeOut = 0;

	// *********************************** Main Method ********************

	public static void main(String args[]) {

		Scanner reader = new Scanner(System.in); // Reading from System.in
		System.out.println("Enter comma separated list of client IDs: ");
		String n = reader.next();
		String[] clients = n.split(",");
		reader.close();

		ExecutorService es = Executors.newFixedThreadPool(clients.length);
		for (int i = 0; i < clients.length; i++) 
		{
			String id = clients[i].trim();
			es.execute(new Runnable() 
			{
				@Override
				public void run() {
					new ThinSocketClient(id);
				}
			});
		}//end for loop
		es.shutdown();
	}

	// *********************************** Constructor ********************
	public ThinSocketClient(String id) {

		// Auto generate client name.

		this.clientName = id;

		// System.out.println("ClientName:"+ clientName);

		// Read the properties file to get the connection info to the server and
		// other preferences
		ReadProperties rp = new ReadProperties();
		rp.loadFile("config.properties");

		portNum = Integer.valueOf(rp.getPropertyValue("portNum"));
		hostName = (rp.getPropertyValue("hostName")).trim();
		timeOut = Integer.valueOf(rp.getPropertyValue("timeOut"));

		if (portNum < 0 || portNum > 65535) {
			System.out.println("Please supply a valid port number");
			return;
		}

		if (hostName == null || hostName == "") {
			System.out.println("Please supply a valid host name or IP address");
			return;
		}

		try {
			// Establish connection to the server
			openSocket();

			// If everything has been initialized then we want to read/write
			// some data to/from the socket
			if (socket != null && outStream != null && inStream != null) {
				runClient();

			}
		} finally {
			try {
				if (outStream != null)
					outStream.close();
				if (inStream != null)
					inStream.close();
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}// end constructor

	// ********************** Method to establish connection with the server	 ********************
	private void openSocket() {
		try {
			System.out.println(
					"Client" + clientName + " is trying to connect with server:" + hostName + " at port:" + portNum);

			// create socket
			InetAddress inetAddress = InetAddress.getByName(hostName);
			if (!inetAddress.isReachable(timeOut * 1000)) {
				System.out.println("Error! Unable to connect with server.\nServer IP Address may be wrong.");
				System.exit(1);
			}

			socket = new Socket(hostName, portNum);
			inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outStream = new PrintWriter(socket.getOutputStream(), true);

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host:" + hostName);
		} catch (SocketException e) {
			System.out.println("Unable to connect to server.Please check the hostName and port number in the config fileA,C \n" + e);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to" + hostName);
		}
	}// end method openSocket



	// *********************************** Method to transfer data and read response from server ********************
	public void runClient() 
	{
		//Initiate a new print job 
		//Send the client name to the server
		//Message in the format: "name A"
		
		outStream.println("name " + clientName);
		try
		{
			//Read job creation response message in the form: "job status 1 created"
			String response = inStream.readLine();
			if (response.indexOf("job ") == 0) 
			{
				printJobStatus(response);
			}//end if (response.indexOf("job ") == 0)

			//Read job completion status message in the form: "job status 1 complete"
			response = inStream.readLine();
			if (response.indexOf("job ") == 0) 
			{
				printJobStatus(response);
			}//end if (response.indexOf("job ") == 0) 

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}// end method runClient
	
	// *********************************** Method to print the job status ********************
	private void printJobStatus(String response)
	{
		String[] messageSplit = response.split(" ");
		if (messageSplit.length >= 4) 
		{
			String action = messageSplit[1];
			int id = Integer.parseInt(messageSplit[2]);
			String status = messageSplit[3];

			if (action.equals("status")) {
				System.out.println("Client " + clientName + ": Job " + id + " status: " + status);
			}
		}//end if (messageSplit.length >= 4) 		
	}//end method printJobStatus

}// end class ThinSocketClient
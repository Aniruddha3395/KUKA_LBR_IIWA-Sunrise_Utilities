package utilities;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * This is the TCP/IP communication class. </br>
 * For the communication and data transfer, Robot is the server and Master PC (if communicating from PC) is client. </br>
 * Class constructor is two arguments - robot IP address and socket port number (IP address is not utilized for creating the server socket). </br>
 * For each class method description, description is added on the how client code should look like (in Python & MATLAB programming language). </br> 
 * To start connection, class object should call establishConnection() method first. </br>
 * Class object should always close the socket server with closeConenction() method. </br>
 */
public class TcpipCommunication {
	private int port;
	private String robot_ip_address;
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	
	public TcpipCommunication(String robot_ip_address, int port)
	{
		this.robot_ip_address = robot_ip_address;
		this.port = port;
	}
	
	/**
	 * Establishes communication between robot server and client PC. </p>
	 * Client code (Python) - </p> 
	 * <code>
	 * import socket</br>
	 * import sys</br>
	 * import time</br>
	 * HOST = '<put server ip address>'</br>
	 * PORT = <put port number></br>
	 * sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)</br>
	 * a = sock.connect((HOST, PORT))</br>
	 * sock.sendall(str.encode("Establish Communication\r\n"))</br>
	 * data = sock.recv(1024)</br>
	 * if "Okay" in data:</br>
	 *	print("communication_established to the robot!")</br>
	 * </code>
	 * </p>
	 * Client code (MATLAB) - </p> 
	 * <code>
	 * HOST = '<put server ip address>';</br>
	 * PORT = <put port number>;</br>
	 * comm = tcpip(HOST,PORT,'NetworkRole','Client');</br>
	 * fopen(comm);</br>
	 * fprintf(comm,'Establish Communication');</br>
	 * data = fscanf(comm,'%s');</br>
	 * if (strcmp(data,'Okay')==1)</br>
	 * disp('communication_established to the robot!');</br>
	 * end</br>
	 * </code> 
	 */
	public void establishConnection()
	{
		try
		{
			serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(0);
			System.out.println("================================== \n" + 
					"Robot Server Socket opened at - \n\n" + 
					"host ip: " + robot_ip_address + "\n" +
					"port: " + serverSocket.getLocalPort() + 
					"\n==================================");
			 
			// waiting for client to connect to the robot server
			socket = serverSocket.accept();
			System.out.println("connection request received from the client...");
			Scanner connection_scanner = new Scanner(socket.getInputStream());
			String str =connection_scanner.nextLine();
			if (str.equals("Establish Communication"))
			{
				DataOutputStream connection_output = new DataOutputStream(socket.getOutputStream());
				PrintWriter connection_writer = new PrintWriter(connection_output, true);
				connection_writer.println("Okay");			
				System.out.println("Communication Established!");
			}
		}
		catch(IOException e)
		{
			System.out.println("Server exception while establishing connection: " + e.getMessage());
            e.printStackTrace();
		};
	}
	
	/**
	 * Receive data from the client. </p>
	 * Client code (Python)- </p>
	 * <code>
	 * sock.sendall(str.encode("Establish Communication\r\n"))</br>
	 * </code>
	 * </p>
	 * Client code (MATLAB)- </p>
	 * <code>
	 * fprintf(comm,data);</br>
	 * </code>
	 */
	public String getClientRequest()
	{
		String data = "";
		try
		{
			Scanner data_scan = new Scanner(socket.getInputStream());
			data = data_scan.nextLine();
		}
		catch(IOException e)
		{
			System.out.println("Server exception while receiving data : " + e.getMessage());
            e.printStackTrace();
		};
		return data;
	}
	
	/**
	 * Send data to the client.</p>
	 * Client code (Python)- </p>
	 * <code>
	 * data = sock.recv(1024)</br>
	 * </code>
	 * </p>
	 * Client code (MATLAB)- </p>
	 * <code>
	 * data = fscanf(comm,'%s');</br>
	 * </code>
	 */
	public void sendClientResponse(String resp_str)
	{
		try
		{
			DataOutputStream resp = new DataOutputStream(socket.getOutputStream());
            PrintWriter write_data = new PrintWriter(resp, true);
            write_data.println(resp_str);
		}
		catch(IOException e)
		{
			System.out.println("Server exception while sending data : " + e.getMessage());
            e.printStackTrace();
		};
		
	}
	
	/**
	 * Close the socket connection.</p>
	 * Client code (Python)- </p>
	 * <code>
	 * sock.close()</br>
	 * </code>
	 * </p>
	 * Client code (MATLAB)- </p>
	 * <code>
	 * delete(comm);</br>
	 * </code>
	 */
	public void closeConenction()
	{
		try
		{	
			socket.close();
            serverSocket.close();
		}
		catch(IOException e)
		{
			System.out.println("Server exception while closing socket : " + e.getMessage());
            e.printStackTrace();
		}
	}
		
		
		
		
}
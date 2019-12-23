// File 	: contactProbingExecution.java
// Author 	: Aniruddha Shembekar, Research Engineer, University of Southern California

package utilities;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This is the TCP/IP communication class. </br>
 * For the communication and data transfer, Robot is the server and Master PC (if communicating from PC) is client. </br>
 * This class API also has functionality to use robot as client and PC as a server </br>
 * Class constructor is two arguments - robot IP address and socket port number (IP address is not utilized for creating the server socket). </br>
 * For each class method description, description is added on the how client code should look like (in Python & MATLAB programming language). </br> 
 * To start connection, class object should call establishConnection() method first. </br>
 * Class object should always close the socket server with closeConenction() method. </br>
 */
public class TcpipComm {
	private int port;
	private String ip_address;
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private int socket_timeout = 0;
	public String socket_type = "client";
	
	/**
	 * TCP/IP communication class for connection with PC
	 * if using this class for robot as server, add robot's IP address 
	 * if using this class for robot as client, add PC's IP address
	 * @param robot_ip_address
	 * @param port
	 * @param socket_type : "client" or "server"
	 * @param socket_timeout : Default value is 0 (which waits for indefinite time)
	 */
	public TcpipComm(String ip_address, int port, String socket_type, int socket_timeout)
	{
		this.ip_address = ip_address;
		this.port = port;
		this.socket_type = socket_type;
		this.socket_timeout = socket_timeout;
	}
	
	/**
	 * TCP/IP communication class for connection with PC
	 * if using this class for robot as server, add robot's IP address 
	 * if using this class for robot as client, add PC's IP address
	 * @param robot_ip_address
	 * @param port
	 * @param socket_type : "client" or "server"
	 */
	public TcpipComm(String ip_address, int port, String socket_type)
	{
		this.ip_address = ip_address;
		this.port = port;
		this.socket_type = socket_type;
	}
	
	/**
	 * TCP/IP communication class for connection with PC
	 * if using this class for robot as server, add robot's IP address 
	 * if using this class for robot as client, add PC's IP address
	 * @param robot_ip_address
	 * @param port
	 * @param socket_timeout : Default value is 0 (which waits for indefinite time)
	 */
	public TcpipComm(String ip_address, int port, int socket_timeout)
	{
		this.ip_address = ip_address;
		this.port = port;
		this.socket_timeout = socket_timeout;
	}
	
	/**
	 * TCP/IP communication class for connection with PC
	 * if using this class for robot as server, add robot's IP address 
	 * if using this class for robot as client, add PC's IP address
	 * @param robot_ip_address
	 * @param port
	 */
	public TcpipComm(String ip_address, int port)
	{
		this.ip_address = ip_address;
		this.port = port;
	}
	
	/**
	 * Establishes communication between robot server and client PC. </p>
	 * Client code/Server code (CPP) - </p> 
	 * <code>
     * if (Socket_Type.compare("client")==0){ </br>
     * sockfd = socket(AF_INET, SOCK_STREAM, 0); </br>
     * if (sockfd == -1) { std::cout << "Client socket creation failed..." << std::endl; exit(0);} </br>
     * else{std::cout << "Client Socket successfully created...!" << std::endl; } </br>
     * bzero(&servaddr, sizeof(servaddr));   servaddr.sin_family = AF_INET; servaddr.sin_addr.s_addr = inet_addr(ip_address.c_str()); servaddr.sin_port = htons(port); </br>
     * connect(sockfd, (SA*)&servaddr, sizeof(servaddr));std::string str = "Establish Communication\r\n";write(sockfd, str.c_str(), sizeof(str)); </br>
     * bzero(buff, sizeof(buff)); read(sockfd, buff, sizeof(buff)); std::string data(buff);</br>
     * if (data.find("Okay")==0){std::cout << "Communication established to the robot!" << std::endl;primesockfd = sockfd;return true;}</br>
     * else{std::cout << "Communication could not be established..." << std::endl;primesockfd = sockfd;return false;}}</br>
     * if (Socket_Type.compare("server")==0){</br>
     * sockfd = socket(AF_INET, SOCK_STREAM, 0); int option = 1;setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &option, sizeof(option));</br>
     * if (sockfd < 0){ std::cout << "Server socket creation failed..." << std::endl;exit(1); } </br>
     * else{std::cout << "Server Socket successfully created...!" << std::endl; }</br>
     * bzero(&servaddr, sizeof(servaddr));   servaddr.sin_family = AF_INET; servaddr.sin_addr.s_addr = INADDR_ANY;servaddr.sin_port = htons(port); </br>
     * if (bind(sockfd, (struct sockaddr *) &servaddr, sizeof(servaddr)) < 0) {perror("Binding ERROR");exit(1);}</br>
     * listen(sockfd,5);clilen = sizeof(cliaddr);newsockfd = accept(sockfd, (struct sockaddr *) &cliaddr, (socklen_t*) &clilen);</br>
     * if (newsockfd < 0) {perror("Accept ERROR");exit(1);}</br>
     * bzero(buff, sizeof(buff)); read(newsockfd, buff, sizeof(buff));std::string data(buff);data = data.substr(0,data.size()-2);</br>
     * if (data.compare("Establish_Communication")==0){std::string str = "Okay\r\n"; write(newsockfd, str.c_str(), sizeof(str)); </br>
     * std::cout << "Communication established to the robot!" << std::endl;primesockfd = newsockfd;return true;}</br>
     * else{std::cout << "Communication could not be established to the robot!" << std::endl;primesockfd = newsockfd;return false;}} </br>
	 * </code> 
	 */
	
	public void establishConnection()
	{
		if (socket_type.equals("server"))
		{
			try
			{
				serverSocket = new ServerSocket(port);
				serverSocket.setSoTimeout(socket_timeout);
				
				System.out.println("================================== \n" + 
						"Robot Server Socket opened at - \n\n" + 
						"host ip: " + ip_address + "\n" +
						"port: " + serverSocket.getLocalPort() + 
						"\n==================================");
				 
				// waiting for client to connect to the robot server
				socket = serverSocket.accept();
				System.out.println("connection request received from the client...");
				Scanner connection_scanner = new Scanner(socket.getInputStream());
				String str =connection_scanner.nextLine();
				System.out.println(str);
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
			}
		}
		if (socket_type.equals("client"))
		{
			System.out.println("================================== \n" + 
					"Robot Client Socket opened at - \n\n" + 
					"host ip: " + ip_address + "\n" +
					"port: " + port + 
					"\n==================================");
			while(true)
			{
			
				try
				{
					socket = new Socket(ip_address, port);
					DataOutputStream resp = new DataOutputStream(socket.getOutputStream());
					PrintWriter write_data = new PrintWriter(resp, true);
		            write_data.println("Establish_Communication");
		            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		            String str = br.readLine();	            
					if (str.equals("Okay"))
					{
						System.out.println("Communication Established!");
						break;
					}
				}
				catch(Exception e)
				{
					Timedelay.wait_milliseconds(10);
				}
			}
		}
	}
	
	/**
	 * Receive String from the client/server. </p>
	 * Client/Server code (CPP)- </p>
	 * <code>
	 * void RobotComm::sendString(std::string str){</br>
	 * std::string out_data = str + "\r\n";</br>
	 * send(primesockfd, out_data.c_str(), out_data.length()*sizeof(char), 0); }; </br>
	 * </code>
	 */
	
	public String getStringRequest()
	{
		String data = "";
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			data = br.readLine();
		}
		catch(IOException e)
		{
			System.out.println("Server exception while receiving data : " + e.getMessage());
            e.printStackTrace();
		};
		return data;
	}
	
	/**
	 * Receive Integer from the client/server. </p>
	 * Client/Server code (CPP)- </p>
	 * <code>
	 * void RobotComm::sendInt(int data1){</br>
	 * std::string out_data = std::to_string(data1) + "\r\n";</br>
	 * send(primesockfd, out_data.c_str(), sizeof(out_data), 0); }; </br>
	 * </code>
	 */
	
	public int getIntRequest()
	{
		String data = "";
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			data = br.readLine();
		}
		catch(IOException e)
		{
			System.out.println("Server exception while receiving data : " + e.getMessage());
            e.printStackTrace();
		};
		return Integer.valueOf(data);
	}
	
	/**
	 * Receive Double from the client/server. </p>
	 * Client/Server code (CPP)- </p>
	 * <code>
	 * void RobotComm::sendDouble(double data1){</br>
	 * std::string out_data = std::to_string(data1) + "\r\n";</br>
	 * send(primesockfd, out_data.c_str(), sizeof(out_data), 0); }; </br>
	 * </code>
	 */
	
	public double getDoubleRequest()
	{
		String data = "";
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			data = br.readLine();
		}
		catch(IOException e)
		{
			System.out.println("Server exception while receiving data : " + e.getMessage());
            e.printStackTrace();
		};
		return Double.valueOf(data);
	}
	
	/**
	 * Receive Integer Array from the client/server. </p>
	 * Client/Server code (CPP)- </p>
	 * <code>
	 * void RobotComm::sendIntArray(std::vector<int> data){std::string out_data = "";</br>
	 * for (int i=0;i<data.size();++i){if (i==data.size()-1){</br>
	 * out_data += std::to_string(data[i]) + "\r\n";continue;} </br>
	 * out_data += std::to_string(data[i]) + ",";}</br>
	 * send(primesockfd, out_data.c_str(), out_data.length()*sizeof(char) ,0);}; </br>
	 * </code>
	 */
	
	public int[] getIntArrayRequest()
	{
		String [] data_arr = null;
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String data = br.readLine();
			data_arr = data.split(",");
		}
		catch(IOException e)
		{
			System.out.println("Server exception while receiving data : " + e.getMessage());
            e.printStackTrace();
		};
		int []arr = new int[data_arr.length];
		for (int i=0;i<arr.length;++i) arr[i] = Integer.valueOf(data_arr[i]);
		return arr;
	}
	
	/**
	 * Receive Double Array from the client/server. </p>
	 * Client/Server code (CPP)- </p>
	 * <code>
	 * void RobotComm::sendDoubleArray(std::vector<double> data){std::string out_data = "";</br>
	 * for (int i=0;i<data.size();++i)</br>
	 * {if (i==data.size()-1){out_data += std::to_string(data[i]) + "\r\n";continue;} </br>
	 * out_data += std::to_string(data[i]) + ",";}</br>
	 * send(primesockfd, out_data.c_str(), out_data.length()*sizeof(char) ,0);};</br>
	 * </code>
	 */
	
	public double[] getDoubleArrayRequest()
	{
		String [] data_arr = null;
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String data = br.readLine();
			data_arr = data.split(",");
		}
		catch(IOException e)
		{
			System.out.println("Server exception while receiving data : " + e.getMessage());
            e.printStackTrace();
		};
		double []arr = new double[data_arr.length];
		for (int i=0;i<arr.length;++i) arr[i] = Double.valueOf(data_arr[i]);
		return arr;
	}
	
	/**
	 * Receive Integer Matrix from the client/server. </p>
	 * Client/Server code (CPP)- </p>
	 * <code>
	 * void RobotComm::sendIntMatrix(std::vector<std::vector<int>> data){sendInt(data.size());</br>
	 * receiveString();for (int i=0;i<data.size();++i){sendIntArray(data[i]);receiveString(); }}; </br>
	 * </code>
	 */
	
	public ArrayList<int[]> getIntMatrixRequest()
	{
		int data_size = getIntRequest();
		sendStringResponse("s");
		ArrayList <int[]> data_mat = new ArrayList<int[]>(data_size);
		for (int i=0;i<data_size;++i)
		{
			int [] data_arr = getIntArrayRequest();	
			data_mat.add(i,data_arr);
			sendStringResponse("y");
		}
		return data_mat;
	}

	/**
	 * Receive Double Matrix from the client/server. </p>
	 * Client/Server code (CPP)- </p>
	 * <code>
	 * void RobotComm::sendDoubleMatrix(std::vector<std::vector<double>> data){sendInt(data.size());receiveString();</br>
	 * for (int i=0;i<data.size();++i){sendDoubleArray(data[i]);receiveString();}}; </br>
	 * </code>
	 */
	
	public ArrayList<double[]> getDoubleMatrixRequest()
	{
		int data_size = getIntRequest();
		sendStringResponse("s");
		ArrayList <double[]> data_mat = new ArrayList<double[]>(data_size);
		for (int i=0;i<data_size;++i)
		{
			double [] data_arr = getDoubleArrayRequest();	
			data_mat.add(i,data_arr);
			sendStringResponse("y");
		}
		return data_mat;
	}
	
	/**
	 * Send String to the client/server. </p>
	 * Client/Server code (CPP)- </p>
	 * <code>
	 * std::string RobotComm::receiveString(){bzero(buff, sizeof(buff)); read(primesockfd, buff, sizeof(buff));</br>
	 * std::string in_data(buff);return in_data.substr(0,in_data.size()-2);}; </br>
	 * </code>
	 */
	
	public void sendStringResponse(String resp_str)
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
	 * Send Integer to the client/server. </p>
	 * Client/Server code (CPP)- </p>
	 * <code>
	 * int RobotComm::receiveInt(){bzero(buff, sizeof(buff)); read(primesockfd, buff, sizeof(buff)); </br>
	 * std::string in_data(buff);return std::stoi(in_data.substr(0,in_data.size()-2));};</br>
	 * </code>
	 */
	
	public void sendIntResponse(int resp_num)
	{
		try
		{
			DataOutputStream resp = new DataOutputStream(socket.getOutputStream());
            PrintWriter write_data = new PrintWriter(resp, true);
            write_data.println(resp_num);
		}
		catch(IOException e)
		{
			System.out.println("Server exception while sending data : " + e.getMessage());
            e.printStackTrace();
		};
	}
	
	/**
	 * Send Double to the client/server. </p>
	 * Client/Server code (CPP)- </p>
	 * <code>
	 * double RobotComm::receiveDouble(){bzero(buff, sizeof(buff)); read(primesockfd, buff, sizeof(buff));</br>
	 * std::string in_data(buff); return std::stod(in_data.substr(0,in_data.size()-2));}; </br>
	 * </code>
	 */
	
	public void sendDoubleResponse(double resp_num)
	{
		try
		{
			DataOutputStream resp = new DataOutputStream(socket.getOutputStream());
            PrintWriter write_data = new PrintWriter(resp, true);
            write_data.println(resp_num);
		}
		catch(IOException e)
		{
			System.out.println("Server exception while sending data : " + e.getMessage());
            e.printStackTrace();
		};
	}
	
	/**
	 * Send Integer Array to the client/server. </p>
	 * Client/Server code (CPP)- </p>
	 * <code>
	 * std::vector <int> RobotComm::receiveInt1DVec(){int size = receiveInt();</br>
	 * sendString("y");std::vector<int> arr;for (int i=0;i<size;++i){</br>
	 * arr.push_back(receiveInt());sendString("y");}return arr;}; </br>
	 * </code>
	 */
	
	public void sendIntArrayResponse(int[] resp_arr)
	{
		sendIntResponse(resp_arr.length);
		getStringRequest();
		for (int i=0;i<resp_arr.length;++i)
		{
			sendIntResponse(resp_arr[i]);
			getStringRequest();
		}
	}
	
	/**
	 * Send Double Array to the client/server. </p>
	 * Client/Server code (CPP)- </p>
	 * <code>
	 * std::vector <int> RobotComm::receiveDouble1DVec(){int size = receiveInt();</br>
	 * sendString("y");std::vector<int> arr;for (int i=0;i<size;++i){ </br>
	 * arr.push_back(receiveDouble());sendString("y");}return arr;}; </br>
	 * </code>
	 */
	
	public void sendDoubleArrayResponse(double[] resp_arr)
	{
		sendDoubleResponse(resp_arr.length);
		getStringRequest();
		for (int i=0;i<resp_arr.length;++i)
		{
			sendDoubleResponse(resp_arr[i]);
			getStringRequest();
		}
	}
	
	/**
	 * Send Integer Matrix to the client/server. </p>
	 * Client/Server code (CPP)- </p>
	 * <code>
	 * std::vector<std::vector <int>> RobotComm::receiveIntVec(){int rows = receiveInt(); </br>
	 * sendString("y");int cols = receiveInt();sendString("y");std::vector<std::vector <int> > mat;</br>
	 * for (int i=0;i<rows;++i){mat.push_back(receiveInt1DVec());}return mat;};</br>
	 * </code>
	 */

	public void sendIntMatrixResponse(ArrayList<int[]> resp_mat)
	{
		sendIntResponse(resp_mat.size());
		getStringRequest();
		sendIntResponse(resp_mat.get(0).length);
		getStringRequest();
		for (int i=0;i<resp_mat.size();++i)
		{
			sendIntArrayResponse(resp_mat.get(i));
		}
	}

	/**
	 * Send Double Matrix to the client/server. </p>
	 * Client/Server code (CPP)- </p>
	 * <code>
	 * std::vector<std::vector <double>> RobotComm::receiveDoubleVec(){int rows = receiveInt();</br>
	 * sendString("y");int cols = receiveInt();sendString("y");std::vector<std::vector <double> > mat;</br>
	 * for (int i=0;i<rows;++i){mat.push_back(receiveDouble1DVec());}return mat;}; </br>
	 * </code>
	 */
	
	public void sendDoubleMatrixResponse(ArrayList<double[]> resp_mat)
	{
		sendIntResponse(resp_mat.size());
		getStringRequest();
		sendIntResponse(resp_mat.get(0).length);
		getStringRequest();
		for (int i=0;i<resp_mat.size();++i)
		{
			sendDoubleArrayResponse(resp_mat.get(i));
		}
	}
	
	/**
	 * Close the socket connection.</p>
	 * Client/Server code (CPP)- </p>
	 * <code>    
	 * void RobotComm::closeComm(){if (Socket_Type.compare("client")==0){ </br>
	 * std::cout << "closing socket for client..." << std::endl;close(sockfd); close(primesockfd); }</br>
	 * if (Socket_Type.compare("server")==0){std::cout << "closing socket for server..." << std::endl;</br>
	 * close(sockfd); close(primesockfd);}};</br>
	 * </code>
	 */
	
	public void closeConenction()
	{
		try
		{	
			socket.close();
			if (socket_type.equals("server"))
			{
	            serverSocket.close();	
			}
		}
		catch(IOException e)
		{
			System.out.println("Server exception while closing socket : " + e.getMessage());
            e.printStackTrace();
		}
		finally 
		{
		    try 
		    {  
		    	if (!socket.isClosed()){
		    		socket.close();
		    	}
				if (socket_type.equals("server"))
				{
			    	if (!serverSocket.isClosed()){
			    		serverSocket.close();
			    	}
				}
		    } 
		    catch(Exception e) {}
		}
	}
	
}		// class end


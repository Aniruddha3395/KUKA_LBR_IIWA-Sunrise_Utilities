# KUKA LBR IIWA SUNRISE UTILITIES

These utility classes provide the ease in using the kuka functions for robot motions, communication and data transfer, reading and writing to files, etc.

## Classes Description:

### ReadWriteUtilities

Prints the 2D ArrayList of any object type.
```
public static <T> void display2DListGeneric(ArrayList<T[]> o)
```
Prints the 2D ArrayList of primitive type integer.
```
public static void display2DListInt(ArrayList<int[]> o)
```
Prints the 2D ArrayList of primitive type double.
```
public static void display2DListDouble(ArrayList<double[]> o)
```
Prints the 2D ArrayList of String type.
```
public static void display2DListString(ArrayList<String[]> o)
```
Prints the ArrayList of any object type.
```
public static <T> void displayListGeneric(ArrayList<T> o)
```
Reads data from file and store into the 2D ArrayList of primitive double type.
```
public static ArrayList<double[]> readDelimiterFileDouble(String file_name)
```
Reads data from file and store into the 2D ArrayList of primitive integer type.
```
public static ArrayList<int[]> readDelimiterFileInt(String file_name)
```
Reads data from file and store into the 2D ArrayList of String type.
```
public static ArrayList<String[]> readDelimiterFileString(String file_name)
```
Reads data from file and store into the ArrayList of String type.
```
public static ArrayList<String> readFile(String file_name)
```
File Writer Method - 
Creates the file writer object for the file provided as an argument (provide complete network path with file name). After calling initFileWriter(String), call Write<type>ToFile() methods for writing data into file. Each call to the Write<type>ToFile() method creates data on the new line. Do not forget to call closeFile() method at the end or else no data will be written to the file.
```
public void initFileWriter(String file_name)
public void writeStringToFile(String str)
public void writeIntToFile(int i)
public void writeDoubleToFile(double d)
public <T> void writeObjectToFile(T obj)
public void closeFile()
```

### Timedelay

stops program execution for given time
```
public static void wait_minutes(int t)
public static void wait_seconds(int t)
public static void wait_milliseconds(int t)
public static void wait_microseconds(int t)
public static void wait_nanoseconds(int t)
```

### TcpipComm

This is the TCP/IP communication class.
For the communication and data transfer, Robot is the server and Master PC (if communicating from PC) is client. 
This class API also has functionality to use robot as client and PC as a server.

Class constructor is two arguments - robot IP address and socket port number (IP address is not utilized for creating the server socket).

For each class method description, description is added on the how client code should look like (in Python & MATLAB programming language).
To start connection, class object should call establishConnection() method first.
Class object should always close the socket server with closeConenction() method.

class constructor:
```
public TcpipComm(String ip_address, int port, String socket_type, int socket_timeout)
public TcpipComm(String ip_address, int port, String socket_type)
public TcpipComm(String ip_address, int port, int socket_timeout)
public TcpipComm(String ip_address, int port)
```

establish communication:
```
public void establishConnection()
```
end communication
```
public void closeConenction()
```
for methods on data sending and receiveing, please refer the javadoc from the code.

### RobotMotion

Class constructor
```
public RobotMotion(LBR lBR_iiwa_14_R820_1, ObjectFrame tcp)
```
Sets general robot motion speed (relative). Default value is 0.1.
```
public void setSpeed(double robSpeed)
```
Sets robot Impedance parameters. Default stiffness value is 1800, 1800 and 1200 along x, y and z axes respectively.
```
public void setImpedance(CartesianImpedanceControlMode impedanceMode)
```
Sets the tolerance for robot joint angle limits. Default tolerance is 0.
```
public void setjointsBoundTol(double jointsBoundTol)
```
sets approach distance
```
public void setApproachDistance(double approachDistance)
```
sets retract distance
```
public void setLeavingDistance(double leavingDistance)
```
Sets approach trajectory speed. Default value is 0.1.
```
public void setApproachTrajectorySpeed(double approachTrajectorySpeed)
```
Sets retract trajectory speed. Default value is 0.2
```
public void setRetractTrajectorySpeed(double retractTrajectorySpeed)
```
Performs ptp motion using joint angles
```
public void executePtpMotionFromJointConfig(ArrayList<double[]> joint_config)
```
Performs ptp motion for a joint_config
```
public void executePtpMotionFromJointConfig(double[] joint_state)
```
Performs ptp motion using x, y, z and euler angles ZYX.
```
public void executePtpMotionFromCartesianConfig(ArrayList<double[]> cartesian_config, Frame F)
```
Performs ptp motion for a cartesian state using x, y, z and euler angles ZYX.
```
public void executePtpMotionFromCartesianConfig(double[] cartesian_state, Frame F)
```
Performs ptp motion using x, y, z and euler angles ZYX.
```
public void executePtpMotionFromCartesianConfig(ArrayList<double[]> cartesian_config)
```
Performs ptp motion for a cartesian config using x, y, z and euler angles ZYX.
```
public void executePtpMotionFromCartesianConfig(double[] cartesian_state)
```
Performs spline motion using joint angles.
```
public void executeSplineMotionFromJointConfig(ArrayList<double[]> joint_config)
```
Performs spline motion using x, y, z and euler angles ZYX.
```
public void executeSplineMotionFromCartesianConfig(ArrayList<double[]> cartesian_config, Frame F_in)
```
Performs spline motion using x, y, z and euler angles ZYX.
```
public void executeSplineMotionFromCartesianConfig(ArrayList<double[]> cartesian_config)
```
Performs asynchronous spline motion using joint angles.
```
public IMotionContainer executeAsyncSplineMotionFromJointConfig(ArrayList<double[]> joint_config, int traj_start_pt_idx)
```
Performs asynchronous spline motion using x, y. z and euler angles.
```
public IMotionContainer executeAsyncSplineMotionFromCartesianConfig(ArrayList<double[]> cartesian_config, Frame F_in, int traj_start_pt)
```
Performs asynchronous spline motion using x, y. z and euler angles.
```
public IMotionContainer executeAsyncSplineMotionFromCartesianConfig(ArrayList<double[]> cartesian_config, int traj_start_pt)
```
Performs continuous spline motion using joint angles.
```
public void executeContinuousSplineMotion(ArrayList<double[]> joint_config)
```
Approach to the trajectory.
```
public void approachTrajectory(ArrayList<double[]> joint_config)
```
Retracts from the trajectory.
```
public void exitTrajectory(Frame F)
```


// File 	: contactProbingExecution.java
// Author 	: Aniruddha Shembekar, Research Engineer, University of Southern California

package utilities;

import static com.kuka.roboticsAPI.motionModel.BasicMotions.lin;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.linRel;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.ptp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.kuka.roboticsAPI.deviceModel.JointPosition;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.executionModel.CommandInvalidException;
import com.kuka.roboticsAPI.geometricModel.CartDOF;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;
import com.kuka.roboticsAPI.geometricModel.math.Vector;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.sensorModel.ForceSensorData;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianImpedanceControlMode;


public class contactProbingExecution {
	
	private LBR lBR_iiwa_14_R820_1;
	private ArrayList<double[]> joint_config;
	private ArrayList<double[]> cartesian_config;
	private ObjectFrame tool_tip;
	private double limiting_force_val;
	private double probeSpeed;
	private double robSpeed;
	private String data_unit = "meters";
	private ArrayList<double[]> probe_data = new ArrayList<double[]>();
	private CartesianImpedanceControlMode probeImpedence = new CartesianImpedanceControlMode();
	private CartesianImpedanceControlMode approachImpedence = new CartesianImpedanceControlMode();
	private ArrayList<double[]> probed_data = new ArrayList<double[]>();
	private int record_frequency;
	private double probing_approach_dist;
	
	/** 
	 * Class constructor
	 * @param rob_in
	 * @param tool_tip
	 * @param joint_config
	 * @param cartesian_config
	 */
	public contactProbingExecution(LBR rob_in, ObjectFrame tool_tip, ArrayList<double[]> joint_config, ArrayList<double[]> cartesian_config)
	{
		lBR_iiwa_14_R820_1 = rob_in;
		this.tool_tip = tool_tip;
		limiting_force_val = 35.0;
		probeSpeed = 0.03;
		robSpeed = 0.1;
		probeImpedence.parametrize(CartDOF.X).setStiffness(4000);
		probeImpedence.parametrize(CartDOF.Y).setStiffness(4000);
		probeImpedence.parametrize(CartDOF.Z).setStiffness(50);	
		probeImpedence.parametrize(CartDOF.ROT).setStiffness(300);		
		probeImpedence.parametrize(CartDOF.Z).setAdditionalControlForce(10);
		approachImpedence.parametrize(CartDOF.Z).setStiffness(500);
		this.joint_config = joint_config;
		this.cartesian_config = cartesian_config;	
		record_frequency = 20;
		probing_approach_dist = 30;
	}
	
	/**
	 * Sets probing speed of robot
	 * @param probeSpeed
	 */
	public void setProbingSpeed(double probeSpeed)
	{
		this.probeSpeed = probeSpeed;
	}
	
	/**
	 * Sets probing impedance for robot
	 * @param probeImpedence
	 */
	public void setProbingImpedance(CartesianImpedanceControlMode probeImpedence)
	{
		this.probeImpedence = probeImpedence;
	}
	
	
	/** 
	 * Sets data output unit
	 * if argument is "mm", returned xyz values are in millimeters. </br>
	 * if argument is "meters", returned xyz values are in meters. </br> 
	 * @param data_unit
	 */
	public void setOutputUnit(String data_unit)
	{
		this.data_unit = data_unit;
	}
	
	/**
	 * Sets record frequency.</br>
	 * Default record frequency is 20 Hz. </br>
	 * @param record_frequency
	 */
	public void setRecordFrequency(int record_frequency)
	{
		this.record_frequency = record_frequency;
	}
	
	/** 
	 * Creates approach trajectory for probing. </br>
	 * @param joint_config_partial
	 */
	public void approachSurfaceForProbing(ArrayList<double[]> joint_config_partial)
	{
		IMotionContainer mymotion = null;
		double depth_reduceer = 0;
		try
		{
			JointPosition start_joint_state = new JointPosition(joint_config_partial.get(0));
			Frame F_st = null;
			int approach_dist_reducer = 0;
			while(true && approach_dist_reducer<probing_approach_dist)
			{
				try
				{
					F_st = lBR_iiwa_14_R820_1.getForwardKinematic(start_joint_state);
					F_st.setZ(F_st.getZ()+probing_approach_dist-approach_dist_reducer); 
					lBR_iiwa_14_R820_1.getFlange().move(ptp(F_st).setJointVelocityRel(0.3).setMode(approachImpedence));
					break;
				}
				catch(Exception e)
				{
					System.out.println("This approach point is not possible, reducing approach gap...");
					approach_dist_reducer+=10.0;
				}
			}
			F_st = lBR_iiwa_14_R820_1.getCurrentCartesianPosition(tool_tip);
			boolean flag = true;
			while(flag)
			{
				depth_reduceer = depth_reduceer+0.5;
				F_st.setZ(F_st.getZ()-depth_reduceer);
				mymotion =  tool_tip.moveAsync(lin(F_st).setJointVelocityRel(0.35).setMode(approachImpedence));
				while (!mymotion.isFinished() && !mymotion.hasError())
				{
					ForceSensorData flangeforce = lBR_iiwa_14_R820_1.getExternalForceTorque(tool_tip);
					Vector Fforce = flangeforce.getForce();		
					double Fz= Fforce.getZ();
					if (Fz<-15)
					{
						System.out.println("Force value exceeded, starting probing motion...");
						flag = false;
						mymotion.cancel();					
					}
					Timedelay.wait_milliseconds(10);
				}
			}
		}
		catch(CommandInvalidException e)
		{
			System.out.println("Motion Failed");
			mymotion.cancel();
			return;
		}
		if (mymotion!=null)
		{
			mymotion.cancel();
		}		
	}
	
	/** 
	 * Returns the index of nearest point.</p> 
	 * @param x
	 * @param y
	 * @param z
	 * @param cartesian_config
	 */
	public static int findNearestNeighbour(double x, double y,double z, ArrayList<double[]> cartesian_config)
	{	
		//input coordinates of point and array of points from which neighbor is to be found
		double dist_val;
		double prev_dist = 1e5;
		int idx = 0;
		int best_idx = 0;
		for (double [] cartesian_state : cartesian_config)
		{
			dist_val = Math.sqrt(Math.pow((cartesian_state[0]-x),2) + Math.pow((cartesian_state[1]-y),2)+ Math.pow((cartesian_state[2]-z),2));
			if (dist_val<prev_dist)
				{prev_dist = dist_val;best_idx = idx;}
			idx++;
		}
		return best_idx;
	}
	
	/** 
	 * start with index 0...it will recursively modify index value appropriately. </br>
	 * specify the motion function here...(works with 'Async' Motion function only). </p>
	 * @param closest_pt_idx
	*/
	private void dataCollectionUnderImpedenceControl(int closest_pt_idx)
	{
	// using joint config
	RobotMotion probeMotion = new RobotMotion(lBR_iiwa_14_R820_1, tool_tip);
	probeMotion.setSpeed(probeSpeed);
	probeMotion.setImpedance(probeImpedence);
	IMotionContainer MyMotion = probeMotion.executeAsyncSplineMotionFromJointConfig(joint_config, closest_pt_idx);
	
	// using cartesian config
	// IMotionContainer MyMotion = executeAsyncSplineMotionFromCartesianConfig(cartesian_config, tcp, F_in, closest_pt_idx);
	
	int N = cartesian_config.size();
	while(!MyMotion.isFinished())
	{
			ForceSensorData flangeforce = lBR_iiwa_14_R820_1.getExternalForceTorque(tool_tip);
			
			Vector Fforce = flangeforce.getForce();		
			double Fx= Fforce.getX();
			double Fy= Fforce.getY();
			double Fz= Fforce.getZ();
			if (Fz<-3.0)
			{
//				System.out.println(Fz);
		        Frame store_frame = lBR_iiwa_14_R820_1.getCurrentCartesianPosition(lBR_iiwa_14_R820_1.getFlange());
		        double[] xyz_cba = new double[6];
		        xyz_cba[0] = store_frame.getX();
		        xyz_cba[1] = store_frame.getY();
		        xyz_cba[2] = store_frame.getZ();
		        xyz_cba[3] = store_frame.getAlphaRad();
		        xyz_cba[4] = store_frame.getBetaRad();
		        xyz_cba[5] = store_frame.getGammaRad();
		        probed_data.add(xyz_cba);
			}
			if (Math.abs(Fx)>limiting_force_val || Math.abs(Fy)>limiting_force_val)
			{
				MyMotion.cancel();
				Frame F_st = lBR_iiwa_14_R820_1.getCurrentCartesianPosition(tool_tip);
				try{
					tool_tip.move(linRel(0, 0, -50).setJointVelocityRel(robSpeed));
					System.out.println("Going up, Fx : " + Math.abs(Fx) + " , Fy : " + Math.abs(Fy));
					Timedelay.wait_milliseconds(5000);
				}
				catch (Exception e)
				{
					System.out.println("Going up failed");
				}
				try{
					if (Fx>limiting_force_val){tool_tip.move(linRel(-20, 0, 0).setJointVelocityRel(probeSpeed).setMode(probeImpedence));};
					if (Fx<-limiting_force_val){tool_tip.move(linRel(20, 0, 0).setJointVelocityRel(probeSpeed).setMode(probeImpedence));};
					if (Fy>limiting_force_val){tool_tip.move(linRel(0, -20, 0).setJointVelocityRel(probeSpeed).setMode(probeImpedence));};
					if (Fy<-limiting_force_val){tool_tip.move(linRel(0, 20, 0).setJointVelocityRel(probeSpeed).setMode(probeImpedence));};					
				}
				catch(Exception e){
					System.out.println("Going sideways failed");
					System.out.println("ADD CODES TO SEND ERROR TO INTERFACE");
					break;					
				}
				closest_pt_idx = findNearestNeighbour(F_st.getX(),F_st.getY(),F_st.getZ(),cartesian_config);
				if (N>closest_pt_idx)
				{
					dataCollectionUnderImpedenceControl(closest_pt_idx+5);
				}
			}
			int delay = (int) 1000/record_frequency;
			Timedelay.wait_milliseconds(delay);
		}
	}
	
	/** 
	 * Returns the probed data as ArrayList of double[].</br>
	 */
	public ArrayList<double[]> executeProbingTrajectory() 
	{				
		try 
		{
			dataCollectionUnderImpedenceControl(0);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return probed_data;
	}
	
	/** 
	 * Writes the probed data as into the file.</br>
	 * NOTE: Default unit for x,y and z is meters.</br>
	 * If needs to change the unit, use the .setOutputUnit() method </p>
	 * @param file_name
	 * @param probing_data
	 */
	public void writeProbedData(String file_name, ArrayList<double[]> probing_data)
	{
		File write_file = new File(file_name);
		if (write_file.exists())
		{
			try
			{
				write_file.delete();	
			}
			catch (Exception e)
			{
				System.out.println("Can not delete the existing file...");
			}
		}
		try 
		{
			write_file.createNewFile(); 
			write_file.setWritable(true);
			write_file.setReadable(true);
		} 
		catch (IOException e1) 
		{
			System.out.println("Not able to create the new file...");
		}
		FileWriter fr;
		try 
		{
			fr = new FileWriter(write_file,false);
			PrintWriter out = new PrintWriter(fr);
			int dataset_size = probing_data.size();
			for (int i=0;i<dataset_size;i++)
			{
				// Storing xyzcba
				if (data_unit.equals("meters"))
				{
					// storing the data in meters 
					out.println(probing_data.get(i)[0]/1000 + "," + probing_data.get(i)[1]/1000 + "," +
							probing_data.get(i)[2]/1000 + "," + probing_data.get(i)[3] + "," +
							probing_data.get(i)[4] + "," + probing_data.get(i)[5]);
				}
				else if (data_unit.equals("mm"))
				{
					// storing the data in mm
					 out.println(probing_data.get(i)[0] + "," + probing_data.get(i)[1] + "," +
							 probing_data.get(i)[2] + "," + probing_data.get(i)[3] + "," +
							 probing_data.get(i)[4] + "," + probing_data.get(i)[5]);
				}
			}
			out.flush();
			probing_data.clear();
			if (fr != null)
			{
				try 
				{
					fr.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}	
			}
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
	}
	
	public void performCompleteContactBasedDataCollection(Frame F, ArrayList<int[]> grp_idx, String write_file)
	{	
		contactProbingExecution exeObj = null;
		for (int i=0; i<grp_idx.size(); i++)
		{
			tool_tip.move(ptp(F).setJointVelocityRel(0.5));
			ArrayList<double[]> joint_config_partial = new ArrayList<double[]>(joint_config.subList(grp_idx.get(i)[0]-1, grp_idx.get(i)[1]));
			ArrayList<double[]> cartesian_config_partial = new ArrayList<double[]>(cartesian_config.subList(grp_idx.get(i)[0]-1, grp_idx.get(i)[1]));
			exeObj = new contactProbingExecution(lBR_iiwa_14_R820_1, tool_tip, joint_config_partial, cartesian_config_partial);
			exeObj.setProbingSpeed(probeSpeed);
			exeObj.setOutputUnit(data_unit);
			exeObj.setProbingImpedance(probeImpedence);
			exeObj.setRecordFrequency(record_frequency);
			exeObj.approachSurfaceForProbing(joint_config_partial);
			System.out.println("executing probing: "+i);
			probe_data.addAll(exeObj.executeProbingTrajectory());
		}
		
		exeObj.writeProbedData(write_file, probe_data);
	}

	
	

}

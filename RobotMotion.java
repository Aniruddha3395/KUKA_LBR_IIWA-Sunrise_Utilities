package utilities;


import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;
import java.util.ArrayList;

import com.kuka.roboticsAPI.deviceModel.*;
import com.kuka.roboticsAPI.geometricModel.CartDOF;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.PTP;
import com.kuka.roboticsAPI.motionModel.SPL;
import com.kuka.roboticsAPI.motionModel.Spline;
import com.kuka.roboticsAPI.motionModel.SplineJP;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianImpedanceControlMode;

public class RobotMotion {
	
	private LBR lBR_iiwa_14_R820_1;
	private ObjectFrame tcp;
	private double robSpeed;
	private CartesianImpedanceControlMode impedanceMode = new CartesianImpedanceControlMode();
	private CartesianImpedanceControlMode retractImpedanceMode = new CartesianImpedanceControlMode();
	private double jointsBoundTol;
	private double approachDistance;
	private double leavingDistance;
	private double approachTrajectorySpeed;
	private double retractTrajectorySpeed;
	
	/**
	 * Class constructor
	 * @param lBR_iiwa_14_R820_1
	 * @param tcp
	 */
	public RobotMotion(LBR lBR_iiwa_14_R820_1, ObjectFrame tcp)
	{
		this.lBR_iiwa_14_R820_1 = lBR_iiwa_14_R820_1;
		this.tcp = tcp;
		robSpeed = 0.1;
		approachTrajectorySpeed = 0.1;
		retractTrajectorySpeed = 0.2;
		impedanceMode.parametrize(CartDOF.X).setStiffness(1800);
		impedanceMode.parametrize(CartDOF.Y).setStiffness(1800);
		impedanceMode.parametrize(CartDOF.Z).setStiffness(1200);
		retractImpedanceMode.parametrize(CartDOF.X).setStiffness(300);
		retractImpedanceMode.parametrize(CartDOF.Y).setStiffness(300);
		retractImpedanceMode.parametrize(CartDOF.Z).setStiffness(300);
		jointsBoundTol = 0;
		approachDistance = 100; // mm
		leavingDistance = 20; // mm
	}
	
	/**
	 * Sets general robot motion speed.</br>
	 * Default value is 0.1. </br>
	 * @param robSpeed
	 */
	public void setSpeed(double robSpeed)
	{
		this.robSpeed = robSpeed;
	}

	/**
	 * Sets robot Impedance parameters.</br>
	 * Default stiffness value is 1800, 1800 and 1200 along x, y and z axes respectively.</br>
	 * @param impedanceMode
	 */
	public void setImpedance(CartesianImpedanceControlMode impedanceMode)
	{
		this.impedanceMode = impedanceMode;
	}
	
	/**
	 * Sets the tolerance for robot joint angle limits.</br>
	 * Default tolerance is 0.</br>
	 * @param jointsBoundTol
	 */
	public void setjointsBoundTol(double jointsBoundTol)
	{
		this.jointsBoundTol = jointsBoundTol;
	}
	
	/**
	 * 
	 * @param approachDistance
	 */
	public void setApproachDistance(double approachDistance)
	{
		this.approachDistance = approachDistance;
	}
	
	/**
	 * 
	 * @param leavingDistance
	 */
	public void setLeavingDistance(double leavingDistance)
	{
		this.leavingDistance = leavingDistance;
	}

	/**
	 * Sets approach trajectory speed.</br>
	 * Default value is 0.1. </br>
	 * @param approachTrajectorySpeed
	 */
	public void setApproachTrajectorySpeed(double approachTrajectorySpeed)
	{
		this.approachTrajectorySpeed = approachTrajectorySpeed;
	}
	
	/**
	 * Sets retract trajectory speed.</br>
	 * Default value is 0.2. </br>
	 * @param retractTrajectorySpeed
	 */
	public void setRetractTrajectorySpeed(double retractTrajectorySpeed)
	{
		this.retractTrajectorySpeed = retractTrajectorySpeed;
	}
	
	/**
	 * Performs ptp motion using joint angles  </br>
	 * @param joint_config
	 */
	public void executePtpMotionFromJointConfig(ArrayList<double[]> joint_config)
	{
		int N = joint_config.size();
		if (N != 0)
		{
			System.out.println("Executing ptp motion from joint angles..."); 
			for (double [] joint_state : joint_config) 
			{
				try
				{
					if (joint_state[0]<-2.96706+jointsBoundTol){joint_state[0]  = -2.96706+jointsBoundTol;} 
					else if (joint_state[0]>2.96706-jointsBoundTol){joint_state[0]  = 2.96706-jointsBoundTol;};
					if (joint_state[1]<-2.0944+jointsBoundTol){joint_state[1]  = -2.0944+jointsBoundTol;} 
					else if (joint_state[1]>2.0944-jointsBoundTol){joint_state[1]  = 2.0944-jointsBoundTol;};
					if (joint_state[2]<-2.96706+jointsBoundTol){joint_state[2]  = -2.96706+jointsBoundTol;} 
					else if (joint_state[2]>2.96706-jointsBoundTol){joint_state[2]  = 2.96706-jointsBoundTol;};
					if (joint_state[3]<-2.0944+jointsBoundTol){joint_state[3]  = -2.0944+jointsBoundTol;}	
					else if (joint_state[3]>2.0944-jointsBoundTol){joint_state[3]  = 2.0944-jointsBoundTol;};
					if (joint_state[4]<-2.96706+jointsBoundTol){joint_state[4]  = -2.96706+jointsBoundTol;} 
					else if (joint_state[4]>2.96706-jointsBoundTol){joint_state[4]  = 2.96706-jointsBoundTol;};
					if (joint_state[5]<-2.0944+jointsBoundTol){joint_state[5]  = -2.0944+jointsBoundTol;}	
					else if (joint_state[5]>2.0944-jointsBoundTol){joint_state[5]  = 2.0944-jointsBoundTol;};
					if (joint_state[6]<-3.05433+jointsBoundTol){joint_state[6]  = -3.05433+jointsBoundTol;} 
					else if (joint_state[6]>3.05433-jointsBoundTol){joint_state[6]  = 3.05433-jointsBoundTol;};
					
					tcp.move( ptp( joint_state[0],joint_state[1],joint_state[2],joint_state[3],joint_state[4],joint_state[5],joint_state[6] )
								.setJointVelocityRel(robSpeed).setMode(impedanceMode));
				}
				catch(Exception e)
				{
					System.out.println(" Motion is not possible at - "); 
					System.out.println(joint_state[0]+","+joint_state[1]+","+joint_state[2]+","+joint_state[3]+","+joint_state[4]+","+joint_state[5]+
							","+ joint_state[5]+","+joint_state[6]); 
				}
			}
		}
		else
		{
			System.out.println("0 waypoints found");
		}
	}
	
	/**
	 * Performs ptp motion for a joint_config </br>
	 * @param joint_states
	 */
	public void executePtpMotionFromJointConfig(double[] joint_state)
	{
		System.out.println("Executing ptp motion for a given joint config..."); 
		try
		{
			if (joint_state[0]<-2.96706+jointsBoundTol){joint_state[0]  = -2.96706+jointsBoundTol;} 
			else if (joint_state[0]>2.96706-jointsBoundTol){joint_state[0]  = 2.96706-jointsBoundTol;};
			if (joint_state[1]<-2.0944+jointsBoundTol){joint_state[1]  = -2.0944+jointsBoundTol;} 
			else if (joint_state[1]>2.0944-jointsBoundTol){joint_state[1]  = 2.0944-jointsBoundTol;};
			if (joint_state[2]<-2.96706+jointsBoundTol){joint_state[2]  = -2.96706+jointsBoundTol;} 
			else if (joint_state[2]>2.96706-jointsBoundTol){joint_state[2]  = 2.96706-jointsBoundTol;};
			if (joint_state[3]<-2.0944+jointsBoundTol){joint_state[3]  = -2.0944+jointsBoundTol;}	
			else if (joint_state[3]>2.0944-jointsBoundTol){joint_state[3]  = 2.0944-jointsBoundTol;};
			if (joint_state[4]<-2.96706+jointsBoundTol){joint_state[4]  = -2.96706+jointsBoundTol;} 
			else if (joint_state[4]>2.96706-jointsBoundTol){joint_state[4]  = 2.96706-jointsBoundTol;};
			if (joint_state[5]<-2.0944+jointsBoundTol){joint_state[5]  = -2.0944+jointsBoundTol;}	
			else if (joint_state[5]>2.0944-jointsBoundTol){joint_state[5]  = 2.0944-jointsBoundTol;};
			if (joint_state[6]<-3.05433+jointsBoundTol){joint_state[6]  = -3.05433+jointsBoundTol;} 
			else if (joint_state[6]>3.05433-jointsBoundTol){joint_state[6]  = 3.05433-jointsBoundTol;};
			
			tcp.move( ptp( joint_state[0],joint_state[1],joint_state[2],joint_state[3],joint_state[4],joint_state[5],joint_state[6] )
						.setJointVelocityRel(robSpeed).setMode(impedanceMode));
		}
		catch(Exception e)
		{
			System.out.println(" Motion is not possible at - "); 
			System.out.println(joint_state[0]+","+joint_state[1]+","+joint_state[2]+","+joint_state[3]+","+joint_state[4]+","+joint_state[5]+
					","+ joint_state[5]+","+joint_state[6]); 
		}
	}
		
	/**
	 * Performs ptp motion using x, y, z and euler angles ZYX.</p>
	 * NOTE: To define the frame - </br>
	 * Frame F = new Frame();</br>
	 * F = getApplicationData().getFrame(<PoseFrame String>).copyWithRedundancy(); </br> 
	 * ...This is used to get the redundancy value for motion.</br>
	 * @param cartesian_config
	 * @param F
	*/
	public void executePtpMotionFromCartesianConfig(ArrayList<double[]> cartesian_config, Frame F)
	{
		// NOTE: To define the frame - 
		////////////////////////////////////////////////////		
		// Frame F = new Frame();
		// F = getApplicationData().getFrame(<PoseFrame String>).copyWithRedundancy();  ...This is used to get the redundancy value for motion.
		////////////////////////////////////////////////////
		
		int N = cartesian_config.size();
        if (N != 0)
        {
        	System.out.println("Executing ptp motion from cartesian waypoints..."); 
        	try
        	{
	        	for (double [] cartesian_state : cartesian_config) 
	        	{
	        		F.setX(cartesian_state[0]);
			    	F.setY(cartesian_state[1]);
			    	F.setZ(cartesian_state[2]);
			    	F.setAlphaRad(cartesian_state[3]);
			    	F.setBetaRad(cartesian_state[4]);
			    	F.setGammaRad(cartesian_state[5]);
			    	tcp.move(ptp(F).setJointVelocityRel(robSpeed).setMode(impedanceMode));
	        	}
        	}
        	catch(Exception e)
        	{
				System.out.println(" Motion is not possible"); 
        	}
    	}
        else
        {
        	System.out.println("0 waypoints found");
        }
	}	
	
	/**
	 * Performs ptp motion for a cartesian state using x, y, z and euler angles ZYX.</p>
	 * NOTE: To define the frame - </br>
	 * Frame F = new Frame();</br>
	 * F = getApplicationData().getFrame(<PoseFrame String>).copyWithRedundancy(); </br> 
	 * ...This is used to get the redundancy value for motion.</br>
	 * @param cartesian_state
	 * @param F
	*/
	public void executePtpMotionFromCartesianConfig(double[] cartesian_state, Frame F)
	{
		// NOTE: To define the frame - 
		////////////////////////////////////////////////////		
		// Frame F = new Frame();
		// F = getApplicationData().getFrame(<PoseFrame String>).copyWithRedundancy();  ...This is used to get the redundancy value for motion.
		////////////////////////////////////////////////////
		
		System.out.println("Executing ptp motion for a given cartesian waypoint..."); 
		try
    	{
    		F.setX(cartesian_state[0]);
	    	F.setY(cartesian_state[1]);
	    	F.setZ(cartesian_state[2]);
	    	F.setAlphaRad(cartesian_state[3]);
	    	F.setBetaRad(cartesian_state[4]);
	    	F.setGammaRad(cartesian_state[5]);
	    	tcp.move(ptp(F).setJointVelocityRel(robSpeed).setMode(impedanceMode));
    	}
    	catch(Exception e)
    	{
			System.out.println(" Motion is not possible"); 
    	}
	}     	

	/**
	 * Performs ptp motion using x, y, z and euler angles ZYX.</p>
	 * @param cartesian_config
	 */
	public void executePtpMotionFromCartesianConfig(ArrayList<double[]> cartesian_config)
	{
		// NOTE: To define the frame - 
		////////////////////////////////////////////////////		
		// Frame F = new Frame();
		// F = getApplicationData().getFrame(<PoseFrame String>).copyWithRedundancy();  ...This is used to get the redundancy value for motion.
		////////////////////////////////////////////////////
		
		int N = cartesian_config.size();
        if (N != 0)
        {
        	System.out.println("Executing ptp motion from cartesian waypoints..."); 
        	try
        	{
	        	for (double [] cartesian_state : cartesian_config) 
	        	{
	        		Frame F = new Frame(cartesian_state[0],cartesian_state[1],cartesian_state[2],
	        							cartesian_state[3],cartesian_state[4],cartesian_state[5]);
	        		tcp.move(ptp(F).setJointVelocityRel(robSpeed).setMode(impedanceMode));
	        	}
        	}
        	catch(Exception e)
        	{
				System.out.println(" Motion is not possible"); 
        	}
    	}
        else
        {
        	System.out.println("0 waypoints found");
        }
	}	
	
	/**
	 * Performs ptp motion for a cartesian config using x, y, z and euler angles ZYX.</p>
	 * @param cartesian_config
	 */
	public void executePtpMotionFromCartesianConfig(double[] cartesian_state)
	{
		// NOTE: To define the frame - 
		////////////////////////////////////////////////////		
		// Frame F = new Frame();
		// F = getApplicationData().getFrame(<PoseFrame String>).copyWithRedundancy();  ...This is used to get the redundancy value for motion.
		////////////////////////////////////////////////////
		
		System.out.println("Executing ptp motion for a given cartesian waypoint..."); 
    	try
    	{
        		Frame F = new Frame(cartesian_state[0],cartesian_state[1],cartesian_state[2],
        							cartesian_state[3],cartesian_state[4],cartesian_state[5]);
        		tcp.move(ptp(F).setJointVelocityRel(robSpeed).setMode(impedanceMode));   	
    	}
    	catch(Exception e)
    	{
			System.out.println(" Motion is not possible"); 
    	}
	}
	
	/**
	 * Performs spline motion using joint angles.</p>
	 * @param joint_config
	*/
	public void executeSplineMotionFromJointConfig(ArrayList<double[]> joint_config)
	{
		int N = joint_config.size();
		if (N != 0)
		{
			System.out.println("Executing spline motion from joint angles..."); 
			PTP[] path = new PTP[N];
			JointPosition joint;
			int idx = 0;
			for (double [] joint_state : joint_config) 
			{
				if (joint_state[0]<-2.96706+jointsBoundTol){joint_state[0]  = -2.96706+jointsBoundTol;} 
				else if (joint_state[0]>2.96706-jointsBoundTol){joint_state[0]  = 2.96706-jointsBoundTol;};
				if (joint_state[1]<-2.0944+jointsBoundTol){joint_state[1]  = -2.0944+jointsBoundTol;} 
				else if (joint_state[1]>2.0944-jointsBoundTol){joint_state[1]  = 2.0944-jointsBoundTol;};
				if (joint_state[2]<-2.96706+jointsBoundTol){joint_state[2]  = -2.96706+jointsBoundTol;} 
				else if (joint_state[2]>2.96706-jointsBoundTol){joint_state[2]  = 2.96706-jointsBoundTol;};
				if (joint_state[3]<-2.0944+jointsBoundTol){joint_state[3]  = -2.0944+jointsBoundTol;}	
				else if (joint_state[3]>2.0944-jointsBoundTol){joint_state[3]  = 2.0944-jointsBoundTol;};
				if (joint_state[4]<-2.96706+jointsBoundTol){joint_state[4]  = -2.96706+jointsBoundTol;} 
				else if (joint_state[4]>2.96706-jointsBoundTol){joint_state[4]  = 2.96706-jointsBoundTol;};
				if (joint_state[5]<-2.0944+jointsBoundTol){joint_state[5]  = -2.0944+jointsBoundTol;}	
				else if (joint_state[5]>2.0944-jointsBoundTol){joint_state[5]  = 2.0944-jointsBoundTol;};
				if (joint_state[6]<-3.05433+jointsBoundTol){joint_state[6]  = -3.05433+jointsBoundTol;} 
				else if (joint_state[6]>3.05433-jointsBoundTol){joint_state[6]  = 3.05433-jointsBoundTol;};
				
				joint = new JointPosition(joint_state[0],
											joint_state[1],
											joint_state[2],
											joint_state[3],
											joint_state[4],
											joint_state[5],
											joint_state[6]); 	
				path[idx++] = new PTP(joint);
			}
			SplineJP curve = new SplineJP(path);
			try
			{
				tcp.move( curve.setJointVelocityRel(robSpeed).setMode(impedanceMode));
			}
			catch (Exception e)
			{
				System.out.println(" Motion is not possible");  
			}	
		}
		else
		{
			System.out.println("0 waypoints found");
		}
	}

	/**
	 * Performs spline motion using x, y, z and euler angles ZYX.</p>
	 * NOTE: To define the frame - </br>
	 * Frame F = new Frame();</br>
	 * F = getApplicationData().getFrame(<PoseFrame String>).copyWithRedundancy(); </br> 
	 * ...This is used to get the redundancy value for motion.</br>
	 * @param cartesian_config
	 * @param F_in
	*/
	public void executeSplineMotionFromCartesianConfig(ArrayList<double[]> cartesian_config, Frame F_in)
	{
		// NOTE: To define the frame - 
		////////////////////////////////////////////////////		
		// Frame F = new Frame();
		// F = getApplicationData().getFrame(<PoseFrame String>).copyWithRedundancy();  ...This is used to get the redundancy value for motion.
		////////////////////////////////////////////////////
		
		int N = cartesian_config.size();
		if (N != 0)
		{
			System.out.println("Executing spline motion from cartesian waypoints..."); 
			Frame F[] = new Frame[N];
			SPL[] path = new SPL[N];
			int idx = 0;
			for (double[] cartesian_state : cartesian_config) 
			{
				F[idx] = F_in.copy();
				F[idx].setX(cartesian_state[0]);
	        	F[idx].setY(cartesian_state[1]);
	        	F[idx].setZ(cartesian_state[2]);
	        	F[idx].setAlphaRad(cartesian_state[3]);
	        	F[idx].setBetaRad(cartesian_state[4]);
	        	F[idx].setGammaRad(cartesian_state[5]);
	        	path[idx]=new SPL(F[idx]);
	        	idx++;
			}
			Spline curve = new Spline(path);
			try
			{
				tcp.move( curve.setJointVelocityRel(robSpeed).setMode(impedanceMode) );		
				
			}
        	catch(Exception e)
        	{
				System.out.println(" Motion is not possible"); 
        	}
		}
		else
		{
			System.out.println("0 waypoints found");
		}
	}	
	
	/**
	 * Performs spline motion using x, y, z and euler angles ZYX.</p>
	 * @param cartesian_config
	*/
	public void executeSplineMotionFromCartesianConfig(ArrayList<double[]> cartesian_config)
	{		
		int N = cartesian_config.size();
		if (N != 0)
		{
			System.out.println("Executing spline motion from cartesian waypoints..."); 
			Frame F[] = new Frame[N];
			SPL[] path = new SPL[N];
			int idx = 0;
			for (double[] cartesian_state : cartesian_config) 
			{
				F[idx] = new Frame(cartesian_state[0], cartesian_state[1], cartesian_state[2], 
									cartesian_state[3], cartesian_state[4], cartesian_state[5]);
				path[idx]=new SPL(F[idx]);
	        	idx++;
			}
			Spline curve = new Spline(path);
			try
			{
				tcp.move( curve.setJointVelocityRel(robSpeed).setMode(impedanceMode) );		
				
			}
        	catch(Exception e)
        	{
				System.out.println(" Motion is not possible"); 
        	}
		}
		else
		{
			System.out.println("0 waypoints found");
		}
	}	
	
	/**
	 * Performs asynchronous spline motion using joint angles.</p>
	 * @param joint_config
	 * @param traj_start_pt_idx
	 */
	public IMotionContainer executeAsyncSplineMotionFromJointConfig(ArrayList<double[]> joint_config, int traj_start_pt_idx)
	{
		IMotionContainer MyMotion = null;
		int N = joint_config.size();
		if (N != 0)
		{
			System.out.println("Executing asynchronized spline motion from joint angles..."); 
			PTP[] path = new PTP[N-traj_start_pt_idx];
			JointPosition joint;
			int idx = 0;
			for (double[] joint_state : joint_config)
			{
				if (joint_state[0]<-2.96706+jointsBoundTol){joint_state[0]  = -2.96706+jointsBoundTol;} 
				else if (joint_state[0]>2.96706-jointsBoundTol){joint_state[0]  = 2.96706-jointsBoundTol;};
				if (joint_state[1]<-2.0944+jointsBoundTol){joint_state[1]  = -2.0944+jointsBoundTol;} 
				else if (joint_state[1]>2.0944-jointsBoundTol){joint_state[1]  = 2.0944-jointsBoundTol;};
				if (joint_state[2]<-2.96706+jointsBoundTol){joint_state[2]  = -2.96706+jointsBoundTol;} 
				else if (joint_state[2]>2.96706-jointsBoundTol){joint_state[2]  = 2.96706-jointsBoundTol;};
				if (joint_state[3]<-2.0944+jointsBoundTol){joint_state[3]  = -2.0944+jointsBoundTol;}	
				else if (joint_state[3]>2.0944-jointsBoundTol){joint_state[3]  = 2.0944-jointsBoundTol;};
				if (joint_state[4]<-2.96706+jointsBoundTol){joint_state[4]  = -2.96706+jointsBoundTol;} 
				else if (joint_state[4]>2.96706-jointsBoundTol){joint_state[4]  = 2.96706-jointsBoundTol;};
				if (joint_state[5]<-2.0944+jointsBoundTol){joint_state[5]  = -2.0944+jointsBoundTol;}	
				else if (joint_state[5]>2.0944-jointsBoundTol){joint_state[5]  = 2.0944-jointsBoundTol;};
				if (joint_state[6]<-3.05433+jointsBoundTol){joint_state[6]  = -3.05433+jointsBoundTol;} 
				else if (joint_state[6]>3.05433-jointsBoundTol){joint_state[6]  = 3.05433-jointsBoundTol;};
				
				joint = new JointPosition(joint_state[0],joint_state[1],joint_state[2],joint_state[3],joint_state[4],joint_state[5],joint_state[6]); 	
				path[idx++] = new PTP( joint );
			}
			SplineJP curve = new SplineJP(path);
			try
			{
				MyMotion =  tcp.moveAsync(curve.setJointVelocityRel(robSpeed).setMode(impedanceMode) );
			}
			catch (Exception e)
			{
				System.out.println(" Motion is not possible");  
			}
		}
		else
		{
			System.out.println("0 waypoints found");
		}
		return MyMotion;
	}	
	
	/**
	 * Performs asynchronous spline motion using x, y. z and euler angles.</p>
	 * NOTE: To define the frame - </br>
	 * Frame F = new Frame();</br>
	 * F = getApplicationData().getFrame(<PoseFrame String>).copyWithRedundancy(); </br> 
	 * ...This is used to get the redundancy value for motion.</br>
	 * @param cartesian_config
	 * @param F_in
	 * @param traj_start_pt
	*/
	public IMotionContainer executeAsyncSplineMotionFromCartesianConfig(ArrayList<double[]> cartesian_config, Frame F_in, int traj_start_pt)
	{
		// NOTE: To define the frame - 
		////////////////////////////////////////////////////		
		// Frame F = new Frame();
		// F = getApplicationData().getFrame(<PoseFrame String>).copyWithRedundancy();  ...This is used to get the redundancy value for motion.
		////////////////////////////////////////////////////
		
		IMotionContainer MyMotion = null;
		int N = cartesian_config.size();
		Frame F[] = new Frame[N-traj_start_pt];
		if (N != 0)
		{
			System.out.println("Executing asynchronized spline motion from cartesian waypoints..."); 
			SPL[] path = new SPL[N-traj_start_pt];
			int idx = 0;
			for (int i = traj_start_pt;i<N;i++)
			{
				F[idx] = F_in.copy();
				F[idx].setX(cartesian_config.get(i)[0]);
				F[idx].setY(cartesian_config.get(i)[1]);
				F[idx].setZ(cartesian_config.get(i)[2]);
				F[idx].setAlphaRad(cartesian_config.get(i)[3]);
				F[idx].setBetaRad(cartesian_config.get(i)[4]);
				F[idx].setGammaRad(cartesian_config.get(i)[5]);
	        	path[idx]=new SPL(F[idx]);
	        	idx++;
			}
			Spline curve = new Spline(path);
			try
			{
				MyMotion = tcp.moveAsync(curve.setJointVelocityRel(robSpeed).setMode(impedanceMode));
			}
        	catch(Exception e)
        	{
				System.out.println(" Motion is not possible"); 
        	}
		}
		else
		{
			System.out.println("0 waypoints found");
		}
		return MyMotion;
	}
	
	/**
	 * Performs asynchronous spline motion using x, y. z and euler angles.</p>
	 * @param cartesian_config
	 * @param traj_start_pt
	*/
	public IMotionContainer executeAsyncSplineMotionFromCartesianConfig(ArrayList<double[]> cartesian_config, int traj_start_pt)
	{
		IMotionContainer MyMotion = null;
		int N = cartesian_config.size();
		Frame F[] = new Frame[N-traj_start_pt];
		if (N != 0)
		{
			System.out.println("Executing asynchronized spline motion from cartesian waypoints..."); 
			SPL[] path = new SPL[N-traj_start_pt];
			int idx = 0;
			for (int i = traj_start_pt;i<N;i++)
			{
				F[idx] = new Frame(cartesian_config.get(i)[0], cartesian_config.get(i)[1], cartesian_config.get(i)[2],
									cartesian_config.get(i)[3], cartesian_config.get(i)[4], cartesian_config.get(i)[5]);
				path[idx]=new SPL(F[idx]);
	        	idx++;
			}
			Spline curve = new Spline(path);
			try
			{
				MyMotion = tcp.moveAsync(curve.setJointVelocityRel(robSpeed).setMode(impedanceMode));
			}
        	catch(Exception e)
        	{
				System.out.println(" Motion is not possible"); 
        	}
		}
		else
		{
			System.out.println("0 waypoints found");
		}
		return MyMotion;
	}
	
	private int executeJPSplineContinuousMotionFromJointConfig(ArrayList<double[]> joint_config, int start, int last_pt)
	{
		int N = last_pt;
		int end_pt = N;
		PTP[] path = new PTP[N-start];
		JointPosition joint;
		int idx = 0;
		for (int i=start;i<N;i++) 
		{
			joint = new JointPosition(joint_config.get(i)[0],joint_config.get(i)[1],joint_config.get(i)[2],
					joint_config.get(i)[3],joint_config.get(i)[4],joint_config.get(i)[5],joint_config.get(i)[6]); 	
			path[idx++] = new PTP(joint);
		}		
		SplineJP curve = new SplineJP(path);
		try
		{
			tcp.move(curve.setJointVelocityRel(robSpeed).setMode(impedanceMode));	
		}
		catch(Exception e)
		{
			end_pt = executeJPSplineContinuousMotionFromJointConfig(joint_config, start, N-1);	
		}
		return end_pt;
	}	
	
	/**
	 * Performs continuous spline motion using joint angles.</br>
	 * if spline computation error occurs during execution, it automatically computes the segmented splines.</p>
	 * @param joint_config
	 */
	public void executeContinuousSplineMotion(ArrayList<double[]> joint_config)
	{
		int a = executeJPSplineContinuousMotionFromJointConfig(joint_config, 0, joint_config.size());
		while (a<joint_config.size()-1)
		{
			a = executeJPSplineContinuousMotionFromJointConfig(joint_config, a+2,joint_config.size());
		}
	}
	
	/**
	 * Approach to the trajectory.</p>
	 * @param joint_config
	 */
	public void approachTrajectory(ArrayList<double[]> joint_config)
	{
		JointPosition start_joint_state = new JointPosition(joint_config.get(0));
		Frame F = lBR_iiwa_14_R820_1.getForwardKinematic(start_joint_state);
		System.out.println("Approaching the trajectory start point...");
		try
		{
			F.setZ(F.getZ() + approachDistance);
			lBR_iiwa_14_R820_1.getFlange().move(ptp(F).setJointVelocityRel(approachTrajectorySpeed).setMode(impedanceMode));
		}
		catch (Exception e)
		{
			System.out.println("can not got to approach point...directly starting the motion"); 
		}
	}
	
	/**
	 * Retracts from the trajectory.</p>
	 * @param F
	 */	
	public void exitTrajectory(Frame F)
	{
		System.out.println("Exiting the trajectory from end point...");
		int i = 1;
		IMotionContainer mtn = null;
		while (i<5)
		{
			try
			{
				F.setZ(F.getZ() + leavingDistance*i);
				mtn = lBR_iiwa_14_R820_1.getFlange().moveAsync(ptp(F).setJointVelocityRel(retractTrajectorySpeed).setMode(retractImpedanceMode));
				i++;
			}
			catch (Exception e)
			{
				System.out.println("can not go to exit point...");
				break;
			}	
		}
		while(!mtn.isFinished())
		{
		}
		// NOTE: Do not delete the commented section
		//		i = 1;
		//		Boolean flag_X = true;
		//		Boolean flag_Y = true;
		//		while (flag_X || flag_Y)
		//		{
		//			try{
		//				if (F.getX()>0)
		//				{
		//					F.setX(F.getX() - leavingDistance*i);
		//				}
		//				else
		//				{
		//					flag_X = false;
		//				}
		//				tcp.move(ptp(F).setJointVelocityRel(retractTrajectorySpeed).setMode(retractImpedanceMode));
		//			}
		//			catch (Exception e)
		//			{
		//				System.out.println("can not go to exit point...");
		//				flag_X = false;
		//			}
		//			try
		//			{
		//				if (F.getY()>0)
		//				{
		//					F.setY(F.getY() + leavingDistance*i);	
		//				}
		//				else
		//				{
		//					F.setY(F.getY() - leavingDistance*i);	
		//				}
		//				tcp.move(ptp(F).setJointVelocityRel(retractTrajectorySpeed).setMode(retractImpedanceMode));
		//			}
		//			catch (Exception e)
		//			{
		//				System.out.println("can not go to exit point...");
		//				flag_Y = false;
		//			}
		//			++i;
		//		}
	}

	
}

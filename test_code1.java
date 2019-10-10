package utilities;


import java.util.ArrayList;

import javax.inject.Inject;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.deviceModel.JointPosition;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.CartDOF;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianImpedanceControlMode;

/**
 * Implementation of a robot application.
 * <p>
 * The application provides a {@link RoboticsAPITask#initialize()} and a 
 * {@link RoboticsAPITask#run()} method, which will be called successively in 
 * the application lifecycle. The application will terminate automatically after 
 * the {@link RoboticsAPITask#run()} method has finished or after stopping the 
 * task. The {@link RoboticsAPITask#dispose()} method will be called, even if an 
 * exception is thrown during initialization or run. 
 * <p>
 * <b>It is imperative to call <code>super.dispose()</code> when overriding the 
 * {@link RoboticsAPITask#dispose()} method.</b> 
 * 
 * @see UseRoboticsAPIContext
 * @see #initialize()
 * @see #run()
 * @see #dispose()
 */
public class test_code1 extends RoboticsAPIApplication {
	@Inject
	private LBR lBR_iiwa_7_R800_1;
	private Tool tool;
	ObjectFrame tool_tip;
	private CartesianImpedanceControlMode mode;
	String start_pos;
	
	@Override
	public void initialize() {
		// initialize your application here
		tool = getApplicationData().createFromTemplate("CompositeLayupProbe");
		tool.attachTo(lBR_iiwa_7_R800_1.getFlange());
		tool_tip = tool.getFrame("/ProbeTip");
		mode = 	new CartesianImpedanceControlMode();
		mode.parametrize(CartDOF.X).setStiffness(3000);
		mode.parametrize(CartDOF.Y).setStiffness(3000);
		mode.parametrize(CartDOF.Z).setStiffness(150);	
		mode.parametrize(CartDOF.ROT).setStiffness(200);		
		mode.parametrize(CartDOF.Z).setAdditionalControlForce(3);
		start_pos = "/P1";
	}

	@Override
	public void run() {
		
		// your application execution starts here
		// going to home position 
		tool_tip.move(ptp(getApplicationData().getFrame("/P1")).setJointVelocityRel(0.5));
		
		
		// read files and store data
		String joint_angles_file_name = "//192.168.10.11/KUKA_Shared/probing_joint_angles.csv";
		String cartesian_waypoints_file_name = "//192.168.10.11/KUKA_Shared/probing_xyz_cba.csv";
		String grp_idx_file_name = "//192.168.10.11/KUKA_Shared/probing_group_idx.csv";
		String write_file_name = "//192.168.10.11/KUKA_Shared/probing_data.csv"; 
		
		ArrayList<double[]> joint_config = ReadWriteUtilities.readDilimiterFileDouble(joint_angles_file_name);
		ArrayList<double[]> cartesian_config = ReadWriteUtilities.readDilimiterFileDouble(cartesian_waypoints_file_name);
		ArrayList<int[]> grp_idx = ReadWriteUtilities.readDilimiterFileInt(grp_idx_file_name);

		if (!joint_config.isEmpty())
		{
			contactProbingExecution m = new contactProbingExecution(lBR_iiwa_7_R800_1, tool_tip, joint_config, cartesian_config);
			Frame F = new Frame();
			F = getApplicationData().getFrame("/P1").copyWithRedundancy();
			m.setRecordFrequency(40);
			m.performCompleteContactBasedDataCollection(F, grp_idx, write_file_name);
			tool_tip.move(ptp(getApplicationData().getFrame("/P1")).setJointVelocityRel(0.5));
		}
		else
		{
			System.out.println("there are no joint angles");
		}
		
	}
}
package utilities;

import java.util.concurrent.TimeUnit;

public class Timedelay {
		
		/**
		 * Pauses execution thread for t minutes. </p>
		 * @param t </br>
		 */
		public static void wait_minutes(int t)
		{
			try{
				TimeUnit.MINUTES.sleep(t);
			}
			catch (Exception e){
				System.out.println("Delay Failed");
			}
		}
	
		/**
		 * Pauses execution thread for t seconds. </p>
		 * @param t </br>
		 */
		public static void wait_seconds(int t)
		{
			try{
				TimeUnit.SECONDS.sleep(t);
			}
			catch (Exception e){
				System.out.println("Delay Failed");
			}
		}
		
		/**
		 *	Pauses execution thread for t milliseconds. </p> 
		 * @param t </br>
		 */
		public static void wait_milliseconds(int t)
		{
			try{
				TimeUnit.MILLISECONDS.sleep(t);
			}
			catch (Exception e){
				System.out.println("Delay Failed");
			}
		}
		
		/**
		 *	Pauses execution thread for t microseconds. </p> 
		 * @param t </br>
		 */
		public static void wait_microseconds(int t)
		{
			try{
				TimeUnit.MICROSECONDS.sleep(t);
			}
			catch (Exception e){
				System.out.println("Delay Failed");
			}
		}
		
		/**
		 *	Pauses execution thread for t nanoseconds. </p> 
		 * @param t </br>
		 */
		public static void wait_nanoseconds(int t)
		{
			try{
				TimeUnit.NANOSECONDS.sleep(t);
			}
			catch (Exception e){
				System.out.println("Delay Failed");
			}
		}
}

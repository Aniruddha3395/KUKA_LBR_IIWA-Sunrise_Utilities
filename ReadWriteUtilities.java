package utilities;

import java.io.*;
import java.util.*;

public class ReadWriteUtilities {
	private BufferedWriter bw = null;
	private FileWriter fw = null;
	
	/**
	 * Prints the 2D ArrayList of any object type.</p>
	 * @param o
	 */
	public static <T> void display2DListGeneric(ArrayList<T[]> o)
	{
		if (o.isEmpty()==false)
		{
		for (int i=0; i<o.size(); ++i)
			{
				String S = "";
				for (int j=0; j<o.get(0).length; ++j)
				{
					S = S + String.valueOf(o.get(i)[j]);
					if (j!=o.get(0).length-1) {
						S = S + ", ";
					}
				}
				System.out.println(S);
			}
		}
		else
		{
			System.out.println("-- Empty List --");
		}
	}

	/**
	 * Prints the 2D ArrayList of primitive type integer.</p>
	 * @param o
	 */
	public static void display2DListInt(ArrayList<int[]> o)
	{
		if (o.isEmpty()==false)
		{
		for (int i=0; i<o.size(); ++i)
			{
				String S = "";
				for (int j=0; j<o.get(0).length; ++j)
				{
					S = S + String.valueOf(o.get(i)[j]);
					if (j!=o.get(0).length-1) {
						S = S + ", ";
					}
				}
				System.out.println(S);
			}
		}
		else
		{
			System.out.println("-- Empty List --");
		}
	}

	/**
	 * Prints the 2D ArrayList of primitive type double.</p>
	 * @param o
	 */
	public static void display2DListDouble(ArrayList<double[]> o)
	{
		if (o.isEmpty()==false)
		{
		for (int i=0; i<o.size(); ++i)
			{
				String S = "";
				for (int j=0; j<o.get(0).length; ++j)
				{
					S = S + String.valueOf(o.get(i)[j]);
					if (j!=o.get(0).length-1) {
						S = S + ", ";
					}
				}
				System.out.println(S);
			}
		}
		else
		{
			System.out.println("-- Empty List --");
		}
	}
	
	/**
	 * Prints the 2D ArrayList of String type.</p>
 	 * @param o
	 */
	public static void display2DListString(ArrayList<String[]> o)
	{
		if (o.isEmpty()==false)
		{
		for (int i=0; i<o.size(); ++i)
			{
				String S = "";
				for (int j=0; j<o.get(0).length; ++j)
				{
					S = S + String.valueOf(o.get(i)[j]);
					if (j!=o.get(0).length-1) {
						S = S + ", ";
					}
				}
				System.out.println(S);
			}
		}
		else
		{
			System.out.println("-- Empty List --");
		}
	}
	
	/**
	 * Prints the ArrayList of any object type.</p>
 	 * @param o
	 */
	public static <T> void displayListGeneric(ArrayList<T> o)
	{
		if (o.isEmpty()==false)
		{
		for (int i=0; i<o.size(); ++i)
			{
					System.out.println(o.get(i));
			}
		}
		else
		{
			System.out.println("-- Empty List --");
		}
	}
	
	/**
	 * Reads data from file and store into the 2D ArrayList of primitive double type.</p>
	 * @param file_name
	 */
	public static ArrayList<double[]> readDelimiterFileDouble(String file_name)
	{
		File file = new File(file_name);
		ArrayList<double[]> ls = new ArrayList<double[]>();
		String str;
		try 
		{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			String[] s_arr = null;
			while ((str = br.readLine())!=null)
			{
				s_arr = str.split(",");
				double[] data = new double[s_arr.length];
				
				for (int i=0;i<s_arr.length;++i) 
				{
					data[i] = Double.valueOf(s_arr[i]);
				}
				ls.add(data);
			}
			br.close();
			fr.close();
		}
		catch (IOException e)
		{
			System.out.println("File Not Found");
		}
		return ls;
	}
	
	/**
	 * Reads data from file and store into the 2D ArrayList of primitive integer type.</p>
	 * @param file_name
	 */
	public static ArrayList<int[]> readDelimiterFileInt(String file_name)
	{
		File file = new File(file_name);
		ArrayList<int[]> ls = new ArrayList<int[]>();
		String str;
		try 
		{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			String[] s_arr = null;
			while ((str = br.readLine())!=null)
			{
				s_arr = str.split(",");
				int[] data = new int[s_arr.length];
				
				for (int i=0;i<s_arr.length;++i) 
				{
					data[i] = Integer.valueOf(s_arr[i]);
				}
				ls.add(data);
			}
			br.close();
			fr.close();
		}
		catch (IOException e)
		{
			System.out.println("File Not Found");
		}
		return ls;
	}
	
	/**
	 * Reads data from file and store into the 2D ArrayList of String type.</p>
	 * @param file_name
	 */
	public static ArrayList<String[]> readDelimiterFileString(String file_name)
	{
		File file = new File(file_name);
		ArrayList<String[]> ls = new ArrayList<String[]>();
		String str;
		try 
		{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			String[] s_arr = null;
			while ((str = br.readLine())!=null)
			{
				s_arr = str.split(",");
				ls.add(s_arr);
			}
			br.close();
			fr.close();
		}
		catch (IOException e)
		{
			System.out.println("File Not Found");
		}
		return ls;
	}
	
	/**
	 * Reads data from file and store into the ArrayList of String type.</p>
	 * @param file_name
	 */
	public static ArrayList<String> readFile(String file_name)
	{
		File file = new File(file_name);
		ArrayList<String> ls = new ArrayList<String>();
		String str;
		try 
		{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			while ((str = br.readLine())!=null)
			{
				ls.add(str);
			}
			br.close();
			fr.close();
		}
		catch (IOException e)
		{
			System.out.println("File Not Found");
		}
		return ls;
	}
	
	/**
	 * File Writer Method - </p>
	 * Creates the file writer object for the file provided as an argument (provide complete network path with file name). </br>
	 * After calling initFileWriter(String), call Write<type>ToFile() methods for writing data into file. </br>
	 * Each call to the Write<type>ToFile() method creates data on the new line. </br>
	 * Do not forget to call closeFile() method at the end or else no data will be written to the file. </br> 
	 * @param file_name
	 */
	public void initFileWriter(String file_name)
	{
		File file = new File(file_name);
		file.setWritable(true);
		file.setReadable(true);
		try 
		{
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes String to the file. </br> 
	 * @param str
	 */
	public void writeStringToFile(String str)
	{
		try
		{
			bw.write(str+ "\n");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes Integer to the file. </br> 
	 * @param i
	 */
	public void writeIntToFile(int i)
	{
		try
		{
			bw.write(String.valueOf(i)+ "\n");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Writes Double to the file. </br> 
	 * @param d
	 */
	public void writeDoubleToFile(double d)
	{
		try
		{
			bw.write(String.valueOf(d)+ "\n");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes Object to the file. </br> 
	 * @param obj
	 */
	public <T> void writeObjectToFile(T obj)
	{
		try
		{
			bw.write(String.valueOf(obj)+ "\n");		
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes the file writer. </br> 
	 */
	public void closeFile()
	{
		try
		{
			bw.close();
			fw.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
}

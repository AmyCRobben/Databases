/*
 * Project 1
 * CSCI 4710 DATABASES, Dr. Yao
 * Amy Robben
 */

import java.lang.*;

import java.util.Scanner;

public class possibleSubsets
{
	public static void main(String[] args)
	{
		/*Defines the user input, String set, which will hold the data to be divided into all possible
		 * subsets.
		 * input is an array that will hold each character extracted from set
		 */
		String set;
		String input[] = new String[20];
		Scanner kb= new Scanner(System.in);
		
		/*length will be used to define the number of values in the data set, as well as the number
		 * of rows to be used in map[][] (our binary table)
		 */
		int length;
		
		//get user input (the set of data)
		
		System.out.println("Enter set of values: ");
			set= kb.nextLine();
			length= set.length();
			
		/* try/catch block is used to determine whether or not the data set values are within
		 * the range 1-20	
		 */
			
		try{
			
			//fill input array with individual values from the user input (create the data set)
			for(int i=0; i<length; i++)
				input[i]= Character.toString(set.charAt(i));
			
			//establishes the numbers of rows and columns needed for our binary table
			double temp= Math.pow(2, length)-1;
			int rows= (int)temp;
			
			int map[][] = new int[rows][length];
			
			/*
			 * fills map with all possible subsets, i.e the binary values for 2^n-1, where n is the 
			 * number of individual values in the data set
			 */
			
			
			for(int i= 0; i<rows; i++)
			{
				int column=0;
				
				
				//converts row number (base 10 value) into binary digits
				
				String binary= Integer.toBinaryString(i+1);
				for(int bit= binary.length()-1; bit>=0; bit--)
				{
					//fills each entry in binary table with proper bit (1 or 0)
					map[i][column]= Integer.parseInt(Character.toString(binary.charAt(bit)));
					column++;
				}
			}
			
			System.out.println("All possible subsets: ");
			
			/*
			 * Iterate through map to check whether each bit for each base 10 value (row) is 
			 * a 1, in which case the corresponding value in input (our data values) will be included
			 * in that particular subset. Then, each subset is printed to the screen.
			 */
			
			for(int i=0; i<rows; i++)
			{
				String subset= "";
				
				for(int column=0; column<length; column++)
				{
					
					if(map[i][column]==1)
						subset += input[column];
					
				}
				
				System.out.print(subset + ", ");
				
			}
			
			//System.out.print("\n"+ rows);
		}
		
		//if data set is out of range, error message will appear
		catch(Exception e)
		{
			System.out.print("Set of data must be in the range 1-20");
		}
		
		
		
		
		
	}
}

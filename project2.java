/*
Amy Robben

October 2, 2016
Databases, Dr. Yao

Project 2- using Java 8.0


************************************************************************************************
PURPOSE OF PROJECT:
When given Super Keys, we are able to distinguish a set of Candidate keys from the entire set
(or relation) of Super Keys. 
A Candidate Key is a Super Key which does not contain a proper subset of a Super Key from within
the relation. Therefore, when given a set of super keys, we can use all binary combinations
(given the size of the super key) to determine all proper subsets of the Super Key; if that 
particular Super Key does not contain a proper subset that is equal to another Super Key within
the relation, we can conclude it is a Candidate Key.
************************************************************************************************
HOW TO USE PROGRAM:
You will be prompted to enter the name of the file that contains a relation of Super Keys
The program will run to determine the Candidate Keys within the relation, then write the output
to a file named "candidate-keys.txt"

Expected Console output:
"Enter the name of the file that contains a relation of superkeys: 
super-keys.txt
Finished"

Expected format for "candidate-keys.txt:"

Current Relation:
ABCF CDF ACDF BCDF ABCDF ABCEF CDEF ACDEF BCDEF ABCDEF 
All Candidate Keys: 
ABCF CDF 

************************************************************************************************
*/
import java.io.*; 
import java.lang.*;
import java.util.Scanner;
import java.util.ArrayList;

public class project2_robben_domaleski_cervantes {

	public static void main(String[] args) throws IOException
	{
		
		//get input from the user (name of the file that contains the realtion of superkeys)
		Scanner kb= new Scanner(System.in);
		System.out.println("Enter the name of the file that contains a relation of superkeys: ");
			String input= kb.nextLine();
			
			
		File inputFile = new File(input);
		Scanner infile = new Scanner(inputFile);
		
		//we use ArrayList to account for possible different sizes of relations.
		//sKeys is used for the user input (relation of superkeys) and cKeys will be used to 
		//hold all found candidate keys within the relation
		
		ArrayList<String> sKeys = new ArrayList<String>();
		ArrayList<String> cKeys = new ArrayList<String>();
		
		//read from the file of superkeys and store in ArrayList sKeys
		while(infile.hasNext())
		{
			sKeys.add(infile.nextLine());
						
		}		
		infile.close();
		
		
		//this outermost for-loop is used to iterate through each superkey
		//overall, we loop through each superkey, create all binary combinations of 
		//proper subsets, then compare those proper subsets to each of the other superkeys
		//to determine whether or not the current superkey is a candidate key
		
		for(int i=0; i<sKeys.size(); i++)
		{
			//extract all characters of the superkey and store them in an ArrayList
			ArrayList<String> chars = new ArrayList<String>();
			//will be used as the storage of all subsets within the superkey that we find
			ArrayList<String> subsets = new ArrayList<String>();
			
			
			//we initialize the superkey to true (in regards to being a candidate key)
			boolean isCandidate = true;
			
			//extract each individual character from the superkey (so we can later use
			//the individual characters to create subsets)
			
			String temp = "";
			for(int a = 0; a<sKeys.get(i).length(); a++)
			{
				temp = Character.toString(sKeys.get(i).charAt(a));
				chars.add(temp);
			}
			
			//create dimensions of binary table. given n, the length of the superkey, there will be 
			//n columns, and 2^n - 1 rows. we create a 2-dimensional array with the given results
			
			String current = sKeys.get(i);
			int length = current.length();
			int rows = (int)Math.pow(2, length)-1;
			
			int map[][] = new int[rows][length];
			
			//store results into the "binary chart" 2-dimensional array; i.e all proper subsets
			//of the current subkey to compare to each other superkey
			
			for(int x= 0; x<rows; x++)
			{
				int column=0;
				
				//converts row number (base 10 value) into binary digits
				
				String binary= Integer.toBinaryString(x+1);
				for(int bit= binary.length()-1; bit>=0; bit--)
				{
					//fills each entry in binary table with proper bit (1 or 0)
					map[x][column]= Integer.parseInt(Character.toString(binary.charAt(bit)));
					column++;
					
				}
			}
			
			//stores results of binary chart into an ArrayList. Each row of binary values is 
			//converted back to String value (with corresponding characters) then stored in the 
			//ArrayList called "subsets"
			for(int f=0; f<rows; f++)
			{
				String set= "";
				
				for(int column=0; column<length; column++)
				{
					
					if(map[f][column]==1)
						set += chars.get(column);
					
				}
				
				subsets.add(set);
			}
			
			//loop through arraylist of all subsets of the current superkey to see if any subset matches
			//another superkey. if there are no matches between subset and other superkey, then it is a 
			//candidate key. (No proper subset is a superkey within the relation).
			
			for(int z=0; z < subsets.size()-1; z++)
			{
				for(int s = 0; s<sKeys.size(); s++)
				{
					if(subsets.get(z).equals(sKeys.get(s)))
							isCandidate = false;
							
				}			
			}
			
			if(isCandidate == true)
				cKeys.add(current);
			
			
		}
		
		//Write results to the file "candidate-keys.txt" using a PrintWriter
		
		PrintWriter p = new PrintWriter("candidate-keys.txt");
		
		p.println("Current Relation:");
		for(int sup=0; sup<sKeys.size(); sup++)
		{
			p.print(sKeys.get(sup) + " ");
		}
		p.print("\n");
		p.println("All Candidate Keys: ");
		for(int can=0; can<cKeys.size(); can++)
		{
			p.print(cKeys.get(can)+ " ");
		}
			
		
		p.close();
		
		//When the program has completed, we print "Finished" to the console to know that 
		//the file "candidate-keys.txt" has been updated to the current super key/candidate key
		//results.
		
		System.out.println("Finished");
		
	}
}

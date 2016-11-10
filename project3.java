/*
Amy Robben

October 31, 2016
Databases, Dr. Yao

Project 3- using Java 8.0


************************************************************************************************
PURPOSE OF PROJECT:
When given a relation (all of a relation's attributes) and a list of all functional dependencies,
we are able to determine the super keys of that relation. All possible super keys are determined
by creating all the proper subsets of the relation's attributes. 
(Using the format Functional Dependencies (U->V)):
if a proper subset of the relation contains FD.U, then we can "append" FD.V, until there is no
longer a pattern to determine any more functional dependencies.
By looping through all proper subsets until no more changes have been made to our "proper subset"
aka RELATION, then we can determine whether or not that subset (relation) is equal to a superkey 
by if it DOES or DOES NOT contain each attribute of the relation.

************************************************************************************************
HOW TO USE PROGRAM:
The program runs automatically by reading our relation and functional dependency information 
from the file "FDs.txt"
It then creates all subsets of the relation,
loops through each subset
	loops through each subset OF that subset to detect FD.U in a proper subset, then updating as
	necessary
When all subsets of our relation have been studied, the program prints to our file "superkeys.txt"

Expected Console output:
"Finished"

Expected format for "superkeys.txt:"

Current Relation:
ABCDEF
Superkeys of Relation:
ABCF CDF ACDF BCDF ABCDF ABCEF CDEF ACDEF BCDEF ABCDEF

************************************************************************************************
*/
import java.util.*;
import java.util.regex.Pattern;
import java.io.*;
	
public class project3_robben_cervantes_domaleski 
{
	public static void main(String[] args) throws IOException
	{	
		File inputFile = new File("FDs.txt");
		Scanner infile = new Scanner(inputFile);
		PrintWriter pw = new PrintWriter("Superkeys.txt");
		
		//initial relation that we look at
		//we extract each character in the string (relation) so that we can 
		//create the subsets in our binary table
		String relation = infile.nextLine();
		ArrayList<String> chars = new ArrayList<String>();
			String rel = "";
			for(int a = 0; a<relation.length(); a++)
			{
				rel = Character.toString(relation.charAt(a));
				chars.add(rel);
			}
		
		//in the functional dependency format FD: U->V
		// U is used to hold all combinations 
		//and V is used as what the U point to
		ArrayList<String> U = new ArrayList<String>();
		ArrayList<String> V = new ArrayList<String>();
		ArrayList<String> superKeys = new ArrayList<String>();
		
		while(infile.hasNextLine())
		{
			String input = infile.nextLine();
			String[] tokens = input.split(Pattern.quote("."));
			
			U.add(tokens[0]);
			V.add(tokens[1]);
		}
		
		
		//using static methods created below, we create the binary table that determines all
		//subset combinations, and then store all proper subsets in the ArrayList subsets
		
		int[][] binaryTable = createBinary(relation.length());
		ArrayList<String> subsets = listSubsets(binaryTable, chars);
		
		
		//loop through all proper subsets of our relation to see if any are equal to a superKey
		//it is equal to a superKey if the functional dependencies make it equal to the entire relation
		for(int i=0; i<subsets.size(); i++)
		{	
			String possKey= subsets.get(i);
			String temp = possKey;
			//System.out.println(temp);
			boolean isRelationLength = false; // size of relation, therefore is superKey
			boolean isSuperKey =false;
			boolean isChanged = false;
			int newLength=temp.length();
			
			//we create subsets of the subsets, to determine all functional dependencies
			//we do this because as more dependencies are discovered, the "potential" superkey changes
			//so we loop through and create the binary tables/proper subsets of the growing "potential 
			//super key until no changes are made
			
			
			do{
				isChanged = false;
				ArrayList<String> new_sub_chars = new ArrayList<String>();
				String current_sub = "";
				for(int a = 0; a<temp.length(); a++)
				{
					current_sub = Character.toString(temp.charAt(a));
					new_sub_chars.add(current_sub);
				}
				int[][] sub_binaryChart = createBinary(temp.length());
				ArrayList<String> new_subsets = listSubsets(sub_binaryChart, new_sub_chars);
				
				//loop through the FD.U values to see if any of the subsets contain 
				for(int outer= 0; outer<new_subsets.size(); outer++)
				{
					for(int inner=0; inner<U.size(); inner++)
					{
						
						if(new_subsets.get(outer).contains(U.get(inner)))
						{
							char[] vChar = (V.get(inner)).toCharArray();
							
							//if the subset contains FD.U we append each character of V, where FD.U->V
							// individually to avoid redundancy
							
							for(int v_inner=0; v_inner<vChar.length; v_inner++)
							{
								if(!(temp.contains(Character.toString(vChar[v_inner]))))
								{
									temp += Character.toString(vChar[v_inner]);
									isChanged= true;
								}	
							}
							
							//reorders our potential super key in alphabetical order
							//helps with detecting FD.U
							
							temp = newClosure(temp);
							newLength= temp.length();
								
							//System.out.print(temp + " ");
							//System.out.println();
							if(isLength(temp, relation))
							{
								isRelationLength = true;
								isSuperKey = true;
							}
							
							
						}
					}
				}
				
			}while(isChanged == true );
				
			if(isSuperKey== true)
					superKeys.add(possKey);
			//System.out.println("***********************************");
			
		}
		
		//for(String supers: superKeys)
			//System.out.print(supers + " ");
		
		//after looping through the subsets of the relation (and the subsets of each subset) 
		//we write to the file superkeys.txt the super keys result.
		
		
		pw.println("Current Relation:");
		pw.println(relation);
		pw.println("Super keys: ");
		for(String sup: superKeys)
			pw.print(sup + " ");
		
		System.out.print("Finished");
		pw.close();
		
			
	}
	
	//newClosure is used to update the closure as we evaluate the subsets
	public static String newClosure(String old)
	{
		char[] chars = old.toCharArray();
		Arrays.sort(chars);
		String now = new String(chars);
		
		return now;
		
	}
	
	//determines if current closure is the same size as the relation at hand
	public static boolean isLength(String a, String b)
	{
		if(a.length()==b.length() )
			return true;
		else
			return false;
	}
	
	//creates the binary table that is used to create each proper subset
	public static int[][] createBinary(int x)
	{		
		int rows =(int)Math.pow(2, x)-1;
		int chart[][] = new int[rows][x];
		for(int i= 0; i<rows; i++)
		{
			int column=0;
		
			//converts row number (base 10 value) into binary digits
		
			String binary= Integer.toBinaryString(i+1);
			for(int bit= binary.length()-1; bit>=0; bit--)
			{
			//fills each entry in binary table with proper bit (1 or 0)
				chart[i][column]= Integer.parseInt(Character.toString(binary.charAt(bit)));
				column++;
			
			}
		}
		return chart;
	}
	
	//creates the arraylist that holds all subsets that are generated using our
	//createBinary method
	public static ArrayList<String> listSubsets(int[][] a, ArrayList<String> b)
	{
		ArrayList<String> subsets = new ArrayList<String>();
		int rows = a.length;
		int columns = a[0].length;
		//System.out.println(rows + " " + columns);
		for(int i=0; i<rows; i++)
		{
			String set= "";
		
			for(int column=0; column<columns; column++)
			{
				if(a[i][column]==1)
					set += b.get(column);
			}
		
			subsets.add(set);
			//System.out.println(set);
		}
		return subsets;
	}
		
}

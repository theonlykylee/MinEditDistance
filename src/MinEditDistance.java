import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class MinEditDistance {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		String firstString;
		String secondString;
		int[][] minEdDisTable;
		
		Scanner s = new Scanner(System.in);
		
		System.out.println("===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===");
		System.out.println(":: STRING SUBSTITUTION USING MINIMUM EDIT DISTANCE ::");
		System.out.print("   > Enter FIRST string: ");
		
		firstString = s.next();
		
		System.out.print("   > Enter SECOND string: ");
		
		secondString = s.next();
		
		System.out.println("===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===");

		minEdDisTable = createMEDTable(firstString, secondString);
		System.out.println(":: MINIMUM EDIT DISTANCE: " + getMinEditDistance(minEdDisTable));
		printAlignment(generateBacktrace(minEdDisTable), firstString, secondString);
	}
	
	public static int[][] createMEDTable(String first, String second) {
		int firstSize = first.length() + 1;
		int secondSize = second.length() + 1;
		
		char[] fStr = first.toCharArray();
		char[] sStr = second.toCharArray();
		int[][] medTable = new int[firstSize][secondSize];
		
		int left;
		int down;
		int diag;
		
		for(int i = 0; i < firstSize; i++) {
			for(int j = 0; j < secondSize; j++) {
				if(i == 0) {
					medTable[i][j] = j;
					 
				} else if(j == 0) {
					medTable[i][j] = i;
					
				} else {
					left = medTable[i - 1][j] + 1;
					down = medTable[i][j - 1] + 1;
					diag = medTable[i - 1][j - 1];
					
					if(fStr[i - 1] != sStr[j - 1]) {
						diag += 2;
					}
					
					medTable[i][j] = Math.min(Math.min(left, down), diag);
				}
				
				System.out.print("[" + medTable[i][j] + "]   ");
			}
			
			System.out.println();
		}
		
		return medTable;
	}
	
	public static int getMinEditDistance(int[][] medTable) {
		int i = medTable.length - 1;
		int j = medTable[0].length - 1;
		
		return medTable[i][j];
	}
	
	public static ArrayList<String> generateBacktrace(int[][] medTable) {
		ArrayList<String> bTrace = new ArrayList<String>();
		int i = medTable.length - 1;
		int j = medTable[0].length - 1;
		
		//for()
		
		while(i != 0 && j != 0) {
			String action = i + "/" + j + "/";
			
			int curr = medTable[i][j];
			int left = medTable[i][j - 1];
			int up = medTable[i - 1][j];
			int diag = medTable[i - 1][j - 1];
			
			if("DIAGONAL".equalsIgnoreCase(getMinAction(left, up, diag))) {
				if(curr == diag)
					action += "ALIGN";
				else
					action += "SUBSTITUTE";
				i--;
				j--;
			} else if("UP".equalsIgnoreCase(getMinAction(left, up, diag))) {
				if(curr == up)
					action += "ALIGN";
				else
					action += "DELETE";
				i--;
			} else {
				if(curr == left)
					action += "ALIGN";
				else
					action += "INSERT";
				j--;
			}
			
			bTrace.add(action);
		}
		
		Collections.reverse(bTrace);
		
		return bTrace;
	}
	
	public static String getMinAction(int left, int up, int diag) {
		if(left > up) {
			if(up > diag)
				return "DIAGONAL";
			else
				return "UP";
		} else {
			if(left > diag)
				return "DIAGONAL";
			else
				return "LEFT";
		}
	}
	
	public static void printAlignment(ArrayList<String> backTrace, String first, String second) {
		char[] fArray = first.toCharArray();
		char[] sArray = second.toCharArray();
		
		for(String bt : backTrace) {
			String[] act = bt.split("/");
			
			System.out.print(fArray[Integer.parseInt(act[0]) - 1]);
			
			if(act[2].equalsIgnoreCase("align"))
				System.out.print(" -- ");
			else
				System.out.print("    ");
			
			System.out.print(sArray[Integer.parseInt(act[1]) - 1]);
			
			System.out.println();
		}
	}

}

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
		
		System.out.println("===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===");
		System.out.println(":: MINIMUM EDIT DISTANCE: " + getMinEditDistance(minEdDisTable));
		printAlignment(generateBacktrace(minEdDisTable), firstString, secondString);
	}
	
	public static int[][] createMEDTable(String first, String second) {
		int firstSize = first.length() + 1;
		System.out.println("[LOG] : " + firstSize);
		int secondSize = second.length() + 1;
		System.out.println("[LOG] : " + secondSize);
		
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
		
		String action = "";
		
		int left = medTable[i][j - 1];
		int up = medTable[i - 1][j];
		int diag = medTable[i - 1][j - 1];
		int curr = medTable[i][j];
		
		boolean flag = true;
		
		if((curr != left) && (curr != diag) && (curr != up))
			action = "SUBSTITUTE";
		else
			action = "ALIGN";
		
		while(flag) {
			bTrace.add(i + "/" + j + "/" + action);
			System.out.println("[LOG] : " + i + "/" + j + "/" + action);
			
			if(i == 1 && j == 1) {
				flag = false;
			} else if(i == 1 && j > 1) {
				j--;
			} else if(j == 1 && i > 1) {
				i--;
			} else {
				left = medTable[i][j - 1];
				up = medTable[i - 1][j];
				diag = medTable[i - 1][j - 1];
				
                if(up < diag) {
                	if(curr == up)
            			action = "ALIGN";
            		else
            			action = "DELETE";
                    i--;
                } else if(left < diag) {
                	if(curr == left)
            			action = "ALIGN";
            		else
            			action = "INSERT";
                    j--;
                } else {
                	if(curr == diag)
            			action = "ALIGN";
            		else
            			action = "SUBSTITUTE";
                    i--;
                    j--;
                }
            }
		}
		
//		while(i != 0 && j != 0) {
//			action = i + "/" + j + "/";
//			
//			curr = medTable[i][j];
//			left = medTable[i][j - 1];
//			up = medTable[i - 1][j];
//			diag = medTable[i - 1][j - 1];
//			
//			System.out.print("[LOG] : curr="+curr+"("+i+","+j+"); ");
//			System.out.print("left="+left+"("+i+","+(j-1)+"); ");
//			System.out.print("up="+up+"("+(i-1)+","+j+"); ");
//			System.out.println("diag="+diag+"("+(i-1)+","+(j-1)+"); ");
//			
//			if("DIAGONAL".equalsIgnoreCase(getMinAction(left, up, diag))) {
//				if(curr == diag)
//					action += "ALIGN";
//				else
//					action += "SUBSTITUTE";
//				i--;
//				j--;
//			} else if("UP".equalsIgnoreCase(getMinAction(left, up, diag))) {
//				if(curr == up)
//					action += "ALIGN";
//				else
//					action += "DELETE";
//				i--;
//			} else {
//				if(curr == left)
//					action += "ALIGN";
//				else
//					action += "INSERT";
//				j--;
//			}
//			
//			bTrace.add(action);
//		}
		
		System.out.println("[LOG] : Backtrace Size = " + bTrace.size());
		
		Collections.reverse(bTrace);
		
		return bTrace;
	}
	
//	public static String getMinAction(int left, int up, int diag) {
//		if(left > up) {
//			if(up > diag)
//				return "DIAGONAL";
//			else
//				return "UP";
//		} else {
//			if(left > diag)
//				return "DIAGONAL";
//			else
//				return "LEFT";
//		}
//	}
	
	public static void printAlignment(ArrayList<String> backTrace, String first, String second) {
		char[] fArray = first.toCharArray();
		char[] sArray = second.toCharArray();
		
		String[] act;
		int fIndex;
		int sIndex;
		String action;
		
//		int fPast = 0;
//		int sPast = 0;
		
		String firstFinal = "   > F: -> ";
		String actionFinal = "   > A: -> ";
		String secondFinal = "   > S: -> ";
		
		for(String bt : backTrace) {
			act = bt.split("/");
			fIndex = Integer.parseInt(act[0]) - 1;
			sIndex = Integer.parseInt(act[1]) - 1;
			action = act[2];
			
			if(action.equalsIgnoreCase("INSERT")) {
				firstFinal += "*  ";
				secondFinal += sArray[sIndex] + "  ";
				actionFinal += "   ";
			} else if(action.equalsIgnoreCase("DELETE")) {
				firstFinal += fArray[fIndex] + "  ";
				secondFinal += "*  ";
				actionFinal += "   ";
			} else if(action.equalsIgnoreCase("ALIGN")) {
				firstFinal += fArray[fIndex] + "  ";
				secondFinal += sArray[sIndex] + "  ";
				actionFinal += "|  ";
			} else {
				firstFinal += fArray[fIndex] + "  ";
				secondFinal += sArray[sIndex] + "  ";
				actionFinal += "   ";
			}
			
//			if(fPast == fArray[fIndex])
//				firstFinal += "*  ";
//			else
//				firstFinal += fArray[fIndex] + "  ";
//			
//			if(action.equalsIgnoreCase("align"))
//				actionFinal += "|  ";
//			else
//				actionFinal += "   ";
//			
//			if(sPast == sArray[sIndex])
//				secondFinal += "*  ";
//			else
//				secondFinal += sArray[sIndex] + "  ";
//			
//			fPast = fArray[fIndex];
//			sPast = sArray[sIndex];
		}
		
		System.out.println(firstFinal);
		System.out.println(actionFinal);
		System.out.println(secondFinal);
	}

}

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class MinEditDistance {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		String firstString;
		String secondString;
		int[][] minEdDisTable;
		int minEdDisVal;
		ArrayList<String> minEdBacktrace;
		
		Scanner s = new Scanner(System.in);
		
		System.out.println("===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===");
		System.out.println(":: STRING SUBSTITUTION USING MINIMUM EDIT DISTANCE ::");
		System.out.print("   > Enter FIRST string: ");
		firstString = s.next();
		System.out.print("   > Enter SECOND string: ");
		secondString = s.next();
		
		System.out.println("===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===");

		minEdDisTable = createMEDTable(firstString, secondString);
		minEdDisVal = getMinEditDistance(minEdDisTable);
		minEdBacktrace = generateBacktrace(minEdDisTable);
		
		System.out.println("===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===-===");
		System.out.println(":: MINIMUM EDIT DISTANCE: " + minEdDisVal);
		printAlignment(verifyAction(minEdBacktrace, minEdDisTable), firstString, secondString);
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
		
		int left;
		int up;
		int diag;
		
		boolean flag = true;
		
		while(flag) {
			bTrace.add(i + "/" + j + "/");
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
                    i--;
                } else if(left < diag) {
                    j--;
                } else {
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
	
	public static ArrayList<String> verifyAction(ArrayList<String> bTrace, int[][] medTable) {
		int[] currIndex = new int[2];
		int[] pastIndex = {0,0};
		String[] act;
		
		ArrayList<String> newBT = new ArrayList<String>();
		String nBT;
		
		for(String bT : bTrace) {
			nBT = bT;
			act = bT.split("/");
			currIndex[0] = Integer.parseInt(act[0]);
			currIndex[1] = Integer.parseInt(act[1]);
			
			if(medTable[pastIndex[0]][pastIndex[1]] == medTable[currIndex[0]][currIndex[1]]) {
				nBT += "ALIGN";
			} else {
				if((pastIndex[0] == currIndex[0]) && (pastIndex[1] == currIndex[1] - 1)) {
					nBT += "INSERT";
				} else if((pastIndex[0] == currIndex[0] - 1) && (pastIndex[1] == currIndex[1])) {
					nBT += "DELETE";
				} else {
					nBT += "SUBSTITUTE";
				}
			}
			
			newBT.add(nBT);
			
			pastIndex[0] = currIndex[0];
			pastIndex[1] = currIndex[1];
		}
	
		return newBT;	
	}
	
	public static void printAlignment(ArrayList<String> backTrace, String first, String second) {
		char[] fArray = first.toCharArray();
		char[] sArray = second.toCharArray();
		
		String[] act;
		int fIndex;
		int sIndex;
		String action;
		
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
		}
		
		System.out.println(firstFinal);
		System.out.println(actionFinal);
		System.out.println(secondFinal);
	}

}

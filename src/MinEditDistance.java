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

}

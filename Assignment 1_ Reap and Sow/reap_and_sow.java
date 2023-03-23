// Ariel Towne

import java.util.Scanner;

public class reap_and_sow {
	
	public static int numOfRow, numOfCol;
	public static final int UNUSED = -1;
	public static final int FARMED = 1;
	public static final int NOT_FARMED = 0;

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
        
		numOfRow = input.nextInt();
		numOfCol = input.nextInt();
		
		int[][] farm_that_land = new int[numOfRow][numOfCol];
		
		// Reads input like a grid: left to right, top to bottom.
        for (int i = 0; i < numOfRow; i++)
            for (int j = 0; j < numOfCol; j++)
                farm_that_land[i][j] = input.nextInt();
        
        // Finds assignment of crops that satisfies all conditions, if any.
        if (solve(0, 0, farm_that_land))
        	printResult(farm_that_land);
        else
            System.out.println("impossible");

        input.close();
	}
	
	public static boolean solve(int r, int c, int[][] farm_that_land){
		if (r >= numOfRow)
			return true;
		
		if (c >= numOfCol)
			return solve(r + 1, 0, farm_that_land);
		
		// Skips parcel intersections were already farmed or deemed unfit for farming.
		if (farm_that_land[r][c] != UNUSED)
			return solve(r, c + 1, farm_that_land);	
		
		for (int i = NOT_FARMED; i <= FARMED; i++)
		{
			farm_that_land[r][c] = i;
			
			// Recurses to next intersection only if current intersection passes 3 conditions.
			if (valid(r, c, farm_that_land))
				if (solve(r, c + 1, farm_that_land))
					return true;
			
			farm_that_land[r][c] = UNUSED;
		}
		
		return false;
	}
	
	public static boolean valid (int r, int c, int[][] farm_that_land) {
		// Checks validity of the current row and column separately.
		return (check_row(r, c, farm_that_land) && check_column(r, c, farm_that_land));
	}
	
	public static boolean check_row(int r, int c, int[][] farm_that_land) {
		// Keeps count of farmed and not farmed intersections in current row.
		int plants = 0, boulders = 0;

		// Will hold a copy of the current parcel intersection's row.
		int[] currentRow = new int[numOfCol];
		
		for (int i = 0; i < numOfCol; i++)
		{
			currentRow[i] = farm_that_land[r][i];

			if (currentRow[i] == FARMED)
				plants++;
			else if (currentRow[i] == NOT_FARMED)
				boulders++;

			// Condition 3 (rows have same number of farmed intersections):
			// Checks for three contiguous farmed or not farmed intersections.
			if (i > 1 && currentRow[i] == currentRow[i-1] && currentRow[i-1] == currentRow[i-2])
				if (currentRow[i] != UNUSED)
					return false;
		}

		// Condition 1:
		// Checks that farmed and not farmed intersections don't exceed 50% of row.
		if (plants > (numOfCol/2) || boulders > (numOfCol/2))
			return false;

		// Condition 2:
		// Only checks for identical rows when current row is completely filled in.
		if (c < (numOfCol - 1))
			return true;

		for (int i = 0; i < numOfRow; i++)
		{
			if (i == r)
				continue;
			
			boolean identical = true;
			
			// Compares current row to every other row.
			for (int j = 0; j < numOfCol; j++)
				if(currentRow[j] != farm_that_land[i][j])
					identical = false;
			
			if (identical == true)
				return false;
		}
		
		return true;
	}
	
	public static boolean check_column(int r, int c, int[][] farm_that_land) {
		// Keeps count of farmed and not farmed intersections in current column.
		int plants = 0, boulders = 0;

		// Will hold a copy of the current parcel intersection's column.
		int[] currentCol = new int[numOfRow];
		
		for (int i = 0; i < numOfRow; i++)
		{
			currentCol[i] = farm_that_land[i][c];
			
			if (currentCol[i] == FARMED)
				plants++;
			else if (currentCol[i] == NOT_FARMED)
				boulders++;

			// Condition 3 (columns have same number of farmed intersections):
			// Checks for three contiguous farmed or not farmed intersections.
			if (i > 1 && currentCol[i] == currentCol[i-1] && currentCol[i-1] == currentCol[i-2])
				if (currentCol[i] != UNUSED)
					return false;
		}
		
		// Condition 1:
		// Checks that farmed and not farmed intersections don't exceed 50% of row.
		if (plants > (numOfRow/2) || boulders > (numOfRow/2))
			return false;

		// Condition 2:
		// Only checks for identical columns when current column has only 0s and 1s.
		if (r < (numOfRow - 1))
			return true;
		
		for (int j = 0; j < numOfCol; j++)
		{
			if (j == c)
				continue;
			
			boolean identical = true;

			// Compares current column to every other column.
			for (int i = 0; i < numOfRow; i++)
				if(currentCol[i] != farm_that_land[i][j])
					identical = false;
			
			if (identical == true)
				return false;
		}
		
		return true;
	}
	
	public static void printResult(int[][] farm_that_land)
    {
        for (int i = 0; i < numOfRow; i++)
        {
            for (int j = 0; j < numOfCol; j++)
                // Prints a space after each value except the last one in each row.
                System.out.print(farm_that_land[i][j] + (j == (numOfCol-1) ? "" : " "));

            System.out.println();
        }
    }

}

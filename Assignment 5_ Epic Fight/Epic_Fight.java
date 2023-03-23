// Ariel Towne

import java.util.*;

public class Epic_Fight {

	public static long[][] cur;
	public static long[][] next;
    public static Moves[] XorvierMoves;
    public static Moves[] RuffusMoves;
    public static int maxStaminaX, maxStaminaR, t;
    public static int MOD = 10007;

    public static void main(String[] args)
    {
    	Scanner input = new Scanner(System.in);
    	
    	t = input.nextInt();
    	
    	// The number of moves Xorvier has and his maximum stamina.
    	int numOfMovesX = input.nextInt();
    	maxStaminaX = input.nextInt();
    	
    	// This is a list of all of Xorvier's moves.
    	XorvierMoves = new Moves[numOfMovesX];
    	
    	for (int z = 0; z < numOfMovesX; z++)
    	{
    		// Each move takes stamina away from Xorvier, then Ruffus.
    		int firstCost = input.nextInt();
    		int secondCost = input.nextInt();
    		XorvierMoves[z] = new Moves(firstCost, secondCost);
    	}

    	// The number of moves Ruffus has and his maximum stamina.
    	int numOfMovesR = input.nextInt();
    	maxStaminaR = input.nextInt();

    	// This is a list of all of Ruffus's moves.
    	RuffusMoves = new Moves[numOfMovesR];
    	
    	for (int z = 0; z < numOfMovesR; z++)
    	{
    		// Each move takes stamina away from Ruffus, then Xorvier.
    		int firstCost = input.nextInt();
    		int secondCost = input.nextInt();
    		
    		RuffusMoves[z] = new Moves(firstCost, secondCost);
    	}
        
    	// First dimension represents Xorvier's stamina loss,
    	// second dimension represents Ruffus's stamina loss.
        cur = new long[maxStaminaX][maxStaminaR + 1];
        next = new long[maxStaminaX][maxStaminaR + 1];
        cur[0][0] = 1;
        
        for (int i = 0; i <= t; i+=2)
        {
        	// Reset next array to 0 for reuse.
        	for (long[] row: next)
        	    Arrays.fill(row, 0);
            
        	// Loop through the max stamina array.
            for (int j = 0; j < maxStaminaX; j++)
            	for (int p = 0; p <= maxStaminaR; p++)
	                tryMoves(j, p, i, numOfMovesX, numOfMovesR);
            
            // Swap arrays.
            long[][] tmp = cur;
            cur = next;
            next = tmp;
        }
        
        long total = 0;
        
        // The final answer includes all combinations of moves that result in
        // Xorvier having positive stamina and Ruffus having no stamina.
        for (int l = 0; l < maxStaminaX; l++)
        	total += cur[l][maxStaminaR];
        
        total %= MOD;
        System.out.println(total);
        
        input.close();
    }
    
    public static void tryMoves(int j, int p, int i, int numOfMovesX, int numOfMovesR)
	{
    	// Loop through all combinations of Xorvier's and Ruffus's moves.
		for (int m = 0; m < numOfMovesX; m++)
			for (int n = 0; n < numOfMovesR; n++)
			{
				// First, test out the first stamina loss caused by each combination.
				int staminaCostX = j + XorvierMoves[m].getFirstCost();
				int staminaCostR = p + RuffusMoves[n].getFirstCost();
				
				// Xorvier can never lose all his stamina.
				if (staminaCostX >= maxStaminaX)
					continue;
				
				// Any stamina that would take them over their initial amount is lost.
				if (staminaCostX < 0)
					staminaCostX = 0;
				
				if (staminaCostR < 0)
					staminaCostR = 0;
				
				// Ruffus can only ever lose all his stamina if the fight reached its time limit.
				if (staminaCostR >= maxStaminaR && i == t)
					staminaCostR = maxStaminaR;
				else if (staminaCostR >= maxStaminaR)
					continue;
				
				// The moves will not reduce the opponent's stamina only if the
				// time limit has been reached.
				if (i < t)
				{
					// Then, test out the second stamina loss caused by each combination.
					staminaCostX += RuffusMoves[n].getSecondCost();
					staminaCostR += XorvierMoves[m].getSecondCost();

					// Xorvier can never lose all his stamina.
					if (staminaCostX >= maxStaminaX)
						continue;
					
					// Any stamina that would take them over their initial amount is lost.
					if (staminaCostX < 0)
						staminaCostX = 0;
					
					if (staminaCostR < 0)
						staminaCostR = 0;

					// Ruffus can only ever lose all his stamina if the fight reached its time limit.
					if (staminaCostR >= maxStaminaR && i+1 == t)
						staminaCostR = maxStaminaR;
					else if (staminaCostR >= maxStaminaR)
						continue;
				}
				
				// The move combination was valid, so apply the change in stamina.
				next[staminaCostX][staminaCostR] += cur[j][p];
				next[staminaCostX][staminaCostR] %= MOD;
			}
		}
}

// The moves that Xorvier and Ruffus execute are contained in their respective arrays.
class Moves
{
	int firstCost, secondCost;

    public Moves(int firstCost, int secondCost)
    {
    	// This is the stamina cost for the executer of the move.
    	this.firstCost = firstCost;
    	// This is the stamina cost for the opponent of the executer.
    	this.secondCost = secondCost;
    }
    
    public int getFirstCost()
    {
		return firstCost;
	}
    
    public int getSecondCost()
    {
		return secondCost;
	}
    
    public void setFirstCost(int firstCost) {
		this.firstCost = firstCost;
	}
    
    public void setSecondCost(int secondCost) {
		this.secondCost = secondCost;
	}
}
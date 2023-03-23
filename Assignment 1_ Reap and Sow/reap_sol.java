
import java.util.*;
import java.io.*;

public class reap_sol
{
    public static int h;
    public static int w;
    public static int[][] grid;
    public static final int IMPOSSIBLE = -1;
    public static final int NO_CROP = 0;
    public static final int CROP = 1;
    public static Random r = new Random(201);
    
    public static void main(String[] Args)
    {
        Scanner sc = new Scanner(System.in);
        h = sc.nextInt();
        w = sc.nextInt();
        grid = new int[h][w];
        boolean good = true;
        
        // Make everything impossible
        for (int i = 0; i < h; i++)
        {
            for (int j = 0; j < w; j++)
            {
                grid[i][j] = IMPOSSIBLE;
            }
        }
        
        
        for (int i = 0; i < h; i++)
        {
            for (int j = 0; j < w; j++)
            {
                grid[i][j] = sc.nextInt();
                
                // Check the original board is valid
                if (!valid(grid, i, j))
                {
                    good = false;
                }
            }
        }
        
        if (good && solve(0,0))
            printGrid(grid);
        else
            System.out.println("Impossible");
    }
    
    public static boolean solve(int r, int c)
    {
        // Check if we are at the end of our row
        if (c == w)
            return solve(r + 1, 0);
        
        // Check if we are at the last row
        if (r == h)
            return true;
        
        // Check if we cannot give this spot a value
        if (grid[r][c] != IMPOSSIBLE)
            return solve(r, c + 1);
        
        // Find a spot that is forced
        for (int i = 0; i < h; i++)
        {
            for (int j = 0; j < w; j++)
            {
                // Check if the value is not deteremined
                if (grid[i][j] == IMPOSSIBLE) 
                {
                    // Check if the crop at the location is in valid
                    grid[i][j] = CROP;
                    if (!valid(grid, i, j)) 
                    {
                        // We must use no crop
                        grid[i][j] = NO_CROP;
                        
                        // Check if no solution
                        if (!valid(grid, i, j))
                        {
                            grid[i][j] = IMPOSSIBLE;
                            return false;
                        }
                        if (solve(r, c))
                            return true;
                        grid[i][j] = IMPOSSIBLE;
                        return false;
                    }
                    grid[i][j] = NO_CROP;
                    if (!valid(grid, i, j)) {
                        grid[i][j] = CROP;
                        if (solve(r, c))
                            return true;
                        grid[i][j] = IMPOSSIBLE;
                        return false;
                    }
                    grid[i][j] = IMPOSSIBLE;
                }
            }
        }
        
        // No spot is forced try both values
        grid[r][c] = CROP;
        if (solve(r, c + 1))
            return true;
        
        // Failed to find a solution with the first attempt
        grid[r][c] = NO_CROP;
        if (solve(r, c + 1))
            return true;
        
        // No possible correct decision (BACKTRACK)
        grid[r][c] = IMPOSSIBLE;
        return false;
    }
    
    public static boolean valid(int[][] g, int r, int c)
    {    
        if (g[r][c] == IMPOSSIBLE)
            return true;
        //System.out.println(Arrays.deepToString(g));
        // No triples
        for (int i = r - 1; i <= r + 1; i++)
        {
            if (i > 0 && i <  h - 1 && g[i][c] != IMPOSSIBLE && g[i][c] == g[i + 1][c] && g[i][c] == g[i - 1][c])
                return false;
        }    
        for (int j = c - 1; j <= c + 1; j++)
        {
            if (j > 0 && j < w - 1 && g[r][j] != IMPOSSIBLE && g[r][j] == g[r][j + 1] && g[r][j] == g[r][j - 1])
                return false;            
        }
        
        // Not too many in row
        for (int i = r; i <= r; i++)
        {
            int count1 = 0;
            int count0 = 0;
            for (int j = 0; j < w; j++)
            {
                if (g[i][j] == CROP)
                    count1++;
                if (g[i][j] == NO_CROP)
                    count0++;
            }
            if (count0 + count0 > w || count1 + count1 > w)
                return false;
        }
        
        // Not too many in col
        for (int j = c; j <= c; j++)
        {
            int count1 = 0;
            int count0 = 0;
            for (int i = 0; i < h; i++)
            {
                if (g[i][j] == CROP)
                    count1++;
                if (g[i][j] == NO_CROP)
                    count0++;
            }
            if (count0 + count0 > h || count1 + count1 > h)
                return false;
        }
        
        // No Dupe col
        for (int j = c; j <= c; j++)
        {
            for (int j2 = 0; j2 < w; j2++)
            {
                if (j == j2)
                    continue;
                boolean id = true;
                for (int i = 0; i < h; i++)
                {
                    if (g[i][j] != g[i][j2] || g[i][j] == IMPOSSIBLE)
                        id = false;
                }
                if (id)
                    return false;
            }
        }
        
        // No Dupe row
        for (int i = r; i <= r; i++)
        {
            for (int i2 = 0; i2 < h; i2++)
            {
                if (i == i2)
                    continue;
                boolean id = true; // Assume identical until otherwise
                for (int j = 0; j < w; j++)
                {
                    if (g[i][j] != g[i2][j] || g[i][j] == IMPOSSIBLE)
                        id = false;
                }
                if (id)
                    return false;
            }
        }
        
        return true;
    }
    
    public static void printGrid(int[][] g)
    {
        for (int i = 0; i < h; i++){
            for (int j = 0; j < w; j++){
                //if (g[i][j] != IMPOSSIBLE)
                System.out.print((g[i][j] < 0 ? "" : " ") + g[i][j] + " ");
                //else System.out.print("   ");
            }
            System.out.println();
        }
    }
}
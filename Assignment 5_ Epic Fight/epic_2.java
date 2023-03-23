import java.util.*;

public class epic_2
{
    public static int MOD = 10007;
    public static ArrayList<Edge>[] xor;
    public static ArrayList<Edge>[] ruf;
    public static int xHealth, rHealth;
    public static int CD_STATE = 0;
    public static void main(String[] Args)
    {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        
        // Read in the initial information for xorvier
        int xMove = sc.nextInt() + 1;
        xHealth = sc.nextInt() + 1;
        
        // Make a graph of the different moves that xorvier can perform at each state
        xor = new ArrayList[xMove];
        for (int i = 0; i < xMove; i++)
            xor[i] = new ArrayList<Edge>();
        
        // Populate the xorviers graph
        for (int i = 1; i < xMove; i++)
        {
            int start = sc.nextInt();
            int end = sc.nextInt();
            xor[0].add(new Edge(start, 0, i));
            xor[i].add(new Edge(0, end, 0));
        }
        
        
        // Read in the initial information for xorvier
        int rMove = sc.nextInt() + 1;
        rHealth = sc.nextInt() + 1;
        
        // Make a graph of the different moves that xorvier can perform at each state
        ruf = new ArrayList[rMove];
        for (int i = 0; i < rMove; i++)
            ruf[i] = new ArrayList<Edge>();
        
        // Populate the xorviers graph
        for (int i = 1; i < rMove; i++)
        {
            int start = sc.nextInt();
            int end = sc.nextInt();
            ruf[0].add(new Edge(start, 0, i));
            ruf[i].add(new Edge(0, end, 0));
        }
        
        int[][] counts = new int[rHealth * xHealth][xMove * rMove];
        int[][] next = new int[rHealth * xHealth][xMove * rMove];
        
        
        for (int[] a : counts)
            Arrays.fill(a, 0);
        
        // Start the fight at full health and in the cooldown State
        counts[rHealth * xHealth - 1][CD_STATE] = 1;    
        for (int i = 0; i <= t; i++)
        {
            for (int[] a : next)
                Arrays.fill(a, 0);
            int minX = ((i&1) == 0) ? 0 : 1;
            int maxX = ((i&1) == 0) ? 1 : xMove;
            int minR = ((i&1) == 0) ? 0 : 1;
            int maxR = ((i&1) == 0) ? 1 : rMove;
            for (int xH = 1; xH < xHealth; xH++)
            {
                for (int rH = 1; rH < rHealth; rH++)
                {
                    int HPState = rH * xHealth + xH;
                    for (int xM = minX; xM < maxX; xM++)
                    {
                        for (int rM = minR; rM < maxR; rM++)
                        {
                            int amt = counts[HPState][rM * xMove + xM];
                            if (amt != 0)
                            {
                                for (Edge xEdge : xor[xM])
                                {
                                    for (Edge rEdge : ruf[rM])
                                    {
                                        int nextXM = xEdge.end;
                                        int nextRM = rEdge.end;
                                        int nextXH = xH - xEdge.self - rEdge.other;
                                        int nextRH = rH - rEdge.self - xEdge.other;
                                        if (nextXH < 0)
                                            nextXH = 0;
                                        if (nextRH < 0)
                                            nextRH = 0;
                                        if (nextXH >= xHealth)
                                            nextXH = xHealth - 1;
                                        if (nextRH >= rHealth)
                                            nextRH = rHealth - 1;
                                        next[nextRH * xHealth + nextXH][nextRM * xMove + nextXM] += amt;
                                        if (next[nextRH * xHealth + nextXH][nextRM * xMove + nextXM] >= MOD)
                                            next[nextRH * xHealth + nextXH][nextRM * xMove + nextXM] -= MOD;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            int[][] tmp = counts;
            counts = next;
            next = tmp;
        }
        int ans = 0;
        for (int i = 1; i < xHealth; i++)
            for (int j = 0; j < xMove; j++)
                for (int k = 0; k < rMove; k++)
                    ans += counts[i][k * xMove + j];
        ans %= MOD;
        System.out.println(ans);
   }
    
    public static class Edge{
        int end;
        int self;
        int other;
        Edge (int a, int b, int dest)
        {
            end = dest;
            self = a;
            other = b;
        }
    }
}
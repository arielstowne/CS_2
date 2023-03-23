
import java.util.*;
import java.math.*;

public class arrest
{
    public static int MAX_MUT = 20;
    public static void main(String[] Args) throws Exception
    {
        Scanner sc = new Scanner(System.in);
        
        // Read in the description of Bitland
        int n = sc.nextInt();
        int m = sc.nextInt();
        
        if (n > 10000 || n < 2)
            throw new Exception("BAD NODES");
        if (m > 20000 || m < 2)
            throw new Exception("BAD NODES");
        
        
        int[] piratesInCity = new int[n];
        int[] bribes = new int[n];
        
        for (int i = 0; i < n; i++)
        {
            piratesInCity[i] = sc.nextInt();
            bribes[i] = sc.nextInt();
            
            if (bribes[i] > 1000 || bribes[i] < 1)
                throw new Exception("BAD BRIBE " +bribes[i] + " " + i);
            if (piratesInCity[i] > 1000 || piratesInCity[i] < 0)
                throw new Exception("BAD PIRATE");
        }
        
        ArrayList<Edge>[] adj = new ArrayList[n];
        for (int i = 0; i < n; i++)
        {
            adj[i] = new ArrayList<Edge>();
        }
        
        for (int i = 0; i < m; i++)
        {
            int st = sc.nextInt() - 1;
            int en = sc.nextInt() - 1;
            int cost = sc.nextInt();
            adj[st].add(new Edge(en, cost));
            adj[en].add(new Edge(st, cost));
            if (cost > 1000 || cost < 1)
                throw new Exception("BAD COST");
        }
        
        long oo = 987654321l * 987654321;
        long[] dist = new long[n * (MAX_MUT + 1)];
        int[] prev = new int[n * (MAX_MUT + 1)];
        boolean[] vis = new boolean[n * (MAX_MUT + 1)];
        Arrays.fill(vis, false);
        Arrays.fill(dist, oo);
        dist[0 * (MAX_MUT + 1) + MAX_MUT] = 0;
        prev[0 * (MAX_MUT + 1) + MAX_MUT] = -1;
        PriorityQueue<Pair> pq = new PriorityQueue<Pair>();
        pq.add(new Pair(0 * (MAX_MUT + 1) + MAX_MUT, 0));
        long ans = oo;
        while (!pq.isEmpty())
        {
            Pair curP = pq.poll();
            if (vis[curP.id])
            {
                continue;
            }
            vis[curP.id] = true;
            long curDist = curP.dist;
            int curCity = curP.id / (MAX_MUT + 1);
            if (curCity == n - 1)
            {
                ans = curDist;
                //print(prev, curP.id);
                break;
            }
            int numPeeps = curP.id % (MAX_MUT + 1);
            for (Edge e : adj[curCity])
            {
                int min = numPeeps - piratesInCity[e.dest];
                int max = numPeeps + piratesInCity[e.dest];
                if (min < 0)
                    min = (max & 1); // Make the value non-negative but keep parity
                if (max > MAX_MUT)
                    max = MAX_MUT - (max & 1); // Make the value less than or equal to MAX_MUT but keep parity
                for (int i = min; i <= max; i += 2)
                {
                    if (e.dest != n - 1 && i == 0)
                        continue;
                    int bribeCount = ((i + piratesInCity[e.dest]) - numPeeps) >> 1;
                    long newCost = curDist + bribeCount * bribes[e.dest] + e.cost * numPeeps;
                    int newId = i + (MAX_MUT + 1) * e.dest;
                    if (dist[newId] > newCost)
                    {
                        dist[newId] = newCost;
                        prev[newId] = curP.id;
                        pq.add(new Pair(newId, newCost));
                    }
                }
            }
        }
        
        // Print the found answer
        System.out.println(ans);
    }
    public static class Edge
    {
        int dest;
        long cost;
        Edge(int d, long c){
            dest = d;
            cost = c;
        }
    }
    
    public static class Pair implements Comparable<Pair>
    {
        int id;
        long dist;
        Pair(int i, long d)
        {
            id = i;
            dist = d;
        }
        
        public int compareTo(Pair o)
        {
            return Long.compare(dist, o.dist);
        }
    }
    
    public static void print(int[] prev, int loc)
    {
        if (prev[loc] != -1)
            print(prev, prev[loc]);
        System.out.print("(" + loc / (MAX_MUT + 1) + " " + loc % (MAX_MUT + 1) + ")   ");
    }
}
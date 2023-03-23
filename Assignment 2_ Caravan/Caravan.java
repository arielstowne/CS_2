
import java.util.*;
import java.lang.Math;

public class Caravan
{
	public static int budget, costOfWagon;
	public static double shipmentWeight;
	
    public static void main(String[] Args)
    {
        Scanner input = new Scanner(System.in);
        int numCities = input.nextInt();
        int numRoads = input.nextInt();
        
        // This will act as my PQ so it can be reused several times.
        ArrayList<Edge> PriorityQueue = new ArrayList<Edge>();
        
        for (int i = 0; i < numRoads; i++)
        {
        	// Reads the cities connected by, and the cost and capacity of, each road.
            int A = input.nextInt() - 1;
            int B = input.nextInt() - 1;
            int weight = input.nextInt();
            int cap = input.nextInt();
            Edge e1 = new Edge(A, B, weight, cap);
            
            PriorityQueue.add(e1);
        }
        
        budget = input.nextInt();
        costOfWagon = input.nextInt();
        shipmentWeight = input.nextInt();
        
        // The edges are sorted in increasing order of cost.
        Collections.sort(PriorityQueue); 
        
        // Kruskal's algorithm is used to find MSTs that meet several conditions.
        Kruskal(PriorityQueue, numCities);

        input.close();
    }
	
	public static void Kruskal(ArrayList<Edge> PriorityQueue, int numCities)
    {
		// This will hold the valid vehicle counts in increasing order.
    	LinkedList<Integer> solutionList = new LinkedList<Integer>();

    	int numOfSol = 0;
    	Edge road;
    	
    	for (double numberOfWagons = 1; numberOfWagons <= 10; numberOfWagons++)
    	{
    		// Buying the wagons reduces our budget for the roads.
    		int newBudget = budget - (costOfWagon * (int)numberOfWagons);
    		int validEdges = 0;
    		
    		// This keeps track of which cities are connected.
        	int[] DisjointSet = CreateSet(numCities);
    		
        	// Each wagon will carry a minimum of this amount of weight.
    		double weightPerWagon = Math.ceil(shipmentWeight/numberOfWagons);

    		Iterator<Edge> cheapestRoad = PriorityQueue.iterator();
    		
    		// Roads are considered starting from the cheapest.
            while (cheapestRoad.hasNext())
            {
            	road = cheapestRoad.next();
            	
            	// Road cap must be able to support the minimum wagon weight.
                if (road.capacity < weightPerWagon)
                	continue;
                
                // Road cost must fit in our budget.
                if ((newBudget - road.cost) < 0)
                	continue;

                // Road must not form a cycle.
                if (union(DisjointSet, road.st, road.en) == true)
                {
                	newBudget -= road.cost;
                	// The road met all conditions, so it's valid.
                	validEdges++;
                }
                
                // Found a MST that meets all conditions.
                if (validEdges == (numCities - 1))
                {
                	solutionList.add((int)numberOfWagons);
                	numOfSol++;
                	break;
                }
            }
    	}
    	
    	// Print result
    	System.out.println(numOfSol);
    	
    	Iterator<Integer> sol = solutionList.iterator();
    	while(sol.hasNext())
    		System.out.print(sol.next() + " ");
    	
    	System.out.println();
    }
	
	public static int[] CreateSet(int cities)
	{
		int[] DisjointSet = new int[cities];
		
		// Each node starts in their own set.
		for (int i = 0; i < cities; i++)
			DisjointSet[i] = i;
		
		return DisjointSet;
	}
    
    public static int find(int[] DisjointSet, int city)
    {
		// Find the parent of the node.
		while (city != DisjointSet[city])
			city = DisjointSet[city];

		return city;
	}
    
    public static boolean union(int[] DisjointSet, int city1, int city2)
    {
    	// Find the parent of each node.
        int r1 = find(DisjointSet, city1);
        int r2 = find(DisjointSet, city2);
        
        // Nodes with the same parents make a cycle.
        if (r1 == r2)
			return false;
        
        // Otherwise, connect the 2 nodes.
        DisjointSet[r1] = r2;
        return true;
    }
}

class Edge implements Comparable<Edge>
{
    int st, en, cost, capacity;
    
    // Each road is connected by 2 cities, and has a cost and capacity.
    public Edge(int start, int end, int w, int cap)
    {
        st = start;
        en = end;
        cost = w;
        capacity = cap;
    }
    
    @Override
    public int compareTo(Edge o)
    {
        return Integer.compare(this.cost, o.cost);
    }
}
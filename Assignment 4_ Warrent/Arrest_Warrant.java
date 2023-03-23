// Ariel Towne

import java.util.*;

public class Arrest_Warrant {
	
	public static long curMinCost = Long.MAX_VALUE;

	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		
		// Values in here shouldn't change after initialization.
		ArrayList<Node> allCities = new ArrayList<Node>();
		
		int numCities = input.nextInt();
        int numRoads = input.nextInt();
        
        // Keeps track of all adjacent edges.
		ArrayList<Road>[] Graph = new ArrayList[numCities];
		
		// Keeps track of number of mutexes we had before each visit to city at corresponding index.
		ArrayList<Integer>[] PreviousStates = new ArrayList[numCities];
		
		// Keeps track of the minimum cost of the path that had
		// the corresponding number of mutexes from PreviousStates.
		ArrayList<Long>[] StateCosts = new ArrayList[numCities];
        
        for (int i = 0; i < numCities; i++)
        {
        	// Reading from and writing to standard input
            int numOfPirates = input.nextInt();
            int costPerPirate = input.nextInt();
            
            // The number of pirates and cost per pirate are specific to cities.
            Node city = new Node(numOfPirates, costPerPirate);

            allCities.add(city);
        }
        
        // Initializing the ArrayLists[].
        
        for (int k = 0; k < numCities; k++)
        	Graph[k] = new ArrayList<Road>();
		
		for (int l = 0; l < numCities; l++)
			PreviousStates[l] = new ArrayList<Integer>();

		for (int m = 0; m < numCities; m++)
			StateCosts[m] = new ArrayList<Long>();
        
        for (int j = 0; j < numRoads; j++)
        {
        	// Reading from and writing to standard input
            int start = input.nextInt() - 1;
            int end = input.nextInt() - 1;
            int mutexCost = input.nextInt();
            
            Road road1 = new Road(end, 20, mutexCost, 0);
            
            // Since each path is bidirectional, we add a reverse edge.
            Road road2 = new Road(start, 20, mutexCost, 0);

            Graph[start].add(road1);
            Graph[end].add(road2);
        }

        dijkstras(Graph, allCities, numCities, PreviousStates, StateCosts);
        
        System.out.println(curMinCost);
		
        input.close();
	}
	
	public static void stateGenerator(PriorityQueue<Road> shortestPath, ArrayList<Node> allCities, Road curPath, Road adj, ArrayList<Integer>[] PreviousStates, int numCities, ArrayList<Long>[] StateCosts)
	{
		int required, bribed, remainingPirates;
		
		// // A distance algorithm that calculates the travel costs and adds it to the total.
		long costBeforePirates = curPath.getPathCost() + ((long)curPath.getMutexes() * (long)adj.getMutexCost());
		
		int index;
		
		// Either even numbers of mutexes or odd numbers will be possible to obtain in the next city.
		if( (allCities.get(adj.getEndCity()).getPirates() % 2) == (curPath.getMutexes() % 2))
			index = 0;
		else
			index = 1;	
			
		// Each state of the city can only have 1-20 mutexes,
		// so our "goal" is to obtain a certain number of mutexes per iteration.
		for (int goal = index; goal <= 20; goal+=2)
		{
			bribed = 0;
			
			// Determines minimum # of pirates needed to arrest or bribe to get to the goal number of mutexes.
			required = goal - curPath.getMutexes();
			
			// We need to bribe more mutexes to reach goal.
			if (required > 0)
				bribed += required;
			
			// This is the number of pirates left that we need to deal with.
			remainingPirates = allCities.get(adj.getEndCity()).getPirates() - Math.abs(required);
			
			// This means the goal was not possible with the number of mutexes we had and the pirates available.
			if (remainingPirates < 0)
				continue;
			
			// Bribe half and arrest half of the rest of the pirates to cancel each other out.
			bribed += (remainingPirates / 2);
			
			// We can only have 1-20 Mutexes unless we reached the .
			if ( (goal == 0 && (adj.getEndCity() != (numCities - 1))) || goal < 0 || goal > 20 )
				continue;
			
			// A distance algorithm that calculates the cost of bribing these pirates and adds it to the total.
			long newCost = costBeforePirates + ((long)allCities.get(adj.getEndCity()).getPirateCost() * (long)bribed);

			// Don't bother with paths that cost more than a previous one.
			if (newCost >= curMinCost)
				continue;
			
			// Check if this state was already visited.
			int stateIndex = PreviousStates[adj.getEndCity()].indexOf(goal);
					
			// Don't bother trying this state again if the cost isn't going to improve.
			if (stateIndex != -1)
				if (StateCosts[adj.getEndCity()].get(stateIndex) <= newCost)
					continue;

			// Adjust or create this current state.
			if (stateIndex != -1)
				StateCosts[adj.getEndCity()].set(stateIndex, newCost);
			else
			{
				PreviousStates[adj.getEndCity()].add(goal);
				StateCosts[adj.getEndCity()].add(newCost);
			}
			
			// This is a better complete path from Southchester to Tarasoga.
			if ( adj.getEndCity() == (numCities - 1) )
			{
				curMinCost = newCost;
				continue;
			}
			
			// Add this new path to the Priority Queue.
			shortestPath.add(new Road(adj.getEndCity(), goal, adj.getMutexCost(), newCost));
		}
		
	}
	
	// Returns the shortest distance from start vertex to end vertex.
	public static long dijkstras(ArrayList<Road>[] Graph, ArrayList<Node> allCities, int numCities, ArrayList<Integer>[] PreviousStates, ArrayList<Long>[] StateCosts) {
	
		// Set up the priority queue.
		PriorityQueue<Road> shortestPath = new PriorityQueue<Road>();
		
		// Start at Southchester.
		shortestPath.add(new Road(0, 20, 0, 0));
		
		// Go through all paths.
		while (!shortestPath.isEmpty())
		{
			// Get the shortest path so far.
			Road curPath = shortestPath.poll();
			
			// Enqueue all the neighboring roads by “splitting” nodes and allowing nodes to be revisited.
			for (Road adj : Graph[curPath.getEndCity()])
				stateGenerator(shortestPath, allCities, curPath, adj, PreviousStates, numCities, StateCosts);
		}
		
		return curMinCost;
	}

}

// Stores the number of pirates and cost to bribe each pirate.
class Node
{
	int pirates, pirateCost;
	

	public Node(int numOfPirates, int costPerPirate)
	{
	 	pirates = numOfPirates;
	 	pirateCost = costPerPirate;
	}

    public int getPirates()
    {
		return pirates;
	}
    
    public int getPirateCost()
    {
		return pirateCost;
	}
}

// Stores number of mutexes, cost to transport each mutex,
// the node the path goes to, and the total cost to take the path.
class Road implements Comparable<Road>
{
	int end, mutexes, mutexCost;
	long pathCost;

    public Road(int endNode, int numOfMutexes, int costPerMutex, long totalPathCost)
    {
    	end = endNode;
    	mutexes = numOfMutexes;
    	mutexCost = costPerMutex;
    	pathCost = totalPathCost;
    }
    
    public int getEndCity()
    {
		return end;
	}
    
    public int getMutexes()
    {
		return mutexes;
	}

    public int getMutexCost()
    {
		return mutexCost;
	}

    public long getPathCost()
    {
		return pathCost;
	}

    public int compareTo(Road o)
    {
    	return Long.compare(this.pathCost, o.pathCost);
    }
}
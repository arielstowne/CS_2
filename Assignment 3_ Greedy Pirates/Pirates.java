// Ariel Towne

import java.util.*;

public class Pirates {
	public static Interval Curtain = new Interval(0,0);
	
	public static void main(String[] Args)
    {
		ArrayList<Interval> allSortedIntervals = new ArrayList<Interval>();
		
		// Reads input, creates pirate intervals, sorts the intervals.
		int numOfIntervals = initializeIntervalsArray(allSortedIntervals);
		
		// Determines minimum number of reveals needed for all pirates.
		int numOfReveals = determineRevealCount(numOfIntervals, allSortedIntervals);
		
		System.out.println(numOfReveals);
    }
	
	public static double findXIntercept(double pirateX, double pirateY, double slope)
	{		
		// Finds the part of loot that the pirate's line of vision can see.
		return (pirateX - (pirateY / slope));
	}
	
	public static double findSlope(double pirateX, double pirateY, double curtainX, double curtainY)
	{
		// Finds the slope of line of vision between pirate and end of curtain.
		return ((curtainY - pirateY) / (curtainX - pirateX));
	}
	
	public static Interval createPirateInterval(double pirateX, double pirateY, double curtainDistance)
	{
		Interval Pirate = new Interval(0,0);
		
		// Finds the left end of the pirate interval.
		double slope = findSlope(pirateX, pirateY, Curtain.start, curtainDistance);
		Pirate.setStart(findXIntercept(pirateX, pirateY, slope));

		// Finds the right end of the pirate interval.
		slope = findSlope(pirateX, pirateY, Curtain.end, curtainDistance);
		Pirate.setEnd(findXIntercept(pirateX, pirateY, slope));
		
		return Pirate;
	}
	
	public static void findMin(double curtain1, double curtain2)
	{
		// Determines which end of the curtain's gap each point represents.
		if (curtain1 < curtain2)
		{
			Curtain.setStart(curtain1);
			Curtain.setEnd(curtain2);
		}
		else
		{
			Curtain.setStart(curtain2);
			Curtain.setEnd(curtain1);
		}
	}
	
	public static int initializeIntervalsArray(ArrayList<Interval> allSortedIntervals)
	{
		Scanner input = new Scanner(System.in);
		
        double curtain1 = input.nextDouble();
        double curtainDistanceFromLoot = input.nextDouble();
        
        double curtain2 = input.nextDouble();
        
        // This value should not change.
        curtainDistanceFromLoot = input.nextDouble();
        
        // Sets the start and end of curtain's interval.
        findMin(curtain1, curtain2);
        
        int numOfPirates = input.nextInt();
        
        for ( int i = 0; i < numOfPirates; i++ )
        {
	        double pirateX = input.nextDouble();
	        double pirateY = input.nextDouble();
	        
	        // Creates the interval representing the how much and which loot the pirates see.
	        Interval currentInterval = createPirateInterval(pirateX, pirateY, curtainDistanceFromLoot);
	        
	        allSortedIntervals.add(currentInterval);
        }
        
        input.close();
        
        // Sorts interval start value smallest to largest.
        Collections.sort(allSortedIntervals);
        
        return numOfPirates;
	}
	
	public static int determineRevealCount(int numOfIntervals, ArrayList<Interval> allSortedIntervals)
	{
		// Sorts interval end value smallest to largest.
		PriorityQueue<Reveal> currentlyUtilizedReveals = new PriorityQueue<Reveal>();
    	
		// Take the leftmost interval and give it it's own reveal to start.
		Reveal temp = new Reveal(allSortedIntervals.get(0).getStart(), allSortedIntervals.get(0).getEnd());
		currentlyUtilizedReveals.add(temp);
		int numOfReveals = 1;
		
		// Already dealt with the first interval, so start from the second.
		for (int i = 1; i < numOfIntervals; i++)
		{
			// Check if the current interval overlaps with the shortest reveal.
			if (allSortedIntervals.get(i).getStart() >= currentlyUtilizedReveals.peek().getEnd())
			{
				// Add the interval to the end of the reveal and reinsert reveal into PriorityQueue.
				Reveal currentReveal = new Reveal(currentlyUtilizedReveals.peek().getStart(), allSortedIntervals.get(i).getEnd());
				currentlyUtilizedReveals.poll();
				currentlyUtilizedReveals.add(currentReveal);
			}
			// Otherwise the interval would definitely overlap with longer reveals.
			else
			{
				// So we'll create a new reveal for the current interval.
				numOfReveals++;
				Reveal currentReveal = new Reveal(allSortedIntervals.get(i).getStart(), allSortedIntervals.get(i).getEnd());
				currentlyUtilizedReveals.add(currentReveal);
			}
		}
		
		return numOfReveals;
	}
}

// Represents how much loot each individual pirate sees.
class Interval implements Comparable<Interval>
{
    double start, end;
    
    public Interval(double start, double end)
    {
    	this.start = start;
    	this.end = end;
    }
    
    public void setStart(double start) {
		this.start = start;
	}
    
    public void setEnd(double end) {
		this.end = end;
	}
    
    public double getStart() {
		return start;
	}
    
    public double getEnd() {
		return end;
	}

    // Sorts smallest to largest.
    @Override
    public int compareTo(Interval o)
    {
        return Double.compare(this.start, o.start);
    }
}

// Represents the loot that all pirates in specific reveal can see.
class Reveal implements Comparable<Reveal>
{
    double start, end;
    
    public Reveal(double start, double end)
    {
    	this.start = start;
    	this.end = end;
    }
    
    public void setStart(double start) {
		this.start = start;
	}
    
    public void setEnd(double end) {
		this.end = end;
	}
    
    public double getStart() {
		return start;
	}
    
    public double getEnd() {
		return end;
	}
    
    // Sorts smallest to largest.
    @Override
    public int compareTo(Reveal o)
    {
        return Double.compare(this.end, o.end);
    }
}
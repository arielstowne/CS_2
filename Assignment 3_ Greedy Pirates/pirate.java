
import java.util.*;
import java.math.*;

public class pirate
{
    public static void main(String[] Args) throws Exception
    {
        Scanner sc = new Scanner(System.in);
        
        // Read in the curtains
        Point p1 = new Point(sc.nextInt(), sc.nextInt());
        Point p2 = new Point(sc.nextInt(), sc.nextInt());
        
        
        // Make the p1 be the smaller coordinate
        if (p1.x.compareTo(p2.x) > 0)
        {
            Point tmp = p1;
            p1 = p2;
            p2 = tmp;
        }
        
        // Make the x-axis using projective geometry
        Point xAxis = (new Point(0, 0)).cross(new Point(1, 0));
        
        
        // Read in the number of pirates
        int n = sc.nextInt();
        
        ArrayList<PirateEvent> events = new ArrayList<PirateEvent>();
        
        for (int i = 0; i < n; i++)
        {
            Point curP = new Point(sc.nextInt(), sc.nextInt());
            
            // Get the lines from the pirate to the curtain ends
            Point lineP1 = curP.cross(p1);
            Point lineP2 = curP.cross(p2);
            
            // Get the intersection points
            Point intersection1 = lineP1.cross(xAxis);
            Point intersection2 = lineP2.cross(xAxis);
            
            // The y values should be zero sanity check
            if (intersection1.y.compareTo(BigInteger.ZERO) != 0)
            {
                throw new Exception("Bad Data");
            }
            if (intersection2.y.compareTo(BigInteger.ZERO) != 0)
            {
                throw new Exception("Bad Data");
            }
            
            // Get the pirate events store the events into the list of events
            PirateEvent pe1 = new PirateEvent(intersection1, 1);
            PirateEvent pe2 = new PirateEvent(intersection2, -1);
            
            events.add(pe1);
            events.add(pe2);
        }
        
        // Store the answer as zero
        int ans = 0;
        int curOverlap = 0;
        
        // Sort the data
        Collections.sort(events);
        
        // Loop through the events
        for (PirateEvent pe : events)
        {
            curOverlap += pe.type;
            if (curOverlap > ans)
                ans = curOverlap;
        }
        
        // Print the found answer
        System.out.println(ans);
    }
    
    public static class PirateEvent implements Comparable<PirateEvent>
    {
        Point p;
        int type;
        
        PirateEvent(Point p, int type)
        {
            this.p = p;
            this.type = type;
        }
        
        public int compareTo(PirateEvent o)
        {
            if (p.compareTo(o.p) != 0)
                return p.compareTo(o.p);
            return type - o.type;
        }
    }
    
    public static class Point implements Comparable<Point>
    {
        BigInteger x, y, z;
        Point(BigInteger x, BigInteger y, BigInteger z){
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        Point(long x, long y){
            this.x = BigInteger.valueOf(x);
            this.y = BigInteger.valueOf(y);
            this.z = BigInteger.ONE;
        }
        
        Point cross(Point o)
        {
            return new Point((y.multiply( o.z ).subtract( z.multiply( o.y ) )),
                             (z.multiply( o.x ).subtract( x.multiply( o.z ) )),
                             (x.multiply( o.y ).subtract( y.multiply( o.x ) )));
        }
        
        public int compareTo(Point o)
        {
            return x.multiply(o.z).compareTo(o.x.multiply(z));
        }
        
        public String toString()
        {
            BigInteger g = x.gcd(z);
            g = g.gcd(y);
            return x.divide(g) + " " + y.divide(g) + " " + z.divide(g);
        }
    }
}
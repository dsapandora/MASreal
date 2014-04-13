package org.teacake.monolith.apk;

public class LinearInterpolator 
{
	public LinearInterpolator()
	{
		values= new java.util.Vector<Float>();
		timevalues = new java.util.Vector<Integer>();
	}
	public void addValue(float value, int timevalue)
	{
		values.add(new Float(value));
		timevalues.add(new Integer(timevalue));
	}
	public float getValue(int time)
	{
		float retval=0;
		for(int i=0;i<timevalues.size()-1;i++)
		{
			int firsttimeval=timevalues.get(i);
			int secondtimeval=timevalues.get(i+1);
			if(time>=firsttimeval&&time<=secondtimeval)
			{
				float firstval= values.get(i);
				float secondval= values.get(i+1);
				if(secondval>=firstval)
				{
					retval = ((time-firsttimeval)*(secondval-firstval)/(secondtimeval-firsttimeval))+firstval;
				}
				else
				{
					retval = ((secondtimeval-time)*((firstval-secondval)/(secondtimeval-firsttimeval)))+secondval;
				}
                return retval;
			}
		}
		return retval;
	}
	private java.util.Vector<Float> values;
	private java.util.Vector<Integer> timevalues;
	public static void main(String[] args)
	{
		LinearInterpolator li = new LinearInterpolator();
		li.addValue(0, 0);
		li.addValue(90, 1000);
		li.addValue(0, 2000);
                li.addValue(180,3000);
                li.addValue(0, 4000);
		for(int i=0;i<=40;i++)
		{
			System.out.println("t="+(i*100)+" value="+li.getValue(i*100));
		}
	}
}

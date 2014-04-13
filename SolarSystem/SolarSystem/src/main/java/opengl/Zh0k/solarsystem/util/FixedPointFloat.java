package opengl.Zh0k.solarsystem.util;

public class FixedPointFloat {
	
	public static int floatToFixedPoint(float number)
	{

                return (int)(number * 65536F);
	}
	
	public static float fixedPointToFloat(int number)
	{

            return ((float)number)/65536.0f;
    }

	
}

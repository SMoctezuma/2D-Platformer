package finalGame;

public class HealingTest {

	public static void main (String[] args)
	{
		int maxHP = 30;
		int healing = 0;
		healing = (int)(2*((Math.random()*((float)maxHP)/2f) + 1));
		int i = 0;
		while(i < 100)
		{
			healing = (int)(2*((Math.random()*((float)maxHP)/2f) + 1));
			System.out.println(healing);
			i++;
		}
	}
	
}

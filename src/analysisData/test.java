package analysisData;

import java.util.HashMap;

public class test {
	public static void main(String[] argv) {
		Atom a1 = new Atom(1);
		Atom a2 = new Atom(2);
		
		Bond a = new Bond(a1,a2);
		Bond b = new Bond(a1,a2);
		HashMap<Bond,Integer> map = new HashMap<Bond,Integer>();
		map.put(a,1);
		System.out.println(map.containsKey(b));
		
	}
}

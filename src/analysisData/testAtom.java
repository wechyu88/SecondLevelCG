package analysisData;

import java.util.ArrayList;

public class testAtom {
	public static void main(String[] argv) {
		ArrayList<Atom> atoms = new ArrayList<Atom>();
		Atom a1copy = new Atom(1);
		a1copy.setType(1);
		a1copy.setCrossx(true);
		a1copy.setXYZ(1.0,2.0,3.0);
		
		atoms.add(a1copy);
		Atom a2copy = new Atom(2);
		a2copy.setType(1);
		a2copy.setCrossx(true);
		a2copy.setXYZ(1.0,2.0,3.0);
		if(atoms.contains(a2copy)) {
			System.out.println("yes");
		}else {
			System.out.println("no");
		}
		//a1.setOutSideBoxAtom(a1copy);


		
	}
}

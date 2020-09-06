package analysisData;

import java.util.Comparator;

public class Atomdis implements Comparable<Atomdis>{
	Atom atom;
	double dis;
	public Atomdis(Atom a,double dis) {
		this.atom=a;
		this.dis=dis;
	}
	
	@Override
	public int compareTo(Atomdis a2) {
		
		if(this.dis-a2.dis>0) return 1;
		else if(this.dis-a2.dis<0) return -1;
		else return 0;
	}

}

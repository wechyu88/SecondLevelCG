package analysisData;

import java.util.ArrayList;
import java.util.HashMap;

public class buildSecondLevelSystem {
	double radius=10;
	HashMap<String,Integer> typeToTypeId=new HashMap<String,Integer>();
	ArrayList<Bond> bonds=new ArrayList<Bond>();
	ArrayList<Atom> atoms = new ArrayList<Atom> ();
	int numTypes;
	double celllength;
	double minx=10000000000.0,miny=minx,minz=minx,maxx=-1000000000.0,maxy=maxx,maxz=maxx;
	public buildSecondLevelSystem(CellSurface c) {
		this.maxx=c.xL;
		this.minx=0;
		this.maxy=c.yL;
		this.miny=0;
		this.maxz=100;
		this.minz=0;
		this.atoms=c.atomList;
		this.bonds=c.bondList;
		
	}
	public buildSecondLevelSystem(SecondLevel secCG ) {
		this.maxx=secCG.binNx*secCG.binsize;
		this.minx=0;
		this.maxy=secCG.binNy*secCG.binsize;
		this.miny=0;
		this.maxz=secCG.binNz*secCG.binsize;
		this.minz=0;
		this.atoms=secCG.atomlist;
		this.bonds=secCG.bondlist;
		
	}
	public buildSecondLevelSystem(dataFileInfo df) {
		this.maxx=df.xhi;
		this.minx=df.xlo;
		this.maxy=df.yhi;
		this.miny=df.ylo;
		this.maxz=df.zhi;
		this.minz=df.zlo;
		this.atoms=df.atomlist;
		this.bonds=df.bondlist;		
		
	}
	public void setNumTypes(int n) {
		this.numTypes = n;
	}
	public void setMolId(int id) {
		for(Atom a:this.atoms) {
			a.setMoleculeId(id);
		}		
	}
	public void setAllmass(double mass) {
		for(Atom a:this.atoms) {
			a.setMass(mass);
		}
	}
	public void setAllcharge(double charge) {
		for(Atom a:this.atoms) {
			a.setCharge(charge);
		}
	}
	public void setAlltype(int type) {
		for(Atom a:this.atoms) {
			a.setType(type);
		}
		this.numTypes=1;
	}
	
	public void setAllbondtype(int type) {
		for(Bond b:this.bonds) {
			b.settype(type);
		}
		//this.numTypes=1;		
	}

}

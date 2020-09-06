package analysisData;

import java.util.ArrayList;
import java.util.HashMap;

public class dataFileInfo {
	int numAtom;
	int numbond;
	ArrayList<Atom> atomlist = new ArrayList<Atom>();
	ArrayList<Bond> bondlist = new ArrayList<Bond>();
	double xlo;
	double xhi;
	double ylo;
	double yhi;
	double zlo;
	double zhi;
	HashMap<Integer,Atom> atomMap = new HashMap<Integer,Atom>();
	HashMap<Integer,Double> typeToMass = new HashMap<Integer,Double>();
	public dataFileInfo() {
		
	}
	public void setTypeToMass(HashMap<Integer,Double> typeToMass) {
		this.typeToMass=typeToMass;
	}
	public double caldis(Atom a1,Atom a2) {
		double dx = (a1.x-a2.x)-Math.round((a1.x-a2.x)/(xhi-xlo))*(xhi-xlo);
		//System.out.println("x "+Math.round((a1.x-a2.x)/(xhi-xlo)));
		double dy = (a1.y-a2.y)-Math.round((a1.y-a2.y)/(yhi-ylo))*(yhi-ylo);
		double dz = (a1.z-a2.z)-Math.round((a1.z-a2.z)/(zhi-zlo))*(zhi-zlo);

		double dis = Math.sqrt(dx*dx+dy*dy+dz*dz);

		return dis;
	}
	public void setnumAtom(int numA) {
		this.numAtom = numA;
	}
	public void setbondnum(int numb) {
		this.numbond = numb;
	}
	public void setAtomlist(ArrayList<Atom> atomlist) {
		this.atomlist=atomlist;
		for(Atom a:atomlist) {
			atomMap.put(a.id, a);
		}
	}
	public void setBondList(ArrayList<Bond> bondlist){
		this.bondlist=bondlist;
	}
	public void setboundary(double xl,double xh,double yl,double yh,double zl,double zh) {
		xlo=xl;
		xhi=xh;
		ylo=yl;
		yhi=yh;
		zlo=zl;
		zhi=zh;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atomMap == null) ? 0 : atomMap.hashCode());
		result = prime * result + ((atomlist == null) ? 0 : atomlist.hashCode());
		result = prime * result + numAtom;
		long temp;
		temp = Double.doubleToLongBits(xhi);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(xlo);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(yhi);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(ylo);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(zhi);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(zlo);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		dataFileInfo other = (dataFileInfo) obj;
		if (atomMap == null) {
			if (other.atomMap != null)
				return false;
		} else if (!atomMap.equals(other.atomMap))
			return false;
		if (atomlist == null) {
			if (other.atomlist != null)
				return false;
		} else if (!atomlist.equals(other.atomlist))
			return false;
		if (numAtom != other.numAtom)
			return false;
		if (Double.doubleToLongBits(xhi) != Double.doubleToLongBits(other.xhi))
			return false;
		if (Double.doubleToLongBits(xlo) != Double.doubleToLongBits(other.xlo))
			return false;
		if (Double.doubleToLongBits(yhi) != Double.doubleToLongBits(other.yhi))
			return false;
		if (Double.doubleToLongBits(ylo) != Double.doubleToLongBits(other.ylo))
			return false;
		if (Double.doubleToLongBits(zhi) != Double.doubleToLongBits(other.zhi))
			return false;
		if (Double.doubleToLongBits(zlo) != Double.doubleToLongBits(other.zlo))
			return false;
		return true;
	}
}

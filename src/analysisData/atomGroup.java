package analysisData;

import java.util.ArrayList;

public class atomGroup {
	ArrayList<Atom> atomlist = new ArrayList<Atom>();
	double comx;//center of mass of x
	double comy;//center of mass of y
	double comz;
	double cocx;//center of charge of x
	double cocy;
	double cocz;
	double totalC;
	double totalM;
	double dipolex;//unit dipole
	double dipoley;
	double dipolez;
	double dipoleVx;//dipole without normalize
	double dipoleVy;
	double dipoleVz;
	double theta;//the angle between vector and z axis
	double phi;// the angle between vector projection on xy plane to x axis
	double Rg;
	public atomGroup() {
		
	}
	public void addAtom(Atom a) {
		atomlist.add(a);
	}
	public void deleteAtom(Atom b) {
		if(atomlist.contains(b)) {
			atomlist.remove(b);
		}
	}
	public void caldipole() {
		double sumdx=0.0;
		double sumdy=0.0;
		double sumdz=0.0;
		for(Atom a:atomlist) {
			sumdx+=a.x*a.charge;
			sumdy+=a.y*a.charge;
			sumdz+=a.z*a.charge;
		}
		double length = Math.sqrt(Math.pow(sumdx, 2)+Math.pow(sumdy, 2)+Math.pow(sumdz, 2));
		this.dipolex=sumdx/length;
		this.dipoley=sumdy/length;
		this.dipolez=sumdz/length;
		
		this.theta = Math.toDegrees(Math.atan(Math.sqrt(this.dipolex*this.dipolex+this.dipoley*this.dipoley)/this.dipolez));
		this.phi = Math.toDegrees(Math.atan(this.dipoley/this.dipolex));
		
		
	}
	
	public void calglydipole() {
		double sumdx=0.0;
		double sumdy=0.0;
		double sumdz=0.0;
		double midx=0.0,midy=0.0,midz=0.0;
		double leftx=0.0,lefty=0.0,leftz=0.0;//type 64 id small is left
		double rightx=0.0,righty=0.0,rightz=0.0;//type 64 id large is right
		//glycerol 12 middle carbon, 64 side carbon;
		ArrayList<Atom> carbons =new ArrayList<Atom>();
		for(Atom a:atomlist) {
			if(a.type==12) {
				midx=a.x;
				midy=a.y;
				midz=a.z;
			}else if(a.type==64) {
				carbons.add(a);
			}
		}
		Atom leftC,rightC;
		if(carbons.get(0).id<carbons.get(1).id) {
			leftC=carbons.get(0);
			rightC=carbons.get(1);
		}else {
			leftC=carbons.get(1);
			rightC=carbons.get(0);
		}
		leftx=(leftC.x+midx)/2;
		lefty=(leftC.y+midy)/2;
		leftz=(leftC.z+midz)/2;
		rightx=(rightC.x+midx)/2;
		righty=(rightC.y+midy)/2;
		rightz=(rightC.z+midz)/2;
		sumdx=rightx-leftx;
		sumdy=righty-lefty;
		sumdz=rightz-leftz;
		double length=Math.sqrt(sumdx*sumdx+sumdy*sumdy+sumdz*sumdz);
		this.dipolex=sumdx/length;
		this.dipoley=sumdy/length;
		this.dipolez=sumdz/length;
		
		this.theta = Math.toDegrees(Math.atan(Math.sqrt(this.dipolex*this.dipolex+this.dipoley*this.dipoley)/this.dipolez));
		this.phi = Math.toDegrees(Math.atan(this.dipoley/this.dipolex));
		
		
	}
	
	public void caldipoleV() {
		double sumdx=0.0;
		double sumdy=0.0;
		double sumdz=0.0;
		for(Atom a:atomlist) {
			sumdx+=a.x*a.charge;
			sumdy+=a.y*a.charge;
			sumdz+=a.z*a.charge;
		}
		//double length = Math.sqrt(Math.pow(sumdx, 2)+Math.pow(sumdy, 2)+Math.pow(sumdz, 2));
		this.dipoleVx=sumdx;
		this.dipoleVy=sumdy;
		this.dipoleVz=sumdz;
		this.theta = Math.atan(Math.sqrt(this.dipoleVx*this.dipoleVx+this.dipoleVy*this.dipoleVy)/this.dipoleVz);
		this.phi = Math.atan(this.dipoleVy/this.dipoleVx);
		
	}	
	public ArrayList<Double> calcom() {
		ArrayList<Double> com=new ArrayList<Double>() ;
		double sumM=0.0;
		double sumx=0.0;
		double sumy=0.0;
		double sumz=0.0;
		for(Atom a:atomlist) {
			sumx+=a.x*a.mass;
			sumy+=a.y*a.mass;
			sumz+=a.z*a.mass;
			sumM+=a.mass;
		}
		totalM=sumM;
		sumx/=sumM;
		sumy/=sumM;
		sumz/=sumM;
		com.add(sumx);
		com.add(sumy);
		com.add(sumz);
		comx=sumx;
		comy=sumy;
		comz=sumz;
		return com;
	}
	
	public ArrayList<Double> calcoc() {//calculate center of charge
		ArrayList<Double> coc=new ArrayList<Double>() ;
		double sumC=0.0;
		double sumx=0.0;
		double sumy=0.0;
		double sumz=0.0;
		for(Atom a:atomlist) {
			sumx+=a.x*a.charge;
			sumy+=a.y*a.charge;
			sumz+=a.z*a.charge;
			sumC+=a.charge;
		}
		totalC=sumC;
		sumx/=sumC;
		sumy/=sumC;
		sumz/=sumC;
		coc.add(sumx);
		coc.add(sumy);
		coc.add(sumz);
		cocx=sumx;
		cocy=sumy;
		cocz=sumz;
		return coc;
	}
	public void calRg() {//must be after calcom;
		int count=0;
		double rg=0;
		for(Atom a:atomlist) {
			rg+=(a.x-comx)*(a.x-comx)+(a.y-comy)*(a.y-comy)+(a.z-comz)*(a.z-comz);
			
		}
		rg/=atomlist.size();
		rg=Math.sqrt(rg);
		this.Rg=rg;
	}
}

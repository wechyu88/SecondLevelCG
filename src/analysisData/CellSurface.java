package analysisData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class CellSurface {
	//initialize one surface by put it flat, and there are random atoms above it.
	//atoms below the surface should be connected by the atoms above it , 
	//the angle distribution should follow the first level calculation
	public ArrayList<Atom> atomList = new ArrayList<Atom>(); //atom list
	// all the surfaces are from 0->xL and 0->yL, z=0, using  transform and rotate to get other sides. 
	public ArrayList<Bond> bondList = new ArrayList<Bond>();
	public double xL,yL;
	public double rhoB;// bond density;
	public int nb;
	public static double surfaceR = 5.0;// a fixed cg beads sphere region
	public static double rToChild=5.0;
	public static int bondCounter=0;
	public static int bondtypeToChild=1;
	public static int bondtypeChildToChild=2;
	public static int atomCounter=0;
	public static int MolId=0;
	public static int aboveAtomType=1;// first create plane surface, above atom type is 1, below is 2;
	public static int belowAtomType=2;
	public static int childType=3;
	public CellSurface(double xL,double yL,double rhoB) {
		this.xL=xL;
		this.yL=yL;
		this.rhoB=rhoB;
		double area = xL*yL;
		this.nb = (int)(area*rhoB);//number of Bonds;	
		generateAtoms();
	}
	public void generateAtoms() {
		int genNB = 0;
		double mass = 10.0;
		double charge=0.0;
		//int atomid =this.startAtomId;
		//int molId=this.startMolId;
		while(genNB<this.nb) {
			LinkedList<Atom> atomQueue = new LinkedList<Atom>();
			//atomid++;
			//molId++;
			//System.out.println(" atom id "+atomid+" mole id "+molId+" total bonds "+this.nb);
			Atom firstAtom = createAnAtom(++atomCounter,++MolId,1,mass,charge);

			while(!checkNotOverLap(surfaceR,firstAtom)) {
				firstAtom = createAnAtom(atomCounter,MolId,aboveAtomType,mass,charge);
			}
			Atom firstChildAtom = CreateChildAtom(firstAtom,++atomCounter);
			firstAtom.setChildAtom(firstChildAtom);
			Bond b = new Bond(firstAtom,firstChildAtom);
			b.settype(bondtypeToChild);
			b.setid(++bondCounter);
			bondList.add(b);
			atomQueue.add(firstAtom);
			while(!atomQueue.isEmpty()) {
				Atom tempAtom = atomQueue.poll();

				tempAtom.setVisit(true);
				atomList.add(tempAtom);
				atomList.add(tempAtom.childAtom);
				int tBonds = totalBonds();
				int leftBonds = tBonds-tempAtom.connections.size();
				for(int i=1;i<=leftBonds&&genNB<nb;i++) {
					genNB++;
					//atomid++;
				//	System.out.println(" atom id "+atomid+" mole id "+molId+" total bonds "+this.nb+" "+atomQueue.size());

					Atom cA = ConnectedAtom(tempAtom,0,++atomCounter);
					int newtry = 0;
					while(!checkNotOverLap(surfaceR,cA)) {
						newtry++;
						cA = ConnectedAtom(tempAtom,0,atomCounter);
						//System.out.println("new insert beads test "+newtry+" times");
					}
					tempAtom.connections.add(cA);
					cA.connections.add(tempAtom);
					atomQueue.add(cA);
					
					
					Atom conChildAtom = CreateChildAtom(cA,++atomCounter);
					cA.setChildAtom(conChildAtom);
					Bond b2 = new Bond(cA,conChildAtom);
					b2.settype(bondtypeToChild);
					b2.setid(++bondCounter);
					bondList.add(b2);
					Bond b3 = new Bond(tempAtom.childAtom,conChildAtom);
					b3.settype(bondtypeToChild);
					b3.setid(++bondCounter);
					bondList.add(b3);					
				}
			}
			
		}
		
	}
	public int totalBonds() {
		double probOneB=0.8;
		double probTwoB=0.15;
		double probThreeB=0.03;
		double probFourB=0.02;
		Random rd = new Random();
		double probB = rd.nextDouble();
		int totalB=0;
		if(probB<probOneB) {
			totalB= 1;
		}else if(probB<probTwoB) {
			totalB=2;
		}else if(probB<probThreeB) {
			totalB=3;
		}else if(probB<probFourB) {
			totalB=4;
		}
		return totalB;		
	}
	public boolean checkNotOverLap(double limitR,Atom newAtom) {
		for(Atom a:atomList) {
			if(a.z/newAtom.z<0) continue;
			double dis = Math.sqrt(Math.pow((a.x-newAtom.x),2)+Math.pow((a.y-newAtom.y),2));
			if(dis<limitR) {
				
				//System.out.println(" break bond length is "+dis+" ids are "+a.moelculeid+" "+newAtom.moelculeid+" "+a.id+" "+newAtom.id);
				return false;// return false means overLap
			}
		}
		
		return true;
	}
	public Atom createAnAtom(int aId,int mId,int type,double mass,double charge) {
		Random rd = new Random();

		double X=rd.nextDouble()*xL;
		double Y=rd.nextDouble()*yL;
		double zrange = 10.0;
		
		if(X<0||X>100) {
			//System.out.println("I'm printing");
		}
		double Z = rd.nextDouble()*zrange;//zrange should be changed based on the z distribution
		Atom a = new Atom(aId);
		a.setXYZ(X, Y, Z);
		a.setType(type);
		a.setCharge(charge);
		a.setMass(mass);
		a.setMoleculeId(mId);
		a.setVisit(false);
		
		
		return a;
		
	}
	
	public Atom ConnectedAtom(Atom a, double z,int nextid) {//z is the surface plane
		//theta phi two angle determine next atom. thetais the angle between r and the pertenticular axis, (0-90)degree
		//phi is the 0-360 degree on the plane.
		Random rd = new Random();
		double theta = rd.nextDouble()*Math.toRadians(90);//should time a range, 90 is the range,
		double zrange = 10.0;
		double nextZ = zrange - Math.abs(a.z)+0.5*zrange*rd.nextDouble();
		//double nextZ = rd.nextDouble()*zrange;//should time a range if it is uniform distribute, if other distribution, think of other method
		double r = (Math.abs(a.z)+nextZ)/Math.cos(Math.toRadians(theta));
		//System.out.println(" new insert r is "+r);
		double phi = rd.nextDouble()*Math.toRadians(360);
		double nextX = r*Math.sin(theta)*Math.cos(phi)+a.x;
		double nextY = r*Math.sin(theta)*Math.sin(phi)+a.y;
		//System.out.println(a.x+" "+a.y+" "+nextX+" "+nextY+" "+r);
		nextZ= (0-a.z)/Math.abs(a.z)*nextZ;
		Atom next=new Atom(nextid);
		next.setXYZ(nextX, nextY, nextZ);
		next.setCharge(a.charge);
		next.setMass(a.mass);
		next.setMoleculeId(a.moelculeid);
		next.setType(a.type==1?2:1);
		next.setVisit(false);
		return next;
	}
	
	public Atom CreateChildAtom(Atom a, int id) {// create the child atom of Atom a
		Atom child = new Atom(id);
		Random rd = new Random();
		double u = rd.nextDouble();
		double v = rd.nextDouble();
		double theta = 2*Math.PI*u;
		double phi = Math.acos(2*v-1);
		double x = rToChild*Math.sin(theta)*Math.sin(phi);
		double y = rToChild*Math.cos(theta)*Math.sin(phi);
		double z = rToChild*Math.cos(phi);
		child.setXYZ(x+a.x, y+a.y, z+a.z);
		child.setCharge(a.charge);
		child.setMass(a.mass);
		child.setMoleculeId(a.moelculeid);
		child.setType(childType);
		child.setVisit(false);
		
		return child;
	}
	public void rotateXYtoYZ() {// rotate xy surface to yz surface
		//since the surface is created flat, we can rotate and move the surface
		//the create surfaces of  cubic other sides
		for(Atom a:atomList) {
			double tempx = a.x;
			double tempy = a.y;
			double tempz = a.z;
			a.setXYZ(-1.0*tempz, tempy, tempx);
		}
	}
	
	public void rotateXYtoXZ() {
		for(Atom a:atomList) {
			double tempx = a.x;
			double tempy = a.y;
			double tempz = a.z;
			a.setXYZ(tempx, -1*tempz, tempy);
		}
		
	}
	public void transforUpZ(double length) {// move up by vector (0,0,length) 
		for(Atom a:atomList) {
			double tempx = a.x;
			double tempy = a.y;
			double tempz = a.z;
			a.setXYZ(tempx, tempy, tempz+length);
		}		
	}
	public void transforUpY(double length) {// move up by vector (0,length,0) 
		for(Atom a:atomList) {
			double tempx = a.x;
			double tempy = a.y;
			double tempz = a.z;
			a.setXYZ(tempx, tempy+length, tempz);
		}		
	}	
	public void transforUpX(double length) {// move up by vector (length,0,0) 
		for(Atom a:atomList) {
			double tempx = a.x;
			double tempy = a.y;
			double tempz = a.z;
			a.setXYZ(tempx+length, tempy, tempz);
		}		
	}
}

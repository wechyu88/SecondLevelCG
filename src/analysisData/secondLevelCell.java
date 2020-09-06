package analysisData;

import java.util.ArrayList;
import java.util.Random;

public class secondLevelCell {
	double xlo, xhi,ylo,yhi,zlo,zhi;
	int X,Y,Z;//total number of cells on each direction
	int posX,posY,posZ;//where this cell locate;
	ArrayList<Atom> atoms;
	ArrayList<Bond> bonds;
	int atomNum;
	int startId;
	int crossWallBondLength;
	int sphereBondLength;
	public secondLevelCell() {
		
	}
	public void buildWall(int wallnum) {//
		switch (wallnum) {
			case 1:  
		}
		
	}
	public void buildxFront() {
		
		for(int i = 0;i<atomNum;i++) {
			Random rd = new Random();
			double x= rd.nextDouble()*(xhi-0.9*xhi)+0.9*xhi;
			double y= rd.nextDouble()*(yhi-ylo)+ylo;
			double z= rd.nextDouble()*(zhi-zlo)+zlo;
			Atom a = new Atom(startId+i+1);// inside the wall
			a.setXYZ(x,y,z);
			Atom a_b = new Atom(startId+atomNum+i);// the bead in a spherical shell of atom a
			a_b.setXYZ(x, y, z);
			Atom c = new Atom(startId+atomNum*2+i);// the atom c on the other side of the wall that connect with atom a;
			c.setXYZ(x, y, z);
			Atom c_b = new Atom(startId+atomNum*3+i);//the bead in a spherical shell of atom c
			c_b.setXYZ(x, y, z);

		}
		
	}
	public Atom otherSide(int id, int side,Atom a) {// 1 xfront 2 x back, 3, y front, 4 y back, 5 z front, 6 z bac;
		Atom a_b = new Atom(id);
		if(side==1) {
			
		}
		
		
		return a_b;
	}
}

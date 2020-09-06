package analysisData;

import java.util.ArrayList;

public class SecondLevel {

	ArrayList<Atom> atomlist;
	ArrayList<Bond> bondlist;
	ArrayList<CellSurface> CellSurfaces;
	int binNx,binNy,binNz;
	double binsize;//cubic bin box length
	double bondRho; //density of bond  number of bonds/area
	
	public SecondLevel(int binNx,int binNy,int binNz, double binsize,double bondRho) {
		this.binNx=binNx;
		this.binNy=binNy;
		this.binNz=binNz;
		this.binsize=binsize;
		this.bondRho=bondRho;
		CellSurfaces = new ArrayList<CellSurface>();
		for(int i=0;i<binNx;i++) {
			for(int j=0;j<binNy;j++) {
				for(int k=0;k<binNz;k++) {
					ArrayList<CellSurface> eachBin=this.BuildOneBin();
					for(int m=0;m<3;m++) {
						eachBin.get(m).transforUpZ(binsize*k);
						eachBin.get(m).transforUpX(binsize*i);
						eachBin.get(m).transforUpY(binsize*j);
					}

					System.out.println(i+" "+j+" "+k);
					this.CellSurfaces.addAll(eachBin);
				}
			}
		}
		getAtomList();
	}
	
	public ArrayList<CellSurface> BuildOneBin(){
		ArrayList<CellSurface> oneBin = new ArrayList<CellSurface>();
		CellSurface surfxy = new CellSurface(binsize,binsize,bondRho);
		CellSurface surfyz = new CellSurface(binsize,binsize,bondRho);
		surfyz.rotateXYtoYZ();
		CellSurface surfxz = new CellSurface(binsize,binsize,bondRho);
		surfxz.rotateXYtoXZ();
		oneBin.add(surfxy);
		oneBin.add(surfyz);
		oneBin.add(surfxz);
		return oneBin;
	}
	public void getAtomList() {
		this.atomlist = new ArrayList<Atom> ();
		this.bondlist = new ArrayList<Bond> ();

		for(int i=0;i<CellSurfaces.size();i++) {
			atomlist.addAll(CellSurfaces.get(i).atomList);
			bondlist.addAll(CellSurfaces.get(i).bondList);
		}
	}
	
	
	
}

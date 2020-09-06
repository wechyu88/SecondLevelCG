package analysisData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class bs {
	public static void main(String[] argv) throws FileNotFoundException, IOException {
		bs bs1 = new bs();
		SecondLevel secLevel = new SecondLevel(3,3,3,100.0,0.005);
		bs1.writedata(secLevel);
	}
	public bs() {
		
	}
	public void writedata(CellSurface cSurf) throws FileNotFoundException, IOException {
		//String path = "C:\\Users\\wechyu88\\Google Drive\\x1cWen\\CoarseGrainEpoxy\\SecondLevelCG\\";
		String path = "C:\\Users\\wechyu88\\WenWorkSpace\\CGEPOXY\\secondLevel\\";

		//readMCoutput rmc=new readMCoutput(path+"MassDis1.txt");	
		buildSecondLevelSystem bS = new  buildSecondLevelSystem(cSurf);
		PrintWriter pw = new PrintWriter(new File(path+"2by2by2box.txt"));
		bS.setAllmass(1000);
		bS.setAllcharge(0);
		//bS.setAlltype(1);
		//bS.findbond();

		printTitle(pw,bS);
		for(Atom a:bS.atoms) {
			pw.println(a.id+" "+a.moelculeid+" "+a.type+" "+a.charge+" "+a.x+" "+a.y+" "+a.z);
			//System.out.println(a.id+" "+a.type+" "+a.x+" "+a.y+" "+a.z);
		}
		pw.println("\n"+"Bonds\n");
		int bondid=0;
		for(Bond b:bS.bonds) {
			bondid++;
			pw.println(bondid+" "+1+" "+b.a.id+" "+b.b.id);
		}
		pw.close();
			
		
	}
	public void writedata(SecondLevel secCG) throws FileNotFoundException, IOException {
		//String path = "C:\\Users\\wechyu88\\Google Drive\\x1cWen\\CoarseGrainEpoxy\\SecondLevelCG\\";
		String path = "C:\\Users\\wechyu88\\WenWorkSpace\\CGEPOXY\\secondLevel\\";

		//readMCoutput rmc=new readMCoutput(path+"MassDis1.txt");	
		buildSecondLevelSystem bS = new  buildSecondLevelSystem(secCG);
		PrintWriter pw = new PrintWriter(new File(path+"3by3by3box.txt"));
		bS.setAllmass(1000);
		bS.setAllcharge(0);
		//bS.setAlltype(1);
		//bS.findbond();

		printTitle(pw,bS);
		for(Atom a:bS.atoms) {
			pw.println(a.id+" "+a.moelculeid+" "+a.type+" "+a.charge+" "+a.x+" "+a.y+" "+a.z);
			//System.out.println(a.id+" "+a.type+" "+a.x+" "+a.y+" "+a.z);
		}
		pw.println("\n"+"Bonds\n");
		int bondid=0;
		for(Bond b:bS.bonds) {
			bondid++;
			pw.println(bondid+" "+1+" "+b.a.id+" "+b.b.id);
		}
		pw.close();
			
		
	}	
	public void writedata(randomNetWork rnw) throws FileNotFoundException, IOException {
		//String path = "C:\\Users\\wechyu88\\Google Drive\\x1cWen\\CoarseGrainEpoxy\\SecondLevelCG\\";
		String path = "C:\\Users\\wechyu88\\WenWorkSpace\\CGEPOXY\\secondLevel\\";

		//readMCoutput rmc=new readMCoutput(path+"MassDis1.txt");	
		buildSystem bS = new  buildSystem(rnw);
		PrintWriter pw = new PrintWriter(new File(path+"network.txt"));
		bS.setAllmass(1000);
		bS.setAllcharge(0);
		bS.setAlltype(1);
		//bS.findbond();

		printTitle(pw,bS);
		for(Atom a:bS.atoms) {
			pw.println(a.id+" "+a.moelculeid+" "+a.type+" "+a.charge+" "+a.x+" "+a.y+" "+a.z);
			//System.out.println(a.id+" "+a.type+" "+a.x+" "+a.y+" "+a.z);
		}
		pw.println("\n"+"Bonds\n");
		int bondid=0;
		for(Bond b:bS.bonds) {
			bondid++;
			pw.println(bondid+" "+1+" "+b.a.id+" "+b.b.id);
		}
		pw.close();
			
		
	}
	public void writedata(PrintWriter pw,buildSystem bS) throws FileNotFoundException, IOException {
		//String path = "C:\\Users\\wechyu88\\Google Drive\\x1cWen\\CoarseGrainEpoxy\\SecondLevelCG\\";
		//String path = "C:\\Users\\wechyu88\\WenWorkSpace\\CGEPOXY\\secondLevel\\";

		//readMCoutput rmc=new readMCoutput(path+"MassDis1.txt");	
	//	buildSystem bS = new  buildSystem(rnw);
		
		bS.setAllmass(1000);
		bS.setAllcharge(0);
		//bS.setAlltype(1);
		//bS.findbond();

		printTitle(pw,bS);
		for(Atom a:bS.atoms) {
			pw.println(a.id+" "+a.moelculeid+" "+a.type+" "+a.charge+" "+a.x+" "+a.y+" "+a.z);
			//System.out.println(a.id+" "+a.type+" "+a.x+" "+a.y+" "+a.z);
		}
		pw.println("\n"+"Bonds\n");
		int bondid=0;
		for(Bond b:bS.bonds) {
			bondid++;
			pw.println(bondid+" "+b.type+" "+b.a.id+" "+b.b.id);
		}
		pw.close();
			
		
	}

	public static void printTitle(PrintWriter pw,buildSecondLevelSystem bS) {
		pw.println("lammpsdata file\n");

		pw.println(bS.atoms.size()+" atoms");
		pw.println(bS.bonds.size()+" bonds");
		pw.println("\n"+bS.numTypes+" atom types");
		pw.println(1+" bond types");
		pw.println("\n");
		
		pw.print(bS.minx+" "+bS.maxx+" xlo xhi\n");
		pw.print(bS.miny+" "+bS.maxy+" ylo yhi\n");
		pw.print(bS.minz+" "+bS.maxz+" zlo zhi\n");
		
		pw.println("\n");
		pw.println("Masses\n");
		for(int i=1;i<=bS.numTypes;i++) {
			pw.println("1 1000");
			
		}


		pw.print("\n");
		pw.println("Atoms");
		pw.print("\n");

		
		


	}
	public static void printTitle(PrintWriter pw,buildSystem bS) {
		pw.println("lammpsdata file\n");

		pw.println(bS.atoms.size()+" atoms");
		pw.println(bS.bonds.size()+" bonds");
		pw.println("\n"+bS.numTypes+" atom types");
		pw.println(1+" bond types");
		pw.println("\n");
		
		pw.print(bS.minx+" "+bS.maxx+" xlo xhi\n");
		pw.print(bS.miny+" "+bS.maxy+" ylo yhi\n");
		pw.print(bS.minz+" "+bS.maxz+" zlo zhi\n");
		
		pw.println("\n");
		pw.println("Masses\n");
		for(int i=1;i<=bS.numTypes;i++) {
			pw.println("1 1000");
			
		}


		pw.print("\n");
		pw.println("Atoms");
		pw.print("\n");

		
		


	}
	
	
}

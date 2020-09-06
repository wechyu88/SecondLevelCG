package analysisData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class readData {
	BufferedReader br;

	public static void main(String[] argv) throws IOException {
		//String pathInput = argv[0];
	    //String pathOutput = System.getProperty("user.dir");
		String pathInput = "C:\\Users\\wechyu88\\WenWorkSpace\\CGEPOXY\\secondLevel\\data.noz";
		String path = pathInput;
		readData rd = new readData(path);
		dataFileInfo dinf= readDataInfo(rd.br);
		//ArrayList<Bond> bondList = dinf.bondlist;
		int crossx=0;
		int crossy=0;
		int crossz=0;
		double tmass=0;
		double zmin=10000000;
		double zmax = -1000000000;
		for(Atom a: dinf.atomlist) {
			if(zmin>a.z) {
				zmin=a.z;
			}
			if(zmax<a.z) {
				zmax=a.z;
			}
			tmass+=a.mass;
		}
		System.out.println("total mass is "+tmass+" zmin is "+zmin+ " zmax is "+zmax);
		/*
		 * for(Bond bond:bondList) { Atom a=bond.a; Atom b=bond.b;
		 * if(Math.abs(a.x-b.x)>0.5*Math.abs(dinf.xhi-dinf.xlo)) { crossx++; }else
		 * if(Math.abs(a.y-b.y)>0.5*Math.abs(dinf.yhi-dinf.ylo)) { crossy++; }else
		 * if(Math.abs(a.z-b.z)>0.5*Math.abs(dinf.zhi-dinf.zlo)) { crossz++; } }
		 */
	//	System.out.println(" cross x "+crossx+" cross y "+crossy+" cross z "+crossz+" total bond "+dinf.bondlist.size());
		rd.br.close();
	}
	public readData(String path) throws FileNotFoundException {
		File file = new File(path);
		FileReader fr = new FileReader(file);
		this.br = new BufferedReader(fr);
	}
	public static dataFileInfo readDataInfo(BufferedReader br) throws IOException {
		String line=br.readLine();
		dataFileInfo dataInfo = new dataFileInfo();
		int numAtom=0;
		int numBond=0;
		int numtype=0;
		ArrayList<Atom>atmlist = new ArrayList<Atom>();
		HashMap<Integer,Double> typeToMass = new HashMap<Integer,Double>();

		while((line=br.readLine())!=null) {
			if(line.matches("\\d+\\s+atoms")) {
				System.out.println(line);
				numAtom = Integer.valueOf(line.split(" ")[0]);
				dataInfo.setnumAtom(numAtom);
			}else if(line.matches("\\d+\\s+bonds")) {
				numBond = Integer.valueOf(line.split(" ")[0]);
				dataInfo.setbondnum(numBond);				
			}else if(line.matches("\\d+\\s+atom\\s+types")) {
				numtype=Integer.valueOf(line.split(" ")[0]);
				
			} else if(line.matches(".*xlo xhi")) {
				String [] x = line.split(" ");
				double xlow = Double.valueOf(x[0]);
				double xhigh=Double.valueOf(x[1]);
				line=br.readLine();
				String [] y = line.split(" ");
				double ylow = Double.valueOf(y[0]);
				double yhigh=Double.valueOf(y[1]);
				line=br.readLine();
				String [] z = line.split(" ");
				double zlow = Double.valueOf(z[0]);
				double zhigh=Double.valueOf(z[1]);
				dataInfo.setboundary(xlow, xhigh, ylow, yhigh, zlow, zhigh);				
				
			}else if(line.matches("Masses.*")){
				line=br.readLine();
				for(int i=1;i<=numtype;i++) {
					line=br.readLine();
					Integer type = Integer.valueOf(line.split(" ")[0]);
					Double mass = Double.valueOf(line.split(" ")[1]);
					typeToMass.put(type, mass);
				}
				dataInfo.setTypeToMass(typeToMass);
				
			}else if(line.matches("Atoms.*")) {
				line = br.readLine();
				for(int i =1;i<=numAtom;i++) {
					line=br.readLine();
					//System.out.println(line);

					String [] fields = line.split("\\s+");
					
					Atom atm = new Atom(Integer.valueOf(fields[0]));
					atm.setType(Integer.valueOf(fields[2]));

					atm.setCharge(Double.valueOf(fields[3]));
					//atm.setMass(Double.valueOf(fields[3]));
					atm.setMass(typeToMass.get(atm.type));
						
					atm.setMoleculeId(Integer.valueOf(fields[1]));
					atm.setXYZ(Double.valueOf(fields[4]), Double.valueOf(fields[5]), Double.valueOf(fields[6]));				
					atmlist.add(atm);
				}
				break;
			}
		}
		dataInfo.setAtomlist(atmlist);
		ArrayList<Bond> bondlist = new ArrayList<Bond>();
		while((line=br.readLine())!=null) {
			if(line.matches("^Bonds.*")){
				line = br.readLine();
				for(int i =1;i<=numBond;i++) {
					line=br.readLine();
					//System.out.println(line);
					String [] fields = line.split(" ");
					Atom a =dataInfo.atomMap.get(Integer.valueOf(fields[2]));
					Atom b =dataInfo.atomMap.get(Integer.valueOf(fields[3]));
					try {
					a.addConnection(b);
					b.addConnection(a);
					}catch(Exception e) {
						System.out.println(" a or b is null ");
					}
					
					Bond bond = new Bond(a,b);
					bond.setid(Integer.valueOf(fields[0]));
					bond.settype(Integer.valueOf(fields[1]));
					bondlist.add(bond);
				}				
				
			}
		}
		dataInfo.setBondList(bondlist);



		return dataInfo;
	}
}

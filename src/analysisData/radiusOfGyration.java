package analysisData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import analysisData.atomGroup;

public class radiusOfGyration {
	dataFileInfo df;
	readdumpAtom rdc;
	dumpOneStep dos;
	ArrayList<Atom> atoms;
	HashMap<Integer,Atom> atomMap = new HashMap<Integer,Atom>();
	HashMap<Integer,atomGroup> chainlist = new HashMap<Integer,atomGroup>();
	//double Rg=0.0;
	ArrayList<Double> Rg;

	public radiusOfGyration(String pathData, String pathDumpAtom) throws IOException {
		readData rd = new readData(pathData);
		this.df= readData.readDataInfo(rd.br);
		this.atoms = df.atomlist;
		this.rdc = new readdumpAtom(pathDumpAtom);
		this.dos= this.rdc.readnextNstep(1);
		this.atomMap=df.atomMap;
		
	}
	
	public void analysis() throws IOException {
		this.Rg = new ArrayList<Double>();
		while(this.dos.timestep!=-1) {
			for(Atom a:dos.atomlist) {
				int molid = atomMap.get(a.id).moelculeid;
				if(chainlist.containsKey(molid)) {
					a.setMass(df.typeToMass.get(a.type));
					chainlist.get(molid).addAtom(a);
				}else {
					atomGroup newgroup = new atomGroup();
					a.setMass(df.typeToMass.get(a.type));

					newgroup.addAtom(a);
					chainlist.put(molid, newgroup );
				}	
			}
			double rg=0.0;
			for(Integer mid:chainlist.keySet()) {
				chainlist.get(mid).calcom();
				chainlist.get(mid).calRg();
				rg+=chainlist.get(mid).Rg;
			}
			rg/=chainlist.keySet().size();
			this.Rg.add(rg);

			dos = this.rdc.readnextNstep(1);	
		}
		//double Rg=0.0;

	}
	public static void main(String[] argv) throws IOException {
		String pathdata = "C:\\cygwin64\\home\\wechy\\Tg\\44bpadampd\\data.1";
		String pathdump = "C:\\cygwin64\\home\\wechy\\Tg\\44bpadampd\\dump.atom.400";
		radiusOfGyration rdg = new radiusOfGyration(pathdata,pathdump);
		rdg.analysis();
		for(Double rg:rdg.Rg) {
			System.out.println(rg);
			
		}
	}

	

}

package analysisData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
public class analysis {
	HashMap<Integer, Integer> atombondCount = new HashMap<Integer,Integer>();// <atomid,numberofbonds of this atom>
	HashMap<Integer, Integer> NbondsCount = new HashMap<Integer,Integer>();// <numberOfbonds of 1 atom,number of atoms containing this number of bonds>
	HashMap<Integer,ArrayList<Integer>> idToConnect = new HashMap<Integer,ArrayList<Integer>>(); //<atomid,atom connections id>
	readData rd;
	dataFileInfo dinf;
	ArrayList<Integer> chainSizes = new ArrayList<Integer>();
	public static String stepNum = "100w";
	public static void main(String [] argv) throws IOException {
		//String pathInput = argv[0];
	    //String pathOutput = System.getProperty("user.dir");
		//String pathInput = "C:\\Users\\wechyu88\\WenWorkSpace\\CGEPOXY\\secondLevel\\AfterCGreact\\";
		String pathInput = "C:\\Users\\wechyu88\\WenWorkSpace\\CGEPOXY\\firstLevel\\";
		analysis als = new analysis();
		//analysis als = new analysis(pathInput+"dataToRyan.txt");

		//analysis als = new analysis(pathInput+"boundary.txt");
		//analysis als = new analysis(pathInput+"detdaNetWork.txt");
		//als.checkData(pathInput+"detdaNetWork.txt");
		//als.runAnalyzeMolecules(pathInput+"bondAnalysis.txt");
		//String stepNum = "10W";
		als.runGetDetdaNetWork(pathInput+"data."+stepNum);
		
		//als.digSecLevelNetWork(pathInput+"detdaNetWork"+stepNum+".txt");
		
	}
	public analysis() {
		
	}
	public void runAnalyzeMolecules(String pathResult) throws IOException {
		//String pathInput = "C:\\Users\\wechyu88\\WenWorkSpace\\CGEPOXY\\firstLevel\\";
		//analysis als = new analysis(path);
		bondAnalysis();
		chainAnalysis();
		File outfile = new File(pathResult);
		FileWriter fw = new FileWriter(outfile);
		BufferedWriter bw = new BufferedWriter(fw);
		for(Integer nb: this.NbondsCount.keySet()) {
			bw.write(nb+" "+this.NbondsCount.get(nb)+"\n");
		}
		bw.write(" there are "+this.chainSizes.size()+" chains \n");
		for(int i=0;i<this.chainSizes.size();i++) {
			bw.write(" chain "+i+" has "+this.chainSizes.get(i)+" atoms \n");
			
		}
		bw.close();
		fw.close();
		
		
		
	}
	public void runGetDetdaNetWork(String path) throws IOException {
		
		GenDETDANetWork(path);
	}
	public void runDigNetWork(String path) throws IOException {
		//bondAnalysis();
		//chainAnalysis();
		digEpoxyNetWork(path);
		crossBondAnalysis(path);

	}
	
	
	public analysis(String pathInput) throws IOException {
		this.rd = new readData(pathInput);
		this.dinf= readData.readDataInfo(rd.br);
		rd.br.close();

		
	}
	public void checkData(String pathInput) throws IOException {
		
		String path = pathInput;
		readData rd = new readData(path);
		dataFileInfo dinf= readData.readDataInfo(rd.br);
		rd.br.close();
		ArrayList<Bond> bondList = dinf.bondlist;

		ArrayList<Atom> detdalist=new ArrayList<Atom>();
		ArrayList<Bond> bondlist = new ArrayList<Bond>();
		int bondcount = 0;
		ArrayList<Bond> bondlistnew = new ArrayList<Bond>();
		HashMap<ArrayList<Integer>,Integer> bondtable = new HashMap<ArrayList<Integer>,Integer>();
		for(Bond bond:bondList) {
			int aid = bond.a.id;
			int bid = bond.b.id;
			ArrayList<Integer> bondid1 = new  ArrayList<Integer> ();
			ArrayList<Integer> bondid2 = new  ArrayList<Integer> ();
			bondid1.add(aid);
			bondid1.add(bid);
			bondid2.add(bid);
			bondid2.add(aid);
			if(bondtable.containsKey(bondid1)||bondtable.containsKey(bondid2)) {
				
			}else {
				bondtable.put(bondid1,1);
				bondcount++;
				bondlistnew.add(bond);
				
			}


		}
		System.out.println(bondcount+" bonds");
		System.out.println(" all DETDA boundary is "+detdalist.size()+" bonds size is "+bondlist.size());
		dataFileInfo df = new dataFileInfo();	
		//resetDetdaBoundaryType(detdalist);
		df.setAtomlist(dinf.atomlist);
		df.setBondList(bondlistnew);
		df.setboundary(dinf.xlo,dinf.xhi, dinf.ylo, dinf.yhi, dinf.zlo, dinf.zhi);
		buildSystem builds = new buildSystem(df);
		builds.setNumTypes(5);

		String path2= "C:\\Users\\wechyu88\\WenWorkSpace\\CGEPOXY\\firstLevel\\";
		PrintWriter pw = new PrintWriter(new File(path2+"detdaNetWorkNew10w.txt"));
		bs bs1 = new bs();

		bs1.writedata(pw, builds);

	}
	
	public void bondAnalysis() throws IOException {
		for(Bond b:dinf.bondlist) {
			if(atombondCount.containsKey(b.a.id)) {
				atombondCount.put(b.a.id,atombondCount.get(b.a.id)+1);
			}else {
				atombondCount.put(b.a.id,1);
			}
			if(atombondCount.containsKey(b.b.id)) {
				atombondCount.put(b.b.id,atombondCount.get(b.b.id)+1);
			}else {
				atombondCount.put(b.b.id,1);
			}
			if(idToConnect.containsKey(b.a.id)) {
				ArrayList<Integer> tempc = new ArrayList<Integer>();
				tempc = idToConnect.get(b.a.id);
				tempc.add(b.b.id);
				idToConnect.put(b.a.id,tempc);
			}else {
				ArrayList<Integer> tempc = new ArrayList<Integer>();
				tempc.add(b.b.id);
				idToConnect.put(b.a.id,tempc);				
				
			}
			if(idToConnect.containsKey(b.b.id)) {
				ArrayList<Integer> tempc = new ArrayList<Integer>();
				tempc = idToConnect.get(b.b.id);
				tempc.add(b.a.id);
				idToConnect.put(b.b.id,tempc);
			}else {
				
				ArrayList<Integer> tempc = new ArrayList<Integer>();
				tempc.add(b.a.id);
				idToConnect.put(b.b.id,tempc);
			}
			
		}
		
		
		for(int count: atombondCount.values()) {
			if(NbondsCount.containsKey(count)) {
				NbondsCount.put(count,NbondsCount.get(count)+1);
			}else {
				NbondsCount.put(count,1);
			}
		}		
	}
	
	public void chainAnalysis() {
		ArrayList<Integer> checkIds = new ArrayList<Integer>();
		int chainnums=0;
		for(Integer id:idToConnect.keySet()) {
			if(checkIds.contains(id)) {
				continue;
			}else {
				int chainsize = findMolecule(id,checkIds);
				chainSizes.add(chainsize);
				chainnums++;
			}
		}		
	}
	public int findMolecule(Integer atomid,ArrayList<Integer> checkIds) {// given an atom, using breadth first search to find the whole molecule
		int tempcount = checkIds.size();
		checkIds.add(atomid);
		Queue<Integer> moleculeids = new LinkedList<Integer>();
		moleculeids.add(atomid);
		while(!moleculeids.isEmpty()) {
			Integer tempid = moleculeids.remove();
			for(Integer c:idToConnect.get(tempid)) {
				if(checkIds.contains(c)) {
					continue;
				}else {
					checkIds.add(c);
					moleculeids.add(c);
				}
			}
		}
		
		return checkIds.size()-tempcount;
	}
	
	public static void crossBondAnalysis(String pathInput) throws IOException {
		String path = pathInput;
		readData rd = new readData(path);
		dataFileInfo dinf= readData.readDataInfo(rd.br);
		rd.br.close();
		ArrayList<Bond> bondList = dinf.bondlist;
		int crossx=0;
		int crossy=0;
		int crossz=0;
		int cross=0;
		ArrayList<Atom> detdaboudary = new ArrayList<Atom>();
		for(Bond bond:bondList) {
			//if(bond.type==7) {
				boolean [] crossdata = CrossBondOrNot(bond,dinf.xhi,dinf.xlo,dinf.yhi,dinf.ylo,dinf.zhi,dinf.zlo,detdaboudary);
				if(crossdata[0]||crossdata[1]||crossdata[2]) cross++;
				if(crossdata[0]) crossx++;
				if(crossdata[1]) crossy++;
				if(crossdata[2]) crossz++;
			//}
		}
		System.out.println(" total cross is "+ cross+" cross x "+crossx+" cross y "+crossy+" cross z "+crossz+" total bond "+dinf.bondlist.size());
		
		
	}
	
	public void digEpoxyNetWork(String pathInput) throws IOException {
		String path = pathInput;
		readData rd = new readData(path);
		dataFileInfo dinf= readData.readDataInfo(rd.br);
		rd.br.close();
		ArrayList<Bond> bondList = dinf.bondlist;
		int crossx=0;
		int crossy=0;
		int crossz=0;
		ArrayList<Atom> detdaboundary=new ArrayList<Atom>();
		ArrayList<Bond> bondlist = new ArrayList<Bond>();
		int bondcount=0;
		for(Bond bond:bondList) {
			ArrayList<Atom> detdas= findBoundaryDETDA(dinf,bond);
			if(detdas.size()==0) {
				
				
			}else {
				Atom a1 = detdas.get(0);
				Atom a2 = detdas.get(1);
				boolean bothexist = true;
				if(detdaboundary.contains(a1)) {
					
				}else {
					bothexist=false;
					detdaboundary.add(a1);
				}
				if(detdaboundary.contains(a2)) {
					
				}else {
					bothexist=false;
					detdaboundary.add(a2);
				}
				Bond b = new Bond(a1, a2);
				b.settype(1);
				Bond b2 = new Bond(a2,a1);
				b2.settype(1);
				if(bondlist.contains(b)||bondlist.contains(b2)) {
					
				}else {
					
					
					bondcount++;
					b.setid(bondcount);
					bondlist.add(b);	
				}
				
			}

		}
		System.out.println(" all DETDA boundary is "+detdaboundary.size()+" bonds size is "+bondlist.size());
		dataFileInfo df = new dataFileInfo();	
		
		
		
		df.setAtomlist(detdaboundary);
		df.setBondList(bondlist);
		df.setboundary(dinf.xlo,dinf.xhi, dinf.ylo, dinf.yhi, dinf.zlo, dinf.zhi);
		buildSystem builds = new buildSystem(df);
		String path2= "C:\\Users\\wechyu88\\WenWorkSpace\\CGEPOXY\\firstLevel\\";
		PrintWriter pw = new PrintWriter(new File(path2+"boundary2.txt"));
		bs bs1 = new bs();

		bs1.writedata(pw, builds);
	}
	public void digSecLevelNetWork(String pathInput) throws IOException {
		String path = pathInput;
		readData rd = new readData(path);
		dataFileInfo dinf= readData.readDataInfo(rd.br);
		rd.br.close();
		ArrayList<Bond> bondList = dinf.bondlist;
		int crossx=0;
		int crossy=0;
		int crossz=0;
		ArrayList<Atom> detdaboundary=new ArrayList<Atom>();
		ArrayList<Bond> bondlist = new ArrayList<Bond>();
		int bondcount=0;
		for(Bond bond:bondList) {

				Atom a1 = bond.a;
				Atom a2 = bond.b;
				boolean CrosOrNot=false;
				if(checkCrossbox(a1.x,a2.x,dinf.xhi,dinf.xlo)) {
					CrosOrNot=true;
				}
				if(checkCrossbox(a1.y,a2.y,dinf.yhi,dinf.ylo)) {
					CrosOrNot=true;

				}		
				if(checkCrossbox(a1.z,a2.z,dinf.zhi,dinf.zlo)) {
					CrosOrNot=true;

				}
				if(CrosOrNot) {
					boolean bothexist = true;
					if(detdaboundary.contains(a1)) {
						
					}else {
						bothexist=false;
						detdaboundary.add(a1);
					}
					if(detdaboundary.contains(a2)) {
						
					}else {
						bothexist=false;
						detdaboundary.add(a2);
					}
					Bond b = new Bond(a1, a2);
					b.settype(1);
					Bond b2 = new Bond(a2,a1);
					b2.settype(1);
					if(bondlist.contains(b)||bondlist.contains(b2)) {
						
					}else {						
						bondcount++;
						b.setid(bondcount);
						bondlist.add(b);	
					}					
				}else {
					boolean bothexist = true;
					if(detdaboundary.contains(a1)) {
						
					}else {
						bothexist=false;
						detdaboundary.add(a1);
					}
					if(detdaboundary.contains(a2)) {
						
					}else {
						bothexist=false;
						detdaboundary.add(a2);
					}
					continue;
				}
				
			

		}
		System.out.println(" all DETDA boundary is "+detdaboundary.size()+" bonds size is "+bondlist.size());
		dataFileInfo df = new dataFileInfo();	
		
		
		
		df.setAtomlist(detdaboundary);
		df.setBondList(bondlist);
		df.setboundary(dinf.xlo,dinf.xhi, dinf.ylo, dinf.yhi, dinf.zlo, dinf.zhi);
		buildSystem builds = new buildSystem(df);
		String path2= "C:\\Users\\wechyu88\\WenWorkSpace\\CGEPOXY\\firstLevel\\";
		PrintWriter pw = new PrintWriter(new File(path2+"boundary"+stepNum+".txt"));
		bs bs1 = new bs();

		bs1.writedata(pw, builds);
	}
	public static boolean checkCrossbox(double x1,double x2,double xhi,double xlo) {
		if((x1<xhi&&x1>xlo)&&(x2>xhi||x2<xlo)) return true;
		if((x2<xhi&&x2>xlo)&&(x1>xhi||x1<xlo)) return true;
		return false;
	}

	public void resetDetdaBoundaryType(ArrayList<Atom> atomlist) {
		for(Atom a:atomlist) {
			if(a.crossx&&!a.crossy&&!a.crossz) {
				a.setType(1);
			}else if(!a.crossx&&a.crossy&&!a.crossz) {
				a.setType(2);
			}else if(!a.crossx&&!a.crossy&&a.crossz) {
				a.setType(3);
			}else if(!a.crossx&&!a.crossy&&!a.crossz) {
				a.setType(5);
			}else {
				a.setType(4);
			}
			
		}
	}

	public void GenDETDANetWork(String pathInput) throws IOException {
		String path = pathInput;
		readData rd = new readData(path);
		dataFileInfo dinf= readData.readDataInfo(rd.br);
		rd.br.close();
		ArrayList<Bond> bondList = dinf.bondlist;
		int crossx=0;
		int crossy=0;
		int crossz=0;
		int largestid = dinf.atomlist.size();
		ArrayList<Atom> detdalist=new ArrayList<Atom>();
		ArrayList<Bond> bondlist = new ArrayList<Bond>();
		int bondcount=0;
		HashMap<Integer,ArrayList<Integer>> bondpair = new HashMap<Integer,ArrayList<Integer>>(); 
		for(Bond bond:bondList) {
			ArrayList<Atom> detdas= findDETDA(dinf,bond);
			if(detdas.size()==0) {
				
				
			}else {
				Atom a1 = detdas.get(0);
				Atom a2 = detdas.get(1);

				boolean bothexist = true;
				if(detdalist.contains(a1)) {
					
				}else {
					bothexist=false;
					detdalist.add(a1);
				}
				if(detdalist.contains(a2)) {
					
				}else {
					bothexist=false;
					detdalist.add(a2);
				}
				if(a1.equals(a2)) {
					continue;
				}
				Bond b = new Bond(a1, a2);
				Bond b2 = new Bond(a2,a1);
				b.settype(1);
				b2.settype(1);
				boolean [] result = {false,false, false};

				
				if(Math.abs(a1.x-a2.x)>Math.abs(dinf.xhi-dinf.xlo)*0.5) {
					a1.setCrossx(true);
					a2.setCrossx(true);
					result[0]=true;
				}
				if(Math.abs(a1.y-a2.y)>Math.abs(dinf.yhi-dinf.ylo)*0.5) {
					a1.setCrossy(true);
					a2.setCrossy(true);
					result[1]=true;
				}		
				if(Math.abs(a1.z-a2.z)>Math.abs(dinf.zhi-dinf.zlo)*0.5) {
					a1.setCrossz(true);
					a2.setCrossz(true);
					result[2]=true;
				}

				//if(a1.id==45868) {
					if(bondpair.containsKey(a1.id)) {
						if(!bondpair.get(a1.id).contains(a2.id)) {
							bondpair.get(a1.id).add(a2.id);	
						}else {
							continue;
						}
						
						
					}else {
						ArrayList<Integer> ids = new ArrayList<Integer>();
						ids.add(a2.id);
						bondpair.put(a1.id,ids);
					}
				//	System.out.println("a1 "+a1.id+" x y z is "+a1.x+" "+a1.y+" "+a1.z);
				//	System.out.println("a2 "+a2.id+" x y z is "+a2.x+" "+a2.y+" "+a2.z);

			//	}
				
				//if(a2.id==45868) {
					if(bondpair.containsKey(a2.id)) {
						if(!bondpair.get(a2.id).contains(a1.id)) {
							bondpair.get(a2.id).add(a1.id);	
						}else {
							continue;
						}						
					}else {
						ArrayList<Integer> ids = new ArrayList<Integer>();
						ids.add(a1.id);
						bondpair.put(a2.id,ids);
					}
				//	System.out.println("a1 "+a1.id+" x y z is "+a1.x+" "+a1.y+" "+a1.z);
				//	System.out.println("a2 "+a2.id+" x y z is "+a2.x+" "+a2.y+" "+a2.z);

			//	}

				if(result[0]&&!result[1]&&!result[2]) {
					
					Atom a1copy = new Atom(++largestid);
					Atom a2copy = new Atom(++largestid);
					//if(a1.OutSideBoxAtom==null) {
						a1copy.setType(a1.type);
						a1copy.setCrossx(true);
						int a1copyaddbox = a1.x-a2.x>0?-1:1;
						a1copy.setXYZ(a1.x+a1copyaddbox*(dinf.xhi-dinf.xlo), a1.y, a1.z);
						//a1.setOutSideBoxAtom(a1copy);
					if(detdalist.contains(a1copy)) {
						a1copy = detdalist.get(detdalist.indexOf(a1copy));
					}else {
						detdalist.add(a1copy);
						
					}

					//}
					//else {
					//	a1copy = a1.OutSideBoxAtom;
					//}
					
					//if(a2.OutSideBoxAtom==null) {
						
					a2copy.setType(a2.type);
					a2copy.setCrossx(true);

					int a2copyaddbox = a2.x-a1.x>0?-1:1;
					a2copy.setXYZ(a2.x+a2copyaddbox*(dinf.xhi-dinf.xlo), a2.y, a2.z);
					a2.setOutSideBoxAtom(a2copy);
					//detdalist.add(a2copy);

					//}else {
						//a2copy = a2.OutSideBoxAtom;
					//}
					if(detdalist.contains(a2copy)) {
						a2copy = detdalist.get(detdalist.indexOf(a2copy));

					}else {
						detdalist.add(a2copy);
						
					}

					Bond tempb1a = new Bond(a1copy, a2);
					Bond tempb1b = new Bond(a2,a1copy);

					Bond tempb2a = new Bond(a2copy, a1);
					Bond tempb2b = new Bond(a1,a2copy);
					
					if(bondlist.contains(tempb1a)||bondlist.contains(tempb1b)) {
						
					}else {
						
						bondcount++;
						b.setid(bondcount);
						tempb1a.settype(2);
						bondlist.add(tempb1a);	
						

					}
					if(bondlist.contains(tempb2a)||bondlist.contains(tempb2b)) {
						
					}else {
						
						bondcount++;
						b.setid(bondcount);
						tempb2a.settype(2);

						bondlist.add(tempb2a);	

					}
					

				}
				if(!result[0]&&result[1]&&!result[2]) {
					Atom a1copy = new Atom(++largestid);
					Atom a2copy = new Atom(++largestid);
					
					
					//if(a1.OutSideBoxAtom==null) {
						a1copy.setType(a1.type);
						a1copy.setCrossy(true);

						int a1copyaddbox = a1.y-a2.y>0?-1:1;
						a1copy.setXYZ(a1.x,a1.y+a1copyaddbox*(dinf.yhi-dinf.ylo), a1.z);
						a1.setOutSideBoxAtom(a1copy);
//						detdalist.add(a1copy);

					//}else {
						//a1copy = a1.OutSideBoxAtom;
					//}
						if(detdalist.contains(a1copy)) {
							a1copy = detdalist.get(detdalist.indexOf(a1copy));

						}else {
							detdalist.add(a1copy);
							
						}
					
				//	if(a2.OutSideBoxAtom==null) {
						a2copy.setType(a2.type);
						a2copy.setCrossy(true);

						int a2copyaddbox = a2.y-a1.y>0?-1:1;
						a2copy.setXYZ(a2.x,a2.y+a2copyaddbox*(dinf.yhi-dinf.ylo), a2.z);
						a2.setOutSideBoxAtom(a1copy);
						//detdalist.add(a2copy);

					//}else {
						//a2copy = a2.OutSideBoxAtom;
					//}
						if(detdalist.contains(a2copy)) {
							a2copy = detdalist.get(detdalist.indexOf(a2copy));

						}else {
							detdalist.add(a2copy);
							
						}


					Bond tempb1a = new Bond(a1copy, a2);
					Bond tempb1b = new Bond(a2,a1copy);

					Bond tempb2a = new Bond(a2copy, a1);
					Bond tempb2b = new Bond(a1,a2copy);
					
					if(bondlist.contains(tempb1a)||bondlist.contains(tempb1b)) {
						
					}else {
						
						bondcount++;
						b.setid(bondcount);
						tempb1a.settype(3);

						bondlist.add(tempb1a);	

					}
					if(bondlist.contains(tempb2a)||bondlist.contains(tempb2b)) {
						
					}else {
						
						bondcount++;
						b.setid(bondcount);
						tempb2a.settype(3);

						bondlist.add(tempb2a);	

					}
					
				}
				if(!result[0]&&!result[1]&&result[2]) {
					Atom a1copy = new Atom(++largestid);
					Atom a2copy = new Atom(++largestid);
				//	if(a1.OutSideBoxAtom==null) {
						a1copy.setCrossz(true);

						a1copy.setType(a1.type);
						int a1copyaddbox = a1.z-a2.z>0?-1:1;
						a1copy.setXYZ(a1.x,a1.y,a1.z+a1copyaddbox*(dinf.zhi-dinf.zlo));
						a1.setOutSideBoxAtom(a1copy);
					//	detdalist.add(a1copy);

				//	}else {
					//	a1copy = a1.OutSideBoxAtom;
					//}
						if(detdalist.contains(a1copy)) {
							a1copy = detdalist.get(detdalist.indexOf(a1copy));

						}else {
							detdalist.add(a1copy);
							
						}
					
					//if(a2.OutSideBoxAtom==null) {
						a2copy.setType(a2.type);
						a2copy.setCrossz(true);

						int a2copyaddbox = a2.z-a1.z>0?-1:1;
						a2copy.setXYZ(a2.x,a2.y,a2.z+a2copyaddbox*(dinf.zhi-dinf.zlo));
						a2.setOutSideBoxAtom(a1copy);
						//detdalist.add(a2copy);

					//}else {
						//a2copy = a2.OutSideBoxAtom;
					//}
						if(detdalist.contains(a2copy)) {
							a2copy = detdalist.get(detdalist.indexOf(a2copy));

						}else {
							detdalist.add(a2copy);
							
						}
					


					Bond tempb1a = new Bond(a1copy, a2);
					Bond tempb1b = new Bond(a2,a1copy);

					Bond tempb2a = new Bond(a2copy, a1);
					Bond tempb2b = new Bond(a1,a2copy);
					
					if(bondlist.contains(tempb1a)||bondlist.contains(tempb1b)) {
						
					}else {
						
						bondcount++;
						b.setid(bondcount);
						tempb1a.settype(4);

						bondlist.add(tempb1a);	

					}
					if(bondlist.contains(tempb2a)||bondlist.contains(tempb2b)) {
						
					}else {
						
						bondcount++;
						b.setid(bondcount);
						tempb2a.settype(4);

						bondlist.add(tempb2a);	

					}
					
				}
				if(result[0]&&result[1]&&!result[2]) {
					Atom a1copy = new Atom(++largestid);
					Atom a2copy = new Atom(++largestid);
					
					//if(a1.OutSideBoxAtom==null) {
						a1copy.setType(a1.type);

						a1copy.setCrossx(true);
						a1copy.setCrossy(true);

						int a1copyaddboxX = a1.x-a2.x>0?-1:1;
						int a1copyaddboxY = a1.y-a2.y>0?-1:1;

						a1copy.setXYZ(a1.x+a1copyaddboxX*(dinf.xhi-dinf.xlo),a1.y+a1copyaddboxY*(dinf.yhi-dinf.ylo),a1.z);
						a1.setOutSideBoxAtom(a1copy);
					//	detdalist.add(a1copy);

					//}else {
						//a1copy = a1.OutSideBoxAtom;
					//}
						if(detdalist.contains(a1copy)) {
							a1copy = detdalist.get(detdalist.indexOf(a1copy));

						}else {
							detdalist.add(a1copy);
							
						}
				//	if(a2.OutSideBoxAtom==null) {
						a2copy.setType(a2.type);

						a2copy.setCrossx(true);
						a2copy.setCrossy(true);
						int a2copyaddboxX = a2.x-a1.x>0?-1:1;
						int a2copyaddboxY = a2.y-a1.y>0?-1:1;
						a2copy.setXYZ(a2.x+a2copyaddboxX*(dinf.xhi-dinf.xlo),a2.y+a2copyaddboxY*(dinf.yhi-dinf.ylo),a2.z);

						a2.setOutSideBoxAtom(a1copy);
					//	detdalist.add(a2copy);

				//	}else {
					//	a2copy = a2.OutSideBoxAtom;
					//}
						if(detdalist.contains(a2copy)) {
							a2copy = detdalist.get(detdalist.indexOf(a2copy));

						}else {
							detdalist.add(a2copy);
							
						}


					Bond tempb1a = new Bond(a1copy, a2);
					Bond tempb1b = new Bond(a2,a1copy);

					Bond tempb2a = new Bond(a2copy, a1);
					Bond tempb2b = new Bond(a1,a2copy);
					
					if(bondlist.contains(tempb1a)||bondlist.contains(tempb1b)) {
						
					}else {
						
						bondcount++;
						b.setid(bondcount);
						tempb1a.settype(5);

						bondlist.add(tempb1a);	

					}
					if(bondlist.contains(tempb2a)||bondlist.contains(tempb2b)) {
						
					}else {
						
						bondcount++;
						b.setid(bondcount);
						tempb2a.settype(5);

						bondlist.add(tempb2a);	

					}
					
				}				
				
				if(result[0]&&!result[1]&&result[2]) {
					Atom a1copy = new Atom(++largestid);
					Atom a2copy = new Atom(++largestid);
				//	if(a1.OutSideBoxAtom==null) {
						a1copy.setType(a1.type);

						a1copy.setCrossx(true);
						a1copy.setCrossz(true);
						int a1copyaddboxX = a1.x-a2.x>0?-1:1;
						int a1copyaddboxZ = a1.z-a2.z>0?-1:1;

						a1copy.setXYZ(a1.x+a1copyaddboxX*(dinf.xhi-dinf.xlo),a1.y,a1.z+a1copyaddboxZ*(dinf.zhi-dinf.zlo));
						a1.setOutSideBoxAtom(a1copy);
					//	detdalist.add(a1copy);

					//}else {
						//a1copy = a1.OutSideBoxAtom;
					//}
						if(detdalist.contains(a1copy)) {
							a1copy = detdalist.get(detdalist.indexOf(a1copy));

						}else {
							detdalist.add(a1copy);
							
						}
					//if(a2.OutSideBoxAtom==null) {
						a2copy.setType(a2.type);
						a2copy.setCrossx(true);
						a2copy.setCrossz(true);
						int a2copyaddboxX = a2.x-a1.x>0?-1:1;
						int a2copyaddboxZ = a2.z-a1.z>0?-1:1;
						a2copy.setXYZ(a2.x+a2copyaddboxX*(dinf.xhi-dinf.xlo),a2.y,a2.z+a2copyaddboxZ*(dinf.zhi-dinf.zlo));
						a2.setOutSideBoxAtom(a1copy);
					//	detdalist.add(a2copy);

				//	}else {
					//	a2copy = a2.OutSideBoxAtom;
					//}
		
						if(detdalist.contains(a2copy)) {
							a2copy = detdalist.get(detdalist.indexOf(a2copy));

						}else {
							detdalist.add(a2copy);
							
						}
					Bond tempb1a = new Bond(a1copy, a2);
					Bond tempb1b = new Bond(a2,a1copy);

					Bond tempb2a = new Bond(a2copy, a1);
					Bond tempb2b = new Bond(a1,a2copy);
					
					if(bondlist.contains(tempb1a)||bondlist.contains(tempb1b)) {
						
					}else {
						
						bondcount++;
						b.setid(bondcount);
						tempb1a.settype(6);

						bondlist.add(tempb1a);	

					}
					if(bondlist.contains(tempb2a)||bondlist.contains(tempb2b)) {
						
					}else {
						
						bondcount++;
						b.setid(bondcount);
						tempb2a.settype(6);

						bondlist.add(tempb2a);	

					}
					
				}		
				if(!result[0]&&result[1]&&result[2]) {
					Atom a1copy = new Atom(++largestid);
					Atom a2copy = new Atom(++largestid);
					//if(a1.OutSideBoxAtom==null) {
						a1copy.setType(a1.type);
						a1copy.setCrossy(true);
						a1copy.setCrossz(true);
						int a1copyaddboxY = a1.y-a2.y>0?-1:1;
						int a1copyaddboxZ = a1.z-a2.z>0?-1:1;

						a1copy.setXYZ(a1.x,a1.y+a1copyaddboxY*(dinf.yhi-dinf.ylo),a1.z+a1copyaddboxZ*(dinf.zhi-dinf.zlo));
						a1.setOutSideBoxAtom(a1copy);
					//	detdalist.add(a1copy);

					//}else {
						//a1copy = a1.OutSideBoxAtom;
					//}
						if(detdalist.contains(a1copy)) {
							a1copy = detdalist.get(detdalist.indexOf(a1copy));

						}else {
							detdalist.add(a1copy);
							
						}
					//if(a2.OutSideBoxAtom==null) {
						a2copy.setType(a2.type);
						a2copy.setCrossy(true);
						a2copy.setCrossz(true);
						int a2copyaddboxY = a2.y-a1.y>0?-1:1;
						int a2copyaddboxZ = a2.z-a1.z>0?-1:1;
						a2copy.setXYZ(a2.x,a2.y+a2copyaddboxY*(dinf.yhi-dinf.ylo),a2.z+a2copyaddboxZ*(dinf.zhi-dinf.zlo));
						a2.setOutSideBoxAtom(a1copy);
						//detdalist.add(a2copy);

					//}else {
						//a2copy = a2.OutSideBoxAtom;
					//}
						if(detdalist.contains(a2copy)) {
							a2copy = detdalist.get(detdalist.indexOf(a2copy));

						}else {
							detdalist.add(a2copy);
							
						}


					Bond tempb1a = new Bond(a1copy, a2);
					Bond tempb1b = new Bond(a2,a1copy);

					Bond tempb2a = new Bond(a2copy, a1);
					Bond tempb2b = new Bond(a1,a2copy);
					
					if(bondlist.contains(tempb1a)||bondlist.contains(tempb1b)) {
						
					}else {
						
						bondcount++;
						b.setid(bondcount);
						tempb1a.settype(7);

						bondlist.add(tempb1a);	

					}
					if(bondlist.contains(tempb2a)||bondlist.contains(tempb2b)) {
						
					}else {
						
						bondcount++;
						b.setid(bondcount);
						tempb2a.settype(7);

						bondlist.add(tempb2a);	

					}
					
				}
				if(result[0]&&result[1]&&result[2]) {
					Atom a1copy = new Atom(++largestid);
					Atom a2copy = new Atom(++largestid);
					
				//	if(a1.OutSideBoxAtom==null) {
						a1copy.setType(a1.type);
						a1copy.setCrossx(true);
						a1copy.setCrossy(true);
						a1copy.setCrossz(true);

						int a1copyaddboxX = a1.x-a2.x>0?-1:1;
						int a1copyaddboxY = a1.y-a2.y>0?-1:1;
						int a1copyaddboxZ = a1.z-a2.z>0?-1:1;


						a1copy.setXYZ(a1.x+a1copyaddboxX*(dinf.xhi-dinf.xlo),a1.y+a1copyaddboxY*(dinf.yhi-dinf.ylo),a1.z+a1copyaddboxZ*(dinf.zhi-dinf.zlo));
						a1.setOutSideBoxAtom(a1copy);
						//detdalist.add(a1copy);

				//	}else {
					//	a1copy = a1.OutSideBoxAtom;
					//}
						if(detdalist.contains(a1copy)) {
							a1copy = detdalist.get(detdalist.indexOf(a1copy));

						}else {
							detdalist.add(a1copy);
							
						}
					//if(a2.OutSideBoxAtom==null) {
						a2copy.setType(a2.type);
						a2copy.setCrossx(true);
						a2copy.setCrossy(true);
						a2copy.setCrossz(true);
						int a2copyaddboxX = a2.x-a1.x>0?-1:1;
						int a2copyaddboxY = a2.y-a1.y>0?-1:1;
						int a2copyaddboxZ = a2.z-a1.z>0?-1:1;
						a2copy.setXYZ(a2.x+a2copyaddboxX*(dinf.xhi-dinf.xlo),a2.y+a2copyaddboxY*(dinf.yhi-dinf.ylo),a2.z+a2copyaddboxZ*(dinf.zhi-dinf.zlo));
						a2.setOutSideBoxAtom(a1copy);
						//detdalist.add(a2copy);

					//}else {
						//a2copy = a2.OutSideBoxAtom;
					//}
						if(detdalist.contains(a2copy)) {
							a2copy = detdalist.get(detdalist.indexOf(a2copy));

						}else {
							detdalist.add(a2copy);
							
						}


					Bond tempb1a = new Bond(a1copy, a2);
					Bond tempb1b = new Bond(a2,a1copy);

					Bond tempb2a = new Bond(a2copy, a1);
					Bond tempb2b = new Bond(a1,a2copy);
					
					if(bondlist.contains(tempb1a)||bondlist.contains(tempb1b)) {
						
					}else {
						
						bondcount++;
						b.setid(bondcount);
						tempb1a.settype(8);

						bondlist.add(tempb1a);	

					}
					if(bondlist.contains(tempb2a)||bondlist.contains(tempb2b)) {
						
					}else {
						
						bondcount++;
						b.setid(bondcount);
						tempb2a.settype(8);

						bondlist.add(tempb2a);	

					}
					
				}
				if(result[0]||result[1]||result[2]) {
					continue;
				}
				if(bondlist.contains(b)||bondlist.contains(b2)) {
					
				}else {
					
					bondcount++;
					System.out.println(bondcount);
					b.setid(bondcount);
					bondlist.add(b);	
				}
				
			}

		}
		HashMap<Integer,Integer> counter= new HashMap<Integer,Integer>();
		for(int keys:bondpair.keySet()) {
			//if(counter.containsKey(bondpair.get(keys).size())) {
				int newcount = counter.getOrDefault(bondpair.get(keys).size(),0)+1;
				counter.put(bondpair.get(keys).size(),newcount);
			//}
		}


		System.out.println(" all DETDA boundary is "+detdalist.size()+" bonds size is "+bondlist.size());
		dataFileInfo df = new dataFileInfo();	
		resetDetdaBoundaryType(detdalist);
		df.setAtomlist(detdalist);
		df.setBondList(bondlist);
		df.setboundary(dinf.xlo,dinf.xhi, dinf.ylo, dinf.yhi, dinf.zlo, dinf.zhi);
		buildSystem builds = new buildSystem(df);
		builds.setNumTypes(5);

		String path2= "C:\\Users\\wechyu88\\WenWorkSpace\\CGEPOXY\\firstLevel\\";
		analysis als = new analysis();
		als.analysisBondDensity(bondlist,builds);

		PrintWriter pw = new PrintWriter(new File(path2+"detdaNetWork"+stepNum+".txt"));

		bs bs1 = new bs();

		bs1.writedata(pw, builds);
	}

	public void analysisBondDensity(ArrayList<Bond> bondlist,buildSystem builds) throws FileNotFoundException {
		int counter=0;
		int numbins = 10;
		double binsize = (builds.maxx-builds.minx)/numbins;
		int [][] tablex = new int[numbins][numbins];//[y][z]
		int [][] tabley = new int[numbins][numbins];//xz
		int [][] tablez = new int[numbins][numbins];//xy
		int [][] tablexyz = new int[numbins][numbins];//xy

		for(Bond b:bondlist) {
			if(b.type>1) {
				counter++;
			}else {
				continue;
			}
			if(b.type==2) {
				if(b.a.id>50000&&b.b.id>50000) {
					System.out.println("something wrong bonddensity");
				}else if(b.a.id>50000) {
					int posx=((int) Math.floor((b.b.y-builds.miny)/binsize))%numbins;
					int posy=((int) Math.floor((b.b.z-builds.minz)/binsize))%numbins;
					tablex[posx][posy]++;
					tablexyz[posx][posy]++;
				}else if(b.b.id>50000) {
					int posx=((int) Math.floor((b.a.y-builds.miny)/binsize))%numbins;
					int posy=((int) Math.floor((b.a.z-builds.minz)/binsize))%numbins;
					tablex[posx][posy]++;					
					tablexyz[posx][posy]++;

				}
				
			}else if(b.type==3) {
				if(b.a.id>50000&&b.b.id>50000) {
					System.out.println("something wrong bonddensity");
				}else if(b.a.id>50000) {
					int posx=((int) Math.floor((b.b.x-builds.minx)/binsize))%numbins;
					int posy=((int) Math.floor((b.b.z-builds.minz)/binsize))%numbins;
					tabley[posx][posy]++;
					tablexyz[posx][posy]++;

				}else if(b.b.id>50000) {
					int posx=((int) Math.floor((b.a.x-builds.minx)/binsize))%numbins;
					int posy=((int) Math.floor((b.a.z-builds.minz)/binsize))%numbins;
					tabley[posx][posy]++;				
					tablexyz[posx][posy]++;

				}		
			}else if(b.type==4) {
				if(b.a.id>50000&&b.b.id>50000) {
					System.out.println("something wrong bonddensity");
				}else if(b.a.id>50000) {
					int posx=((int) Math.floor((b.b.x-builds.minx)/binsize))%numbins;
					int posy=((int) Math.floor((b.b.y-builds.miny)/binsize))%numbins;
					tablez[posx][posy]++;
					tablexyz[posx][posy]++;

				}else if(b.b.id>50000) {
					int posx=((int) Math.floor((b.a.x-builds.minx)/binsize))%numbins;
					int posy=((int) Math.floor((b.a.y-builds.miny)/binsize))%numbins;
					tablez[posx][posy]++;
					tablexyz[posx][posy]++;

				}	
			}else if(b.type==5) {
				if(b.a.id>50000&&b.b.id>50000) {
					System.out.println("something wrong bonddensity");
				}else if(b.a.id>50000) {
					int posx=((int) Math.floor((b.b.y-builds.miny)/binsize))%numbins;
					int posy=((int) Math.floor((b.b.z-builds.minz)/binsize))%numbins;
					tablex[posx][posy]++;
					tablexyz[posx][posy]++;

				}else if(b.b.id>50000) {
					int posx=((int) Math.floor((b.a.y-builds.miny)/binsize))%numbins;
					int posy=((int) Math.floor((b.a.z-builds.minz)/binsize))%numbins;
					tablex[posx][posy]++;		
					tablexyz[posx][posy]++;

				}
				if(b.a.id>50000&&b.b.id>50000) {
					System.out.println("something wrong bonddensity");
				}else if(b.a.id>50000) {
					int posx=((int) Math.floor((b.b.x-builds.minx)/binsize))%numbins;
					int posy=((int) Math.floor((b.b.z-builds.minz)/binsize))%numbins;
					tabley[posx][posy]++;
					tablexyz[posx][posy]++;

				}else if(b.b.id>50000) {
					int posx=((int) Math.floor((b.a.x-builds.minx)/binsize))%numbins;
					int posy=((int) Math.floor((b.a.z-builds.minz)/binsize))%numbins;
					tabley[posx][posy]++;		
					tablexyz[posx][posy]++;

				}		
				
			}else if(b.type==6) {
				
				if(b.a.id>50000&&b.b.id>50000) {
					System.out.println("something wrong bonddensity");
				}else if(b.a.id>50000) {
					int posx=(int) Math.floor((b.b.y-builds.miny)/binsize);
					int posy=(int) Math.floor((b.b.z-builds.minz)/binsize);
					tablex[posx][posy]++;
					tablexyz[posx][posy]++;

				}else if(b.b.id>50000) {
					int posx=(int) Math.floor((b.a.y-builds.miny)/binsize);
					int posy=(int) Math.floor((b.a.z-builds.minz)/binsize);
					tablex[posx][posy]++;		
					tablexyz[posx][posy]++;

				}
				
				if(b.a.id>50000&&b.b.id>50000) {
					System.out.println("something wrong bonddensity");
				}else if(b.a.id>50000) {
					int posx=((int) Math.floor((b.b.x-builds.minx)/binsize))%numbins;
					int posy=((int) Math.floor((b.b.y-builds.miny)/binsize))%numbins;
					tablez[posx][posy]++;
					tablexyz[posx][posy]++;

				}else if(b.b.id>50000) {
					int posx=(int) Math.floor((b.a.x-builds.minx)/binsize);
					int posy=(int) Math.floor((b.a.y-builds.miny)/binsize);
					tablez[posx][posy]++;		
					tablexyz[posx][posy]++;

				}	
			}else if(b.type==7) {
				if(b.a.id>50000&&b.b.id>50000) {
					System.out.println("something wrong bonddensity");
				}else if(b.a.id>50000) {
					int posx=(int) Math.floor((b.b.x-builds.minx)/binsize);
					int posy=(int) Math.floor((b.b.z-builds.minz)/binsize);
					tabley[posx][posy]++;
					tablexyz[posx][posy]++;

				}else if(b.b.id>50000) {
					int posx=(int) Math.floor((b.a.x-builds.minx)/binsize);
					int posy=(int) Math.floor((b.a.z-builds.minz)/binsize);
					tabley[posx][posy]++;		
					tablexyz[posx][posy]++;

				}		
				
				if(b.a.id>50000&&b.b.id>50000) {
					System.out.println("something wrong bonddensity");
				}else if(b.a.id>50000) {
					int posx=(int) Math.floor((b.b.x-builds.minx)/binsize);
					int posy=(int) Math.floor((b.b.y-builds.miny)/binsize);
					tablez[posx][posy]++;
					tablexyz[posx][posy]++;

				}else if(b.b.id>50000) {
					int posx=(int) Math.floor((b.a.x-builds.minx)/binsize);
					int posy=(int) Math.floor((b.a.y-builds.miny)/binsize);
					tablez[posx][posy]++;	
					tablexyz[posx][posy]++;

				}	
			}else if(b.type==8) {
				if(b.a.id>50000&&b.b.id>50000) {
					System.out.println("something wrong bonddensity");
				}else if(b.a.id>50000) {
					int posx=(int) Math.floor((b.b.y-builds.miny)/binsize);
					int posy=(int) Math.floor((b.b.z-builds.minz)/binsize);
					tablex[posx][posy]++;
					tablexyz[posx][posy]++;

				}else if(b.b.id>50000) {
					int posx=(int) Math.floor((b.a.y-builds.miny)/binsize);
					int posy=(int) Math.floor((b.a.z-builds.minz)/binsize);
					tablex[posx][posy]++;			
					tablexyz[posx][posy]++;

				}
				if(b.a.id>50000&&b.b.id>50000) {
					System.out.println("something wrong bonddensity");
				}else if(b.a.id>50000) {
					int posx=(int) Math.floor((b.b.x-builds.minx)/binsize);
					int posy=(int) Math.floor((b.b.z-builds.minz)/binsize);
					tabley[posx][posy]++;
					tablexyz[posx][posy]++;

				}else if(b.b.id>50000) {
					int posx=(int) Math.floor((b.a.x-builds.minx)/binsize);
					int posy=(int) Math.floor((b.a.z-builds.minz)/binsize);
					tabley[posx][posy]++;	
					tablexyz[posx][posy]++;

				}		
				
				if(b.a.id>50000&&b.b.id>50000) {
					System.out.println("something wrong bonddensity");
				}else if(b.a.id>50000) {
					int posx=(int) Math.floor((b.b.x-builds.minx)/binsize);
					int posy=(int) Math.floor((b.b.y-builds.miny)/binsize);
					tablez[posx][posy]++;
					tablexyz[posx][posy]++;

				}else if(b.b.id>50000) {
					int posx=(int) Math.floor((b.a.x-builds.minx)/binsize);
					int posy=(int) Math.floor((b.a.y-builds.miny)/binsize);
					tablez[posx][posy]++;	
					tablexyz[posx][posy]++;

				}	
			}
			
			
		}
		String path2= "C:\\Users\\wechyu88\\WenWorkSpace\\CGEPOXY\\firstLevel\\";

		PrintWriter pwx = new PrintWriter(new File(path2+"tablex.txt"));
		for(int i = 0;i<numbins;i++) {
			for(int j=0;j<numbins;j++) {
				pwx.print(i+" "+j+" "+tablex[i][j]+"\n");
			}
		}
		pwx.close();
		PrintWriter pwy = new PrintWriter(new File(path2+"tabley.txt"));
		for(int i = 0;i<numbins;i++) {
			for(int j=0;j<numbins;j++) {
				pwy.print(i+" "+j+" "+tabley[i][j]+"\n");
			}
		}
		pwy.close();
		PrintWriter pwz = new PrintWriter(new File(path2+"tablez.txt"));
		for(int i = 0;i<numbins;i++) {
			for(int j=0;j<numbins;j++) {
				pwz.print(i+" "+j+" "+tablez[i][j]+"\n");
			}
		}
		
		pwz.close();
		PrintWriter pwxyz = new PrintWriter(new File(path2+"tablexyz.txt"));
		for(int i = 0;i<numbins;i++) {
			for(int j=0;j<numbins;j++) {
				pwxyz.print(i+" "+j+" "+tablexyz[i][j]+"\n");
			}
		}
		
		pwxyz.close();		
		
		System.out.println("crossboundary bond "+counter);
	}
	public static ArrayList<Atom> findBoundaryDETDA(dataFileInfo dinf, Bond bond) {
		Atom at = bond.a;
		Atom bt = bond.b;
		Atom typeSeven1 = findTypeN(at,bt,7);
		Atom typeSeven2 = findTypeN(bt,at,7);
		boolean [] result = {false,false, false};
		ArrayList<Atom> ans = new ArrayList<Atom>();
		if(typeSeven1.id==null||typeSeven2.id==null) {
//			boolean [] result = {false,false, false};
			return ans;
		}
		Atom target1=new Atom();
		Atom target2=new Atom();
		for(Atom a:typeSeven1.connections) {
			if(a.type==8) {
				target1=a;
				break;
			}
		}
		for(Atom a:typeSeven2.connections) {
			if(a.type==8) {
				target2=a;
				break;
			}
		}
		if(Math.abs(target1.x-target2.x)>Math.abs(dinf.xhi-dinf.xlo)*0.5) {
			target1.setCrossx(true);
			target2.setCrossx(true);
			result[0]=true;
		}
		if(Math.abs(target1.y-target2.y)>Math.abs(dinf.yhi-dinf.ylo)*0.5) {
			target1.setCrossy(true);
			target2.setCrossy(true);			
			result[1]=true;
		}		
		if(Math.abs(target1.z-target2.z)>Math.abs(dinf.zhi-dinf.zlo)*0.5) {
			target1.setCrossz(true);
			target2.setCrossz(true);
			result[2]=true;
		}
		if(result[0]||result[1]||result[2]) {
			ans.add(target1);
			ans.add(target2);
		}
		
		return ans;
		
	}
	public static ArrayList<Atom> findDETDA(dataFileInfo dinf, Bond bond) {
		Atom at = bond.a;
		Atom bt = bond.b;
		Atom typeSeven1 = findTypeN(at,bt,7);
		Atom typeSeven2 = findTypeN(bt,at,7);
		ArrayList<Atom> ans = new ArrayList<Atom>();
		if(typeSeven1.id==null||typeSeven2.id==null) {
//			boolean [] result = {false,false, false};
			return ans;
		}
		Atom target1=new Atom();
		Atom target2=new Atom();
		for(Atom a:typeSeven1.connections) {
			if(a.type==8) {
				target1=a;
				break;
			}
		}
		for(Atom a:typeSeven2.connections) {
			if(a.type==8) {
				target2=a;
				break;
			}
		}
		ans.add(target1);
		ans.add(target2);
				
		return ans;
		
	}

	public static boolean[] CrossBondOrNot(Bond bond,double xhi,double xlo,double yhi,double ylo,double zhi,double zlo,ArrayList<Atom> detdalist) {
		//provide a bond that type is 7, find the connected 
		//DETDA to see if they are cross the boundary or not
		Atom at = bond.a;
		Atom bt = bond.b;
		Atom typeSeven1 = findTypeN(at,bt,7);
		Atom typeSeven2 = findTypeN(bt,at,7);
		Atom target1=new Atom();
		Atom target2=new Atom();
		for(Atom a:typeSeven1.connections) {
			if(a.type==8) {
				target1=a;
				break;
			}
		}
		for(Atom a:typeSeven2.connections) {
			if(a.type==8) {
				target2=a;
				break;
			}
		}
		boolean [] result = {false,false, false};
		
		if(target1.id==null||target2.id==null) {
//			boolean [] result = {false,false, false};
			return result;
		}
		
		Atom a1 = target1;
		Atom a2 = target2;
		if(Math.abs(target1.x-target2.x)>Math.abs(xhi-xlo)*0.5) {
			result[0]=true;
		}
		if(Math.abs(target1.y-target2.y)>Math.abs(yhi-ylo)*0.5) {
			result[1]=true;
		}		
		if(Math.abs(target1.z-target2.z)>Math.abs(zhi-zlo)*0.5) {
			result[2]=true;
		}
		if(result[0]||result[1]||result[2]) {
			
		}else {
			return result;
		}
		
		boolean bothexist = true;
		if(detdalist.contains(a1)) {
			
		}else {
			bothexist=false;
			detdalist.add(a1);
		}
		if(detdalist.contains(a2)) {
			
		}else {
			bothexist=false;
			detdalist.add(a2);
		}
		
		if(bothexist) {
			result[0]=false;
			result[1]=false;
			result[2]=false;
			return result;
		}

		

		return result;
	}
	public static Atom findTypeN(Atom a1,Atom a2,int N) {// find typeN atom from direction a1->a2
		Atom finalatom = new Atom();

		HashSet<Atom> visited = new HashSet<Atom>();
		visited.add(a1);
		visited.add(a2);
		Queue<Atom> atomQueue = new LinkedList<Atom>();
		//atomQueue.push(a1);
		atomQueue.add(a2);

		while(!atomQueue.isEmpty()) {
			Atom tempatom=atomQueue.remove();
			if(tempatom.type==N) return tempatom;
			if(tempatom.connections.size()>1) {
				for(Atom a:tempatom.connections) {
					if(visited.contains(a)) continue;
					else {
						atomQueue.add(a);
						visited.add(a);
					}
				}
			}
		}
		
		
		return finalatom;//no atom type 7 is find return an empty atom
	}
	
	public static Atom findType7(Atom a1,Atom a2) {// find type7 atom from direction a1->a2
		Atom final7atom = new Atom();

		HashSet<Atom> visited = new HashSet<Atom>();
		visited.add(a1);
		visited.add(a2);
		Queue<Atom> atomQueue = new LinkedList<Atom>();
		//atomQueue.push(a1);
		atomQueue.add(a2);

		while(!atomQueue.isEmpty()) {
			Atom tempatom=atomQueue.remove();
			if(tempatom.type==7) return tempatom;
			if(tempatom.connections.size()>1) {
				for(Atom a:tempatom.connections) {
					if(visited.contains(a)) continue;
					else {
						atomQueue.add(a);
						visited.add(a);
					}
				}
			}
		}
		
		
		return final7atom;//no atom type 7 is find return an empty atom
	}
}

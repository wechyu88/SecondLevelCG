package analysisData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Stack;

public class randomNetWork {
	ArrayList<Atom> atoms;
	ArrayList<Bond> bonds;
	HashMap<Integer,Integer> atomTobonds; // atomid -> bondnumber
	HashMap<Atom,ArrayList<Atomdis>> neighborlist;
	dataFileInfo df;
	public randomNetWork() {
		
	}
	public randomNetWork(dataFileInfo df, int mu,double sigma,int max) {
		this.atomTobonds = new HashMap<Integer,Integer>();
		this.atoms = df.atomlist;
		this.df=df;
		for(Atom a:atoms) {
			int numb= (int)gaussianRand(mu,sigma,max);
			atomTobonds.put(a.id,numb);
		}
		neighborlist = new HashMap<Atom,ArrayList<Atomdis>> ();
		for(int i=0;i<atoms.size();i++) {
			System.out.println(" this is  "+i+" th atom");
			PriorityQueue<Atomdis> pq = new PriorityQueue<Atomdis>(5);
			for(int j=0;j<atoms.size();j++) {
				if(i==j) {
					continue;
				}
				//double dis = Math.pow((atoms.get(i).x-atoms.get(j).x),2)+Math.pow((atoms.get(i).y-atoms.get(j).y), 2)+Math.pow((atoms.get(i).z-atoms.get(j).z), 2);
				double dis = df.caldis(atoms.get(i), atoms.get(j)) ;
				Atomdis ads = new Atomdis(atoms.get(j),dis);
				pq.add(ads);
				
			}
			ArrayList<Atomdis> orderlist = new ArrayList<Atomdis>();
			for (int k=0;k<50;k++) 
			{
			    Atomdis d = pq.poll();
			    //System.out.println(d.dis+" "+pq.size());
			    orderlist.add(d);
			   
			}
			neighborlist.put(atoms.get(i),orderlist);
		}
		
	}

	public void generateNetWork() {
		//this.atomTobonds = new HashMap<Integer, Integer>();
		this.bonds = new ArrayList<Bond>();
		Stack<Atom> network = new Stack<Atom>();
		network.add(atoms.get(0));
		int countn=0;
		try {
		while(!network.isEmpty()||network!=null) {
			Atom tempatm = network.pop();
			countn++;
			System.out.println(" this is "+countn +" atom");
			int count=0;
			int tempc=tempatm.connections.size();
			if(tempc==atomTobonds.get(tempatm.id)) continue;
			count=tempc;
			ArrayList<Atomdis> list = neighborlist.get(tempatm);
			//System.out.println(tempatm.id+" "+list.size());
			int count2=0;
			boolean meet=false;
			for(int i=0;i<list.size();i++) {
				count2++;
				Atomdis ads = list.get(i);
				System.out.println(" list id "+ads.atom.id+" "+count2);

				if(ads.atom.visited==false) {
					if(ads.atom.connections.size()<atomTobonds.get(ads.atom.id)&&!tempatm.connections.contains(ads.atom)) {
						tempatm.addConnection(ads.atom);
						ads.atom.addConnection(tempatm);
						network.add(ads.atom);
						Bond b = new Bond(tempatm,ads.atom);
						this.bonds.add(b);
						count++;
						meet=true;
						if(count==atomTobonds.get(tempatm.id)) break;
					}else if(ads.atom.connections.size()==atomTobonds.get(ads.atom.id)) {
						ads.atom.setVisit(true);
					}
				}else {
					continue;
				}
			}
			tempatm.setVisit(true);
			if(meet==false) {
				atomTobonds.put(tempatm.id, tempatm.connections.size());
				
			}
			
		}
		}
		catch(NullPointerException e) {
			System.out.println(" error here");
		}
		System.out.println("I'M HERE ");
		
	}
	public int gaussianRand(int mu, double sigma, int max) {
		Random rd = new Random();
		
		double temprd = rd.nextGaussian();
		long x = Math.round(sigma*temprd) + mu;
		while(x>max||x<1) {
			temprd = rd.nextGaussian();
			x = Math.round(sigma*temprd) + mu;
		}
		return (int)x;
	}
	public static void main(String[] argv) throws IOException {
		randomNetWork rdnw = new randomNetWork();
		String pathInput = "C:\\Users\\wechyu88\\WenWorkSpace\\CGEPOXY\\secondLevel\\1.data";
		readData rdata = new readData(pathInput);
		dataFileInfo dinf= rdata.readDataInfo(rdata.br);
		randomNetWork rnw = new randomNetWork(dinf,10,2.0,12);
		rnw.generateNetWork();
		ArrayList<Atom> newatoms = new ArrayList<Atom>();
		for(Atom a:rnw.atoms) {
			if(a.connections.size()!=0) {
				newatoms.add(a);
			}
		}
		System.out.println("neighbor done "+newatoms.size());
	}
}

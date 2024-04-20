package main;

import interfaces.List;
import interfaces.Map;
import interfaces.Set;
import interfaces.Stack;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import data_structures.ArrayList;
import data_structures.ArrayListStack;
import data_structures.HashSet;
import data_structures.HashTableSC;
import data_structures.LinkedStack;
import data_structures.SimpleHashFunction;
public class TrainStationManager {


	//AVERIGUA QUE HACER PARA EL SIZE(10)
	private Map<String,List<Station>> stations = new HashTableSC<String,List<Station>>(10,new SimpleHashFunction<String>());
	private List<String> tester=new ArrayList<String>();
	private Map<String,Station> shortestRoute;
	private Stack<Station> toVisit = new ArrayListStack<Station>();
	private Set<Station> visited = new HashSet<Station>();
	private Map<String,Integer>stationAmount=new HashTableSC<String,Integer>(10,new SimpleHashFunction<String>());
	private Map<String,LinkedStack<String>>stops=new HashTableSC<String,LinkedStack<String>>(10,new SimpleHashFunction<String>());
	public TrainStationManager(String station_file) {
		
		try (BufferedReader cities = new BufferedReader(new FileReader("./inputFiles/"+station_file))) {
			//ARREGLA ESTO, HAZ IF STATEMENT PA CHECK SI ES EL TITULO O CITY
			String city=cities.readLine();
			city=cities.readLine();
			
			while(city!=null) {
				String[] temp = city.split(",");
				String src = temp[0]; 
				String dest = temp[1];
				int distance = Integer.valueOf(temp[2]);
				
				//adding the src key to stations with dest value
				//ex: [berlint,dubay,33]=berlint(key):{(dubay,44)}
				Station srcStation = new Station(dest,distance);
				if(!stations.containsKey(src)) {
					List<Station> srcNeighbor=new ArrayList<Station>() {{add(srcStation);}};
					stations.put(src, srcNeighbor);
					tester.add(src);
				}else {
					stations.get(src).add(srcStation);
				}
				
				//adding the dest key to stations with src value
				//ex: [berlint,dubay,33]=dubay(key):{(berlint,44)}
				Station destStation = new Station(src,distance);
				if(!stations.containsKey(dest)) {
					List<Station> destNeighbor=new ArrayList<Station>() {{add(destStation);}};
					stations.put(dest, destNeighbor);
					tester.add(dest);
				}
				else {stations.get(dest).add(destStation);}
				
				city = cities.readLine();
			}
			
//			for(String i:tester) {
//				System.out.println(i+": ");
//				for(Station s:stations.get(i)) {System.out.println(s);}
//			}
			
		} catch(IOException error) {System.err.println(error.getMessage());}
	}
	
	private void findShortestDistance() {
		  this.shortestRoute=new HashTableSC<String, Station>(stations.size(),new SimpleHashFunction<String>());
		//shortest=new HashTableSC<String, Station>(stations.size(),new SimpleHashFunction<String>());
		// Map<String,Station> shortest=new HashTableSC<String, Station>(stations.size(),new SimpleHashFunction<String>());
		  this.shortestRoute.put("Westside", new Station("Westside",0));
		  for(String i:stations.getKeys()) {
			//if(!shortestRoute.getKeys().contains(i)) {shortestRoute.put(i, new Station("Westside",Integer.MAX_VALUE));}
			  shortestRoute.put(i, new Station("Westside",Integer.MAX_VALUE));
			  this.stationAmount.put(i, 2);
			  this.stops.put(i, new LinkedStack<String>());
			  this.stops.get(i).push(i);
		  }
//		 this.toVisit = new ArrayListStack<Station>();
//		 this.visited = new HashSet<Station>();
		 
		 toVisit.push(new Station("Westside",0));
		 this.shortestRoute.put("Westside", new Station("Westside",0));
		 this.stationAmount.put("Westside", 1);
		 //this.stops.get("WestSide").push(new Station("Westside",0));
		 
//		 There’s another condition we could use to identify when we are done with the algorithm
//		 instead of waiting for the stack to empty. This condition could be iterated until you visit
//		 every station (every station is in the set).
		 

		
		while(!toVisit.isEmpty()) {
		
			 Station currentStation=toVisit.pop();
			 visited.add(currentStation);
			 
			// System.out.println(currentStation.getCityName());
			 
			 for(Station s:stations.get(currentStation.getCityName())) {
				 boolean connected=true;
				 if(!stations.get("Westside").contains(s)) {connected=false;}
				 
				 
				 
				
				// System.out.println(s);
				 //A:current shortest distance of neighbor
				 int A=this.shortestRoute.get(s.getCityName()).getDistance();
				
				 //for(Station a:this.stations.get(s.getCityName())) {if(a.getDistance()<A) {A=a.getDistance();}}

				// System.out.println("A: "+A);
				 
				 //B:shortest distance of current station
				 int B = this.shortestRoute.get(currentStation.getCityName()).getDistance();
				 
				 //for(Station i:stations.get(currentStation.getCityName())) {if(i.getDistance() < B) {B = i.getDistance();}}
		
			//	 System.out.println("B: "+B);
				 
				
				 //C:distance between current Station and neighbor
				 
		
				 int c=stations.get(currentStation.getCityName()).firstIndex(s);
				 int C=stations.get(currentStation.getCityName()).get(c).getDistance();
			
				 if(A>(B+C)) {
					 this.shortestRoute.put(s.getCityName(),new Station(currentStation.getCityName(),B+C));
					 if(!connected) {
						 this.stationAmount.put(s.getCityName(), this.stationAmount.get(currentStation.getCityName())+1);
						 this.stops.get(s.getCityName()).push(currentStation.getCityName());
						
					 }
					 
				 }
				
				// System.out.println("tovisit: "+visited.isMember(s));
				 if(!visited.isMember(s)) {sortStack(s,this.toVisit);}
				 
//				 System.out.println("tovisit: "+this.toVisit.size());
//				 System.out.println("C: "+C);
//				 System.out.println("B+C: "+(B+C));
//				 System.out.println("og: "+B);
//				 System.out.println("og+short: "+(s.getDistance()+this.shortestRoute.get(currentStation.getCityName()).getDistance()));
			 }
		}
///////////////// ////////////////////////////////////////////////////
		 
		// System.out.println(shortestRoute.size());
//		 for(String name:shortestRoute.getKeys()) {
//			 System.out.println(name+" =("+shortestRoute.get(name).getCityName()+","+shortestRoute.get(name).getDistance());
//			 System.out.println("sa"+this.stationAmount.get(name));
//		 }
	}

	public void sortStack(Station station, Stack<Station> stackToSort) {
		Stack<Station> temp= new LinkedStack<Station>();
		int target=station.getDistance();
		while(!stackToSort.isEmpty() && stackToSort.top().getDistance()<target) {
			temp.push(stackToSort.pop());
		}
		stackToSort.push(station);
		while(!temp.isEmpty()) {
			stackToSort.push(temp.pop());
		}
	}
	
	public Map<String, Double> getTravelTimes() {
		Map<String,Double> times=new HashTableSC<String,Double>(stations.size(),new SimpleHashFunction<String>());
		Map<String,Station>routes=this.getShortestRoutes();
		for(String i:routes.getKeys()) {
			int stops=this.stationAmount.get(i);
			if(stops>=2) {
				times.put(i,(routes.get(i).getDistance()*2.5)+(15*(stops-2)));
			}else {times.put(i,(routes.get(i).getDistance()*2.5));}
		
		}
		return times;
		// 5 minutes per kilometer
		// 15 min per station
		
	}


	public Map<String, List<Station>> getStations() {
		
		return this.stations;
		
	}


	public void setStations(Map<String, List<Station>> cities) {
		this.stations=cities;
	}


	public Map<String, Station> getShortestRoutes() {
		findShortestDistance();
		return this.shortestRoute;
		
//		findShortestDistance();
//		System.out.print("1");
//		return this.shortest;
		
	}


	public void setShortestRoutes(Map<String, Station> shortestRoutes) {
		this.shortestRoute=shortestRoutes;
	}
	
	/**
	 * BONUS EXERCISE THIS IS OPTIONAL
	 * Returns the path to the station given. 
	 * The format is as follows: Westside->stationA->.....stationZ->stationName
	 * Each station is connected by an arrow and the trace ends at the station given.
	 * 
	 * @param stationName - Name of the station whose route we want to trace
	 * @return (String) String representation of the path taken to reach stationName.
	 */
	public String traceRoute(String stationName) {
		
		// Remove if you implement the method, otherwise LEAVE ALONE
		//throw new UnsupportedOperationException();
		Map<String,Station> route=this.getShortestRoutes();
		LinkedStack<String> answer=new LinkedStack<String>();
		String ret="";
		answer.push(stationName);
		
		while(answer.top()!="Westside") {
			answer.push(route.get(answer.top()).getCityName());
		}
		while(answer.size()>1) {
			ret+=answer.pop();
			ret+="->";
		}
		ret+=answer.pop();
		return ret;
		
	}

}
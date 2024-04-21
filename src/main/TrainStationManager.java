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


	// map with station name as key and list of connected stations as values 
	private Map<String,List<Station>> stations = new HashTableSC<String,List<Station>>(1,new SimpleHashFunction<String>());
	
	//map with station name as key and station closest to westside as value
	private Map<String,Station> shortestRoute;
	
	//stations not yet visited, used for findShortestDistance
	//private Stack<Station> toVisit = new ArrayListStack<Station>();
	
	//stations already visited in findShortestDistance()
	//private Set<Station> visited = new HashSet<Station>();
	
	//amount of stops needed to get to key station, used in getTravelTimes()
	private Map<String,Integer>stationAmount=new HashTableSC<String,Integer>(10,new SimpleHashFunction<String>());
	
	
	public TrainStationManager(String station_file) {
		
		try (BufferedReader cities = new BufferedReader(new FileReader("./inputFiles/"+station_file))) {
			
			String city=cities.readLine();
			city=cities.readLine();
			
			while(city!=null) {
				String[] temp = city.split(",");
				String src = temp[0]; 
				String dest = temp[1];
				int distance = Integer.valueOf(temp[2]);
				
				//adding the src key to stations with destiny value
				//ex: [berlint,dubay,33]=berlint(key):{(dubay,44)}
				Station srcStation = new Station(dest,distance);
				
				
				//if not already in map, add an array with one station
				if(!stations.containsKey(src)) {
					List<Station> srcNeighbor=new ArrayList<Station>() {{add(srcStation);}};
					stations.put(src, srcNeighbor);
					
				//if already in array, add station to existing array 
				}else {
					stations.get(src).add(srcStation);
				}
				
				//adding the dest key to stations with src value
				//ex: [berlint,dubay,33]=dubay(key):{(berlint,44)}
				Station destStation = new Station(src,distance);
				if(!stations.containsKey(dest)) {
					List<Station> destNeighbor=new ArrayList<Station>() {{add(destStation);}};
					stations.put(dest, destNeighbor);
					
				}
				else {stations.get(dest).add(destStation);}
				
				city = cities.readLine();
			}
			

			
		} catch(IOException error) {System.err.println(error.getMessage());}
	}
	
	private void findShortestDistance() {
		// map that will hold the current shortest distance between Westside and every other station
		  this.shortestRoute=new HashTableSC<String, Station>(stations.size(),new SimpleHashFunction<String>());
		  
		  //empty stack that will represent all the stations that have been discovered and need to be visited.
		  Stack<Station> toVisit = new ArrayListStack<Station>();
		  
		  //empty set that will represent the stations that have already been visited
		  Set<Station> visited = new HashSet<Station>();
		
		  //add the starting position (Westside) to the stack since it’s the first station we will visit
		  this.shortestRoute.put("Westside", new Station("Westside",0));
		  
		  //Initially for all keys in the map we will have (Westside, ∞)
		  for(String i:stations.getKeys()) {
			  shortestRoute.put(i, new Station("Westside",Integer.MAX_VALUE));
			  this.stationAmount.put(i, 2);
		  }

		 
		 toVisit.push(new Station("Westside",0));
		 this.shortestRoute.put("Westside", new Station("Westside",0));
		 this.stationAmount.put("Westside", 1);
	
		//While the stack isn’t empty
		while(!toVisit.isEmpty()||stations.size()==visited.size()) {
			//Pop the value from the stack, we’ll call it current station.
			 Station currentStation=toVisit.pop();
			 
			 //Add current station to the set.
			 visited.add(currentStation);

			 //Iterate over the neighbors of the current station.
			 for(Station s:stations.get(currentStation.getCityName())) {
				 boolean connected=true;
				 if(!stations.get("Westside").contains(s)) {connected=false;}
				 
				
			
				 //A:current shortest distance of neighbor
				 int A=this.shortestRoute.get(s.getCityName()).getDistance();
				
				
				 
				 //B:shortest distance of current station
				 int B = this.shortestRoute.get(currentStation.getCityName()).getDistance();
				 
				
				
				 //C:distance between current Station and neighbor
				 int c=stations.get(currentStation.getCityName()).firstIndex(s);
				 int C=stations.get(currentStation.getCityName()).get(c).getDistance();
			
				 if(A>(B+C)) {
					 this.shortestRoute.put(s.getCityName(),new Station(currentStation.getCityName(),B+C));
					 if(!connected) {
						 this.stationAmount.put(s.getCityName(), this.stationAmount.get(currentStation.getCityName())+1);
						
						
					 }
					 
				 }
				
				
				 if(!visited.isMember(s)) {sortStack(s,toVisit);}
				 

			 }
		}

	}

	public void sortStack(Station station, Stack<Station> stackToSort) {
		Stack<Station> temp= new LinkedStack<Station>();
		int target=station.getDistance();
		
		//remove values that are smaller than target and add to temp
		while(!stackToSort.isEmpty() && stackToSort.top().getDistance()<target) {
			temp.push(stackToSort.pop());
		}
		
		//push target value when place is found
		stackToSort.push(station);
		
		//put values smaller than target back into the stack
		while(!temp.isEmpty()) {
			stackToSort.push(temp.pop());
		}
	}
	
	public Map<String, Double> getTravelTimes() {
		//map of station name and time required to get to westside
		Map<String,Double> times=new HashTableSC<String,Double>(stations.size(),new SimpleHashFunction<String>());
		
		//map of shortest routes
		Map<String,Station>routes=this.getShortestRoutes();
		
		//for every key in routes
		for(String i:routes.getKeys()) {
			//stops is the amount of stops to get to westside
			int stops=this.stationAmount.get(i);
			//if not directly connected to westide add fifteen minutes for every stop
			if(stops>=2) {
				times.put(i,(routes.get(i).getDistance()*2.5)+(15*(stops-2)));
				
			//if directly connected to westide
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
		
		//shortest routes 
		Map<String,Station> route=this.getShortestRoutes();
		
		//trace route stack
		LinkedStack<String> answer=new LinkedStack<String>();
		
		//return string
		String ret="";
		answer.push(stationName);
		
		//if it equals westside, we've arrived
		while(answer.top()!="Westside") {
			//push the answer's shortest route
			answer.push(route.get(answer.top()).getCityName());
		}
		
		//add every value in answer to return string
		while(answer.size()>1) {
			ret+=answer.pop();
			ret+="->";
		}
		ret+=answer.pop();
		return ret;
		
	}

}
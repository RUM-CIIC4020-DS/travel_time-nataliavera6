package main;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
//import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import interfaces.Map;

import javax.swing.JLabel;

public class StationGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	//public double hoursmin(int time,double departure);
	private JPanel contentPane;
	private JTable table;
//	private TableColumn column;
//	private JLabel label;
//	private JButton button;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StationGUI frame = new StationGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public StationGUI() {
		TrainStationManager tsm = new TrainStationManager("stations.csv");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		//String[][] data = {{"Bugapest","9:35am"},{"Dubay","10:30am"},{"Berlint","8:25pm"},{"Mosbull","8:2"
		String[][] data=new String[12][3];
		String[]columnName= {"Station","Departure","Arrival"};
		double[] arrivals=new double[12];
		double[] departure= {9.35,10.30,8.25,6.0,6.40,10.25,12.3,1.3,3.35,4.45,7.25,2.0};
		String[] departures= {"9:35am","10:30am","8:25pm","6:00pm","6:40am","10:25am","12:30pm","1:30pm","3:35pm","4:45pm","7:25am","2:00pm"};
		String[] ampm= {"am","am","pm","pm","am","am","pm","pm","pm","pm","am","pm"};
		String[] stations= {"Bugapest","Dubay","Berlint","Mosbull","Cayro","Bostin","Los Angelos","Dome", "Takyo","Unstabul","Chicargo","Loondun"};
		Map<String, Double> travel=tsm.getTravelTimes();
		
		for(int i=0;i<stations.length;i++) {
			double time=travel.get(stations[i]);
			double arrive=hoursmin(time,departure[i]);
			if(arrive-12<=0) {arrivals[i]=arrive;}
			
			else {
				arrivals[i]=arrive-12;
				if(departure[i]-12>1||departure[i]-12<-1) {
					if(ampm[i]=="am") {ampm[i]="pm";}
					else {ampm[i]="am";}
				}
				
			}
		}
		for(int i=0;i<stations.length;i++) {
			int timeHr=(int) Math.floor(arrivals[i]);
			int timeMin=(int) ((arrivals[i]-timeHr)*100);
			String ar=String.valueOf(timeHr)+":"+String.valueOf(timeMin)+ampm[i];
			String[] dataList= {stations[i],departures[i],ar};
			data[i]=dataList;
		}
		table=new JTable(data, columnName);

		contentPane.add(table);
		JScrollPane scrollPane = new JScrollPane(table);
		contentPane.add(scrollPane);
		
//		button=new JButton("Press");
		
		
		System.out.println(hoursmin(325,12.30));
		
	}
	public double hoursmin(double time,double departure){
		System.out.println(" ");
		System.out.println("Start: "+time);
		
		//tiempo q se tarda en llegar
		double hr=time/60;
		double min=(hr-Math.floor(hr));
		min*=60;
		
		hr=Math.floor(hr);
		
		
		System.out.println("hrs time: "+hr);
		System.out.println("min time: "+min);
		
		//hora de salida
		double hrs=Math.floor(departure);
		double mins=(departure-hrs)*100;
		System.out.println("hrs departure: "+hrs);
		System.out.println("min departure: "+mins);
		double arrivalHr=hr+hrs;
		double arrivalmins=min+mins;
		
		System.out.println("hrs: "+arrivalHr);
		System.out.println("min: "+arrivalmins);
		if(arrivalmins<60) {return (arrivalHr)+((arrivalmins/100));}
		if(arrivalmins==60) {return arrivalHr+1;}
		else {
			//int arrivalmins=
			return arrivalHr+hoursmin((int)arrivalmins,0.0);
		}
		//double hrA=arrival/60;
		//return new double[]{hrD,minD};
	}

}

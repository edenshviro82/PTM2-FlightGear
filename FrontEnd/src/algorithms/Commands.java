package algorithms;

//import jdk.jfr.events.FileWriteEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.*;

public class Commands {

	// Default IO interface
	public interface DefaultIO {
		public String readText();

		public void write(String text);

		public float readVal();

		public void write(float val);

		// you may add default methods here
	}

	// the default IO to be used in all commands
	DefaultIO dio;

	public Commands(DefaultIO dio) {
		this.dio = dio;
	}

	// you may add other helper classes here


	//////////////////////////////////////////////////////////////////////////////////
	// the shared state of all commands
	private class SharedState {
		// implement here whatever you need
		SimpleAnomalyDetector ad;
		List<AnomalyReport> ar;
		int timestepslen;

		public SharedState() {
			ad = new SimpleAnomalyDetector();
		}
	}

	private SharedState sharedState = new SharedState();
	//////////////////////////////////////////////////////////////////////////////////

	// Command abstract class
	public abstract class Command {
		protected String description;

		public Command(String description) {
			this.description = description;
		}

		public abstract void execute();
	}

	// Command class for example:
	public class ExampleCommand extends Command {

		public ExampleCommand() {
			super("this is an example of command");
		}

		@Override
		public void execute() {
			dio.write(description);
		}
	}


	// implement here all other commands

	public class uploadCSV extends Command {

		//menu function 1///////////////////////////////////////////////////////////////////
		public uploadCSV() {
			super("upload a time series csv file\n");
		}

		@Override
		public void execute() {
			dio.write("Please upload your local train CSV file.\n");
			this.communicateWclient("anomalyTrain.csv");
			dio.write("Upload complete.\n");
			dio.write("Please upload your local test CSV file.\n");
			this.communicateWclient("anomalyTest.csv");
			dio.write("Upload complete.\n");
		}

		public void communicateWclient(String filename) {
			String line = null;
			FileWriter out = null;
			try {
				out = new FileWriter(filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (true) {
				line = dio.readText();
				//in case of empty line reading
				if (line.length() == 0)
					line = dio.readText();
				//sign from client to stop reading
				if (line.equals("done"))
					break;
				try {
					out.write(line);
					out.write("\n");
					if (filename.equals("anomalyTest.csv"))
						//counting the number of the files time steps
						sharedState.timestepslen++;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//menu function number 2///////////////////////////////////////////////////////////////////
	public class Setting extends Command {
		public Setting() {
			super("algorithm settings\n");
		}

		@Override
		public void execute() {
			dio.write("The current correlation threshold is " + sharedState.ad._minCor + "\n");
			dio.write("Type a new threshold\n");
			double thres = dio.readVal();
			//check with eli how he want to implement a case of unvalid input
			while (thres < 0 || thres > 1) {
				dio.write("please choose a value between 0 and 1.\n");
				thres = dio.readVal();
			}
		}
	}

	//menu function 3/////////////////////////////////////////////////////////////////////////
	public class anomalyDetect extends Command {
		public anomalyDetect() {
			super("detect anomalies\n");
		}

		@Override
		public void execute() {
			//learn the normal model with our ad while creating a new time series for train csv file
			sharedState.ad.learnNormal(new TimeSeries("anomalyTrain.csv"));
			sharedState.ar = sharedState.ad.detect(new TimeSeries("anomalyTest.csv"));
			//detecting anomalies with our ad while creating a new time series for test csv file
			dio.write("anomaly detection complete.\n");
		}
	}

	//menu function 4/////////////////////////////////////////////////////////////////////////
	public class display extends Command {
		public display() {
			super("display results\n");
		}

		@Override
		public void execute() {
			for (int i = 0; i < sharedState.ar.size(); i++) {
				//getting the i anomaly report from our anomaly detector
				AnomalyReport print = sharedState.ar.get(i);
				dio.write(print.timeStep + "\t " + print.description + "\n");
			}
			dio.write("Done.\n");
		}
	}

	//menu function 5//////////////////////////////////////////////////////////////////////////
	public class analayze extends Command {
		public analayze() {
			super("upload anomalies and analyze results\n");
		}

		@Override
		public void execute() {
			ArrayList<Point> detectedRange = combineAnomalies(); //not getting parameters because he's using the shared state
			ArrayList<Point> userRange = getUserRange(); //also not getting parameters because of shared state
			double truePositive = 0;
			double falsePositive = 0;
			//run on all of our detected anomalies intervals
			for (int i=0;i< detectedRange.size(); i++) {
				//get the i anomaly interval
				Point detect = detectedRange.get(i);
				boolean flag = false;
				//run on all the correct intervals we get from the user
				for (int j=0; j<userRange.size(); j++) {
					//pull the j user interval
					Point Range = userRange.get(j);
					//check if there is any cut between our current anomaly interval to one of the intervals we get
					if (!(detect.y< Range.x || detect.x > Range.y)) {
						truePositive++;
						flag = true;
						break;
					}
				}
				if (!flag)
					falsePositive++;
			}
			//get the number og total time steps from shared state (minus 1 because of the headline)
			int totalTimeSteps = sharedState.timestepslen-1;
			//subtract all the anomalies to get only the number of valid time steps
			for (int i=0; i<userRange.size(); i++) {
				totalTimeSteps-= (userRange.get(i).y-userRange.get(i).x+1);
			}
			//creating a new decimal format
			DecimalFormat df = new DecimalFormat("#0.0");
			df.setMaximumFractionDigits(3);
			df.setRoundingMode(RoundingMode.DOWN);
			dio.write("True Positive Rate: " + df.format(truePositive/(double)userRange.size()) +"\n");
			dio.write("False Positive Rate: " + df.format(falsePositive/(double)totalTimeSteps)+ "\n");
		}

		ArrayList<Point> getUserRange() {
			//create a new points list to get the user intervals
			ArrayList<Point> Range = new ArrayList<>();
			dio.write("Please upload your local anomalies file.\n");
			String line;
			while (true) {
				line = dio.readText();
				//getting the signal from the client to stop reading
				if (line.equals("done"))
					break;
				if (line.length() == 0)
					line = dio.readText();
				//save the line we got int to new string and split the values by , to get the values we from the client
				String[] split = line.split(",");
				//parsing the values from the string read line to int
				int x = Integer.parseInt(split[0]);
				int y = Integer.parseInt(split[1]);
				//create a new point from the user intervals
				Range.add(new Point(x, y));
			}
			dio.write("Upload complete.\n");
			return Range;
		}

		ArrayList<Point> combineAnomalies() {
			//get our anomaly reports list
			List<AnomalyReport> check = sharedState.ar;
			//create a new list of point to save the anomalies intervals
			ArrayList<Point> detectedRanges = new ArrayList<>();
			int count = 0;
			//sorting the anomalies by their time steps
			check.sort(((o1, o2) -> { return (int) (o1.timeStep - o2.timeStep);}));
			for (int i = 0; i < check.size(); i++) {
				//pulling the i anomaly for check
				AnomalyReport toCheck = check.get(i);
				//cond1: make sure we stay at the array limit, cond2: make sure the anomalies are serial, cond3: make sure the anomalies from the same type
				if (i+1 < check.size() && toCheck.timeStep + 1 == check.get(i + 1).timeStep && toCheck.description.equals(check.get(i + 1).description))
					//if all of above is true, we would like to count the serial anomalies
					count++;
				else {
					//if the sequence breaks, then save the new interval and start a new counting
					detectedRanges.add(new Point(toCheck.timeStep - count, toCheck.timeStep));
					count = 0;
				}
			}
			return detectedRanges;
		}
	}

	//menu function 6//////////////////////////////////////////////////////////////////////////
	public class turnoff extends Command {
		public turnoff () {super("exit\n");}

		@Override
		public void execute() {
			System.exit(0);
		}
	}
}

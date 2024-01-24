package semesterProject;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class SlaveB {

	public static void main(String[] args) throws IOException {
		ArrayList<Job> jobs = new ArrayList<Job>();

		// Hard code in IP and Port
		// create the slave socket
		Socket slaveSocket = new Socket("127.0.0.1", 7777);

		SlaveReadThread rt = new SlaveReadThread(slaveSocket, jobs);
		SlaveWriteThread wt = new SlaveWriteThread(slaveSocket, jobs);

		rt.start();
		wt.start();

	}
}

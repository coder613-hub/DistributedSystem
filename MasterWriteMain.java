package semesterProject;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MasterWriteMain extends Thread {

	private Socket client1;
	private Socket client2;
	private ArrayList<Job> completedJobs;

	// receive both sockets and send the correct client based on the job clientNum
	public MasterWriteMain(Socket client1, Socket client2, ArrayList<Job> completedJobs) {
		this.client1 = client1;
		this.client2 = client2;
		this.completedJobs = completedJobs;
	}

	// send main that the job is completed
	public void run() {
		try (PrintWriter writer1 = new PrintWriter(client1.getOutputStream(), true);
				PrintWriter writer2 = new PrintWriter(client2.getOutputStream(), true);) {
			Job job = null;
			while (true) {
				if (completedJobs.size() != 0) {
					synchronized (completedJobs) {
						if (completedJobs.size() != 0) {
							job = completedJobs.get(0);
							completedJobs.remove(0);
						} else {
							continue;
						}
					}

					if (job.getClientNum() == 1) {
						writer1.println("Job " + job.getId() + " is complete.");
					} else {
						writer2.println("Job " + job.getId() + " is complete.");
					}

					System.out.println("Master informed client that job " + job.getId() + " is complete.");
				} else {
					Thread.sleep(1);
				}
			}
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
}

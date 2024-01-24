package semesterProject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class SlaveReadThread extends Thread {
	private Socket socket;
	private ArrayList<Job> jobs;

	public SlaveReadThread(Socket socket, ArrayList<Job> jobs) {
		this.socket = socket;
		this.jobs = jobs;

	}

	//reads jobs from master
	@Override
	public void run() {
		try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());) {
			Job job;
			
			while (true) {
				if ((job = (Job) input.readObject()) != null) {
					System.out.println("Slave " + job.getSlaveAssigned() + " received job " + job.getId());
					// add the job to the list
					synchronized (jobs) {
						jobs.add(job);
					}
				}else {
					Thread.sleep(1);
				}
			}

		} catch (Exception e) {

		}
	}
}

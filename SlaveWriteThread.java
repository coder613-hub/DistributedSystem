package semesterProject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class SlaveWriteThread extends Thread {
	private Socket socket;
	private ArrayList<Job> jobs;

	public SlaveWriteThread(Socket socket, ArrayList<Job> jobs) {
		this.socket = socket;
		this.jobs = jobs;

	}

	@Override
	public void run() {
		try (ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());) {
			while (true) {

				if (jobs.size() != 0) {
					Job currJob = jobs.get(0);
					// if its not the optimal slave, sleep 10 seconds otherwise 2 seconds
					if (currJob.getType() != currJob.getSlaveAssigned()) {
						Thread.sleep(10000);
					} else {
						Thread.sleep(2000);
					}

					synchronized (jobs) {
						jobs.remove(0);
					}
					System.out.println("Slave " + currJob.getSlaveAssigned() + " completed job " + currJob.getId());

					// send the completed job to the server
					output.writeObject(currJob);
					System.out.println("Slave " + currJob.getSlaveAssigned()
							+ " informed master about the completion of job " + currJob.getId());

				}else {
					Thread.sleep(1);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

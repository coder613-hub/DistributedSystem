package semesterProject;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MasterReadSlaveThread extends Thread {
	private Socket socket;
	private ArrayList<Job> completedJobs;
	private Master master;

	public MasterReadSlaveThread(Socket socket, ArrayList<Job> completedJobs, Master master) {
		this.socket = socket;
		this.completedJobs = completedJobs;
		this.master = master;
	}

	// read completed jobs from slaves
	@Override
	public void run() {
		try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());) {
			Job job;

			while (true) {
				if ((job = (Job) input.readObject()) != null) {
					System.out.println(
							"Master received completed job " + job.getId() + " from slave " + job.getSlaveAssigned());
					synchronized (completedJobs) {
						completedJobs.add(job);
						master.removeTime(job);
					}
				} else {
					Thread.sleep(1);
				}

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}

	}
}

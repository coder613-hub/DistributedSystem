package semesterProject;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MainWriteThread extends Thread {
	private Socket socket;
	private ArrayList<Job> jobs;

	public MainWriteThread(Socket socket, ArrayList<Job> jobs) {
		this.socket = socket;
		this.jobs = jobs;

	}

	// sends the jobs from the user to master
	@Override
	public void run() {
		try (ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());) {
			while (true) {
				if (jobs.size() != 0) {
					Job currJob = null;
					synchronized (jobs) {
						if (jobs.size() != 0) {
							currJob = jobs.get(0);
							jobs.remove(0);
						}
					}
					// send the job to master
					output.writeObject(currJob);
					System.out.println("Client submitted job " + currJob.getId() + " to master.");

				} else {
					Thread.sleep(1);
				}

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

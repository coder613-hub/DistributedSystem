package semesterProject;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MasterWriteSlaveThread extends Thread {
	private Socket socket;
	private ArrayList<Job> assignedJobs;

	public MasterWriteSlaveThread(Socket socket,ArrayList<Job> assignedJobs) {
		this.socket = socket;
		this.assignedJobs = assignedJobs;
	}

	// send slave a job
	@Override
	public void run() {
		try (ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());) {
			Job currJob = null;
			while (true) {
				if(assignedJobs.size() != 0) {
					synchronized(assignedJobs) {
						if(assignedJobs.size() != 0) {
						currJob = assignedJobs.get(0);
						assignedJobs.remove(0);
						}else {
							continue;
						}
					}
					output.writeObject(currJob);
					System.out.println("Master submitted job " + currJob.getId() + " to slave " + currJob.getSlaveAssigned());
				}else {
					Thread.sleep(1);
				}

			}
		} catch (Exception e) {
			System.out.println("problem in master write slave");
		}
	}

}

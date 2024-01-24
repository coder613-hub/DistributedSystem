package semesterProject;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

//read from client
public class MasterReadMainThread extends Thread {
	private Socket socket;
	private ArrayList<Job> assignedJobsA;
	private ArrayList<Job> assignedJobsB;
	private Master master;
	private Socket client1;

	public MasterReadMainThread(Socket socket, Socket client1, ArrayList<Job> assignedJobsA,
			ArrayList<Job> assignedJobsB, Master master) {
		this.socket = socket;
		this.assignedJobsA = assignedJobsA;
		this.assignedJobsB = assignedJobsB;
		this.master = master;
		this.client1 = client1;
	}

	@Override
	public void run() {
		try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());) {
			Job currJob;
			while (true) {
				if ((currJob = (Job) input.readObject()) != null) {

					System.out.println("Master received job " + currJob.getId() + " from client.");
					master.assignTask(currJob);

					if (currJob.getSlaveAssigned() == JobType.A) {
						synchronized (assignedJobsA) {
							assignedJobsA.add(currJob);
						}
					} else {
						synchronized (assignedJobsB) {
							assignedJobsB.add(currJob);
						}
					}

					// if the job came from the first client
					if (socket == client1) {
						currJob.setClientNum(1);
						// if the job came from the second client
					} else {
						currJob.setClientNum(2);
					}
					System.out.println(
							"Master assigned job " + currJob.getId() + " to slave " + currJob.getSlaveAssigned());
				} else {
					Thread.sleep(1);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}

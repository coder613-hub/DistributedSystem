package semesterProject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Master {
	int timeA;
	int timeB;

	public static void main(String[] args) {
		args = new String[] { "7777" };
		String hostName = args[0];
		int portNumber = Integer.parseInt(args[0]);

		// Master object to use the master class methods
		Master master = new Master();

		try (ServerSocket masterSocket = new ServerSocket(portNumber);
				Socket client1 = masterSocket.accept();
				Socket client2 = masterSocket.accept();
				Socket slaveA = masterSocket.accept();
				Socket slaveB = masterSocket.accept();

		) {
			System.out.println("Connected");
			ArrayList<Job> completedJobs = new ArrayList<Job>();
			ArrayList<Job> assignedJobsA = new ArrayList<Job>();
			ArrayList<Job> assignedJobsB = new ArrayList<Job>();

			// threads to read jobs from client
			MasterReadMainThread mrtClient1 = new MasterReadMainThread(client1, client1, assignedJobsA, assignedJobsB,
					master);
			MasterReadMainThread mrtClient2 = new MasterReadMainThread(client2, client1, assignedJobsA, assignedJobsB,
					master);

			// threads to write to slaves
			MasterWriteSlaveThread mwtSlaveA = new MasterWriteSlaveThread(slaveA, assignedJobsA);
			MasterWriteSlaveThread mwtSlaveB = new MasterWriteSlaveThread(slaveB, assignedJobsB);

			// threads to read from slaves
			MasterReadSlaveThread mrtSlaveA = new MasterReadSlaveThread(slaveA, completedJobs, master);
			MasterReadSlaveThread mrtSlaveB = new MasterReadSlaveThread(slaveB, completedJobs, master);

			// threads to write to client
			MasterWriteMain mwtClient1 = new MasterWriteMain(client1, client2, completedJobs);
			MasterWriteMain mwtClient2 = new MasterWriteMain(client1, client2, completedJobs);

			mrtClient1.start();
			mrtClient2.start();
			mwtSlaveA.start();
			mwtSlaveB.start();
			mrtSlaveA.start();
			mrtSlaveB.start();
			mwtClient1.start();
			mwtClient2.start();

			mrtClient1.join();

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
			System.exit(1);
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	public Master() {
		timeA = 0;
		timeB = 0;
	}

	public void removeTime(Job job) {
		if (job.getSlaveAssigned() == JobType.A) {
			if (job.getType().equals(JobType.A)) {
				timeA -= 2;
			} else {
				timeB -= 10;
			}
		} else {
			if (job.getType() == JobType.B) {
				timeB -= 2;
			} else {
				timeA -= 10;
			}
		}
	}

	public void assignTask(Job job) {

		switch (job.getType()) {
		case A:
			if (timeA + 2 <= timeB + 10) {
				// assign to timeA
				timeA += 2;
				job.setSlaveAssigned(JobType.A);
			} else {
				// assign to timeB
				timeB += 10;
				job.setSlaveAssigned(JobType.B);
			}
			break;
		case B:
			if (timeB + 2 <= timeA + 10) {
				// assign to timeB
				timeB += 2;
				job.setSlaveAssigned(JobType.B);
			} else {
				// assign to timeA
				timeA += 10;
				job.setSlaveAssigned(JobType.A);
			}
			break;
		}

	}
}

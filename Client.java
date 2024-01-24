package semesterProject;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) throws IOException {

		boolean cont = true;
		ArrayList<Job> jobs = new ArrayList<Job>();
		Integer id = 1;
		ArrayList<Integer> ids = new ArrayList<Integer>();
		try {

			// client socket to connect with master
			Socket client = new Socket("127.0.0.1", 7777);

			// thread to write to Master
			MainWriteThread wt = new MainWriteThread(client, jobs);
			// Thread to read from master
			MainReadThread rt = new MainReadThread(client);
			wt.start();
			rt.start();

			while (cont) {
				Scanner keyboard = new Scanner(System.in);
				System.out.println("Enter a job type: A or B: ");
				char job = keyboard.next().toUpperCase().charAt(0);

				while (job != 'A' && job != 'B') {
					System.out.println("Invalid entry. Enter a job type: A or B: ");
					job = keyboard.next().toUpperCase().charAt(0);
				}

				System.out.println("Enter a unique ID number: ");
				boolean tryAgain;
				id = 0;
				do {
					tryAgain = false;
					try {
						id = keyboard.nextInt();
						keyboard.nextLine();
						while(ids.contains(id)) {
							System.out.println("ID numbers must be unique.");
							System.out.println("Enter a new ID number: ");
							id=keyboard.nextInt();
							keyboard.nextLine();
						}
						ids.add(id);
					} catch (InputMismatchException e) {
						tryAgain = true;
						keyboard.nextLine();
						System.out.println("Enter a valid ID number");
					} 
				} while (tryAgain);

				Job newJob = new Job(job == 'A' ? JobType.A : JobType.B, id);

				System.out.println("Client received job " + newJob.getId() + " from user.");
				synchronized (jobs) {
					jobs.add(newJob);
				}
				keyboard.nextLine();
				System.out.println("Do you want to enter another job? Y/N: ");
				Character again = keyboard.nextLine().toUpperCase().charAt(0);

				while (!again.equals('Y') && !again.equals('N')) {
					System.out.println("Invalid entry. Do you want to enter another job? Y/N: ");
					again = keyboard.nextLine().toUpperCase().charAt(0);
				}

				if (again.equals('N')) {
					cont = false;
					System.out.println("Goodbye!");
					System.exit(0);
				}
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}

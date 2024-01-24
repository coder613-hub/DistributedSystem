package semesterProject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MainReadThread extends Thread {
	private Socket socket;
	private Job job;

	public MainReadThread(Socket socket) {
		this.socket = socket;
	}

	// accepts completed jobs from master
	@Override
	public void run() {
		try (BufferedReader responseReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
			String response;
			while (true) {
				if ((response = responseReader.readLine()) != null) {
					System.out.println(response);
				}else {
					Thread.sleep(1);
				}
			}

		} catch (Exception e) {

		}

	}
}

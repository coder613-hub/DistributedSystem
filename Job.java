package semesterProject;

import java.io.Serializable;

public class Job implements Serializable {
	private JobType type;
	private Integer id;
	private JobType slaveAssigned;
	private int clientNum;
	
	public Job(JobType type, Integer id) {
		
		this.type = type;
		this.id = id;
		
	}
	
	public void setClientNum(int clientNum) {
		this.clientNum = clientNum;
	}
	
	public int getClientNum() {
		return clientNum;
	}

	/**
	 * @return the type
	 */
	public JobType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(JobType type) {
		this.type = type;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * @return the slaveAssigned
	 */
	public JobType getSlaveAssigned() {
		return slaveAssigned;
	}
	
	/**
	 * @param slaveAssigned the slave to set
	 */
	public void setSlaveAssigned(JobType slaveAssigned) {
		this.slaveAssigned = slaveAssigned;
	}	
}

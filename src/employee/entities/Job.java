package employee.entities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Job {
	
	public String job_ID;
	public String job_title;
	float min_salary;
	float max_salary;
	
	public Job(String job_ID, String job_title, float min_salary, float max_salary) {
		this.job_ID = job_ID;
		this.job_title = job_title;
		this.min_salary = min_salary;
		this.max_salary = max_salary;
	}
	
	
	public static Job retrieveJob(String job_title, Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "select * from jobs where job_title = '"+job_title+"'";
		ResultSet rs = stmt.executeQuery(s);
		if(rs.next()) {
			return new Job(rs.getString("job_ID"), rs.getString("job_title"), rs.getFloat("min_salary"), rs.getFloat("max_salary"));
		}else {
			throw new Exception("No job found!");
		}
	}


	public static Job getJobById(String job_ID, Connection con ) throws Exception {
		Statement stmt = con.createStatement();
		String s = "select * from jobs where job_ID = '"+job_ID+"'";
		ResultSet rs = stmt.executeQuery(s);
		if(rs.next()) {
			Job j = new Job(rs.getString("job_ID"), rs.getString("job_title"), rs.getFloat("min_salary"), rs.getFloat("max_salary"));
			return j;
		}else {
			throw new Exception("No job found!");
		}
	}
	
	public void save(Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "insert into jobs (job_ID, job_title, min_salary, max_salary) values ('"+job_ID+"', '"+job_title+"', "+min_salary+", "+max_salary+")";
		stmt.executeUpdate(s);
	}


	public static void showAll(Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "select * from jobs";
		ResultSet rs = stmt.executeQuery(s);
		while(rs.next()) {
			System.out.println(rs.getString("job_ID")+rs.getString("job_title")+rs.getInt("min_salary")+rs.getInt("max_salary"));
		}
	}
	
	public static List<String> getAllJobs(Connection con) throws Exception{
		Statement stmt = con.createStatement();
		String s = "select * from jobs";
		ResultSet rs = stmt.executeQuery(s);
		List<String> jobList = new ArrayList<>();
		while(rs.next()) {
			jobList.add(rs.getString("job_title"));
		}
		return jobList;
	}

}

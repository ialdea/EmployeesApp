package employee.entities;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class Employee {
	
	int employee_ID;
	String first_name;
	String last_name;
	String email;
	String phone_number;
	Date hire_date; 
	Job job;
	float salary;
	int commission_pct;
	Departament departament;
	Location location;
	
	public void setJob(Job j) {job = j;}
	public void setDepartament(Departament departament) {this.departament = departament;}
	
	public Employee (int employee_ID, String first_name, String last_name, String email, String phone_number, Date hire_date, float salary, 
			int commission_pct) {
		this.employee_ID = employee_ID;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.phone_number = phone_number;
		this.hire_date = hire_date;
		this.salary = salary; 
		this.commission_pct = commission_pct;
	}
	
	public Employee(int employee_ID, String first_name, String last_name, String email, String phone_number, Date hire_date, String job_title, float salary,
			int commission_pct, String departament_name, int location_ID, Connection con) throws Exception {
		this.employee_ID = employee_ID;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.phone_number = phone_number;
		this.hire_date = hire_date;
		this.job = Job.retrieveJob(job_title, con);
		this.salary = salary;
		this.commission_pct = commission_pct;
		this.departament = Departament.retrieveDepartament(departament_name, location_ID, con);
		this.location = Location.getLocationById(location_ID, con);
	}
	
	public void save(Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String hireDateString = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(this.hire_date.getTime()));
		String s = "insert into employees (employee_ID, first_name, last_name, email, phone_number, hire_date, job_ID, salary, commission_pct, "
				+ "departament_ID) values ("+employee_ID+", '"+first_name+"', '"+last_name+"', '"+email+"', '"+phone_number+"', '"+hireDateString+"', '"
				+job.job_title+"', "+salary+", "+commission_pct+", "+departament.departament_ID+")";
		stmt.executeUpdate(s);
	}
	
	public static void showAll (Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "select * from emploees";
		ResultSet rs = stmt.executeQuery(s);
		while(rs.next()) {
			System.out.println(rs.getInt("employee_ID")+rs.getString("first_name")+rs.getString("last_name")+rs.getString("email")
			+rs.getString("phone_number")+rs.getDate("hire_date")+rs.getString("job_ID")+rs.getInt("salary")+rs.getInt("commission_pct")
			+rs.getInt("departament_ID"));
		}
	}
	
	public static Employee retrieveEmployee(String first_name, String last_name, Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "select * from employees where first_name = '"+first_name+"' and last_name = '"+last_name+"'";
		ResultSet rs = stmt.executeQuery(s);
		if(rs.next()) {
			Employee e = new Employee(rs.getInt("employee_ID"), rs.getString("first_name"), rs.getString("last_name"),
					rs.getString("email"), rs.getString("phone_number"), rs.getDate("hire_date"), rs.getFloat("salary"), rs.getInt("commission_pct"));
			Job j = Job.getJobById(rs.getString("job_ID"), con);
			Departament d = Departament.getDepartamentById(rs.getInt("departament_ID"), con);
			e.setJob(j);
			e.setDepartament(d);
			return e;
		}else {
			throw new Exception("No employee found!");
		}
	}
	
//	public static void showAllForEmplApp(Connection con) throws Exception {
//		Statement stmt = con.createStatement();
//		String s = "select first_name, last_name, salary, hire_date, job_title, departament_name from employees";
//		ResultSet rs = stmt.executeQuery(s);
//		while(rs.next()) {
//			String firstName = rs.getString("first_name");
//			String lastName = rs.getString("last_name");
//			float salary = rs.getFloat("salary");
//			Date hireDate = rs.getDate("hire_date");
//			Job j = Job.getJobById(rs.getString("job_ID"), con);
//			Departament d = Departament.getDepartamentById(rs.getInt("departament_ID"), con);
//			System.out.println(firstName+" "+lastName+" "+salary+" "+hireDate+" "+j.job_title+" "+d.departament_name);
//		}
//	}

}

package employee.entities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Departament {
	
	public int departament_ID;
	public String departament_name;
	int manager_ID;
	Location location;
	
	public void setLocation(Location l) {location = l;}
	
	public Departament(int departament_ID, String departament_name, int manager_ID, String street_address, String postal_code, String city, String state_province,
			Connection con) throws Exception {
		this.departament_ID = departament_ID;
		this.departament_name = departament_name;
		this.manager_ID = manager_ID;
		this.location = Location.retrieveLocation(street_address, postal_code, city, state_province, con);
	}
	
	public Departament (int departament_ID, String departament_name, int manager_ID) {
		this.departament_ID = departament_ID;
		this.departament_name = departament_name;
		this.manager_ID = manager_ID;
	}
	
	public void save(Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "insert into departaments (departament_ID, departament_name, manager_ID, location_ID) values ("+departament_ID+", '"+departament_name+
				"', "+manager_ID+", "+location.location_ID+")";
		stmt.executeUpdate(s);
	}
	
	public static Departament retrieveDepartament (String departament_name, int location_ID, Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "select * from departaments where departament name = '"+departament_name+"' and location_ID = "+location_ID;
		ResultSet rs = stmt.executeQuery(s);
		if(rs.next()) {
			Departament d = new Departament(rs.getInt("departament_ID"), rs.getString("departament_name"), rs.getInt("manager_ID"));
			Location l = Location.getLocationById(rs.getInt("location_ID"), con);
			d.setLocation(l);
			return d;
		}else {
			throw new Exception("No departament found!");
		}
	}

	public static Departament getDepartamentById(int departament_ID, Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "select * from departaments where departament_ID = "+departament_ID;
		ResultSet rs = stmt.executeQuery(s);
		if(rs.next()) {
			Departament d = new Departament(rs.getInt("departament_ID"), rs.getString("departament_name"), rs.getInt("manager_ID"));
			Location l = Location.getLocationById(rs.getInt("location_ID"), con);
			d.setLocation(l);
			return d;
		}else {
			throw new Exception("No departament found");
		}
		
	}

	public static void showAll(Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "select * from departaments";
		ResultSet rs = stmt.executeQuery(s);
		while(rs.next()) {
			System.out.println(rs.getInt("departament_ID")+rs.getString("departament_name")+rs.getInt("manager_ID")+rs.getInt("location_ID"));
		}
	}
	
	public static List<String> getAllDepartaments(Connection con) throws Exception{
		Statement stmt = con.createStatement();
		String s = "select departament_name, departament_ID from departaments";
		ResultSet rs = stmt.executeQuery(s);
		List<String> list = new ArrayList<>();
		while(rs.next()) {
			list.add(rs.getString("departament_name")+", "+rs.getInt("departament_ID"));
		}
		return list;
	}

}

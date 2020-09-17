package employee.entities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Region {
	
	public int region_ID;
	public String region_name;
	
	public Region(int region_ID, String region_name) {
		this.region_ID = region_ID;
		this.region_name =region_name;
	}
	
	public void save(Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = ("insert into regions (region_ID, region_name) values ("+region_ID+", '"+region_name+"')");
		System.out.println(s);
		stmt.executeUpdate(s);
	}
	
	public static void showAll (Connection con) throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from regions");
		while(rs.next()) {
			System.out.println(rs.getInt("region_ID")+rs.getString("region_name"));
		}
	}
	
	public static Region retrieveRegion(String region_name, Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "select * from regions where region_name = '"+region_name+"'";
		ResultSet rs = stmt.executeQuery(s);
		if(rs.next()) {
			return new Region(rs.getInt("region_ID"), rs.getString("region_name"));
		}else {
			throw new Exception("No region found!");
		}
		
	}

	public static Region getRegionById(int region_ID, Connection con) throws Exception{
		Statement stmt = con.createStatement();
		String s = "select * from regions where region_ID = "+region_ID+"";
		ResultSet rs = stmt.executeQuery(s);
		if(rs.next()) {
			return new Region(rs.getInt("region_ID"), rs.getString("region_name"));
		}else {
			throw new Exception("No region found!");
		}
	}
	
	public String toString() {
		return region_ID+" "+region_name;
	}

}

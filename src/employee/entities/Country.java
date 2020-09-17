package employee.entities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Country {

	String country_ID;
	String country_name;
	Region region;
	
//	public Country(String country_ID, String country_name, Region region) {
//		this.country_ID = country_ID;
//		this.country_name = country_name;
//		this.region = region;
//	}
	
	public void setRegion(Region r) { region = r;}
	
	public Country(String country_ID, String country_name) {
		this.country_ID = country_ID;
		this.country_name = country_name;
	}
	
	public Country(String country_ID, String country_name, String region_name, Connection con) throws Exception {
		this.country_ID = country_ID;
		this.country_name = country_name;
		this.region = Region.retrieveRegion(region_name, con);
	}
	
	public void save(Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "insert into countries (country_ID, country_name, region_ID) values ('"+country_ID+"', '"+country_name+"', "+region.region_ID+")";
		stmt.executeUpdate(s);
	}
	
	public static Country retrieveCountry(String country_name, Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "select * from countries where country_name = '"+country_name+"'";
		ResultSet rs = stmt.executeQuery(s);
		if(rs.next()) {
			Country c = new Country(rs.getString("country_ID"), rs.getString("country_name"));
			Region r = Region.getRegionById(rs.getInt("region_ID"), con);
			c.setRegion(r);
			return c;
		}else {
			throw new Exception("No country found");
		}
	}

	public static Country getCountryById(String country_ID, Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "select * from countries where country_ID = '"+country_ID+"'";
		ResultSet rs = stmt.executeQuery(s);
		if(rs.next()) {
			Country c = new Country(rs.getString("country_ID"), rs.getString("country_name"));
			Region r = Region.getRegionById(rs.getInt("region_ID"), con);
			c.setRegion(r);
			return c;
		}else {
			throw new Exception("No country found");
		}
	}
	
	public String toString() {
		return country_ID+" "+country_name+" "+" "+ this.region.toString();
	}
	
	public static void showAll(Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "select * from countries";
		ResultSet rs = stmt.executeQuery(s);
		while(rs.next()) {
			System.out.println(rs.getString("country_ID")+rs.getString("country_name")+rs.getInt("region_ID"));
		}
	}
}

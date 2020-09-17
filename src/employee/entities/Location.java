package employee.entities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Location {
	
	int location_ID;
	String street_address;
	String postal_code;
	String city;
	String state_province;
	Country country;
	
	public void setCountry(Country c) {country = c;}
	
	public Location(int location_ID, String street_address, String postal_code, String city, String state_province, String country_name, Connection con) throws Exception {
		this.location_ID = location_ID;
		this.street_address = street_address;
		this.postal_code = postal_code;
		this.city = city;
		this.state_province = state_province;
		this.country = Country.retrieveCountry(country_name, con);
	}
	
	public Location(int location_ID, String street_address, String postal_code, String city, String state_province) {
		this.location_ID = location_ID;
		this.street_address = street_address;
		this.postal_code = postal_code;
		this.city = city;
		this.state_province = state_province;
	}
	
	public void save(Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "insert into locations (location_ID, street_address, postal_code, city, state_province, country_ID) values ("+location_ID+", '"
				+street_address+"', '"+postal_code+"', '"+city+"', '"+state_province+"', '"+country.country_ID+"')";
		stmt.executeUpdate(s);
	}
	
	public static Location retrieveLocation(String street_address, String postal_code, String city, String state_province, Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "select * from locations where street_address = '"+street_address+"' and postal_code = '"+postal_code+"' and city = '"+
				city+"' and state_province = '"+state_province+"'";
		ResultSet rs = stmt.executeQuery(s);
		if(rs.next()) {
			Location l = new Location(rs.getInt("location_ID"), rs.getString("street_address"), rs.getString("postal_code"),
					rs.getString("city"), rs.getString("state_province"));
			Country c = Country.getCountryById(rs.getString("country_ID"), con);
			l.setCountry(c);
			return l;
		}else {
			throw new Exception("No location found");
		}
	}
	
	public static void showAll(Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "select * from locations";
		ResultSet rs = stmt.executeQuery(s);
		while(rs.next()) {
//			System.out.println(rs.getInt("location_ID")+rs.getString("street_address")+rs.getString("postal_code")+rs.getString("city")+
//					rs.getString("state_province")+rs.getString("country_ID"));
			Location l = getLocationById(rs.getInt("location_ID"), con);
			System.out.println(l);
		}
	}

	public static Location getLocationById(int location_ID, Connection con) throws Exception {
		Statement stmt = con.createStatement();
		String s = "select * from locations where location_ID = "+location_ID;
		ResultSet rs = stmt.executeQuery(s);
		if(rs.next()) {
			Location l = new Location(rs.getInt("location_ID"), rs.getString("street_address"), rs.getString("postal_code"), rs.getString("city"), 
					rs.getString("state_province"));
			Country c = Country.getCountryById(rs.getString("country_ID"), con);
			l.setCountry(c);
			return l;
		}else {
			throw new Exception("No location found!");
		}
		
	}
	
	public String toString() {
		return location_ID+" "+street_address+" "+" "+postal_code + this.country.toString();
	}

}

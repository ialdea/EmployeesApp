package employee;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import employee.entities.Departament;
import employee.entities.Employee;
import employee.entities.Job;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.Font;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;

public class EmployeesApp {

	private JFrame frame;
	private JTextField textField;
	private JTable table;
	private JTextField textFieldFirstName;
	private JTextField textFieldHireDate;
	private JTextField textFieldLastName;
	private JTextField textFieldSalary;
	private Connection con;
	private JTextField textFieldFirstNameForDel;
	private JTextField textFieldLastNameToDel;
	private List<Object[]> modelRows;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmployeesApp window = new EmployeesApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws Exception 
	 */
	public EmployeesApp() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbemployee", "root", "root");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws Exception 
	 */
	private void initialize() throws Exception {
		frame = new JFrame();
		frame.setBounds(100, 100, 614, 590);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Employees", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 578, 187);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Filter by Name");
		lblNewLabel.setBounds(10, 21, 103, 20);
		panel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(96, 21, 132, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnViewEmplByName = new JButton("View Employee by Name");
		btnViewEmplByName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = new DefaultTableModel();		
				model.addColumn("first_name");
				model.addColumn("last_name");
				model.addColumn("hire_date");
				model.addColumn("salary");
				model.addColumn("departament_name");
				model.addColumn("job_title");
				try {
					Statement stmt = con.createStatement();
					String s = "select * from employees where first_name = '"+textField.getText()+"'";
					ResultSet rs = stmt.executeQuery(s);
					while(rs.next()) {
						String firstName = rs.getString("first_name");
						String lastName = rs.getString("last_name");
						float salary = rs.getFloat("salary");
						Date hireDate = rs.getDate("hire_date");
						Job j = Job.getJobById(rs.getString("job_ID"), con);
						Departament d = Departament.getDepartamentById(rs.getInt("departament_ID"), con);
						System.out.println(firstName+" "+lastName+" "+salary+" "+hireDate+" "+j.job_title+" "+d.departament_name);
					
						Object[] obj = {firstName, lastName, salary, hireDate, j.job_title, d.departament_name};
						model.addRow(obj);
					}
					table.setModel(model);
					
				}catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnViewEmplByName.setBounds(238, 20, 165, 23);
		panel.add(btnViewEmplByName);
		
		JButton btnViewAllEmpl = new JButton("View All Employees");
		btnViewAllEmpl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modelRows = new ArrayList<Object[]>();
				DefaultTableModel model = new DefaultTableModel();		//sau (DefaultTableModel) table.getModel();
				model.addColumn("first_name");
				model.addColumn("last_name");
				model.addColumn("salary");
				model.addColumn("hire_date");
				model.addColumn("job_title");
				model.addColumn("departament_name");
				
				try {
					Statement stmt = con.createStatement();
					String s = "select employee_ID, first_name, last_name, salary, hire_date, job_ID, departament_ID from employees";
					ResultSet rs = stmt.executeQuery(s);
					while(rs.next()) {
						String firstName = rs.getString("first_name");
						String lastName = rs.getString("last_name");
						float salary = rs.getFloat("salary");
						Date hireDate = rs.getDate("hire_date");
						Job j = Job.getJobById(rs.getString("job_ID"), con);
						Departament d = Departament.getDepartamentById(rs.getInt("departament_ID"), con);
//						System.out.println(firstName+" "+lastName+" "+salary+" "+hireDate+" "+j.job_title+" "+d.departament_name);
						
						Object[] obj = {firstName, lastName, salary, hireDate, j.job_title, d.departament_name};
						model.addRow(obj);
						
						Object[] copy = new Object[obj.length+3];
						for(int i=0; i<obj.length; i++) {
							copy[i] = obj[i];
						}
						copy[copy.length - 3]=rs.getInt("employee_ID");
						copy[copy.length-2] = j.job_ID;
						copy[copy.length -1 ] = d.departament_ID;
						modelRows.add(copy);
					}
					
					table.setModel(model);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnViewAllEmpl.setBounds(413, 20, 155, 23);
		panel.add(btnViewAllEmpl);
		
		table = new JTable();
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.setBounds(10, 52, 558, 94);
		panel.add(table);
		
		JButton btnNewButton_2 = new JButton("Update Employee");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowCount = table.getModel().getRowCount();
				for(int i=0;i<rowCount; i++) {
					for(int j=0; j<table.getModel().getColumnCount(); j++) {
						Object s = table.getModel().getValueAt(i, j) ;
						Object[] oldRow = modelRows.get(i);
						Object oldValue = oldRow[j] ;
						if((s == null && oldValue != null) || (s!=null && !s.equals(oldValue))){
							System.out.println(s+" value modified for emp id: "+oldRow[oldRow.length-3]);
							String columnName = table.getModel().getColumnName(j);
							String sql = "UPDATE employees set "+columnName+" = ";//;
							if(columnName.equals("salary")) {
								sql+=s+" where employee_ID="+oldRow[oldRow.length-3];
							}else if(columnName.equals("hire_date")) {
								sql+="'"+s+"' where employee_ID="+oldRow[oldRow.length-3];
							}else if(columnName.equals("first_name") || columnName.equals("last_name")) {
								sql+="'"+s+"' where employee_ID="+oldRow[oldRow.length-3];
							}else if(columnName.equals("job_title")) {
								Job nJob = null;
								try {
									nJob = Job.retrieveJob(s+"", con);
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								sql = "UPDATE employees set job_ID = '"+nJob.job_ID+"' where employee_ID="+oldRow[oldRow.length-3];
							}else{
								System.out.println("Cannot modify this column");
							}
									
							try {
								con.createStatement().executeUpdate(sql);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			}
		});
		btnNewButton_2.setBounds(413, 157, 155, 23);
		panel.add(btnNewButton_2);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Insert", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(10, 204, 578, 156);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Departament");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel_1.setBounds(10, 27, 77, 23);
		panel_1.add(lblNewLabel_1);
		
		JComboBox comboBoxDep = new JComboBox();
		List<String> list = Departament.getAllDepartaments(con);
		comboBoxDep.setModel(new DefaultComboBoxModel( list.toArray(new String[0]) ) );
		comboBoxDep.setToolTipText("");
		comboBoxDep.setBounds(83, 27, 195, 22);
		panel_1.add(comboBoxDep);
		
		JLabel lblNewLabel_2 = new JLabel("First Name");
		lblNewLabel_2.setBounds(10, 64, 63, 14);
		panel_1.add(lblNewLabel_2);
		
		textFieldFirstName = new JTextField();
		textFieldFirstName.setBounds(83, 61, 195, 20);
		panel_1.add(textFieldFirstName);
		textFieldFirstName.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Hire Date");
		lblNewLabel_3.setBounds(10, 95, 63, 14);
		panel_1.add(lblNewLabel_3);
		
		textFieldHireDate = new JTextField();
		textFieldHireDate.setBounds(83, 92, 195, 20);
		panel_1.add(textFieldHireDate);
		textFieldHireDate.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Job Title");
		lblNewLabel_4.setBounds(288, 31, 57, 14);
		panel_1.add(lblNewLabel_4);
		
		JComboBox comboBoxJob = new JComboBox();
		List<String> jobList = Job.getAllJobs(con);
		comboBoxJob.setModel(new DefaultComboBoxModel( jobList.toArray(new String[0]) ) );
		comboBoxJob.setBounds(350, 27, 207, 22);
		panel_1.add(comboBoxJob);
		
		JLabel lblNewLabel_5 = new JLabel("Last Name");
		lblNewLabel_5.setBounds(288, 64, 69, 14);
		panel_1.add(lblNewLabel_5);
		
		textFieldLastName = new JTextField();
		textFieldLastName.setBounds(350, 61, 207, 20);
		panel_1.add(textFieldLastName);
		textFieldLastName.setColumns(10);
		
		JLabel lblNewLabel_6 = new JLabel("Salary");
		lblNewLabel_6.setBounds(288, 95, 46, 14);
		panel_1.add(lblNewLabel_6);
		
		textFieldSalary = new JTextField();
		textFieldSalary.setBounds(350, 92, 207, 20);
		panel_1.add(textFieldSalary);
		textFieldSalary.setColumns(10);
		
		JButton btnNewButton = new JButton("Insert Employee");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Statement stmt = con.createStatement();
					Job j = Job.retrieveJob(String.valueOf(comboBoxJob.getSelectedItem()), con);
					String selectedDept = comboBoxDep.getSelectedItem()+"";
					int departament_ID = Integer.parseInt(selectedDept.split(", ")[1]);
					Departament d = Departament.getDepartamentById(departament_ID, con);
					String s = "insert into employees (first_name, last_name, salary, hire_date, job_ID, departament_ID) values ('"+
							textFieldFirstName.getText()+"', '"+textFieldLastName.getText()+"', "+textFieldSalary.getText()+", '"+textFieldHireDate.getText()+"', '"+
							j.job_ID+"', "+d.departament_ID+")";
					stmt.executeUpdate(s);
					
					JOptionPane.showMessageDialog(null, "Insert OK!");
				}catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(428, 122, 129, 23);
		panel_1.add(btnNewButton);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Delete", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(20, 371, 568, 156);
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblNewLabel_7 = new JLabel("First Name");
		lblNewLabel_7.setBounds(10, 61, 63, 14);
		panel_2.add(lblNewLabel_7);
		
		textFieldFirstNameForDel = new JTextField();
		textFieldFirstNameForDel.setBounds(83, 58, 180, 20);
		panel_2.add(textFieldFirstNameForDel);
		textFieldFirstNameForDel.setColumns(10);
		
		JLabel lblNewLabel_8 = new JLabel("Last Name");
		lblNewLabel_8.setBounds(273, 61, 68, 14);
		panel_2.add(lblNewLabel_8);
		
		textFieldLastNameToDel = new JTextField();
		textFieldLastNameToDel.setBounds(340, 58, 199, 20);
		panel_2.add(textFieldLastNameToDel);
		textFieldLastNameToDel.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Delete Employee");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Statement stmt = con.createStatement();
					String s = "delete from employees where first_name = '"+textFieldFirstNameForDel.getText()+"' and last_name ='"+
							textFieldLastNameToDel.getText()+"'";
					stmt.executeUpdate(s);
					
					JOptionPane.showConfirmDialog(null, "Do you want to delete the employee "+textFieldFirstNameForDel.getText()+
							" "+textFieldLastNameToDel.getText()+"?");
					
				}catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnNewButton_1.setBounds(418, 103, 123, 23);
		panel_2.add(btnNewButton_1);
	}
}

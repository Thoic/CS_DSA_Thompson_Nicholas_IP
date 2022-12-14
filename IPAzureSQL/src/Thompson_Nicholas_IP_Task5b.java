import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Thompson_Nicholas_IP_Task5b {

    // Database credentials
    final static String HOSTNAME = "thom0414-sql-server.database.windows.net";
    final static String DBNAME = "cs-dsa-4513-sql-db";
    final static String USERNAME = "thom0414";
    final static String PASSWORD = "IHateSql!!!";

    // Database connection string
    final static String URL = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
            HOSTNAME, DBNAME, USERNAME, PASSWORD);

    // Query templates
    final static String QUERY_TEMPLATE_1 = "EXEC ip_query1 @type = ?, @name = ?, @address = ?, @salary = ?, @product_type = ?, @max_products_per_day = ?, @technical_position = ?, @degrees = ?";

    final static String QUERY_TEMPLATE_2 = "EXEC ip_query2 @type = ?, @product_id = ?, @size = ?, @software_name = ?, @color = ?, @weight = ?, @wname = ?, @prod_date = ?, @prod_dur = ?, @qname = ?, @sname = ?, @repair_date = ?, @complaint_id = ?";
    
    final static String QUERY_TEMPLATE_3 = "EXEC ip_query3 @cname = ?, @address = ?, @product_ids = ?";
    
    final static String QUERY_TEMPLATE_4 = "EXEC ip_query4 @account_no = ?, @date_established = ?, @cost = ?, @product_id = ?";
    
    final static String QUERY_TEMPLATE_5 = "EXEC ip_query5 @complaint_id = ?, @description = ?, @treatment_expected = ?, @product_id = ?, @date = ?";
    
    final static String QUERY_TEMPLATE_6 = "EXEC ip_query6 @accident_no = ?, @days_lost = ?, @product_id = ?, @date = ?";
    
    final static String QUERY_TEMPLATE_7 = "EXEC ip_query7 @product_id = ?";
    
    final static String QUERY_TEMPLATE_8 = "EXEC ip_query8 @wname = ?";
    
    final static String QUERY_TEMPLATE_9 = "EXEC ip_query9 @qname = ?";
    
    final static String QUERY_TEMPLATE_10 = "EXEC ip_query10 @qname = ?";
    
    final static String QUERY_TEMPLATE_11 = "EXEC ip_query11 @color = ?";
    
    final static String QUERY_TEMPLATE_12 = "EXEC ip_query12 @salary = ?";
    
    final static String QUERY_TEMPLATE_13 = "EXEC ip_query13";
    
    final static String QUERY_TEMPLATE_14 = "EXEC ip_query14 @year = ?";
    
    final static String QUERY_TEMPLATE_15 = "EXEC ip_query15 @date_begin = ?, @date_end = ?";

    // User input prompt//
    final static String PROMPT = 
            "\nWELCOME TO THE DATABASE SYSTEM OF MyProducts, Inc. \n" +
            "(1)  Enter a new employee; \n" + 
            "(2)  Enter a new product associated with the person who made the product, repaired the product if it is repaired, or checked the product; \n" + 
            "(3)  Enter a customer associated with some products; \n" +
            "(4)  Create a new account associated with a product; \n" +
            "(5)  Enter a complaint associated with a customer and product; \n" +
            "(6)  Enter an accident associated with an appropriate employee and product; \n" +
            "(7)  Retrieve the date produced and time spent to produce a particular product; \n" +
            "(8)  Retrieve all products made by a particular worker; \n" +
            "(9)  Retrieve the total number of errors a particular quality controller made. This is the total number \n"
          + "     of products certified by this controller and got some complaints; \n" +
            "(10) Retrieve the total costs of the products in the product3 category which were repaired at the \n"
          + "     request of a particular quality controller; \n" +
            "(11) Retrieve all customers (in name order) who purchased all products of a particular color; \n" +
            "(12) Retrieve all employees whose salary is above a particular salary; \n" +
            "(13) Retrieve the total number of workdays lost due to accidents in repairing the products which got complaints; \n" +
            "(14) Retrieve the average cost of all products made in a particular year; \n" +
            "(15) Delete all accidents whose dates are in some range; \n" +
            "(16) Import: enter new employees from a data file until the file is empty (the user must be asked to enter the input file name); \n" +
            "(17) Export: Retrieve all customers (in name order) who purchased all products of a particular color and output them to a datafile \n" +
            "     instead of screen (the user must be asked to enter the output file name); \n" +
            "(18) Quit.";

    public static void main(String[] args) throws SQLException {

        System.out.println("Welcome to the sample application!");

        final Scanner sc = new Scanner(System.in); // Scanner is used to collect the user input
        String option = ""; // Initialize user option selection as nothing
        while (!option.equals("18")) { // As user for options until option 18 is selected
            System.out.println(PROMPT); // Print the available options
            option = sc.nextLine(); // Read in the user option selection

            switch (option) { // Switch between different options
                case "1": {// Insert a new employee option
                	// Select type of employee to insert
                	System.out.println("Please select which type of employee to insert. \n" +
                						"(1) Quality Controller \n" +
                						"(2) Worker \n" +
                						"(3) Technical Staff");
                	final int type = sc.nextInt();
                	
                    // Collect the new employee data from the user
                    System.out.println("Please enter the unique employee name:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                    sc.nextLine();
                    final String name = sc.nextLine(); // Read in the user input of employee name

                    System.out.println("Please enter the employee address:");
                    final String address = sc.nextLine(); // Read in user input of employee address

                    System.out.println("Please enter employee salary:");
                    // No need to call nextLine extra time here, because the preceding nextLine consumed the newline character.
                    final float salary = sc.nextFloat(); // Read in user input of employee salary
                    sc.nextLine(); //consume newline

                    String product_type = "";
                    int max_products_per_day = -1;
                    String degrees = "";
                    String technical_position = "";
                    //employee type is quality controller
                    if (type == 1) {
                    	System.out.println("Please enter the product type the employee will check:");
                    	product_type = sc.nextLine(); //read qc product_type
                    } 
                    //employee type is worker
                    else if (type == 2) {
                    	System.out.println("Please enter the maximum number of products the employee will make per day:");
                    	max_products_per_day = sc.nextInt(); //read worker max_products
                    	sc.nextLine();
                    }
                    //employee type is technical staff
                    else if (type == 3) {
                    	System.out.println("Please enter the degree(s) the employee has obtained, if any, separated by commas (BS,MS,PhD):");
                    	degrees = sc.nextLine();
                    	System.out.println("Please enter the technical position of the employee:");
                    	technical_position = sc.nextLine();
                    }

                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_1)) {
                            // Populate the query template with the data collected from the user
                        	//"EXEC ip_query1 @type = ?, @name = ?, @address = ?, @salary = ?, @product_type = ?, @max_products_per_day = ?, @technical_position = ?, @degrees = ?";
                            statement.setInt(1, type);
                            statement.setString(2, name);
                            statement.setString(3, address);
                            statement.setFloat(4, salary);
                            statement.setString(5, product_type);
                            statement.setInt(6, max_products_per_day);
                            statement.setString(7, technical_position); 
                            statement.setString(8, degrees);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        } catch (SQLServerException e) {
                        	e.printStackTrace(); //print the stack trace of any sql errors
                        }
                    }

                    break;
                }
                case "2": { //Insert a new product option
                	// Select type of product to insert
                	System.out.println("Please select which type of product to insert. \n" +
                						"(1) Product1 \n" +
                						"(2) Product2 \n" +
                						"(3) Product3");
                	final int type = sc.nextInt();
                	sc.nextLine(); //consume the newline
                	
                    // Collect the new product data from the user
                    System.out.println("Please enter the unique Product ID:");

                    final int product_id = sc.nextInt(); // Read in the user input of product_id
                    sc.nextLine(); //consume the newline

                    System.out.println("Please enter the product size (small, medium, large):");
                    final String size = sc.nextLine(); // Read in user input of employee address
                    
                    System.out.println("Please enter the name of the Worker who created the product:");
                    final String wname = sc.nextLine();
                    
                    System.out.println("Please enter the production date of the product (yyyy-mm-dd):");
                    final String prod_date_string = sc.nextLine(); //read in the date of production from the user as string
                    final Date prod_date = Date.valueOf(prod_date_string); //conver the user date to a sql date
                    
                    System.out.println("Please enter the time spent to make the product (in hours):");
                    final float prod_dur = sc.nextFloat(); //read in the production duration
                    sc.nextLine();
                    
                    System.out.println("Please enter the name of the Quality Controller who checked the product:");
                    final String qname = sc.nextLine(); //read in the quality controller
                    
                    System.out.println("Please enter the name of the Technical Staff that repaired the product, if it was repaired:");
                    final String sname = sc.nextLine(); //read in the technical staff, (or empty string if not repaired)
                    
                    
                    int complaint_id = -1; //initialize the complaint_id in this scope
                    Date repair_date = null;
                    if (sname != "") { // if a name of a technical staff was given
                    	System.out.println("Why was the product repaired? \n" +
                    						"(1) It was repaired at the request of the Quality Controller \n" +
                    						"(2) It was because the product received a complaint by the Customer.");
                    	final int repair_reason = sc.nextInt();
                    	sc.nextLine();
                    	
                    	//if the repair reason is 1, leave the complaint_id as -1
                    	if (repair_reason == 2) { //if the repair reason is 2, ask for the complaint id
                    		System.out.println("Please Enter the ID of the complaint that triggered the repair:");
                    		complaint_id = sc.nextInt(); //read in the complaint id from the user
                    		sc.nextLine(); //read the newline char
                    	}
                    	
                    	System.out.println("Please enter the date of repair (yyyy-mm-dd):");
                    	final String repair_date_string = sc.nextLine();
                    	repair_date = Date.valueOf(repair_date_string);
                    }

                    String software_name = "";
                    String color = "";
                    float weight = 0.0f;
                    //product type is product1
                    if (type == 1) {
                    	System.out.println("Please enter the software name used to create the product:");
                    	software_name = sc.nextLine(); //read product1 software_name
                    } 
                    //product type is product2
                    else if (type == 2) {
                    	System.out.println("Please enter the color of the product:");
                    	color = sc.nextLine(); //read product2 color
                    }
                    //product type is product3
                    else if (type == 3) {
                    	System.out.println("Please enter the weight of the product in lbs:");
                    	weight = sc.nextFloat(); //read product3 weight
                    	sc.nextLine();
                    }

                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_2)) {
                            // Populate the query template with the data collected from the user
                            statement.setInt(1, type);
                            statement.setInt(2, product_id);
                            statement.setString(3, size);
                            statement.setString(4, software_name);
                            statement.setString(5, color);
                            statement.setFloat(6, weight);
                            statement.setString(7, wname); 
                            statement.setDate(8, prod_date);
                            statement.setFloat(9, prod_dur);
                            statement.setString(10, qname);
                            statement.setString(11, sname);
                            statement.setDate(12, repair_date);
                            statement.setInt(13, complaint_id);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        }
                        catch (SQLServerException e) {
                        	e.printStackTrace(); //print the stack trace of any sql errors
                        }
                    }

                    break;
                }
                case "3": { //Insert a new customer option
                	System.out.println("Please enter the name of the Customer that purchased a product:");
                	final String cname = sc.nextLine(); //read customer name from user
                	
                	System.out.println("Please enter the address of the Customer:");
                	final String address = sc.nextLine(); // read customer addresss from user
                	
                	System.out.println("Please enter the IDs of the Products that the customer purchased separated by commas (1,2,3,...):");
                	final String product_ids = sc.nextLine(); //read product_ids from the user
                	
                	System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_3)) {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, cname);
                            statement.setString(2, address);
                            statement.setString(3, product_ids);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        } catch (SQLServerException e) {
                        	e.printStackTrace(); //print the stack trace of any sql errors
                        }
                    }
                	break;
                }
                case "4": { //insert a new account option
                	System.out.println("Please enter the account number for the new account:");
                	final int account_no = sc.nextInt(); //read the value for the account number
                	sc.nextLine();
                	
                	System.out.println("Please enter the product ID that this account is tracking the cost for:");
                	final int product_id = sc.nextInt(); //read the value for the product id
                	sc.nextLine();
                	
                	System.out.println("Please enter the date the account was established (yyyy-mm-dd):");
                	final String date_established_string = sc.nextLine(); //read the value of the date as a string
                	final Date date_established = Date.valueOf(date_established_string); //conver the date string to sql date
                	
                	System.out.println("Please enter the cost of the product this account is keeping track of:");
                	final float cost = sc.nextFloat(); //read the value of the cost from the user
                	sc.nextLine();
                	
                	System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_4)) {
                            // Populate the query template with the data collected from the user
                            statement.setInt(1, account_no);
                            statement.setDate(2, date_established);
                            statement.setFloat(3, cost);
                            statement.setInt(4, product_id);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        } catch (SQLServerException e) {
                        	e.printStackTrace(); //print the stack trace of any sql errors
                        }
                    }
                	break;
                }
                case "5": { // Enter a complaint associated with a customer and product
                	System.out.println("Please enter the ID of the complaint:");
                	final int complaint_id = sc.nextInt(); //read the id of the complaint from the user
                	sc.nextLine();
                	
                	System.out.println("Please enter the description of the complaint:");
                	final String description = sc.nextLine(); //read the value of the complaint description from the user
                	
                	System.out.println("Please enter the treatment expected by the customer:");
                	final String treatment_expected = sc.nextLine(); //read the value of the treatment expected from the user
                	
                	System.out.println("Please enter the ID of the product associated with this complaint:");
                	final int product_id = sc.nextInt();//read the value of the product id from the user
                	sc.nextLine();
                	
                	System.out.println("Please enter the date the complaint was made (yyyy-mm-dd):");
                	final String date_string = sc.nextLine();//read the value of the date as a string
                	final Date date = Date.valueOf(date_string); //convert the date string to a sql date
                	
                	
                	System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_5)) {
                            // Populate the query template with the data collected from the user
                            statement.setInt(1, complaint_id);
                            statement.setString(2, description);
                            statement.setString(3, treatment_expected);
                            statement.setInt(4, product_id);
                            statement.setDate(5, date);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        } catch (SQLServerException e) {
                        	e.printStackTrace(); //print the stack trace of any sql errors
                        }
                    }
                	break;
                }
                case "6": {//Enter an accident associated with an appropriate employee and product
                	System.out.println("Please enter the accident number of the accident:");
                	final int accident_no = sc.nextInt();//read the accident number from the user
                	sc.nextLine();
                	
                	System.out.println("Please enter the number of work days lost due to the accident:");
                	final int days_lost = sc.nextInt();//read the number of days lost from the user
                	sc.nextLine();
                	
                	System.out.println("Please enter the product ID that was being worked on/repaired when the accident occured:");
                	final int product_id = sc.nextInt();//read the value of the product id
                	sc.nextLine();
                	
                	System.out.println("Please enter the date of the accident (yyyy-mm-dd):");
                	final String date_string = sc.nextLine();//read the accident date from the user as a string
                	final Date date = Date.valueOf(date_string);//convert the accident date string to a sql date
                	
                	System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_6)) {
                            // Populate the query template with the data collected from the user
                            statement.setInt(1, accident_no);
                            statement.setInt(2, days_lost);
                            statement.setInt(3, product_id);
                            statement.setDate(4, date);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        } catch (SQLServerException e) {
                        	e.printStackTrace(); //print the stack trace of any sql errors
                        }
                    }
                	break;
                }
                case "7": { //Retrieve the date produced and time spent to produce a particular product
                	System.out.println("Please enter the ID of the product:");
                	final int product_id = sc.nextInt();//read the value of the product id from the user
                	sc.nextLine();
                	
                	System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_7)) {
                            // Populate the query template with the data collected from the user
                            statement.setInt(1, product_id);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final ResultSet resultSet = statement.executeQuery();
                            System.out.println("prod_date  | prod_dur");
                            while (resultSet.next()) {
                            	System.out.println(String.format("%s | %s", resultSet.getString(1), resultSet.getString(2)));
                            }
                        } catch (SQLServerException e) {
                        	e.printStackTrace(); //print the stack trace of any sql errors
                        }
                    }
                	break;
                }
                case "8": {//Retrieve all products made by a particular worker
                	System.out.println("Please enter the name of the worker:");
                	final String wname = sc.nextLine();//read wname from the user
                	
                	System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_8)) {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, wname);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final ResultSet resultSet = statement.executeQuery();
                            System.out.println("product_id");
                            while (resultSet.next()) {
                            	System.out.println(String.format("%s", resultSet.getString(1)));
                            }
                        } catch (SQLServerException e) {
                        	e.printStackTrace(); //print the stack trace of any sql errors
                        }
                    }
                	break;
                }
                case "9": {//Retrieve the total number of errors a particular quality controller made. This is the total number 
                			//of products certified by this controller and got some complaints
                	System.out.println("Please enter the name of the quality controller:");
                	final String qname = sc.nextLine();//read the quality controller name from the user
                	
                	System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_9)) {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, qname);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final ResultSet resultSet = statement.executeQuery();
                            System.out.println("Errors");
                            while (resultSet.next()) {
                            	System.out.println(String.format("%s", resultSet.getString(1)));
                            }
                        } catch (SQLServerException e) {
                        	e.printStackTrace(); //print the stack trace of any sql errors
                        }
                    }
                	break;
                }
                case "10": {//Retrieve the total costs  of the  products  in the product3 category which were repaired at the 
                			//request of a particular quality controller
                	System.out.println("Please enter name of the quality controller:");
                	final String qname = sc.nextLine();
                	
                	System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_10)) {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, qname);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final ResultSet resultSet = statement.executeQuery();
                            System.out.println("Total Costs");
                            while (resultSet.next()) {
                            	System.out.println(String.format("%s", resultSet.getString(1)));
                            }
                        } catch (SQLServerException e) {
                        	e.printStackTrace(); //print the stack trace of any sql errors
                        }
                    }
                	break;
                }
                case "11": {//Retrieve  all  customers  (in  name  order)  who  purchased  all  products  of  a  particular  color
                	System.out.println("Please enter the color to check:");
                	final String color = sc.nextLine();//read the color from the user
                	
                	System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_11)) {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, color);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final ResultSet resultSet = statement.executeQuery();
                            System.out.println("Customer Name");
                            while (resultSet.next()) {
                            	System.out.println(String.format("%s", resultSet.getString(1)));
                            }
                        } catch (SQLServerException e) {
                        	e.printStackTrace(); //print the stack trace of any sql errors
                        }
                    }
                	break;
                }
                case "12": {//Retrieve all employees whose salary is above a particular salary
                	System.out.println("Please enter the minimum salary to return:");
                	final float salary = sc.nextFloat();//read salary from the user
                	sc.nextLine();
                	
                	System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_12)) {
                            // Populate the query template with the data collected from the user
                            statement.setFloat(1, salary);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final ResultSet resultSet = statement.executeQuery();
                            System.out.println("Employee Name");
                            while (resultSet.next()) {
                            	System.out.println(String.format("%s", resultSet.getString(1)));
                            }
                        } catch (SQLServerException e) {
                        	e.printStackTrace(); //print the stack trace of any sql errors
                        }
                    }
                	break;
                }
                case "13": {//Retrieve the total number of workdays lost due to accidents in repairing the products which got 
                			//complaints
                	
                	System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_13)) {

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final ResultSet resultSet = statement.executeQuery();
                            System.out.println("Total Days Lost");
                            while (resultSet.next()) {
                            	System.out.println(String.format("%s", resultSet.getString(1)));
                            }
                        } catch (SQLServerException e) {
                        	e.printStackTrace(); //print the stack trace of any sql errors
                        }
                    }
                	break;                	
                }
                case "14": {//Retrieve the average cost of all products made in a particular year
                	System.out.println("Please enter the production year:");
                	final int year = sc.nextInt();//read the year from the user
                	sc.nextLine();
                	
                	System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_14)) {
                            // Populate the query template with the data collected from the user
                            statement.setInt(1, year);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final ResultSet resultSet = statement.executeQuery();
                            System.out.println("Average Cost");
                            while (resultSet.next()) {
                            	System.out.println(String.format("%s", resultSet.getString(1)));
                            }
                        } catch (SQLServerException e) {
                        	e.printStackTrace(); //print the stack trace of any sql errors
                        }
                    }
                	break;
                	
                }
                case "15": {//Delete all accidents whose dates are in some range
                	System.out.println("Please enter the lower bound for the range (yyyy-mm-dd):");
                	final String date_begin_string = sc.nextLine();//read the date from the user as a string
                	final Date date_begin = Date.valueOf(date_begin_string);//convert the date string to a sql date
                	
                	System.out.println("Please enter upper bound for the range (yyyy-mm-dd):");
                	final String date_end_string = sc.nextLine(); //read the date from the user as a string
                	final Date date_end = Date.valueOf(date_end_string); //convert the date string to a sql date
                	
                	System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_15)) {
                            // Populate the query template with the data collected from the user
                            statement.setDate(1, date_begin);
                            statement.setDate(2, date_end);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_updated = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows updated.", rows_updated));
                        } catch (SQLServerException e) {
                        	e.printStackTrace(); //print the stack trace of any sql errors
                        }
                    }
                	break;
                }
                case "16": {// Import: enter new employees from a data file until the file is empty (the user must be asked to enter the input file name);
                	System.out.println("NOTE: file must be a csv (delimited with semicolon) with the following attributes (depending on the type of employee): \n"
                			+ "Quality Controller: 1; <name>; <address>; <salary>; <product_type>;\n"
                			+ "Worker: 2; <name>; <address>; <salary>; <max_products_per_day>;\n"
                			+ "Technical Staff: 3; <name>; <address>; <salary>; <technical_position>; <degrees>;");
                	System.out.println("Please enter the filename to read new employees from:");
                	final String filename = sc.nextLine();
                	
                	//if the given filename doesnt have the extension csv; break
                	if (!filename.substring(filename.lastIndexOf(".") + 1).equals("csv")) {
                		System.out.println("Error: given file is not a csv.");
                		break;
                	}
                	
                	//create a structure to hold each line of the csv
                	List<Map<String, Object>> employees = new ArrayList<>();
                	// Create a buffered reader for the given filename
                	try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
                		
                		//temp variable for the line being parsed
                		String line;
                		 while ((line = bufferedReader.readLine()) != null) {
                			 String[] values = line.split(";"); //split the line by semicolon
                			 
                			 //read employee attributes into a hashmap
                			 HashMap<String, Object> employee = new HashMap<>();
                			 final Integer type = Integer.parseInt(values[0]);
                			 employee.put("type", type);
                			 employee.put("name", values[1]);
                			 employee.put("address", values[2]);
                			 employee.put("salary", Float.parseFloat(values[3]));
                			 
                			 //employee specialization specific attributes
                			 if (type == 1) { //quality controller
                				 employee.put("product_type", values[4]);                				 
                			 }
                			 else if (type == 2) { //worker
                				 employee.put("max_products_per_day", Integer.parseInt(values[4]));
                				 
                			 } else {//technical staff
                				 employee.put("technical_position", values[4]);
                				 employee.put("degrees", values[5]);                				 
                			 }
                			 
                			 //add the employee hashmap to the employees list
                			 employees.add(employee);
                		 }
                	} catch(IOException e) {
                        e.printStackTrace();
                        break;//file not found, end 
                    }
                	
                	System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_1)) {
                        	
                        	// for each employee, execute query 1
                        	for (var emp : employees) {
                                // Populate the query template with the data collected from the csv
                                //"EXEC ip_query1 @type = ?, @name = ?, @address = ?, @salary = ?, @product_type = ?, @max_products_per_day = ?, @technical_position = ?, @degrees = ?";
                                statement.setInt(1, (Integer) emp.get("type"));
                                statement.setString(2, (String) emp.get("name"));
                                statement.setString(3, (String) emp.get("address"));
                                statement.setFloat(4, (Float) emp.get("salary"));
                                statement.setString(5, (String) emp.getOrDefault("product_type", ""));
                                statement.setInt(6, (Integer) emp.getOrDefault("max_products_per_day", -1));
                                statement.setString(7, (String) emp.getOrDefault("technical_position", "")); 
                                statement.setString(8, (String) emp.getOrDefault("degrees", ""));
                                

                                System.out.println("Dispatching the query...");
                                // Actually execute the populated query
                                final int rows_inserted = statement.executeUpdate();
                                System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        	}
                        } catch (SQLServerException e) {
                        	e.printStackTrace(); //print the stack trace of any sql errors
                        }
                    }
                    break;
                }
                case "17": { //export customers to file
                	System.out.println("Please enter the filename for the exported data:");
                	final String filename = sc.nextLine();
                	
                	//open a bufferedwriter for the output file
                	try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
                		
                		System.out.println("Please enter the color to check:");
                    	final String color = sc.nextLine();//read the color from the user
                    	
                    	System.out.println("Connecting to the database...");
                        // Get a database connection and prepare a query statement
                        try (final Connection connection = DriverManager.getConnection(URL)) {
                            try (
                                final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_11)) {
                                // Populate the query template with the data collected from the user
                                statement.setString(1, color);

                                System.out.println("Dispatching the query...");
                                // Actually execute the populated query
                                final ResultSet resultSet = statement.executeQuery();
                                while (resultSet.next()) {//for each item in the result set, write to file
                                	bw.write(String.format("%s\n", resultSet.getString(1)));
                                }
                            } catch (SQLServerException e) {
                            	e.printStackTrace(); //print the stack trace of any sql errors
                            }
                        }
                	} catch (IOException e) {
                		e.printStackTrace();
                		break;
                	}
                	break;
                }
                case "18": // Do nothing, the while loop will terminate upon the next iteration
                    System.out.println("Exiting! Goodbye!");
                    break;
                default: // Unrecognized option, re-prompt the user for the correct one
                    System.out.println(String.format(
                        "Unrecognized option: %s\n" + 
                        "Please try again!", 
                        option));
                    break;
            }
        }

        sc.close(); // Close the scanner before exiting the application
    }
}

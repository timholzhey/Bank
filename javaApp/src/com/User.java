package com;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class User {

    private static String username;
    private static String hashedPw;
    private static int accID;

    public static int login(String username, String password) {
        String hashedPassword = "";
        int nameID = 0;
        int pwID = 0;
        ResultSet res;

        System.out.println("Logging in with username: \"" + username + "\" and password: \"" + password + "\" ...");

        // Searches db for account with given username
        res = App.sqlc.req("SELECT accID as res FROM accounts WHERE accName = \"" + username + "\"");
        try {
            if (res.next()) {
                nameID = Integer.valueOf(res.getString("res"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Hashes password string with Message-Digest Algorithm 5 to get a 128-bit fingerprint
        res = App.sqlc.req("SELECT md5(\"" + password + "\") as res");
        try {
            if (res.next()) {
                hashedPassword = res.getString("res");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Searches db for account with given hash
        res = App.sqlc.req("SELECT accID as res FROM accounts WHERE accPassword = \"" + hashedPassword + "\"");
        try {
            if (res.next()) {
                pwID = Integer.valueOf(res.getString("res"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Login attempt successful login if username and password were found and ids are matching
        if(nameID == pwID && nameID != 0) {
            User.username = username;
            User.hashedPw = hashedPassword;
            System.out.println("Successfully logged in with account ID: " + nameID);
            User.accID = nameID;
            return nameID;
        }
        // Else: Login attempt failed
        System.out.println("Account with given username and password not found!");
        return 0;
    }

    public static void logout() {
        // forget credentials
        username = "";
        hashedPw = "";
        accID = -1;
    }

    public static ArrayList<String> createAccount(
        String username,
        char[] password,
        char[] repeatPassword,
        String firstName,
        String lastName,
        String emailAddress,
        String telNumber,
        String streetAndHouseNum,
        String zipCodeAndCity
    ) {
        System.out.println("Creating account ...");
        int accRes = -1;
        int custRes = -1;
        int custID = -1;
        ResultSet res;
        String hash = "";
        ArrayList<String> inputErrors= new ArrayList<String>();

        // Validate inputs and write error messages
        if(!java.util.Arrays.equals(password, repeatPassword)) {
            inputErrors.add("Password mismatch");
        }
        if(username == "" || password.length == 0 || firstName == "" || lastName == "" || emailAddress == "" || telNumber == "" || streetAndHouseNum == "" || zipCodeAndCity == "") {
            inputErrors.add("No fields can be empty");
        }
        if(!(emailAddress.indexOf("@") > 0 && emailAddress.lastIndexOf(".") > emailAddress.indexOf("@") && emailAddress.indexOf(".") < emailAddress.length())) {
            inputErrors.add("No valid email addess entered");
        }
        if(zipCodeAndCity.split(" ").length != 2) {
            inputErrors.add("No valid postal code and city entered");
        }
        if(password.length > 0 && password.length < 5) {
            inputErrors.add("Password too short");
        }

        // If no syntax errors -> Check semantic errors
        if(inputErrors.size() == 0) {
            // Hashes password string with Message-Digest Algorithm 5 to get a 128-bit fingerprint
            res = App.sqlc.req("SELECT md5(\"" + new String(password) + "\") as res");
            try {
                if (res.next()) {
                    hash = res.getString("res");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Check if username is already taken
            res = App.sqlc.req("SELECT accID as res FROM accounts WHERE accName = \"" + username + "\"");
            try {
                if (res.next()) {
                    inputErrors.add("Username already taken");
                    return inputErrors;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Get current date in format YYYY-MM-DD hh:mm:ss
            Format f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String curDate = f.format(new Date());
            String[] zipCityArray = zipCodeAndCity.split(" ");

            // Insert data into "customers" table
            custRes = App.sqlc.update("INSERT INTO customers (firstName, lastName, email, tel, zip, street) VALUES (\"" 
                + firstName + "\",\"" + lastName + "\",\"" + emailAddress + "\"," + telNumber + ",\"" + zipCityArray[0] + "\",\"" + streetAndHouseNum + "\")");
            
            // Get foreign key: Customer ID
            res = App.sqlc.req("SELECT cusID as res FROM customers WHERE email = \"" + emailAddress + "\"");
            try {
                if (res.next()) {
                    custID = Integer.valueOf(res.getString("res"));
                    System.out.println("customer ID: " + custID);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Insert data into "accounts" table
            accRes = App.sqlc.update("INSERT INTO accounts (cusID, creadetAt, updatedAt, accName, accPassword) VALUES (\"" 
                + custID + "\",\"" +  curDate + "\",\"" + curDate + "\",\"" + username + "\",\"" + hash + "\")");

            // Server response error
            if(accRes != 1 || custRes != accRes) {
                inputErrors.add("Server error");
            }
        }
        return inputErrors;
    }

    public static float getBalance() {
        // Request balance from db
        ResultSet res = App.sqlc.req("SELECT balance as res FROM accounts WHERE accID = " + accID);
        try {
            if (res.next()) {
                return Float.valueOf(res.getString("res"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    public static String getName() {
        // Request first and last name from db
        ResultSet res = App.sqlc.req("SELECT firstName as fn, lastName as ln from customers inner join accounts on accounts.cusID = customers.cusID where accID = " + accID);
        try {
            if (res.next()) {
                return res.getString("fn") + " " + res.getString("ln");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getEmail() {
        // Request email address from db
        ResultSet res = App.sqlc.req("SELECT email as res from customers inner join accounts on accounts.cusID = customers.cusID where accID = " + accID);
        try {
            if (res.next()) {
                return res.getString("res");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return hashedPw;
    }
}

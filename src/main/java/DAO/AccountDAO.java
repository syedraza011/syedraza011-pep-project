package DAO;
import static org.mockito.ArgumentMatchers.contains;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.*;
import Model.Account;

/*### Account
```
account_id integer primary key auto_increment,
username varchar(255) unique,
password varchar(255)
```


``` */

import java.util.List;
import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    public List<Account> getAllAccounts() {
        Connection connection=ConnectionUtil.getConnection();
        List <Account> accounts = new ArrayList<>();

        try {  String sql = "select * from Account;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet= preparedStatement.executeQuery();
        while(resultSet.next()){
            Account account = new Account(
                resultSet.getInt("account_id"),
                resultSet.getString("username"),
                resultSet.getString("password"));
                accounts.add(account);
        }


        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }
    
    public boolean getAccountsByUserName(String userName) {
        String sql = "SELECT * FROM Account WHERE userName = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             
            preparedStatement.setString(1, userName);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
              
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.err.println("Error fetching accounts for user: " + e.getMessage());
        }
        return false; 
    }
    
    /*
     ## 1: Our API should be able to process new User registrations.

As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register. 
The body will contain a representation of a JSON Account, but will not contain an account_id.

- The registration will be successful if and only if the username is not blank,
 the password is at least 4 characters long, 
 and an Account with that username does not already exist. 
 If all these conditions are met, the response body should contain a JSON of the Account,
  including its account_id. The response status should be 200 OK, which is the default. 
 The new account should be persisted to the database.
- If the registration is not successful, the response status should be 400. (Client error)
     */

   public Account createAccountDAO(Account account ){
    Connection connection=ConnectionUtil.getConnection();
        try {
        String sql = "insert into account (userName, Password) values(?,?)";
            PreparedStatement preparedStatement =connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,account.username);
            preparedStatement.setString(2, account.password);
            preparedStatement.executeUpdate();
            ResultSet primaryKeyResultSet =preparedStatement.getGeneratedKeys();
            if(primaryKeyResultSet.next()){
                int generated_Account_id= (int) primaryKeyResultSet.getLong(1);
                return new Account(generated_Account_id, account.getUsername(),account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Account loginAccountDAO(String username, String password){
        Connection connection=ConnectionUtil.getConnection();
        try{
           String sql= "Select * from Account where username=? and password=?;";
           PreparedStatement preparedStatement=connection.prepareStatement(sql);
           ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                Account account= new Account(
                    resultSet.getInt("Account_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"));
                if(username.equals(account.getUsername()) && password.equals(account.getPassword())){
                    return account;
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
       return null;
    }

}

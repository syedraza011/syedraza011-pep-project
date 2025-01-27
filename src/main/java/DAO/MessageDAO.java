package DAO;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import Model.Message;
import Util.ConnectionUtil;

/*### Message
```
message_id integer primary key auto_increment,
posted_by integer,
message_text varchar(255),
time_posted_epoch long,
foreign key (posted_by) references Account(account_id)
 */
public class MessageDAO {
    public List<Message> getAllMessagesDAO(){
        Connection connection = ConnectionUtil.getConnection();
        List <Message> messages = new ArrayList<>();
        try {
            String sql = "select * from Message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet= preparedStatement.executeQuery();
            while(resultSet.next()){
                Message message = new Message(resultSet.getInt("message_id"),
                resultSet.getInt("posted_by"),resultSet.getString("message_text"),
                resultSet.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
    public Message getMessageByIdDAO(int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql= "Select * from Message where message_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Message message = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch"));

                return message;
            }
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
       return null;
    }
    public boolean deleteMessageByIdDAO(int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "delete from message where message_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            int rowaffected = preparedStatement.executeUpdate();
           
            if(rowaffected>0){
                return true;
            }
          
        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public Message createNewMessageDAO(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {

            String sql = "insert into message (posted_by,message_text,time_posted_epoch) values(?,?,?)" ;
        
            PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            //preparedStatement.setInt(1, message.message_id);
            preparedStatement.setInt(1, message.posted_by);
            preparedStatement.setString(2, message.message_text);
            preparedStatement.setLong(3, message.time_posted_epoch);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            // if(pkeyResultSet.next()){
            //     int generated_message_id = (int) pkeyResultSet.getLong(1);
            //     return new Message(generated_message_id, message.getPosted_by(),message.getMessage_text(),message.getTime_posted_epoch());
            // }
            // //-----------------------------------
            while(resultSet.next()){
                Message newMessage = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch"));

                return newMessage;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public boolean updateMessageByIdDAO(Message updatedMessage, int id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message set message_text = ?  WHERE message_id = ?";
    
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
           // preparedStatement.setInt(1, updatedMessage.getPosted_by());
            preparedStatement.setString(1, updatedMessage.getMessage_text());
           // preparedStatement.setLong(3, updatedMessage.getTime_posted_epoch());
            preparedStatement.setInt(2, id);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error updating message: " + e.getMessage());
            return false;
        } 
    }
}
    
    


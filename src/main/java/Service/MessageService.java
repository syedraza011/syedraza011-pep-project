package Service;
import java.util.*;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;
    
    public MessageService(){
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO){
        this.messageDAO= messageDAO;
    }
    public List<Message> getAllMessagesService(){
        return messageDAO.getAllMessagesDAO();
    }
    public List<Message> getAllMessagesbyAUserService(int posted_by){
        return messageDAO.getAllMessagesbyAUserDAO(posted_by);
    }

    public Message getMessageByIdService(int message_id){
        return messageDAO.getMessageByIdDAO(message_id);
     
    }
    public Message deleteMessageByIdService(int message_id){
      Message msg=  messageDAO.getMessageByIdDAO(message_id);
        messageDAO.deleteMessageByIdDAO(message_id);
        return msg;
    }
    public Message createMessageService(Message message){
       // if (message.message_text.trim().isEmpty()) {
         //   throw new IllegalArgumentException("Message text cannot be empty.");
        //}
        //if (message.message_text.length() > 255) { 
          //  throw new IllegalArgumentException("Message text exceeds the allowed length.");
        //}else{
         return messageDAO.createNewMessageDAO(message);
        //}
        
    }
    public Message updateMessageByIdService(Message message, int id){
        if (message.message_text == null || message.message_text.trim().isEmpty()) {
            throw new IllegalArgumentException("Message text cannot be empty.");
        }
        if (message.message_text.length() > 255) { 
            throw new IllegalArgumentException("Message text exceeds the allowed length.");
        }
        boolean updatedMessage = messageDAO.updateMessageByIdDAO(message, id);
       if(updatedMessage){
        return messageDAO.getMessageByIdDAO(id);
       }
        return null;
       
    }
}

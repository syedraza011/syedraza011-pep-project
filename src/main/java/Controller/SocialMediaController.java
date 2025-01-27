package Controller;

import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.*;
import Model.Account;
import Model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TO DO:
 * You will need to write your own endpoints and handlers for your controller.
 *  The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    MessageService messageService = new MessageService();
    AccountService accountService = new AccountService();
    public SocialMediaController (){
       
    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

      //Account Controller
        app.post("/register", this::createAccountHandler);
        app.post("/login", this::loginAccountHandler);
       
        //messages Controller
        app.get("/messages",this::getAllMessagesHandler);
        app.post("/messages",this::postCreateMessageHandler);
        app.delete("messages/{message_id}",this::deleteMessageByIdHandler);
        app.patch("messages/{id}", this:: patchUpdateMessageByIdHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
   
   
    
    
    

   // Account Controller
   
   
   /*
    * ## 1: Our API should be able to process new User registrations.

    As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register. 
    The body will contain a representation of a JSON Account, but will not contain an account_id.
    The registration will be successful if and only if the username is not blank, 
    the password is at least 4 characters long, and an Account with that username does not already exist. 
    If all these conditions are met, the response body should contain a JSON of the Account, including its account_id. 
    The response status should be 200 OK, which is the default. The new account should be persisted to the database.
    If the registration is not successful, the response status should be 400. (Client error)
    */
    private void createAccountHandler(Context context) {
        try {
           
            Account account = context.bodyAsClass(Account.class);
            AccountService accountService = new AccountService();
            Account createdAccount = accountService.createAccountService(account);
            if (createdAccount != null) {
                context.json(createdAccount);
            } else {
                context.status(400).result("Failed to create account.");
            }
            
        } catch(IllegalArgumentException e){
            context.status(401).result("An error occurred while creating the account."+e.getMessage());
        }
        catch(Exception e) {
            System.err.println("Error creating account: " + e.getMessage());
            context.status(500).result("An error occurred while creating the account.");
        }
    }
    /*
     * ## 2: Our API should be able to process User logins.
        As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. 
        The request body will contain a JSON representation of an Account, not containing an account_id.
        In the future, this action may generate a Session token to allow the user to securely use the site.
        We will not worry about this for now.
        The login will be successful if and only if the username and password provided in the request body 
        JSON match a real account existing on the database. 
        If successful, the response body should contain a JSON of the account in the response body, 
        including its account_id. The response status should be 200 OK, which is the default.
        If the login is not successful, the response status should be 401. (Unauthorized)
     */
   
    private void loginAccountHandler(Context context) {
        try {
            Account account = context.bodyAsClass(Account.class);
            AccountService accountService = new AccountService();
            Account loggedInAccount = accountService.loginAccountService(account);
          
            if (account.getUsername() == null || account.getPassword() == null) {
                context.status(400).json("Username and password are required.");
                return;
            }
            if (loggedInAccount != null) {
                context.status(200).json(loggedInAccount);
            } 
            else {
               context.status(401).json("Invalid username or password.");
            }
        } catch (Exception e) {
            context.status(500).json("Internal server error.");
        }
    }
    
    
    // Messages controller

    private void postCreateMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.createMessageService(message);
        if(addedMessage!=null && message.message_text.length() <256 ){
            context.status(200);
            context.json(mapper.writeValueAsString(addedMessage));
        }else{
            context.status(400);
        }
    }
   private void getAllMessagesHandler(Context context){
        try {
            MessageService messageService = new MessageService();
            List<Message> messages = messageService.getAllMessagesService();
            context.status(200);
            context.json(messages);
        } catch (Exception e) {
                context.status(500).result("An error occurred while fetching messages.");
        }
    
    }
    private void deleteMessageByIdHandler(Context context){
        try{
            MessageService messageService = new MessageService();
            int id = Integer.parseInt(context.pathParam("message_id"));
            Message message = messageService.deleteMessageByIdService(id); // return a deleted message

            if(message){
                context.status(200).result("Message deleted successfully.");
            }

            //return result;
        }catch (Exception e) {
            context.status(500).result("An error occurred while fetching message.");
        }
      
    }
    public void patchUpdateMessageByIdHandler(Context context){
        try{
            MessageService messageService= new MessageService();
            Message message = context.bodyAsClass(Message.class);
            int id = Integer.parseInt(context.pathParam("id"));
            messageService.updateMessageByIdService(message,id);
            context.status(200);
        }catch(Exception e){
            context.status(500).result("An error occurred while fetching message.");
        }
    }
}



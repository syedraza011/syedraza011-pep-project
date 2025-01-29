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
        app.get("messages/{message_id}", this::getMessageByIdHandler);
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
                context.status(400);
            }
            
        } catch(IllegalArgumentException e){
            context.status(400);
        }
        catch(Exception e) {
            System.err.println("Error creating account: " + e.getMessage());
            context.status(500);
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
                context.status(400);
                return;
            }
            if (loggedInAccount != null) {
                context.status(200).json(loggedInAccount);
            } 
            else {
               context.status(401);
            }
        } catch (Exception e) {
            context.status(500);
        }
    }
    
    
    // Messages controller
//
    private void postCreateMessageHandler(Context context)throws JsonProcessingException  {
        ObjectMapper mapper = new ObjectMapper();
       Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.createMessageService(message);
        if(addedMessage!=null ){
            if(addedMessage.message_text.isEmpty() || addedMessage.message_text.length() > 255)
                context.status(400);
            
           else
                context.status(200).json(message);
           
            
        }
        else{
            context.status(400);
        }
    }
    /*
        ## 5: Our API should be able to retrieve a message by its ID.
        As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.
        The response body should contain a JSON representation of the message identified by the message_id.
        It is expected for the response body to simply be empty if there is no such message.
        The response status should always be 200, which is the default.
     */
    private void getMessageByIdHandler(Context context){
        try{
            MessageService messageService= new MessageService();
            int id = Integer.parseInt(context.pathParam("message_id"));
            Message message = messageService.getMessageByIdService(id);
            if(message!=null){
                context.status(200);
            }

        }catch(Exception e){
            context.status(500);
        }
    }
   private void getAllMessagesHandler(Context context){
        try {
            MessageService messageService = new MessageService();
            List<Message> messages = messageService.getAllMessagesService();
           // context.status(200);
            context.json(messages);
        } catch (Exception e) {
                context.status(500);
        }
    
    }
    private void deleteMessageByIdHandler(Context context){
        try{
            MessageService messageService = new MessageService();
            int id = Integer.parseInt(context.pathParam("message_id"));
            Message message = messageService.deleteMessageByIdService(id); 

            if(message!=null){
                context.status(200);
            }
        }catch (Exception e) {
            context.status(500);
        }
    }
    public void patchUpdateMessageByIdHandler(Context context){
        try{
            MessageService messageService= new MessageService();
            Message message = context.bodyAsClass(Message.class);
            int id = Integer.parseInt(context.pathParam("id"));
           Message message2 = messageService.updateMessageByIdService(message,id);
           if(message2 !=null){
            context.status(200).json(message2);
           }else {
            context.status(400);
           }
            
        }catch(Exception e){
            context.status(400);
        }
    }
}



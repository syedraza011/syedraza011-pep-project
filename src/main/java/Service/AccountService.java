package Service;
import java.util.*;
import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public AccountDAO accountDAO;

    public AccountService(){
       accountDAO =new AccountDAO();
    }
    public AccountService(AccountDAO accountDAO){
        this.accountDAO =accountDAO;
    }
    
    public List<Account> getAllAccountsService(){
      return accountDAO.getAllAccounts();
    }

    public Account createAccountService(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account object cannot be null.");
        }
    
        if (account.getUsername() == null || account.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
    
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password cannot be null and must be at least 4 characters long.");
        }
    
        if (accountDAO.getAccountsByUserName(account.getUsername())) {
            throw new IllegalArgumentException("An account with the given username already exists.");
        }
            Account account1=accountDAO.createAccountDAO(account);
        // Create the account
        return account1;
    }
    
    
    public Account loginAccountService(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account object cannot be null.");
        }
    
        if (account.getUsername() == null || account.getUsername().isEmpty() ||
            account.getPassword() == null || account.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Username or Password cannot be null or empty.");
        }
        return accountDAO.loginAccountDAO(account.getUsername(), account.getPassword());
        
    }
    
     
}
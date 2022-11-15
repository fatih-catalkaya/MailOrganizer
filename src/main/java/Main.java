import configuration.Configuration;
import configuration.MailAccount;

public class Main {
    public static void main(String[] args){
        MailAccount[] mailAccounts = null;
        try{
            mailAccounts = Configuration.getInstance().getMailAccounts();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        System.out.println("Hallo");
    }
}

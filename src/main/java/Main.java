import configuration.Configuration;
import configuration.MailAccount;
import jakarta.mail.MessagingException;
import jakarta.mail.NoSuchProviderException;
import mail.MailOrganizer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main {

    public static void main(String[] args){
        MailAccount[] mailAccounts = null;
        try{
            mailAccounts = Configuration.getInstance().getMailAccounts();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return;
        }

        List<CompletableFuture<Void>> cfl = new ArrayList<>(mailAccounts.length);
        for (final MailAccount mailAccount : mailAccounts) {
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                try{
                    MailOrganizer.organizeMailAccount(mailAccount);
                }
                catch (MessagingException ex){
                    ex.printStackTrace();
                }

            });
            cfl.add(completableFuture);
        }
        CompletableFuture.allOf(cfl.toArray(new CompletableFuture[0])).join();
    }
}

import configuration.Configuration;
import configuration.MailAccount;
import jakarta.mail.MessagingException;
import jakarta.mail.NoSuchProviderException;
import logging.Log;
import mail.MailOrganizer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args){
        long start = System.currentTimeMillis();
        Log log = Log.getInstance();
        MailAccount[] mailAccounts = null;
        try{
            mailAccounts = Configuration.getInstance().getMailAccounts();
        }
        catch (Exception ex){
            //ex.printStackTrace();
            log.severe(ex.getMessage());
            return;
        }

        List<CompletableFuture<Void>> cfl = new ArrayList<>(mailAccounts.length);
        for (final MailAccount mailAccount : mailAccounts) {
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                try{
                    MailOrganizer.organizeMailAccount(mailAccount);
                }
                catch (MessagingException ex){
                    //ex.printStackTrace();
                    log.severe(String.format("Organizing mail account %s failed!", mailAccount.getName()));
                    log.severe(ex.getMessage());
                }
            });
            cfl.add(completableFuture);
        }
        CompletableFuture.allOf(cfl.toArray(new CompletableFuture[0])).join();
        long duration = (System.currentTimeMillis() - start) / 1000;
        log.info(String.format("Finished organizing in %o s", duration));
    }
}

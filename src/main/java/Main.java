import configuration.Configuration;
import configuration.MailAccount;
import mail.MailOrganizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> MailOrganizer.organizeMailAccount(mailAccount));
            cfl.add(completableFuture);
        }
        CompletableFuture.allOf(cfl.toArray(new CompletableFuture[0])).join();
    }
}

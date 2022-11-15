package mail;

import com.sun.mail.imap.IMAPFolder;
import configuration.MailAccount;
import configuration.MailFilter;
import jakarta.mail.*;

import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.stream.Stream;

public class MailOrganizer {
    public static void organizeMailAccount(final MailAccount mailAccount) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.imap.host", mailAccount.getImapHost());
        props.put("mail.imap.port", mailAccount.getImapPort());

        switch (mailAccount.getEncryption()){
            case STARTTLS:
                props.put("mail.imap.starttls.enable", true);
                break;
            case NATIVE_TLS:
                props.put("mail.imap.ssl.enable", true);
                break;
            default:
                break;
        }

        Session session = Session.getInstance(props);
        Store store = session.getStore("imap");
        store.connect(mailAccount.getUsername(), mailAccount.getPassword());

        IMAPFolder inbox = (IMAPFolder) store.getFolder(mailAccount.getObservationDirectory());
        inbox.open(Folder.READ_WRITE);
        Message[] msgs = inbox.getMessages();

        //Iterate through all messages
        for (int i = 0; i < msgs.length; i++) {
            Message msg = msgs[i];
            if(msg.isExpunged()){
                continue;
            }
            for(MailFilter mf : mailAccount.getFilter()){
                String[] headerValues = msg.getHeader(mf.getFilterHeader());
                if(headerValues == null){
                    // Not found
                    continue;
                }
                String headerString = Stream.of(headerValues).reduce(";", (a, b) -> {return a+b;});

                boolean hit = false;
                switch (mf.getMatchMethod()){
                    case CONTAINS:
                        hit = headerString.contains(mf.getFilterValue());
                        break;
                    case CONTAINS_IGNORE_CASE:
                        hit = headerString.toLowerCase().contains(mf.getFilterValue().toLowerCase());
                        break;
                    case EQUALS:
                        hit = headerString.equals(mf.getFilterValue());
                        break;
                    case EQUALS_IGNORE_CASE:
                        hit = headerString.equalsIgnoreCase(mf.getFilterValue());
                        break;
                }

                if(!mf.isMarkRead()){
                    msg.setFlag(Flags.Flag.SEEN, false);
                }

                if(hit){
                    Folder destFolder = store.getFolder(mf.getMoveDestination());
                    destFolder.open(Folder.READ_WRITE);
                    inbox.moveMessages(new Message[]{msg}, destFolder);
                    destFolder.close(false);
                    break; // we have to break here, because we do not need to continue checking further filter on this email
                }
            }
        }

        inbox.close(false);
        store.close();
    }
}

package configuration;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Configuration {
    private static final String CONFIG_FILE_NAME = "config.json";

    public static Configuration instance;
    private final MailAccount[] mailAccounts;

    public static Configuration getInstance() throws IOException {
        if(instance == null){
            instance = new Configuration();
        }
        return instance;
    }

    private Configuration() throws IOException {
        File configFile = new File(CONFIG_FILE_NAME);
        if(!configFile.isFile() || !configFile.canRead()){
            throw new IOException("Can not read configuration file!");
        }

        try{
            Any any = JsonIterator.deserialize(Files.readAllBytes(Paths.get(configFile.toURI())));
            this.mailAccounts = parseJson(any);
        }
        catch (Exception ex){
            throw new IOException("Parsing of config file failed!");
        }
    }


    private MailAccount[] parseJson(Any any) throws IOException {
        List<Any> mailAccounts = any.get("mail_accounts").asList();
        MailAccount[] returnList = new MailAccount[mailAccounts.size()];
        for (int i = 0; i < mailAccounts.size(); i++){
            Any mailAccAny = mailAccounts.get(i);

            // First parse mail filters
            List<Any> mailAccFiltersAny = mailAccAny.get("mail_filters").asList();
            MailFilter[] filterList = new MailFilter[mailAccFiltersAny.size()];
            for(int j = 0; j < mailAccFiltersAny.size(); j++){
                Any mailAccFilter = mailAccFiltersAny.get(j);
                MailFilter.Builder mfBuilder = new MailFilter.Builder();
                MailFilter mf = mfBuilder
                                    .filterHeader(mailAccFilter.get("filter_header").toString())
                                    .filterValue(mailAccFilter.get("filter_value").toString())
                                    .filterMatchMethod(FilterMatchMethod.valueOf(mailAccFilter.get("match_method").toString()))
                                    .moveDestination(mailAccFilter.get("move_destination").toString())
                                    .markRead(mailAccFilter.get("mark_read").toBoolean())
                                    .build();
                filterList[j] = mf;
            }

            MailAccount mailAccount = new MailAccount.Builder()
                    .name(mailAccAny.get("name").toString())
                    .imapHost(mailAccAny.get("imap_host").toString())
                    .imapPort(mailAccAny.get("imap_port").toInt())
                    .imapConnectionEncryption(ImapConnectionEncryption.valueOf(mailAccAny.get("imap_com_encryption").toString()))
                    .username(mailAccAny.get("username").toString())
                    .password(mailAccAny.get("password").toString())
                    .observationDirectory(mailAccAny.get("observation_directory").toString())
                    .mailFilter(filterList)
                    .build();
            returnList[i] = mailAccount;
        }

        return returnList;
    }


    public MailAccount[] getMailAccounts() {
        return mailAccounts;
    }
}

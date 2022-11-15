package configuration;

public class MailAccount {
    private final String name;
    private final String imapHost;
    private final int imapPort;
    private final ImapConnectionEncryption encryption;
    private final String username;
    private final String password;
    private final String observationDirectory;
    private final MailFilter[] filter;

    private MailAccount(Builder builder) {
        this.name = builder.name;
        this.imapHost = builder.imapHost;
        this.imapPort = builder.imapPort;
        this.encryption = builder.encryption;
        this.username = builder.username;
        this.password = builder.password;
        this.observationDirectory = builder.observationDirectory;
        this.filter = builder.mailFilter;
    }

    public String getName() {
        return name;
    }

    public String getImapHost() {
        return imapHost;
    }

    public int getImapPort() {
        return imapPort;
    }

    public ImapConnectionEncryption getEncryption() {
        return encryption;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getObservationDirectory() {
        return observationDirectory;
    }

    public MailFilter[] getFilter() {
        return filter;
    }

    public static class Builder {
        private String name;
        private String imapHost;
        private int imapPort;
        private ImapConnectionEncryption encryption;
        private String username;
        private String password;
        private String observationDirectory;
        private MailFilter[] mailFilter;

        public Builder(){
            imapPort = -1;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder imapHost(String host){
            this.imapHost = host;
            return this;
        }

        public Builder imapPort(int imapPort){
            this.imapPort = imapPort;
            return this;
        }

        public Builder imapConnectionEncryption(ImapConnectionEncryption encryption){
            this.encryption = encryption;
            return this;
        }

        public Builder username(String username){
            this.username = username;
            return this;
        }

        public Builder password(String password){
            this.password = password;
            return this;
        }

        public Builder observationDirectory(String observationDirectory){
            this.observationDirectory = observationDirectory;
            return this;
        }

        public Builder mailFilter(MailFilter[] mailFilters){
            this.mailFilter = mailFilters;
            return this;
        }

        public MailAccount build(){
            // Check that every field is set
            if(name == null){
                throw new IllegalArgumentException("Mail account name has not been set!");
            }
            if(imapHost == null){
                throw new IllegalArgumentException("IMAP host has not been set!");
            }
            if(imapPort < 1 || imapPort > 65535){
                throw new IllegalArgumentException("IMAP port has not been set!");
            }
            if(encryption == null){
                throw new IllegalArgumentException("IMAP encryption has not been set!");
            }
            if(username == null){
                throw new IllegalArgumentException("IMAP username has not been set!");
            }
            if(password == null){
                throw new IllegalArgumentException("IMAP password has not been set!");
            }
            if(observationDirectory == null){
                throw new IllegalArgumentException("Observation Directory has not been set!");
            }

            return new MailAccount(this);
        }
    }
}

package configuration;

public class MailFilter {
    private final String filterHeader;
    private final String filterValue;
    private final FilterMatchMethod matchMethod;
    private final String moveDestination;
    private final boolean markRead;

    private MailFilter(Builder builder) {
        this.filterHeader = builder.filterHeader;
        this.filterValue = builder.filterValue;
        this.matchMethod = builder.matchMethod;
        this.moveDestination = builder.moveDestination;
        this.markRead = builder.markRead;
    }

    public String getFilterHeader() {
        return filterHeader;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public FilterMatchMethod getMatchMethod() {
        return matchMethod;
    }

    public String getMoveDestination() {
        return moveDestination;
    }

    public boolean isMarkRead() {
        return markRead;
    }

    public static class Builder{
        private String filterHeader;
        private String filterValue;
        private FilterMatchMethod matchMethod;
        private String moveDestination;
        private boolean markRead;

        public Builder(){};

        public Builder filterHeader(String filterHeader){
            this.filterHeader = filterHeader;
            return this;
        }

        public Builder filterValue(String filterValue){
            this.filterValue = filterValue;
            return this;
        }

        public Builder filterMatchMethod(FilterMatchMethod filterMatchMethod){
            this.matchMethod = filterMatchMethod;
            return this;
        }

        public Builder moveDestination(String moveDestination){
            this.moveDestination = moveDestination;
            return this;
        }

        public Builder markRead(boolean markRead){
            this.markRead = markRead;
            return this;
        }

        public MailFilter build(){
            if(filterHeader == null){
                throw new IllegalArgumentException("FilterHeader has not been set");
            }
            if(filterValue == null){
                throw new IllegalArgumentException("FilterValue has not been set");
            }
            if(matchMethod == null){
                throw new IllegalArgumentException("FilterMatchMethod has not been set");
            }
            if(moveDestination == null){
                throw new IllegalArgumentException("MoveDestination has not been set");
            }
            return new MailFilter(this);
        }
    }
}

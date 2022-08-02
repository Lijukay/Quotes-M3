package com.lijukay.quotesAltDesign;

public class AllRV {
    //Strings
    private String QuoteAll;
    private String AuthorAll;

    //Classes
    public AllRV(String quoteAll, String authorAll) {
        QuoteAll = quoteAll;
        AuthorAll = authorAll;
    }

    public String getQuoteAll() {
        return QuoteAll;
    }

    public void setQuoteAll(String quoteAll) {
        QuoteAll = quoteAll;
    }

    public String getAuthorAll() {
        return AuthorAll;
    }

    public void setAuthorAll(String authorAll) {
        AuthorAll = authorAll;
    }
}


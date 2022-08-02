package com.lijukay.quotesAltDesign;

public class EC {
    //Strings
    private String Quote;
    private String Author;

    //Classes
    public EC(String quote, String author) {
        Quote = quote;
        Author = author;
    }

    public String getQuote() {
        return Quote;
    }

    public void setQuote(String quote) {
        Quote = quote;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }
}

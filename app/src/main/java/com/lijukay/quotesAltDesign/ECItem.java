package com.lijukay.quotesAltDesign;

public class ECItem {
    private String author;
    private String quote;

    public ECItem(String author, String quote) {
        this.author = author;
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public String getQuote() {
        return quote;
    }
}

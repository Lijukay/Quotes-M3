package com.lijukay.quotesAltDesign;

public class PQItem {
    private String authorPQ;
    private String quotePQ;

    public PQItem(String authorPQ, String quotePQ) {
        this.authorPQ = authorPQ;
        this.quotePQ = quotePQ;
    }

    public String getAuthorPQ() {
        return authorPQ;
    }

    public String getQuotePQ() {
        return quotePQ;
    }
}
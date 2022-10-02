package com.lijukay.quotesAltDesign.Items;

public class FavoritesItem {
    private final String authorFav;
    private final String quoteFav;

    public FavoritesItem(String quoteFav, String authorFav) {
        this.authorFav = authorFav;
        this.quoteFav = quoteFav;
    }

    public String getAuthorFav() {
        return authorFav;
    }

    public String getQuoteFav() {
        return quoteFav;
    }
}

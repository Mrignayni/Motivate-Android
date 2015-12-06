package com.jozapps.inspire.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("Quote")
public class Quote extends ParseObject {

    public static final String TEXT = "text";
    public static final String AUTHOR = "author";
    public static final String DISPLAY_DATE = "displayDate";

    public String getText() {
        return (String) get(TEXT);
    }

    public String getAuthor() {
        return (String) get(AUTHOR);
    }

    public Date getDisplayDate() {
        return (Date) get(DISPLAY_DATE);
    }

}

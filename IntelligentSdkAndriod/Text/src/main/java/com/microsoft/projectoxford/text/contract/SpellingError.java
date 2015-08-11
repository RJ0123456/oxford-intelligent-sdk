package com.microsoft.projectoxford.text.contract;

public class SpellingError {
    public int offset;

    public String token;

    public String type;

    public Suggestion[] suggestions;
}

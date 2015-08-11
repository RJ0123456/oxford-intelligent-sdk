package com.microsoft.projectoxford.text;

import com.microsoft.projectoxford.text.contract.SpellCheckResult;
import com.microsoft.projectoxford.text.rest.TextServiceException;

public interface TextServiceClient {
    public SpellCheckResult getSuggestions(String text, String preContextText, String postContextText) throws TextServiceException;

    public SpellCheckResult getSuggestions(String text, String preContextText) throws TextServiceException;

    public SpellCheckResult getSuggestions(String text) throws TextServiceException;
}

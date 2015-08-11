package com.microsoft.projectoxford.text;

import com.google.gson.Gson;
import com.microsoft.projectoxford.text.contract.SpellCheckResult;
import com.microsoft.projectoxford.text.rest.TextServiceException;
import com.microsoft.projectoxford.text.rest.WebServiceRequest;

import java.util.HashMap;
import java.util.Map;

public class TextServiceRestClient implements TextServiceClient{
    private static final String serviceHost = "https://api.projectoxford.ai/text/v1/spellchecks";
    private WebServiceRequest restCall = null;
    private Gson gson = new Gson();

    public TextServiceRestClient(String subscriptKey) {
        this.restCall = new WebServiceRequest(subscriptKey);
    }

    @Override
    public SpellCheckResult getSuggestions(String text, String preContextText, String postContextText) throws TextServiceException{
        Map<String, Object> params = new HashMap<>();

        params.put("text", text);
        params.put("preContextText", preContextText);
        params.put("postContextText", postContextText);

        String json = (String)this.restCall.request(serviceHost, params, "application/x-www-form-urlencoded");
        return this.gson.fromJson(json, SpellCheckResult.class);
    }

    @Override
    public SpellCheckResult getSuggestions(String text, String preContextText) throws TextServiceException {
        Map<String, Object> params = new HashMap<>();

        params.put("text", text);
        params.put("preContextText", preContextText);

        String json = (String)this.restCall.request(serviceHost, params, "application/x-www-form-urlencoded");
        return this.gson.fromJson(json, SpellCheckResult.class);
    }

    @Override
    public SpellCheckResult getSuggestions(String text) throws TextServiceException {
        Map<String, Object> params = new HashMap<>();
        params.put("text", text);

        String json = (String)this.restCall.request(serviceHost, params, "application/x-www-form-urlencoded");
        return this.gson.fromJson(json, SpellCheckResult.class);
    }
}

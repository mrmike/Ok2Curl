package com.moczul.sample;

import okhttp3.CacheControl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequestFactory {

    public static final String TYPE_GET = "type_get";
    public static final String TYPE_POST = "type_post";
    public static final String TYPE_GET_MODIFIED = "type_get_modified";

    private static final String NEW_REPO_BODY = "{" +
            "  \"name\": \"Hello-World\"," +
            "  \"description\": \"This is your first repository\"," +
            "  \"homepage\": \"https://github.com\"," +
            "  \"private\": false," +
            "  \"has_issues\": true," +
            "  \"has_wiki\": true," +
            "  \"has_downloads\": true" +
            "}";

    public static Request sampleGetRequest() {
        return new Request.Builder()
                .url("https://api.github.com/repos/vmg/redcarpet/issues?state=closed")
                .cacheControl(CacheControl.FORCE_CACHE)
                .build();
    }

    public static Request samplePostRequest() {
        return new Request.Builder()
                .url("https://api.github.com/user/repos")
                .post(RequestBody.create(MediaType.parse("application/json"), NEW_REPO_BODY))
                .build();
    }

    public static Request modifiedGetRequest() {
        return new Request.Builder()
                .url("https://api.github.com/repos/vmg/redcarpet/issues?state=closed")
                .header("Authorization", "Basic bWFjaWVrOnRham5laGFzbG8xMjM=")
                .build();
    }

    public static Request getRequest(String type) {
        switch (type) {
            case TYPE_GET:
                return sampleGetRequest();
            case TYPE_POST:
                return samplePostRequest();
            case TYPE_GET_MODIFIED:
                return modifiedGetRequest();
            default:
                throw new IllegalArgumentException("Invalid request type");
        }
    }

}

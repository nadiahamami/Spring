package com.vermeg.utils;

//https://vermeg-entreprise.atlassian.net/rest/api/2/search?jql=project=PKPI&maxResults=1000
public class Uri {
    private static String projectsUri = "agile/1.0/board";
    public static String jqlUri = "api/2/search?jql=";
    public static String postjqlUri = "api/2/search";

    public static String getProjectUri(String args) {
        return projectsUri + args;
    }

    public static String getProjectUri() {
        return projectsUri;
    }


    public static String getJqlUri(String args) {
        StringBuffer uriJql = new StringBuffer(jqlUri);
        uriJql.append(args);
        return uriJql.toString();
    }
}

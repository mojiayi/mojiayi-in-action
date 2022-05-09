package com.mojiayi.action.javabasis.reflection;

/**
 * @author mojiayi
 */
public class DemoGitConfig {
    private String username;
    private String email;
    public String phrase;

    public DemoGitConfig(String username, String email, String phrase) {
        this.username = username;
        this.email = email;
        this.phrase = phrase;
    }

    public DemoGitConfig(String username, String email) {
        this.username = username;
        this.email = email;
    }

    private DemoGitConfig() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String getPhrase() {
        return phrase;
    }

    private void setPhrase(String phrase) {
        this.phrase = phrase;
    }
}

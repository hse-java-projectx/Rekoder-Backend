package ru.hse.rekoder.requests;

import java.util.Collections;
import java.util.Map;

public class TeamRequest {
    private String bio;
    private Map<String, String> contacts = Collections.emptyMap();

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Map<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, String> contacts) {
        this.contacts = contacts;
    }
}

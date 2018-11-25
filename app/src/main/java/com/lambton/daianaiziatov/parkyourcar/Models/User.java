package com.lambton.daianaiziatov.parkyourcar.Models;

import java.util.HashMap;
import java.util.Map;

public class User {

    public String firstName;
    public String lastName;
    public String contactNumber;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String firstName, String lastname, String contactNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastname;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("email", email);
        result.put("contactNumber",contactNumber);
        return result;
    }

}

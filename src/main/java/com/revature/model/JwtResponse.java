package com.revature.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

// A simple model, just containing a string which is our JWT
@AllArgsConstructor
@Getter
public class JwtResponse implements Serializable {
    private String jwttoken;
}

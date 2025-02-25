package com.alonsocorporation.pointofsale.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthorityJsonCreator {

    @JsonCreator
    protected SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role){}
}



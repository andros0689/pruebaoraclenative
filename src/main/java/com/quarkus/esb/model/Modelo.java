package com.quarkus.esb.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@RegisterForReflection
public class Modelo {

    @JsonProperty("id")
    private int id;
    @JsonProperty("nombre")
    private String nombre;

}

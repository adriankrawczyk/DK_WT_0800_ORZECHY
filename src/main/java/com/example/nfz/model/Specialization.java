package com.example.nfz.model;

import com.example.nfz.util.SpecializationNotFoundException;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Specialization {
    CARDIOLOGY("Kardiologia"),
    NEUROLOGY("Neurologia"),
    ONCOLOGY("Onkologia"),
    UROLOGY("Urologia");

    private final String specName;

    Specialization(String specName) {
        this.specName = specName;
    }



    @Override
    public String toString() {
        return specName;
    }


    @JsonValue
    public String getSpecializationName() {
        return specName;
    }


    public static Specialization getSpecialization(String specName) throws SpecializationNotFoundException {
            return Arrays.stream(Specialization.values())
                    .filter(s -> s.specName.equalsIgnoreCase(specName))
                    .findFirst()
                    .orElseThrow(SpecializationNotFoundException::new);
    }
}

package com.example.nfz.util;

import jakarta.persistence.Embeddable;

@Embeddable
public record Address(String street, String city, String zipCode) {
}

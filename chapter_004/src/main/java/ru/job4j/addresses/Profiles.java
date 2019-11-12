package ru.job4j.addresses;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Profiles {
    List<Address> collect(List<Profile> profiles) {
        return profiles.stream()
                .map(Profile::getAddress)
                .distinct()
                .sorted(Comparator.comparing(Address::getCity)
                        .thenComparing(Address::getStreet)
                        .thenComparing(Address::getHome)
                        .thenComparing(Address::getApartment))
                .collect(Collectors.toList());
    }
}
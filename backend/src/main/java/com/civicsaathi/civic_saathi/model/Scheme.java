
package com.civicsaathi.civic_saathi.model;

import java.util.List;

public class Scheme {

    private Long id;
    private String name;
    private String category;
    private String description;
    private String eligibility;
    private List<String> requiredDocuments;
    private String officialWebsite;

    public Scheme() {
    }

    public Scheme(Long id, String name, String category,
                  String description, String eligibility,
                  List<String> requiredDocuments,
                  String officialWebsite) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.eligibility = eligibility;
        this.requiredDocuments = requiredDocuments;
        this.officialWebsite = officialWebsite;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getEligibility() {
        return eligibility;
    }

    public List<String> getRequiredDocuments() {
        return requiredDocuments;
    }

    public String getOfficialWebsite() {
        return officialWebsite;
    }
}


package com.civicsaathi.civic_saathi.service;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.civicsaathi.civic_saathi.model.Scheme;

@Service
public class SchemeService {

    private final List<Scheme> schemes = List.of(

        // 1. Education: Post-Matric Scholarship
        new Scheme(
            1L,
            "Post-Matric Scholarship",
            "Education",
            "Financial assistance for eligible students pursuing "
                + "education after matriculation.",
            "Eligibility depends on the applicable scholarship scheme, "
                + "student category, income limits, course and current "
                + "official rules.",
            List.of(
                "Student identity proof",
                "Educational certificates",
                "Income certificate, if required",
                "Bank account details, if required"
            ),
            "https://jnanabhumi.ap.gov.in/"
        ),

        // 2. Education: National Scholarship Portal
        new Scheme(
            2L,
            "National Scholarship Portal Schemes",
            "Education",
            "The National Scholarship Portal provides information "
                + "about participating central and state scholarship "
                + "schemes and their application processes.",
            "Eligibility varies by scholarship. Applicants must check "
                + "the selected scheme's current official guidelines.",
            List.of(
                "Educational certificates, as required",
                "Income or category certificate, if applicable",
                "Other documents specified by the selected scheme"
            ),
            "https://scholarships.gov.in/"
        ),

        // 3. Agriculture: PM-KISAN
        new Scheme(
            3L,
            "Pradhan Mantri Kisan Samman Nidhi (PM-KISAN)",
            "Agriculture",
            "A Government of India scheme providing eligible "
                + "landholding farmer families with financial support "
                + "of Rs. 6,000 per year in three equal instalments, "
                + "subject to the scheme guidelines.",
            "Eligibility depends on the current official PM-KISAN "
                + "guidelines, landholding and applicable exclusion "
                + "criteria. Do not assume that every farmer qualifies. "
                + "Check the official portal for the applicant's case.",
            List.of(
                "Check the official portal for current registration "
                    + "requirements and required documents",
                "Verify the applicable eligibility conditions",
                "Follow the official application instructions"
            ),
            "https://www.pmkisan.gov.in/"
        )
    );

    public List<Scheme> getAllSchemes() {
        return schemes;
    }

    public Scheme getSchemeById(Long id) {
        return schemes.stream()
                .filter(scheme -> scheme.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Scheme> searchByCategory(String category) {
        if (category == null || category.isBlank()) {
            return List.of();
        }

        return schemes.stream()
                .filter(scheme ->
                    scheme.getCategory() != null
                    && scheme.getCategory()
                        .equalsIgnoreCase(category.trim()))
                .toList();
    }

    public List<Scheme> searchByKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        String query = keyword.toLowerCase(Locale.ROOT).trim();

        return schemes.stream()
                .filter(scheme -> matchesKeyword(scheme, query))
                .toList();
    }

    private boolean matchesKeyword(Scheme scheme, String query) {
        return contains(scheme.getName(), query)
                || contains(scheme.getCategory(), query)
                || contains(scheme.getDescription(), query)
                || contains(scheme.getEligibility(), query);
    }

    private boolean contains(String value, String query) {
        return value != null
                && value.toLowerCase(Locale.ROOT).contains(query);
    }
}

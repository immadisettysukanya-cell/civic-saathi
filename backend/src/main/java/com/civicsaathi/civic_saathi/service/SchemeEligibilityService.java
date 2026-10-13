
package com.civicsaathi.civic_saathi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.civicsaathi.civic_saathi.dto.EligibilityRequest;
import com.civicsaathi.civic_saathi.dto.EligibilityResult;

@Service
public class SchemeEligibilityService {

    private static final String SCHEME_NAME =
            "Andhra Pradesh Post-Matric Scholarships (RTF/MTF)";

    private static final String OFFICIAL_WEBSITE =
            "https://jnanabhumi.ap.gov.in/";

    public EligibilityResult checkEligibility(EligibilityRequest request) {

        List<String> matched = new ArrayList<>();
        List<String> unmet = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        if (request == null) {
            missing.add("Student profile");
            return result("NEEDS_MORE_INFORMATION",
                    matched, unmet, missing);
        }

        // State
        if (blank(request.state())) {
            missing.add("State");
        } else if (!request.state().equalsIgnoreCase("Andhra Pradesh")) {
            unmet.add("This prototype ruleset is for Andhra Pradesh.");
        } else {
            matched.add("State matches the configured ruleset.");
        }

        // Education level
        if (blank(request.educationLevel())) {
            missing.add("Education level");
        } else if (!isCollegeLevel(request.educationLevel())) {
            unmet.add("This prototype checks college/undergraduate students.");
        } else {
            matched.add("Education level matches the configured scope.");
        }

        // Family income
        if (request.annualFamilyIncome() == null) {
            missing.add("Annual family income");
        } else if (request.annualFamilyIncome() < 0) {
            unmet.add("Annual family income cannot be negative.");
        } else if (request.annualFamilyIncome() > 250000) {
            unmet.add("Reported income exceeds the configured ₹2.5 lakh ceiling.");
        } else {
            matched.add("Reported income is within the configured ceiling.");
        }

        // Category: ask only because this particular scheme requires it.
        if (blank(request.socialCategory())) {
            missing.add(
                "Social category, to check whether it is covered by this scheme"
            );
        } else if (!isCoveredCategory(request.socialCategory())) {
            unmet.add(
                "The supplied category is not in this prototype's configured list."
            );
        } else {
            matched.add("Category matches the configured category list.");
        }

        // Institution type
        if (blank(request.collegeType())) {
            missing.add("Institution type (government, aided, private affiliated, or private/deemed university)");
        } else if (isPrivateOrDeemedUniversity(request.collegeType())) {
            unmet.add("Private/deemed universities are excluded by the referenced rules.");
        } else if (isRecognizedCollegeType(request.collegeType())) {
            matched.add("Institution type is not excluded by this basic check.");
        } else {
            missing.add("Clarification of institution type");
        }

        // Mode of study
        if (blank(request.studyMode())) {
            missing.add("Study mode (regular or distance/correspondence)");
        } else if (containsAny(request.studyMode(), "distance", "correspondence")) {
            unmet.add("Distance/correspondence study is excluded by the referenced rules.");
        } else if (containsAny(request.studyMode(), "regular", "full-time", "full time")) {
            matched.add("Study mode matches the configured basic rule.");
        } else {
            missing.add("Clarification of study mode");
        }

        // Admission quota
        if (blank(request.admissionQuota())) {
            missing.add("Admission quota");
        } else if (containsAny(request.admissionQuota(), "management", "spot")) {
            unmet.add("Management/spot quota is excluded by the referenced rules.");
        } else {
            matched.add("Admission quota has no configured exclusion.");
        }

        // Attendance
        if (request.attendancePercentage() == null) {
            missing.add("Attendance percentage");
        } else if (request.attendancePercentage() < 0
                || request.attendancePercentage() > 100) {
            unmet.add("Attendance must be between 0 and 100.");
        } else if (request.attendancePercentage() < 75) {
            unmet.add("Attendance is below the configured 75% requirement for release.");
        } else {
            matched.add("Attendance meets the configured 75% threshold.");
        }

        // The following conditions require review against the current
        // official rules and family circumstances.
        // Family landholding: compare against the published criteria.

if (request.wetLandAcres() == null || request.dryLandAcres() == null) {
    missing.add("Family landholding details, or confirmation that they are unavailable");
} else if (request.wetLandAcres() < 0 || request.dryLandAcres() < 0) {
    unmet.add("Landholding values cannot be negative.");
} else if (request.wetLandAcres() >= 10.0
        || request.dryLandAcres() >= 25.0
        || request.wetLandAcres() + request.dryLandAcres() >= 25.0) {
    unmet.add("Reported family landholding meets or exceeds a configured limit. Verify the current official rules.");
} else {
    matched.add("Reported family landholding is within the configured limits.");
}

        
        if (request.governmentEmployeeOrPensioner() == null) {
            missing.add("Whether a family member is a government employee/pensioner");
        } else if (request.governmentEmployeeOrPensioner()) {
            if (request.governmentEmployeeIsSanitaryWorker() == null) {
                missing.add("Whether the government employee/pensioner is a sanitary worker");
            } else if (request.governmentEmployeeIsSanitaryWorker()) {
                matched.add("Reported sanitary-worker exception; verify current official rules.");
            } else {
                unmet.add("A family member is reported as a government employee/pensioner; verify applicable exceptions.");
            }
        }

        if (request.ownsFourWheeler() == null) {
            missing.add("Family four-wheeler ownership");
        } else if (request.ownsFourWheeler()) {
            String vehicleType = request.fourWheelerType();

            if (vehicleType == null || vehicleType.isBlank()
                    || vehicleType.equalsIgnoreCase("Not sure")) {
                missing.add("Type of family four-wheeler");
            } else if (vehicleType.equalsIgnoreCase("Taxi")
                    || vehicleType.equalsIgnoreCase("Tractor")
                    || vehicleType.equalsIgnoreCase("Auto")) {
                matched.add("Reported vehicle category may qualify for an exception; verify current official rules.");
            } else {
                unmet.add("Family vehicle ownership may affect eligibility; verify applicable exceptions.");
            }
        }


        if (request.urbanBuiltUpSqFt() == null) {
            missing.add("Urban built-up property details, if applicable");
        } else if (request.urbanBuiltUpSqFt() >= 1500) {
            unmet.add("Reported urban built-up area does not meet the basic property threshold.");
        }

        if (request.familyIncomeTaxPayer() == null) {
            missing.add("Whether any family member pays income tax");
        } else if (request.familyIncomeTaxPayer()) {
            unmet.add("A family income-tax payer may affect eligibility; verify applicable rules.");
        }

        if (!unmet.isEmpty()) {
            return result("NOT_MATCHED", matched, unmet, missing);
        }

        if (!missing.isEmpty()) {
            return result("NEEDS_MORE_INFORMATION", matched, unmet, missing);
        }

        return result("POTENTIAL_MATCH", matched, unmet, missing);
    }

    private EligibilityResult result(
            String status,
            List<String> matched,
            List<String> unmet,
            List<String> missing) {

        String message = switch (status) {
            case "NOT_MATCHED" ->
                "One or more configured rules are not met. Check the reasons and current official guidelines.";
            case "NEEDS_MORE_INFORMATION" ->
                "More information is needed before the configured rules can be evaluated.";
            default ->
                "The supplied answers match this prototype's configured rules. This is not an official eligibility decision.";
        };

        return new EligibilityResult(
                status, SCHEME_NAME, matched, unmet, missing,
                message, OFFICIAL_WEBSITE
        );
    }

    private boolean blank(String value) {
        return value == null || value.isBlank();
    }

    private boolean isCollegeLevel(String value) {
        String normalized = value.toLowerCase(Locale.ROOT);
        return normalized.contains("college")
                || normalized.contains("undergraduate")
                || normalized.contains("degree");
    }

    
private boolean isCoveredCategory(String value) {
    if (value == null) {
        return false;
    }

    String normalized = value.trim()
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z]", "");

    return switch (normalized) {
        case "sc", "st", "bc", "ebc", "kapu",
             "minority", "differentlyabled" -> true;
        default -> false;
    };
}


    private boolean isPrivateOrDeemedUniversity(String value) {
        String normalized = value.toLowerCase(Locale.ROOT);
        return normalized.contains("deemed university")
                || normalized.contains("private university");
    }

    private boolean isRecognizedCollegeType(String value) {
        return containsAny(value, "government", "govt", "aided",
                "private affiliated", "affiliated college");
    }

    private boolean containsAny(String value, String... choices) {
        if (value == null) {
            return false;
        }
        String normalized = value.toLowerCase(Locale.ROOT);
        for (String choice : choices) {
            if (normalized.contains(choice)) {
                return true;
            }
        }
        return false;
    }
}

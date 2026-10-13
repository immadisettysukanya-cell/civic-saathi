
package com.civicsaathi.civic_saathi.dto;

import java.util.List;

public record EligibilityResult(
        String status,
        String schemeName,
        List<String> matchedConditions,
        List<String> unmetConditions,
        List<String> missingInformation,
        String message,
        String officialWebsite
) {
}

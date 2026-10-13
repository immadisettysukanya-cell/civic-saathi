
package com.civicsaathi.civic_saathi.dto;

import java.util.List;

public record EligibilityQuestion(
        String field,
        String question,
        String inputType,
        boolean required,
        List<String> options
) {
}

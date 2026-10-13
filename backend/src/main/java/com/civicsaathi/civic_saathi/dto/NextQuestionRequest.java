
package com.civicsaathi.civic_saathi.dto;

import java.util.Map;

public record NextQuestionRequest(
        String state,
        String educationLevel,
        Map<String, String> answers
) {
}

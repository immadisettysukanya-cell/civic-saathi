
package com.civicsaathi.civic_saathi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.civicsaathi.civic_saathi.dto.EligibilityQuestion;
import com.civicsaathi.civic_saathi.dto.EligibilityRequest;
import com.civicsaathi.civic_saathi.dto.EligibilityResult;
import com.civicsaathi.civic_saathi.dto.EligibilitySubmission;
import com.civicsaathi.civic_saathi.dto.NextQuestionRequest;
import com.civicsaathi.civic_saathi.service.SchemeEligibilityService;
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3001"})
@RestController
@RequestMapping("/api/eligibility")
public class EligibilityController {


    private final SchemeEligibilityService eligibilityService;

    public EligibilityController(SchemeEligibilityService eligibilityService) {
        this.eligibilityService = eligibilityService;
    }

    @PostMapping("/check")
    public ResponseEntity<EligibilityResult> checkEligibility(
            @RequestBody EligibilityRequest request) {

        return ResponseEntity.ok(
                eligibilityService.checkEligibility(request)
        );
    }

   

@PostMapping("/next-question")
public ResponseEntity<EligibilityQuestion> getNextQuestion(
        @RequestBody NextQuestionRequest request) {

    if (request == null
            || request.state() == null
            || request.state().isBlank()
            || request.educationLevel() == null
            || request.educationLevel().isBlank()) {
        return ResponseEntity.badRequest().build();
    }

    java.util.Map<String, String> answers =
            request.answers() == null
                    ? java.util.Map.of()
                    : request.answers();

    if (!hasAnswer(answers, "annualFamilyIncome")) {
        return ResponseEntity.ok(new EligibilityQuestion(
        "annualFamilyIncome",
        "What is your approximate annual family income in INR?",
        "number",
        true,
        java.util.List.of()
));
    }

    if (!hasAnswer(answers, "socialCategory")) {
        return ResponseEntity.ok(new EligibilityQuestion(
                "socialCategory",
                "What is your social category?",
                "select",
                true,
                java.util.List.of(
                        "SC", "ST", "BC", "EBC",
                        "Kapu", "Minority", "Differently abled",
                        "Other", "Prefer not to say", "Not sure"
                )
        ));
    }
    
String socialCategory = answers.get("socialCategory");

if (socialCategory != null && !socialCategory.isBlank()
        && !hasValidAnswer(
                answers,
                "socialCategory",
                java.util.List.of(
                        "SC", "ST", "BC", "EBC", "Kapu",
                        "Minority", "Differently abled", "Other",
                        "Prefer not to say", "Not sure"
                ))) {
    return ResponseEntity.badRequest()
        .body(null);
}

    
if (!hasAnswer(answers, "collegeType")) {
    return ResponseEntity.ok(new EligibilityQuestion(
            "collegeType",
            "What type of institution do you study at?",
            "select",
            true,
            java.util.List.of(
                    "Government",
                    "Aided",
                    "Private affiliated",
                    "Private / deemed university",
                    "Not sure"
            )
    ));
}

if (!hasAnswer(answers, "studyMode")) {
    return ResponseEntity.ok(new EligibilityQuestion(
            "studyMode",
            "What is your mode of study?",
            "select",
            true,
            java.util.List.of(
                    "Regular / full-time",
                    "Distance / correspondence",
                    "Not sure"
            )
    ));
}

if (!hasAnswer(answers, "admissionQuota")) {
    return ResponseEntity.ok(new EligibilityQuestion(
            "admissionQuota",
            "How were you admitted to your course?",
            "select",
            true,
            java.util.List.of(
                    "Regular / convener quota",
                    "Management quota",
                    "Spot admission",
                    "Other",
                    "Not sure"
            )
    ));
}

if (!hasAnswer(answers, "attendancePercentage")) {
    return ResponseEntity.ok(new EligibilityQuestion(
        "attendancePercentage",
        "What is your approximate attendance percentage?",
        "number",
        true,
        java.util.List.of()
));
}


if (!hasAnswer(answers, "governmentEmployeeOrPensioner")) {
    return ResponseEntity.ok(new EligibilityQuestion(
            "governmentEmployeeOrPensioner",
            "Is anyone in your family a government employee or pensioner?",
            "select",
            true,
            java.util.List.of("Yes", "No", "Not sure")
    ));
}

if ("Yes".equalsIgnoreCase(
        answers.get("governmentEmployeeOrPensioner"))) {
    if (!hasAnswer(answers, "governmentEmployeeIsSanitaryWorker")) {
        return ResponseEntity.ok(new EligibilityQuestion(
                "governmentEmployeeIsSanitaryWorker",
                "Is that family member a sanitary worker?",
                "select",
                true,
                java.util.List.of("Yes", "No", "Not sure")
        ));
    }
}

if (!hasAnswer(answers, "ownsFourWheeler")) {
    return ResponseEntity.ok(new EligibilityQuestion(
            "ownsFourWheeler",
            "Does your family own a four-wheeler?",
            "select",
            true,
            java.util.List.of("Yes", "No", "Not sure")
    ));
}

if ("Yes".equalsIgnoreCase(answers.get("ownsFourWheeler"))) {
    if (!hasAnswer(answers, "fourWheelerType")) {
        return ResponseEntity.ok(new EligibilityQuestion(
                "fourWheelerType",
                "What type of four-wheeler does your family own?",
                "select",
                true,
                java.util.List.of(
                        "Private car",
                        "Taxi",
                        "Tractor",
                        "Auto",
                        "Other",
                        "Not sure"
                )
        ));
    }
}


if (!hasAnswer(answers, "familyIncomeTaxPayer")) {
    return ResponseEntity.ok(new EligibilityQuestion(
            "familyIncomeTaxPayer",
            "Does anyone in your family pay income tax?",
            "select",
            true,
            java.util.List.of("Yes", "No", "Not sure")
    ));
}


if (!hasAnswer(answers, "wetLandAcres")) {
    return ResponseEntity.ok(new EligibilityQuestion(
            "wetLandAcres",
            "How many acres of wet agricultural land does your family own? Enter 0 if none.",
            "number",
            true,
            java.util.List.of()
    ));
}

if (!hasAnswer(answers, "dryLandAcres")) {
    return ResponseEntity.ok(new EligibilityQuestion(
            "dryLandAcres",
            "How many acres of dry agricultural land does your family own? Enter 0 if none.",
            "number",
            true,
            java.util.List.of()
    ));
}



if (!hasAnswer(answers, "urbanBuiltUpSqFt")) {
    return ResponseEntity.ok(new EligibilityQuestion(
            "urbanBuiltUpSqFt",
            "What is the family's urban built-up property area in square feet? Enter 0 if none.",
            "number",
            true,
            java.util.List.of()
    ));
}



    return ResponseEntity.ok(new EligibilityQuestion(
            "complete",
            "You have answered the questions in this step.",
            "none",
            false,
            java.util.List.of()
    ));
}

private boolean hasAnswer(
        java.util.Map<String, String> answers, String field) {
    String answer = answers.get(field);
    return answer != null && !answer.isBlank();
}

private boolean hasValidAnswer(
        java.util.Map<String, String> answers,
        String field,
        java.util.List<String> allowedOptions) {

    String answer = answers.get(field);

    return answer != null
            && allowedOptions.stream()
                    .anyMatch(option -> option.equalsIgnoreCase(answer.trim()));
}


@PostMapping("/submit")
public ResponseEntity<?> submitEligibility(
        @RequestBody EligibilitySubmission submission) {

    if (submission == null
            || submission.state() == null
            || submission.state().isBlank()
            || submission.educationLevel() == null
            || submission.educationLevel().isBlank()
            || submission.answers() == null) {
        return ResponseEntity.badRequest()
                .body("Please provide state, education level, and answers.");
    }

    java.util.Map<String, String> answers = submission.answers();

    try {
        Double income = parseNumber(
                answers.get("annualFamilyIncome"));

        Double attendance = parseNumber(
                answers.get("attendancePercentage"));

        
EligibilityRequest request = new EligibilityRequest(
        submission.state(),
        submission.educationLevel(),
        income,
        answers.get("socialCategory"),
        answers.get("collegeType"),
        answers.get("studyMode"),
        answers.get("admissionQuota"),
        attendance,
        parseNumber(answers.get("wetLandAcres")),
        parseNumber(answers.get("dryLandAcres")),
        parseBoolean(answers.get("governmentEmployeeOrPensioner")),
        parseBoolean(answers.get("ownsFourWheeler")),
        parseNumber(answers.get("urbanBuiltUpSqFt")),
        parseBoolean(answers.get("familyIncomeTaxPayer")),
        parseBoolean(answers.get("governmentEmployeeIsSanitaryWorker")),
        answers.get("fourWheelerType")

);

        

        return ResponseEntity.ok(
                eligibilityService.checkEligibility(request));

    } catch (NumberFormatException exception) {
        return ResponseEntity.badRequest()
                .body("Income and attendance must be valid numbers.");
    }
}

private Double parseNumber(String value) {
    if (value == null || value.isBlank()
            || value.equalsIgnoreCase("Not sure")) {
        return null;
    }

    double number = Double.parseDouble(value.trim());

    if (!Double.isFinite(number) || number < 0) {
        throw new NumberFormatException("Invalid number");
    }

    return number;
}

private Boolean parseBoolean(String value) {
    if (value == null || value.isBlank()
            || value.equalsIgnoreCase("Not sure")) {
        return null;
    }

    if (value.equalsIgnoreCase("Yes")) {
        return true;
    }

    if (value.equalsIgnoreCase("No")) {
        return false;
    }

    throw new IllegalArgumentException("Expected Yes, No, or Not sure");
}

}

package com.civicsaathi.civic_saathi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.civicsaathi.civic_saathi.dto.EligibilityRequest;
import com.civicsaathi.civic_saathi.dto.EligibilityResult;

class SchemeEligibilityServiceTest {

    private SchemeEligibilityService service;

    @BeforeEach
    void setUp() {
        service = new SchemeEligibilityService();
    }

    private EligibilityRequest validRequest(
            Double wetLand, Double dryLand) {
        return validRequest(
                wetLand,
                dryLand,
                false,
                false,
                false,
                "Private car"
        );
    }

    private EligibilityRequest validRequest(
            Double wetLand,
            Double dryLand,
            Boolean governmentEmployee,
            Boolean sanitaryWorker,
            Boolean ownsFourWheeler,
            String vehicleType) {

        return new EligibilityRequest(
                "Andhra Pradesh",
                "Undergraduate",
                200000.0,
                "SC",
                "Government college",
                "Regular",
                "Convener",
                80.0,
                wetLand,
                dryLand,
                governmentEmployee,
                ownsFourWheeler,
                1000.0,
                false,
                sanitaryWorker,
                vehicleType
        );
    }

    @Test
    void landBelowConfiguredLimitsIsNotRejectedForLandholding() {
        EligibilityResult result =
                service.checkEligibility(validRequest(2.0, 5.0));

        assertTrue(result.matchedConditions().stream()
                .anyMatch(s -> s.toLowerCase().contains("landholding")));

        assertTrue(result.unmetConditions().stream()
                .noneMatch(s -> s.toLowerCase().contains("landholding")));
    }

    @Test
    void wetLandAtConfiguredLimitIsFlagged() {
        EligibilityResult result =
                service.checkEligibility(validRequest(10.0, 0.0));

        assertTrue(result.unmetConditions().stream()
                .anyMatch(s -> s.toLowerCase().contains("landholding")));
    }

    @Test
    void combinedLandAtConfiguredLimitIsFlagged() {
        EligibilityResult result =
                service.checkEligibility(validRequest(5.0, 20.0));

        assertTrue(result.unmetConditions().stream()
                .anyMatch(s -> s.toLowerCase().contains("landholding")));
    }

    @Test
    void missingLandholdingRequestsMoreInformation() {
        EligibilityResult result =
                service.checkEligibility(validRequest(null, 5.0));

        assertEquals("NEEDS_MORE_INFORMATION", result.status());

        assertTrue(result.missingInformation().stream()
                .anyMatch(s -> s.toLowerCase().contains("landholding")));
    }

    @Test
    void negativeLandholdingIsFlagged() {
        EligibilityResult result =
                service.checkEligibility(validRequest(-1.0, 2.0));

        assertTrue(result.unmetConditions().stream()
                .anyMatch(s -> s.toLowerCase().contains("landholding")
                        || s.toLowerCase().contains("negative")));
    }

    @Test
    void sanitaryWorkerAnswerIsHandledAsPotentialException() {
        EligibilityResult result = service.checkEligibility(
                validRequest(2.0, 5.0, true, true, false, "Private car"));

        assertTrue(result.matchedConditions().stream()
                .anyMatch(s -> s.toLowerCase().contains("sanitary-worker")));
    }

    @Test
    void taxiOwnershipIsHandledAsPotentialException() {
        EligibilityResult result = service.checkEligibility(
                validRequest(2.0, 5.0, false, false, true, "Taxi"));

        assertTrue(result.matchedConditions().stream()
                .anyMatch(s -> s.toLowerCase().contains("vehicle category")));
    }

    @Test
    void privateCarOwnershipIsFlaggedForReview() {
        EligibilityResult result = service.checkEligibility(
                validRequest(2.0, 5.0, false, false, true, "Private car"));

        assertTrue(result.unmetConditions().stream()
                .anyMatch(s -> s.toLowerCase().contains("vehicle ownership")));
    }
}
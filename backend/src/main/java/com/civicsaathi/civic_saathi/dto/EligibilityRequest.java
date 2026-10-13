
package com.civicsaathi.civic_saathi.dto;

public record EligibilityRequest(
        String state,
        String educationLevel,
        Double annualFamilyIncome,
        String socialCategory,
        String collegeType,
        String studyMode,
        String admissionQuota,
        Double attendancePercentage,
        Double wetLandAcres,
        Double dryLandAcres,
        Boolean governmentEmployeeOrPensioner,
        Boolean ownsFourWheeler,
        Double urbanBuiltUpSqFt,
        Boolean familyIncomeTaxPayer,
        Boolean governmentEmployeeIsSanitaryWorker,
        String fourWheelerType
) {
}

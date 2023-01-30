package com.sharevax.core.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sharevax.core.model.Suggestion;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SuggestionResponseDto {
    Integer suggestionId;
    Integer countryId;
    Suggestion.ApprovalStatus approvalStatus;
    Date currentDate;
}

package org.egov.web.models.jit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.models.AuditDetails;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PADetails {

    @JsonProperty("id")
    private String id;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("muktaReferenceId")
    private String muktaReferenceId;

    @JsonProperty("piId")
    private String piId;

    @JsonProperty("paBillRefNumber")
    private String paBillRefNumber;

    @JsonProperty("paFinYear")
    private String paFinYear;

    @JsonProperty("paAdviceId")
    private String paAdviceId;

    @JsonProperty("paAdviceDate")
    private String paAdviceDate;

    @JsonProperty("paTokenNumber")
    private String paTokenNumber;

    @JsonProperty("paTokenDate")
    private String paTokenDate;

    @JsonProperty("paErrorMsg")
    private String paErrorMsg;

    @JsonProperty("additionalDetails")
    private Object additionalDetails;

    @JsonProperty("auditDetails")

    private AuditDetails auditDetails;


}

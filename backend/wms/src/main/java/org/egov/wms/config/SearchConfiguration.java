package org.egov.wms.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class SearchConfiguration {

	@Value("${app.timezone}")
	private String timeZone;

	@PostConstruct
	public void initialize() {
		TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
	}

	@Value("${workflow.host}")
	private String workflowHost;

	@Value("${workflow.process.search.path}")
	private String processSearchPath;

	@Value("${workflow.businessservice.search.path}")
	private String businessServiceSearchPath;

	@Value("${workflow.process.count.path}")
	private String processCountPath;
	
	@Value("${workflow.process.nearing.sla.count.path}")
	private String nearingSlaProcessCountPath;

	@Value("${workflow.process.statuscount.path}")
	private String processStatusCountPath;

	@Value("${egov.mdms.host}")
	private String mdmsHost;

	@Value("${egov.mdms.search.endpoint}")
	private String mdmsSearchEndPoint;

	// ES Config

	@Value("${egov.es.username}")
	private String esUserName;

	@Value("${egov.es.password}")
	private String esPassword;

	@Value("${services.esindexer.host}")
	private String indexServiceHost;

	@Value("${egov.services.esindexer.host.search}")
	private String indexServiceHostSearchEndpoint;

	@Value("${parent.level.tenant.id}")
	private String parentLevelTenantId;

	@Value("${es.search.pagination.default.limit}")
	private Long defaultLimit;

	@Value("${es.search.pagination.default.offset}")
	private Long defaultOffset;

	@Value("${es.search.pagination.max.search.limit}")
	private Long maxSearchLimit;

	@Value("${state.level.tenant.id}")
	private String stateLevelTenantId;

	@Value("${es.search.default.sort.order}")
	private String defaultSortOrder;

	@Value("${egov.idgen.host}")
	private String idGenHost;

	@Value("${egov.idgen.path}")
	private String idGenPath;

	@Value("${egov.idgen.report.id.name}")
	private String reportIdName;

	@Value("${wms.kafka.report.create.topic}")
	private String reportTopic;

	@Value("${egov.filestore.host}")
	private String fileStoreHost;

	@Value("${egov.filestore.path}")
	private String fileStoreEndpoint;

	@Value("${report.search.pagination.default.limit}")
	private Integer reportDefaultLimit;

	@Value("${report.search.pagination.default.offset}")
	private Integer reportDefaultOffset;

	@Value("${wms.payment.tracker.module}")
	private String paymentTrackerModule;

	@Value("${wms.estimate.module}")
	private String estimateModule;
	@Value("${wms.kafka.report.update.topic}")
	private String reportUpdateTopic;
}
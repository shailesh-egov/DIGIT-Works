package org.egov.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.config.AttendanceServiceConfiguration;
import org.egov.enrichment.AttendanceLogEnrichment;
import org.egov.common.producer.Producer;
import org.egov.web.models.AttendanceLogSearchCriteria;
import org.egov.repository.AttendanceLogRepository;
import org.egov.util.ResponseInfoFactory;
import org.egov.validator.AttendanceLogServiceValidator;
import org.egov.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AttendanceLogService {
    private final AttendanceLogServiceValidator attendanceLogServiceValidator;

    private final ResponseInfoFactory responseInfoFactory;


    private final AttendanceLogEnrichment attendanceLogEnricher;

    private final Producer producer;

    private final AttendanceServiceConfiguration config;

    private final AttendanceLogRepository attendanceLogRepository;

    @Autowired
    public AttendanceLogService(AttendanceLogServiceValidator attendanceLogServiceValidator, ResponseInfoFactory responseInfoFactory, AttendanceLogEnrichment attendanceLogEnricher, Producer producer, AttendanceServiceConfiguration config, AttendanceLogRepository attendanceLogRepository) {
        this.attendanceLogServiceValidator = attendanceLogServiceValidator;
        this.responseInfoFactory = responseInfoFactory;
        this.attendanceLogEnricher = attendanceLogEnricher;
        this.producer = producer;
        this.config = config;
        this.attendanceLogRepository = attendanceLogRepository;
    }

    /**
     * Create Attendance Log
     *
     * @param attendanceLogRequest
     * @return attendanceLogResponse
     */
    public AttendanceLogResponse createAttendanceLog(AttendanceLogRequest attendanceLogRequest) {
        //Validate the incoming request
        attendanceLogServiceValidator.validateCreateAttendanceLogRequest(attendanceLogRequest);
        //Enrich the incoming request
        attendanceLogEnricher.enrichAttendanceLogCreateRequest(attendanceLogRequest);
        // Push the request object to the topic for persister to listen and persist
        producer.push(config.getCreateAttendanceLogTopic(), attendanceLogRequest);
        // Create the response
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(attendanceLogRequest.getRequestInfo(), true);
        AttendanceLogResponse attendanceLogResponse = AttendanceLogResponse.builder().responseInfo(responseInfo).attendance(attendanceLogRequest.getAttendance()).build();
        String registerId = attendanceLogRequest.getAttendance().get(0).getRegisterId();
        log.info("Attendance logs created successfully for register ["+registerId+"]");
        return attendanceLogResponse;
    }

    /**
     * Search attendace logs based on given search criteria
     *
     * @param requestInfoWrapper
     * @param searchCriteria
     * @return attendanceLogResponse
     */
    public AttendanceLogResponse searchAttendanceLog(RequestInfoWrapper requestInfoWrapper, AttendanceLogSearchCriteria searchCriteria) {
        //Validate the incoming request
        attendanceLogServiceValidator.validateSearchAttendanceLogRequest(requestInfoWrapper, searchCriteria);
        //Enrich the incoming request
        attendanceLogEnricher.enrichAttendanceLogSearchRequest(requestInfoWrapper.getRequestInfo(), searchCriteria);
        //Fetch attendance logs from registry
        List<AttendanceLog> attendanceLogs = attendanceLogRepository.getAttendanceLogs(searchCriteria);
        // Create the response
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true);
        AttendanceLogResponse attendanceLogResponse = AttendanceLogResponse.builder().responseInfo(responseInfo).attendance(attendanceLogs).build();
        log.info("Attendance log response created for register ["+searchCriteria.getRegisterId()+"]");
        return attendanceLogResponse;
    }

    /**
     * Update the given attendance log details
     *
     * @param attendanceLogRequest
     * @return AttendanceLogResponse
     */
    public AttendanceLogResponse updateAttendanceLog(AttendanceLogRequest attendanceLogRequest) {
        //Validate the incoming request
        attendanceLogServiceValidator.validateUpdateAttendanceLogRequest(attendanceLogRequest);
        //Enrich the incoming request
        attendanceLogEnricher.enrichAttendanceLogUpdateRequest(attendanceLogRequest);
        // Push the request object to the topic for persister to listen and persist
        producer.push(config.getUpdateAttendanceLogTopic(), attendanceLogRequest);
        // Create the response
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(attendanceLogRequest.getRequestInfo(), true);
        AttendanceLogResponse attendanceLogResponse = AttendanceLogResponse.builder().responseInfo(responseInfo).attendance(attendanceLogRequest.getAttendance()).build();
        String registerId = attendanceLogRequest.getAttendance().get(0).getRegisterId();
        log.info("Attendance logs updated successfully for register ["+registerId+"]");
        return attendanceLogResponse;
    }

    public void putInCache(List<AttendanceLog> attendanceLogs) {
        log.info("putting {} Attendance Logs in cache", attendanceLogs.size());
        attendanceLogRepository.putInCache(attendanceLogs);
        log.info("successfully put Attendance Logs in cache");
    }
}

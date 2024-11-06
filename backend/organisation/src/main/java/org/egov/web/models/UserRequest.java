package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import digit.models.coremodels.user.enums.BloodGroup;
import digit.models.coremodels.user.enums.Gender;
import digit.models.coremodels.user.enums.GuardianRelation;
import digit.models.coremodels.user.enums.UserType;
import lombok.*;
import org.egov.common.models.core.Role;
import org.egov.util.OrganisationConstant;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private Long id;

    @Size(max = 64)
    private String userName;

    @Size(max = 5)
    private String salutation;

    @Pattern(regexp = OrganisationConstant.PATTERN_NAME)
    @Size(max = 50)
    private String name;

    @Pattern(regexp = OrganisationConstant.PATTERN_GENDER)
    @Size(max = 15)
    private String gender;

    @Pattern(regexp = OrganisationConstant.PATTERN_MOBILE)
    private String mobileNumber;

    @Pattern(regexp = OrganisationConstant.PATTERN_MOBILE)
    private String alternatemobilenumber;

    @Size(max = 128)
    private String emailId;

    @Size(max = 50)
    private String altContactNumber;

    @Size(max = 10)
    private String pan;

    @Size(max = 20)
    private String aadhaarNumber;

    @Size(max = 300)
    private String permanentAddress;

    @Pattern(regexp = OrganisationConstant.PATTERN_CITY)
    @Size(max = 50)
    private String permanentCity;

    @Pattern(regexp = OrganisationConstant.PATTERN_PINCODE)
    @Size(max = 10)
    private String permanentPinCode;

    @Size(max = 300)
    private String correspondenceAddress;

    @Pattern(regexp = OrganisationConstant.PATTERN_CITY)
    @Size(max = 50)
    private String correspondenceCity;

    @Pattern(regexp = OrganisationConstant.PATTERN_PINCODE)
    @Size(max = 10)
    private String correspondencePinCode;
    private Boolean active;

    @Size(max = 16)
    private String locale;

    private UserType type;
    private Boolean accountLocked;
    private Long accountLockedDate;

    @Pattern(regexp = OrganisationConstant.PATTERN_NAME)
    @Size(max = 50)
    private String fatherOrHusbandName;
    private GuardianRelation relationship;

    @Size(max = 36)
    private String signature;

    @Size(max = 32)
    private String bloodGroup;

    @Size(max = 36)
    private String photo;

    @Size(max = 300)
    private String identificationMark;
    private Long createdBy;

    @Size(max = 64)
    private String password;

    private String otpReference;
    private Long lastModifiedBy;

    @Pattern(regexp = OrganisationConstant.PATTERN_TENANT)
    @Size(max = 50)
    private String tenantId;

    private Set<RoleRequest> roles;

    @Size(max = 36)
    private String uuid;


    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date lastModifiedDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dob;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date pwdExpiryDate;

    public UserRequest(User user) {

        this.id = user.getId();
        this.userName = user.getUsername();
        this.salutation = user.getSalutation();
        this.name = user.getName();
        this.gender = user.getGender() != null ? user.getGender().toString() : null;
        this.mobileNumber = user.getMobileNumber();
        this.emailId = user.getEmailId();
        this.altContactNumber = user.getAltContactNumber();
        this.pan = user.getPan();
        this.aadhaarNumber = user.getAadhaarNumber();
        this.active = user.getActive();
        this.dob = user.getDob();
        this.pwdExpiryDate = user.getPasswordExpiryDate();
        this.locale = user.getLocale();
        this.type = user.getType();
        this.accountLocked = user.getAccountLocked();
        this.accountLockedDate = user.getAccountLockedDate();
        this.signature = user.getSignature();
        this.bloodGroup = user.getBloodGroup() != null ? user.getBloodGroup().getValue() : null;
        this.photo = user.getPhoto();
        this.identificationMark = user.getIdentificationMark();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.tenantId = user.getTenantId();
        this.roles = convertDomainRoleToContract(user.getRoles());
        this.fatherOrHusbandName = user.getGuardian();
        this.relationship = user.getGuardianRelation();
        this.uuid = user.getUuid();
        this.alternatemobilenumber = user.getAlternateMobileNumber();
        mapPermanentAddress(user);
        mapCorrespondenceAddress(user);
    }

    private void mapCorrespondenceAddress(User user) {
        if (user.getCorrespondenceAddress() != null) {
            this.correspondenceCity = user.getCorrespondenceAddress().getCity();
            this.correspondencePinCode = user.getCorrespondenceAddress().getPincode();
        }
    }

    private void mapPermanentAddress(User user) {
        if (user.getPermanentAddress() != null) {
            this.permanentCity = user.getPermanentAddress().getCity();
            this.permanentPinCode = user.getPermanentAddress().getPincode();
        }
    }

    private Set<RoleRequest> convertDomainRoleToContract(Set<Role> roleEntities) {
        if (roleEntities == null) return new HashSet<>();
        return roleEntities.stream().map(RoleRequest::new).collect(Collectors.toSet());
    }

    @JsonIgnore
    public User toDomain(Long loggedInUserId, String loggedInUserUuid, boolean isCreate) {
        BloodGroup bloodGroups = null;
        try {
            if (this.bloodGroup != null)
                bloodGroups = BloodGroup.valueOf(this.bloodGroup.toUpperCase());
        } catch (Exception e) {
            bloodGroups = BloodGroup.fromValue(this.bloodGroup);
        }
        return User.builder()
                .uuid(this.uuid)
                .id(this.id)
                .name(this.name)
                .username(this.userName)
                .salutation(this.salutation)
                .mobileNumber(this.mobileNumber)
                .emailId(this.emailId)
                .altContactNumber(this.altContactNumber)
                .pan(this.pan)
                .aadhaarNumber(this.aadhaarNumber)
                .active(isActive(isCreate))
                .dob(this.dob)
                .passwordExpiryDate(this.pwdExpiryDate)
                .locale(this.locale)
                .type(this.type)
                .accountLocked(isAccountLocked(isCreate))
                .accountLockedDate(this.accountLockedDate)
                .signature(this.signature)
                .photo(this.photo)
                .identificationMark(this.identificationMark)
                .gender(this.gender != null ? Gender.valueOf(this.gender.toUpperCase()) : null)
                .bloodGroup(bloodGroups)
                .lastModifiedDate(new Date())
                .createdDate(new Date())
                .otpReference(this.otpReference)
                .tenantId(this.tenantId)
                .password(this.password)
                .roles(toDomainRoles())
                .loggedInUserId(loggedInUserId)
                .loggedInUserUuid(loggedInUserUuid)
                .permanentAddress(toDomainPermanentAddress())
                .correspondenceAddress(toDomainCorrespondenceAddress())
                .guardian(fatherOrHusbandName)
                .guardianRelation(relationship).alternateMobileNumber(this.alternatemobilenumber)
                .build();
    }

    private Boolean isActive(boolean isCreate) {
        if (this.active == null && isCreate) {
            return false;
        }
        return this.active;
    }

    private Boolean isAccountLocked(boolean isCreate) {
        if (this.accountLocked == null && isCreate) {
            return false;
        }
        return this.accountLocked;
    }

    private Address toDomainPermanentAddress() {
        return Address.builder()
                //.type(AddressType.PERMANENT)
                .city(permanentCity)
                .pincode(permanentPinCode)
                // .address(permanentAddress)
                .build();
    }

    private Address toDomainCorrespondenceAddress() {
        return Address.builder()
                //.type(AddressType.CORRESPONDENCE)
                .city(correspondenceCity)
                .pincode(correspondencePinCode)
                //.address(correspondenceAddress)
                .build();
    }

    private Set<Role> toDomainRoles() {
        return this.roles != null
                ? this.roles.stream()
                .map(RoleRequest::toDomain)
                .distinct()
                .collect(Collectors.toSet())
                : null;
    }
}

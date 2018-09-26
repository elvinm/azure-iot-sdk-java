/*
 *
 *  Copyright (c) Microsoft. All rights reserved.
 *  Licensed under the MIT license. See LICENSE file in the project root for full license information.
 *
 */

package com.microsoft.azure.sdk.iot.provisioning.device.internal.parser;

import com.google.gson.annotations.SerializedName;

/**
 * Class that represents the REST API format for DeviceRegistrationResult
 * Format : https://docs.microsoft.com/en-us/rest/api/iot-dps/RuntimeRegistration/RegisterDevice#definitions_deviceregistrationresult
 */
public class DeviceRegistrationResultParser
{
    private static final String REGISTRATION_ID = "registrationId";
    private static final String CREATED_DATE_TIME_UTC = "createdDateTimeUtc";
    private static final String ASSIGNED_HUB = "assignedHub";
    private static final String DEVICE_ID = "deviceId";
    private static final String STATUS = "status";
    private static final String ETAG = "etag";
    private static final String LAST_UPDATES_DATE_TIME_UTC = "lastUpdatedDateTimeUtc";
    private static final String ERROR_CODE = "errorCode";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String TPM = "tpm";
    private static final String X509 = "x509";
    @SerializedName(REGISTRATION_ID)
    private String registrationId;
    @SerializedName(CREATED_DATE_TIME_UTC)
    private String createdDateTimeUtc;
    @SerializedName(ASSIGNED_HUB)
    private String assignedHub;
    @SerializedName(DEVICE_ID)
    private String deviceId;
    @SerializedName(STATUS)
    private String status;
    @SerializedName(ETAG)
    private String eTag;
    @SerializedName(LAST_UPDATES_DATE_TIME_UTC)
    private String lastUpdatesDateTimeUtc;
    @SerializedName(ERROR_CODE)
    private Integer errorCode;
    @SerializedName(ERROR_MESSAGE)
    private String errorMessage;
    @SerializedName(TPM)
    private TpmRegistrationResultParser tpm;
    @SerializedName(X509)
    private X509RegistrationResultParser x509;

    //empty constructor for Gson
    DeviceRegistrationResultParser()
    {
    }

    /**
     * Getter for Registration Id
     *
     * @return Getter for Registration Id
     */
    public String getRegistrationId()
    {
        //SRS_DeviceRegistrationResultParser_25_001: [ This method shall return the parsed registrationId. ]
        return registrationId;
    }

    /**
     * Getter for CreatedDateTimeUtc
     *
     * @return Getter for CreatedDateTimeUtc
     */
    public String getCreatedDateTimeUtc()
    {
        //SRS_DeviceRegistrationResultParser_25_002: [ This method shall return the parsed createdDateTimeUtc. ]
        return createdDateTimeUtc;
    }

    /**
     * Getter for Assigned Iot Hub
     *
     * @return Getter for Assigned Iot Hub
     */
    public String getAssignedHub()
    {
        //SRS_DeviceRegistrationResultParser_25_003: [ This method shall return the parsed assignedHub. ]
        return assignedHub;
    }

    /**
     * Getter for Device Id
     *
     * @return Getter for Device Id
     */
    public String getDeviceId()
    {
        //SRS_DeviceRegistrationResultParser_25_004: [ This method shall return the parsed deviceId. ]
        return deviceId;
    }

    /**
     * Getter for Status
     *
     * @return Getter for Status
     */
    public String getStatus()
    {
        //SRS_DeviceRegistrationResultParser_25_005: [ This method shall return the parsed status. ]
        return status;
    }

    /**
     * Getter for Etag
     *
     * @return Getter for Etag
     */
    public String getEtag()
    {
        //SRS_DeviceRegistrationResultParser_25_006: [ This method shall return the parsed eTag. ]
        return eTag;
    }

    /**
     * Getter for LastUpdatesDateTimeUtc
     *
     * @return Getter for LastUpdatesDateTimeUtc
     */
    public String getLastUpdatesDateTimeUtc()
    {
        //SRS_DeviceRegistrationResultParser_25_007: [ This method shall return the parsed lastUpdatesDateTimeUtc. ]
        return lastUpdatesDateTimeUtc;
    }

    /**
     * Getter for the object TpmRegistrationResultParser
     * https://docs.microsoft.com/en-us/rest/api/iot-dps/RuntimeRegistration/RegisterDevice#definitions_tpmregistrationresult
     *
     * @return Getter for the object TpmRegistrationResultParser
     */
    public TpmRegistrationResultParser getTpm()
    {
        //SRS_DeviceRegistrationResultParser_25_008: [ This method shall return the parsed TpmRegistrationResultParser Object. ]
        return tpm;
    }

    /**
     * Getter for the object X509RegistrationResultParser
     * https://docs.microsoft.com/en-us/rest/api/iot-dps/RuntimeRegistration/RegisterDevice#definitions_x509registrationresult
     *
     * @return Getter for the object X509RegistrationResultParser
     */
    public X509RegistrationResultParser getX509()
    {
        //SRS_DeviceRegistrationResultParser_25_009: [ This method shall return the parsed X509RegistrationResultParser object. ]
        return x509;
    }

    /**
     * Getter for Error Code
     *
     * @return Getter for Error Code
     */
    public Integer getErrorCode()
    {
        //SRS_DeviceRegistrationResultParser_25_010: [ This method shall return the parsed errorCode. ]
        return errorCode;
    }

    /**
     * Getter for Error Message
     *
     * @return Getter for Error Message
     */
    public String getErrorMessage()
    {
        //SRS_DeviceRegistrationResultParser_25_011: [ This method shall return the parsed errorMessage. ]
        return errorMessage;
    }
}

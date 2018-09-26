// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.microsoft.azure.sdk.iot.deps.twin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.microsoft.azure.sdk.iot.deps.serializer.ParserUtility;

/**
 * Twin management representation
 * <p>
 * This class is part of the Twin. It contains the Device identity management.
 */
public class RegisterManager
{
    /**
     * Device name
     * A case-sensitive string (up to 128 char long)
     * of ASCII 7-bit alphanumeric chars
     * + {'-', ':', '.', '+', '%', '_', '#', '*', '?', '!', '(', ')', ',', '=', '@', ';', '$', '''}.
     */
    private static final String DEVICE_ID_TAG = "deviceId";
    /**
     * Module name
     * A case-sensitive string (up to 128 char long)
     * of ASCII 7-bit alphanumeric chars
     * + {'-', ':', '.', '+', '%', '_', '#', '*', '?', '!', '(', ')', ',', '=', '@', ';', '$', '''}.
     */
    private static final String MODULE_ID_TAG = "moduleId";
    /**
     * Device generation Id
     */
    private static final String GENERATION_ID_TAG = "generationId";
    /**
     * A string representing a weak ETAG version
     * of this JSON description. This is a hash.
     */
    private static final String ETAG_TAG = "etag";
    /**
     * An Integer representing a Twin version.
     */
    private static final String VERSION_TAG = "version";
    /**
     * "Enabled", "Disabled".
     * If "enabled", this device is authorized to connect.
     * If "disabled" this device cannot receive or send messages, and statusReason must be set.
     */
    private static final String STATUS_TAG = "status";
    /**
     * A 128 char long string storing the reason of suspension.
     * (all UTF-8 chars allowed).
     */
    private static final String STATUS_REASON_TAG = "statusReason";
    /**
     * Datetime of last time the state was updated.
     */
    private static final String STATUS_UPDATED_TIME_TAG = "statusUpdatedTime";
    /**
     * Status of the device:
     * {"connected" | "disconnected"}
     */
    private static final String CONNECTION_STATE_TAG = "connectionState";
    /**
     * Datetime of last time the connection state was updated.
     */
    private static final String CONNECTION_STATE_UPDATED_TIME_TAG = "connectionStateUpdatedTime";
    /**
     * Datetime of last time the device authenticated, received, or sent a message.
     */
    private static final String LAST_ACTIVITY_TIME_TAG = "lastActivityTime";
    /**
     * Datetime of last time the device authenticated, received, or sent a message.
     */
    private static final String CAPABILITIES_TAG = "capabilities";
    @Expose(serialize = true, deserialize = true)
    @SerializedName(DEVICE_ID_TAG)
    protected String deviceId = null;
    @Expose(serialize = true, deserialize = true)
    @SerializedName(MODULE_ID_TAG)
    protected String moduleId = null;
    @Expose(serialize = true, deserialize = true)
    @SerializedName(GENERATION_ID_TAG)
    protected String generationId = null;
    @Expose(serialize = true, deserialize = true)
    @SerializedName(ETAG_TAG)
    protected String eTag = null;
    @Expose(serialize = true, deserialize = true)
    @SerializedName(VERSION_TAG)
    protected Integer version = null;
    @Expose(serialize = true, deserialize = true)
    @SerializedName(STATUS_TAG)
    protected TwinStatus status = null;
    @Expose(serialize = true, deserialize = true)
    @SerializedName(STATUS_REASON_TAG)
    protected String statusReason = null;
    @Expose(serialize = true, deserialize = true)
    @SerializedName(STATUS_UPDATED_TIME_TAG)
    protected String statusUpdatedTime = null;
    @Expose(serialize = true, deserialize = true)
    @SerializedName(CONNECTION_STATE_TAG)
    protected TwinConnectionState connectionState = null;
    @Expose(serialize = true, deserialize = true)
    @SerializedName(CONNECTION_STATE_UPDATED_TIME_TAG)
    protected String connectionStateUpdatedTime = null;
    @Expose(serialize = true, deserialize = true)
    @SerializedName(LAST_ACTIVITY_TIME_TAG)
    protected String lastActivityTime = null;
    @Expose(serialize = true, deserialize = true)
    @SerializedName(CAPABILITIES_TAG)
    protected DeviceCapabilities capabilities = null;


    /**
     * Empty constructor
     * <p>
     * <p>
     * Used only by the tools that will deserialize this class.
     * </p>
     */
    @SuppressWarnings("unused")
    RegisterManager()
    {
        /* Codes_SRS_REGISTER_MANAGER_21_007: [The RegisterManager shall provide an empty constructor to make GSON happy.] */
    }

    /**
     * Getter for the ETag
     *
     * @return The {@code String} with the stored ETag.
     */
    public String getETag()
    {
        /* Codes_SRS_REGISTER_MANAGER_21_004: [The getETag shall return the stored `eTag` content.] */
        return this.eTag;
    }

    /**
     * Setter for the ETag
     *
     * @param eTag the {@code String} that contains the new ETag.
     */
    public void setETag(String eTag)
    {
        /* Codes_SRS_REGISTER_MANAGER_21_003: [The setETag shall replace the `eTag` by the provided one.] */
        this.eTag = eTag;
    }

    /**
     * Getter for the DeviceId
     *
     * @return The {@code String} with the stored DeviceID.
     */
    public String getDeviceId()
    {
        /* Codes_SRS_REGISTER_MANAGER_21_005: [The getDeviceId shall return the stored `deviceId` content.] */
        return this.deviceId;
    }

    /**
     * Setter for the DeviceId
     *
     * @param deviceId the {@code String} that contains the new DeviceID.
     * @throws IllegalArgumentException If the new DeviceId do not fits the ID criteria.
     */
    public void setDeviceId(String deviceId) throws IllegalArgumentException
    {
        /* Codes_SRS_REGISTER_MANAGER_21_001: [The setDeviceId shall throw IllegalArgumentException if the provided deviceId do not fits the criteria.] */
        ParserUtility.validateId(deviceId);

        /* Codes_SRS_REGISTER_MANAGER_21_002: [The setDeviceId shall replace the `deviceId` by the provided one.] */
        this.deviceId = deviceId;
    }

    /**
     * Getter for the ModuleId
     *
     * @return The {@code String} with the stored ModuleId.
     */
    public String getModuleId()
    {
        /* Codes_SRS_REGISTER_MANAGER_28_003: [The getModuleId shall return the stored `moduleId` content.] */
        return this.moduleId;
    }

    /**
     * Setter for the ModuleId
     *
     * @param moduleId the {@code String} that contains the new ModuleId.
     * @throws IllegalArgumentException If the new ModuleId do not fits the ID criteria.
     */
    public void setModuleId(String moduleId) throws IllegalArgumentException
    {
        /* Codes_SRS_REGISTER_MANAGER_28_001: [The setModuleId shall throw IllegalArgumentException if the provided moduleId do not fits the criteria.] */
        ParserUtility.validateId(moduleId);

        /* Codes_SRS_REGISTER_MANAGER_28_002: [The setModuleId shall replace the `moduleId` by the provided one.] */
        this.moduleId = moduleId;
    }

    /**
     * Getter for the Capabilities
     *
     * @return The {@code String} with the stored ModuleId.
     */
    public DeviceCapabilities getCapabilities()
    {
        /* Codes_SRS_REGISTER_MANAGER_28_004: [The getModuleId shall return the stored `moduleId` content.] */
        return this.capabilities;
    }

    /**
     * Setter for the Capabilities
     *
     * @param capabilities the capabilities
     * @throws IllegalArgumentException If the new capabilities do not fits the ID criteria.
     */
    public void setCapabilities(DeviceCapabilities capabilities) throws IllegalArgumentException
    {
        /* Codes_SRS_REGISTER_MANAGER_28_005: [The setModuleId shall replace the `moduleId` by the provided one.] */
        this.capabilities = capabilities;
    }

    /**
     * Getter for the Version
     *
     * @return The {@code Integer} with the stored version.
     */
    public Integer getVersion()
    {
        /* Codes_SRS_REGISTER_MANAGER_21_006: [The getVersion shall return the stored `version` content.] */
        return this.version;
    }
}

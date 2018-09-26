/*
 * Copyright (c) Microsoft. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */

package com.microsoft.azure.sdk.iot.service;

import java.time.Instant;

/**
 * Data structure for feedback record received
 * Provide getters and setters for feedback record batch properties
 */
public class FeedbackRecord
{
    private Instant enqueuedTimeUtc;
    private String originalMessageId;
    private String correlationId;
    private FeedbackStatusCode statusCode;
    private String description;
    private String deviceGenerationId;
    private String deviceId;

    public Instant getEnqueuedTimeUtc()
    {
        return enqueuedTimeUtc;
    }

    public void setEnqueuedTimeUtc(Instant enqueuedTimeUtc)
    {
        this.enqueuedTimeUtc = enqueuedTimeUtc;
    }

    public String getOriginalMessageId()
    {
        return originalMessageId;
    }

    public void setOriginalMessageId(String originalMessageId)
    {
        this.originalMessageId = originalMessageId;
    }

    public String getCorrelationId()
    {
        return correlationId;
    }

    public void setCorrelationId(String correlationId)
    {
        this.correlationId = correlationId;
    }

    public FeedbackStatusCode getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(FeedbackStatusCode statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDeviceGenerationId()
    {
        return deviceGenerationId;
    }

    public void setDeviceGenerationId(String deviceGenerationId)
    {
        this.deviceGenerationId = deviceGenerationId;
    }

    public String getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

}

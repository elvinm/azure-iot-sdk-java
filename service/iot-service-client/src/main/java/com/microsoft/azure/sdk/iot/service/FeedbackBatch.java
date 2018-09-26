/*
 * Copyright (c) Microsoft. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */

package com.microsoft.azure.sdk.iot.service;

import java.time.Instant;
import java.util.ArrayList;

/**
 * Data structure for feedback messages received in Json array
 * Provide getters and setters for batch properties and messages
 */
public class FeedbackBatch
{
    private Instant enqueuedTimeUtc;
    private String userId;
    private String lockToken;
    private ArrayList<FeedbackRecord> records;

    public Instant getEnqueuedTimeUtc()
    {
        return enqueuedTimeUtc;
    }

    public void setEnqueuedTimeUtc(Instant enqueuedTimeUtc)
    {
        this.enqueuedTimeUtc = enqueuedTimeUtc;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getLockToken()
    {
        return lockToken;
    }

    public void setLockToken(String lockToken)
    {
        this.lockToken = lockToken;
    }

    public ArrayList<FeedbackRecord> getRecords()
    {
        return records;
    }

    public void setRecords(ArrayList<FeedbackRecord> records)
    {
        this.records = records;
    }
}

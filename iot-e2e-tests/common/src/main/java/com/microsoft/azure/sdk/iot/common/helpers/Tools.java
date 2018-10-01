/*
*  Copyright (c) Microsoft. All rights reserved.
*  Licensed under the MIT license. See LICENSE file in the project root for full license information.
*/

package com.microsoft.azure.sdk.iot.common.helpers;

import com.microsoft.azure.sdk.iot.service.IotHubConnectionString;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools
{
    public static String retrieveEnvironmentVariableValue(String environmentVariableName)
    {
        String environmentVariableValue = System.getenv().get(environmentVariableName);
        if ((environmentVariableValue == null) || environmentVariableValue.isEmpty())
        {
            environmentVariableValue = System.getProperty(environmentVariableName);
            if (environmentVariableValue == null || environmentVariableValue.isEmpty())
            {
                throw new IllegalArgumentException("Environment variable is not set: " + environmentVariableName);
            }
        }

        return environmentVariableValue;
    }

    /**
     * Checks if the provided exception contains a certain type of exception in its cause chain
     * @param possibleExceptionCause the type of exception to be searched for
     * @param exceptionToSearch the exception to search the stacktrace of
     * @return if any variant of the possibleExceptionCause is found at any depth of the exception cause chain
     */
    public static boolean isCause(Class<? extends Throwable> possibleExceptionCause, Throwable exceptionToSearch)
    {
        return possibleExceptionCause.isInstance(exceptionToSearch) || (exceptionToSearch != null && isCause(possibleExceptionCause, exceptionToSearch.getCause()));
    }

    public static String buildExceptionMessage(String baseMessage, String hostname, String deviceId, String protocol, String moduleId)
    {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String correlationString = ": Correlation details : Hostname:" + hostname + " Device id: " + deviceId;

        if (moduleId != null && !moduleId.isEmpty())
        {
            correlationString = correlationString + " Module id: " + moduleId;
        }

        correlationString = correlationString  + " Protocol: " + protocol + " Timestamp: " + timeStamp;

        return baseMessage + correlationString;
    }
}
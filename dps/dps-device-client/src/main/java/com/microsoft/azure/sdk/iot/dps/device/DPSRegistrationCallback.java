/*
 * Copyright (c) Microsoft. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for full license information.
 *
 */

package com.microsoft.azure.sdk.iot.dps.device;

public interface DPSRegistrationCallback
{
    void run(DPSRegistrationInfo dpsRegistrationInfo, Object context);
}
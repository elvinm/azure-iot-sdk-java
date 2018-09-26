// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.microsoft.azure.sdk.iot.deps.serializer;

import com.google.gson.annotations.SerializedName;

/**
 * INNER TWINPARSER CLASS
 * <p>
 * Enum for device connection state
 *
 * @deprecated As of release 0.4.0, replaced by {@link com.microsoft.azure.sdk.iot.deps.twin.TwinConnectionState}
 */
@Deprecated
public enum TwinConnectionState
{
    @SerializedName("disconnected") disconnected,

    @SerializedName("connected") connected
}

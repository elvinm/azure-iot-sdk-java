/*
 *  Copyright (c) Microsoft. All rights reserved.
 *  Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */

package com.microsoft.azure.sdk.iot.android.iothubservices;

import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import com.microsoft.azure.sdk.iot.android.helper.Tools;
import com.microsoft.azure.sdk.iot.common.TestConstants;
import com.microsoft.azure.sdk.iot.common.helpers.DeviceTestManager;
import com.microsoft.azure.sdk.iot.common.iothubservices.DeviceMethodCommon;
import com.microsoft.azure.sdk.iot.deps.util.Base64;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.exceptions.ModuleClientException;
import com.microsoft.azure.sdk.iot.service.Device;
import com.microsoft.azure.sdk.iot.service.Module;
import com.microsoft.azure.sdk.iot.service.auth.AuthenticationType;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Collection;

@RunWith(Parameterized.class)
public class DeviceMethodIT extends DeviceMethodCommon
{
    //This function is run before even the @BeforeClass annotation, so it is used as the @BeforeClass method
    @Parameterized.Parameters(name = "{1} with {2} auth using {3}")
    public static Collection inputsCommons() throws IOException, IotHubException, GeneralSecurityException, URISyntaxException, InterruptedException, ModuleClientException
    {
        Bundle bundle = InstrumentationRegistry.getArguments();
        iotHubConnectionString = Tools.retrieveEnvironmentVariableValue(TestConstants.IOT_HUB_CONNECTION_STRING_ENV_VAR_NAME, bundle);
        x509Thumbprint = Tools.retrieveEnvironmentVariableValue("IOTHUB_E2E_X509_THUMBPRINT", bundle);
        String privateKeyBase64Encoded = Tools.retrieveEnvironmentVariableValue("IOTHUB_E2E_X509_PRIVATE_KEY_BASE64", bundle);
        String publicKeyCertBase64Encoded = Tools.retrieveEnvironmentVariableValue("IOTHUB_E2E_X509_CERT_BASE64", bundle);
        privateKey = new String(Base64.decodeBase64Local(privateKeyBase64Encoded.getBytes()));
        publicKeyCert = new String(Base64.decodeBase64Local(publicKeyCertBase64Encoded.getBytes()));

        return DeviceMethodCommon.inputsCommon();
    }

    public DeviceMethodIT(DeviceTestManager deviceTestManager, IotHubClientProtocol protocol, AuthenticationType authenticationType, String clientType, Device device, Module module)
    {
        super(deviceTestManager, protocol, authenticationType, clientType, device, module);
    }
}

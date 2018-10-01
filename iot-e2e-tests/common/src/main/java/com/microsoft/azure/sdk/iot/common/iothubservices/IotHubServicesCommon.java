/*
 *  Copyright (c) Microsoft. All rights reserved.
 *  Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */

package com.microsoft.azure.sdk.iot.common.iothubservices;

import com.microsoft.azure.sdk.iot.common.*;
import com.microsoft.azure.sdk.iot.common.helpers.Tools;
import com.microsoft.azure.sdk.iot.device.*;
import com.microsoft.azure.sdk.iot.device.transport.IotHubConnectionStatus;
import com.microsoft.azure.sdk.iot.service.auth.AuthenticationType;
import org.junit.Assert;

import javax.tools.Tool;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

/*
 * This class contains common code for Junit and Android test cases
 */
public class IotHubServicesCommon
{
    private final static long OPEN_RETRY_TIMEOUT = 3*60*1000; //3 minutes, or about 3 retries if open's keep timing out

    //if error injection message has not taken effect after 1 minute, the test will timeout
    private final static long ERROR_INJECTION_MESSAGE_EFFECT_TIMEOUT = 1 * 60 * 1000;

    /*
     * method to send message over given DeviceClient
     */
    public static void sendMessages(InternalClient client,
                                    IotHubClientProtocol protocol,
                                    List<MessageAndResult> messagesToSend,
                                    final long RETRY_MILLISECONDS,
                                    final long SEND_TIMEOUT_MILLISECONDS,
                                    long interMessageDelay,
                                    List<IotHubConnectionStatus> statusUpdates,
                                    String hostname,
                                    String deviceId,
                                    String moduleId) throws IOException, InterruptedException
    {
        openClientWithRetry(client, hostname, deviceId, moduleId, protocol.toString());

        for (int i = 0; i < messagesToSend.size(); ++i)
        {
            if (isErrorInjectionMessage(messagesToSend.get(i)))
            {
                //error injection message is not guaranteed to be ack'd by service so it may be re-sent. By setting expiry time,
                // we ensure that error injection message isn't resent to service too many times. The message will still likely
                // be sent 3 or 4 times causing 3 or 4 disconnections, but the test should recover anyways.
                messagesToSend.get(i).message.setExpiryTime(200);
            }

            sendMessageAndWaitForResponse(client, messagesToSend.get(i), RETRY_MILLISECONDS, SEND_TIMEOUT_MILLISECONDS, protocol, hostname, deviceId, moduleId);

            if (isErrorInjectionMessage(messagesToSend.get(i)))
            {
                //wait until error injection message takes affect before sending the next message
                long startTime = System.currentTimeMillis();
                while (!statusUpdates.contains(IotHubConnectionStatus.DISCONNECTED_RETRYING))
                {
                    Thread.sleep(300);

                    if (System.currentTimeMillis() - startTime > ERROR_INJECTION_MESSAGE_EFFECT_TIMEOUT)
                    {
                        fail(Tools.buildExceptionMessage("Sending message over " + protocol + " protocol failed: Error injection message never caused connection to be lost", hostname, deviceId, protocol.toString(), moduleId));
                    }
                }
            }
            else
            {
                Thread.sleep(interMessageDelay);
            }
        }

        client.closeNow();
    }

    public static void sendMessagesExpectingConnectionStatusChangeUpdate(InternalClient client,
                                                                         IotHubClientProtocol protocol,
                                                                         List<MessageAndResult> messagesToSend,
                                                                         final long RETRY_MILLISECONDS,
                                                                         final long SEND_TIMEOUT_MILLISECONDS,
                                                                         final IotHubConnectionStatus expectedStatus,
                                                                         int interMessageDelay,
                                                                         AuthenticationType authType,
                                                                         String hostname,
                                                                         String deviceId,
                                                                         String moduleId) throws IOException, InterruptedException
    {
        final List<IotHubConnectionStatus> actualStatusUpdates = new ArrayList<>();
        client.registerConnectionStatusChangeCallback(new IotHubConnectionStatusChangeCallback()
        {
            @Override
            public void execute(IotHubConnectionStatus status, IotHubConnectionStatusChangeReason statusChangeReason, Throwable throwable, Object callbackContext) {
                actualStatusUpdates.add(status);
            }
        }, new Object());

        sendMessages(client, protocol, messagesToSend, RETRY_MILLISECONDS, SEND_TIMEOUT_MILLISECONDS, interMessageDelay, actualStatusUpdates, hostname, deviceId, moduleId);

        assertTrue(Tools.buildExceptionMessage(protocol + ", " + authType + ": Expected connection status update to occur: " + expectedStatus, hostname, deviceId, protocol.toString(), moduleId), actualStatusUpdates.contains(expectedStatus));
    }

    /**
     * Send some messages that wait for callbacks to signify that the SAS token in the client config has expired.
     *
     * @param client the client to send the messages from
     * @param protocol the protocol the client is using
     */
    public static void sendMessagesExpectingSASTokenExpiration(DeviceClient client,
                                                               String protocol,
                                                               int numberOfMessages,
                                                               long retryMilliseconds,
                                                               long timeoutMilliseconds,
                                                               AuthenticationType authType,
                                                               String hostname,
                                                               String deviceId,
                                                               String moduleId)
    {
        for (int i = 0; i < numberOfMessages; ++i)
        {
            try
            {
                Message messageToSend = new Message("Test message expecting SAS Token Expired callback for protocol: " + protocol);
                Success messageSent = new Success();
                Success statusUpdated = new Success();

                ConnectionStatusCallback stateCallback = new ConnectionStatusCallback(IotHubConnectionState.SAS_TOKEN_EXPIRED);
                EventCallback messageCallback = new EventCallback(IotHubStatusCode.UNAUTHORIZED);

                client.registerConnectionStateCallback(stateCallback, statusUpdated);
                client.sendEventAsync(messageToSend, messageCallback, messageSent);

                long startTime = System.currentTimeMillis();
                while(!messageSent.wasCallbackFired() || !statusUpdated.getResult())
                {
                    Thread.sleep(retryMilliseconds);
                    if (System.currentTimeMillis() - startTime > timeoutMilliseconds)
                    {
                        fail(protocol + ", " + authType + ": Sending message over " + protocol + " protocol failed: " +
                                "never received connection status update for SAS_TOKEN_EXPIRED " +
                                "or never received UNAUTHORIZED message callback");
                    }
                }

                if (messageSent.getCallbackStatusCode() != IotHubStatusCode.UNAUTHORIZED)
                {
                    fail(protocol + ", " + authType + ": Send messages expecting sas token expiration failed: expected UNAUTHORIZED message callback, but got " + messageSent.getCallbackStatusCode());
                }
            }
            catch (Exception e)
            {
                Assert.fail(Tools.buildExceptionMessage(protocol + ", " + authType + ": Sending message over " + protocol + " protocol failed", hostname, deviceId, protocol, moduleId));
            }
        }
    }

    /*
     * method to send message over given DeviceClient
     */
    public static void sendMessagesMultiplex(DeviceClient client,
                                             IotHubClientProtocol protocol,
                                             final int NUM_MESSAGES_PER_CONNECTION,
                                             final long RETRY_MILLISECONDS,
                                             final long SEND_TIMEOUT_MILLISECONDS,
                                             String hostname,
                                             String deviceId,
                                             String moduleId)
    {
        String messageString = "Java client e2e test message over " + protocol + " protocol";
        Message msg = new Message(messageString);

        for (int i = 0; i < NUM_MESSAGES_PER_CONNECTION; ++i)
        {
            try
            {
                Success messageSent = new Success();
                EventCallback callback = new EventCallback(IotHubStatusCode.OK_EMPTY);
                client.sendEventAsync(msg, callback, messageSent);

                long startTime = System.currentTimeMillis();
                while (!messageSent.wasCallbackFired())
                {
                    Thread.sleep(RETRY_MILLISECONDS);
                    if (System.currentTimeMillis() - startTime > SEND_TIMEOUT_MILLISECONDS)
                    {
                        fail("Timed out waiting for message callback");
                    }
                }

                if (messageSent.getCallbackStatusCode() != IotHubStatusCode.OK_EMPTY)
                {
                    Assert.fail(Tools.buildExceptionMessage("Sending message over " + protocol + " protocol failed: expected status code OK_EMPTY but received: " + messageSent.getCallbackStatusCode(), hostname, deviceId, protocol.toString(), moduleId));
                }
            }
            catch (Exception e)
            {
                Assert.fail(Tools.buildExceptionMessage("Sending message over " + protocol + " protocol failed: Exception encountered while sending messages: " + e.getMessage(), hostname, deviceId, protocol.toString(), moduleId));
            }
        }
    }

    public static void sendExpiredMessageExpectingMessageExpiredCallback(InternalClient client,
                                                                         IotHubClientProtocol protocol,
                                                                         final long RETRY_MILLISECONDS,
                                                                         final long SEND_TIMEOUT_MILLISECONDS,
                                                                         AuthenticationType authType,
                                                                         String hostname,
                                                                         String deviceId,
                                                                         String moduleId) throws IOException
    {
        try
        {
            Message expiredMessage = new Message("This message has expired");
            expiredMessage.setAbsoluteExpiryTime(1); //setting this to 0 causes the message to never expire
            Success messageSentExpiredCallback = new Success();

            openClientWithRetry(client, hostname, deviceId, moduleId, protocol.toString());
            client.sendEventAsync(expiredMessage, new EventCallback(IotHubStatusCode.MESSAGE_EXPIRED), messageSentExpiredCallback);

            long startTime = System.currentTimeMillis();
            while (!messageSentExpiredCallback.wasCallbackFired())
            {
                Thread.sleep(RETRY_MILLISECONDS);
                if (System.currentTimeMillis() - startTime > SEND_TIMEOUT_MILLISECONDS)
                {
                    fail(protocol + ", " + authType + ": Timed out waiting for a message callback");
                }
            }

            client.closeNow();

            if (messageSentExpiredCallback.getCallbackStatusCode() != IotHubStatusCode.MESSAGE_EXPIRED)
            {
                Assert.fail(Tools.buildExceptionMessage("Sending message over " + protocol + " protocol failed: expected status code MESSAGE_EXPIRED but received: " + messageSentExpiredCallback.getCallbackStatusCode(), hostname, deviceId, protocol.toString(), moduleId));
            }
        }
        catch (Exception e)
        {
            client.closeNow();
            Assert.fail(Tools.buildExceptionMessage("Sending expired message over " + protocol + " protocol failed: Exception encountered while sending message and waiting for MESSAGE_EXPIRED callback: " + e.getMessage(), hostname, deviceId, protocol.toString(), moduleId));
        }
    }

    public static void sendMessagesExpectingUnrecoverableConnectionLossAndTimeout(InternalClient client,
                                                                                  IotHubClientProtocol protocol,
                                                                                  Message errorInjectionMessage,
                                                                                  AuthenticationType authType,
                                                                                  String hostname,
                                                                                  String deviceId,
                                                                                  String moduleId) throws IOException, InterruptedException
    {
        final List<IotHubConnectionStatus> statusUpdates = new ArrayList<>();
        client.registerConnectionStatusChangeCallback(new IotHubConnectionStatusChangeCallback()
        {
            @Override
            public void execute(IotHubConnectionStatus status, IotHubConnectionStatusChangeReason statusChangeReason, Throwable throwable, Object callbackContext) {
                statusUpdates.add(status);
            }
        }, new Object());

        openClientWithRetry(client, hostname, deviceId, moduleId, protocol.toString());

        client.sendEventAsync(errorInjectionMessage, new EventCallback(null), new Success());

        long startTime = System.currentTimeMillis();
        while (!(statusUpdates.contains(IotHubConnectionStatus.DISCONNECTED_RETRYING) && statusUpdates.contains(IotHubConnectionStatus.DISCONNECTED)))
        {
            Thread.sleep(500);

            if (System.currentTimeMillis() - startTime > 30 * 1000)
            {
                break;
            }
        }

        assertTrue(Tools.buildExceptionMessage(protocol + ", " + authType + ": Expected notification about disconnected but retrying.", hostname, deviceId, protocol.toString(), moduleId), statusUpdates.contains(IotHubConnectionStatus.DISCONNECTED_RETRYING));
        assertTrue(Tools.buildExceptionMessage(protocol + ", " + authType + ": Expected notification about disconnected.", hostname, deviceId, protocol.toString(), moduleId), statusUpdates.contains(IotHubConnectionStatus.DISCONNECTED));

        client.closeNow();
    }

    public static void sendMessageAndWaitForResponse(InternalClient client,
                                                     MessageAndResult messageAndResult,
                                                     long RETRY_MILLISECONDS,
                                                     long SEND_TIMEOUT_MILLISECONDS,
                                                     IotHubClientProtocol protocol,
                                                     String hostname,
                                                     String deviceId,
                                                     String moduleId)
    {
        try
        {
            Success messageSent = new Success();
            EventCallback callback = new EventCallback(messageAndResult.statusCode);
            client.sendEventAsync(messageAndResult.message, callback, messageSent);

            long startTime = System.currentTimeMillis();
            while (!messageSent.wasCallbackFired())
            {
                Thread.sleep(RETRY_MILLISECONDS);
                if (System.currentTimeMillis() - startTime > SEND_TIMEOUT_MILLISECONDS)
                {
                    fail("Timed out waiting for a message callback");
                    break;
                }
            }

            if (messageAndResult.statusCode != null && messageSent.getCallbackStatusCode() != messageAndResult.statusCode)
            {
                Assert.fail(Tools.buildExceptionMessage("Sending message over " + protocol + " protocol failed: expected " + messageAndResult.statusCode + " but received " + messageSent.getCallbackStatusCode(), hostname, deviceId, protocol.toString(), moduleId));
            }
        }
        catch (Exception e)
        {
            Assert.fail(Tools.buildExceptionMessage("Sending message over " + protocol + " protocol failed: Exception encountered while sending and waiting on a message: " + e.getMessage(), hostname, deviceId, protocol.toString(), moduleId));
        }
    }

    private static boolean isErrorInjectionMessage(MessageAndResult messageAndResult)
    {
        MessageProperty[] properties = messageAndResult.message.getProperties();
        for (int i = 0; i < properties.length; i++)
        {
            if (properties[i].getValue().equals(ErrorInjectionHelper.FaultCloseReason_Boom.toString()) || properties[i].getValue().equals(ErrorInjectionHelper.FaultCloseReason_Bye.toString()))
            {
                return true;
            }
        }

        return false;
    }

    public static void openClientWithRetry(InternalClient client, String hostname, String deviceId, String moduleId, String protocol)
    {
        boolean clientOpenSucceeded = false;
        long startTime = System.currentTimeMillis();
        while (!clientOpenSucceeded)
        {
            if (System.currentTimeMillis() - startTime > OPEN_RETRY_TIMEOUT)
            {
                Assert.fail(Tools.buildExceptionMessage("Timed out trying to open the client", hostname, deviceId, protocol.toString(), moduleId));
            }

            try
            {
                client.open();
                clientOpenSucceeded = true;
            }
            catch (IOException e)
            {
                //ignore and try again
                System.out.println(Tools.buildExceptionMessage("Encountered exception while opening device client, retrying...", hostname, deviceId, protocol.toString(), moduleId));
                e.printStackTrace();
            }
        }

        System.out.println("Successfully opened connection!");
    }

    public static void openTransportClientWithRetry(TransportClient client, String hostname, String deviceId, String protocol) throws InterruptedException
    {
        boolean clientOpenSucceeded = false;
        long startTime = System.currentTimeMillis();
        while (!clientOpenSucceeded)
        {
            if (System.currentTimeMillis() - startTime > OPEN_RETRY_TIMEOUT)
            {
                Assert.fail(Tools.buildExceptionMessage("Timed out trying to open the transport client", hostname, deviceId, protocol.toString(), null));
            }

            try
            {
                client.open();
                clientOpenSucceeded = true;
            }
            catch (IOException e)
            {
                //ignore and try again
                System.out.println("Encountered exception while opening transport client, retrying...");
                e.printStackTrace();

                try
                {
                    client.closeNow();
                }
                catch (IOException ioException)
                {
                    System.out.println("Failed to close client");
                    ioException.printStackTrace();
                }
                Thread.sleep(200);
            }
        }

        System.out.println("Successfully opened connection!");
    }

    public static void waitForStabilizedConnection(List actualStatusUpdates, long timeout, String hostname, String deviceId, String protocol, String moduleId) throws InterruptedException
    {
        System.out.println("Waiting for stabilized connection...");

        //wait to send the message because we want to ensure that the tcp connection drop happens before the message is received
        long startTime = System.currentTimeMillis();
        long timeElapsed = 0;
        while (!actualStatusUpdates.contains(IotHubConnectionStatus.DISCONNECTED_RETRYING))
        {
            Thread.sleep(200);
            timeElapsed = System.currentTimeMillis() - startTime;

            // 2 minutes timeout waiting for error injection to occur
            if (timeElapsed > timeout)
            {
                fail(Tools.buildExceptionMessage("Timed out waiting for error injection message to take effect", hostname, deviceId, protocol.toString(), moduleId));
            }
        }

        int numOfUpdates = 0;
        while (numOfUpdates != actualStatusUpdates.size() || actualStatusUpdates.get(actualStatusUpdates.size()-1) != IotHubConnectionStatus.CONNECTED)
        {
            numOfUpdates = actualStatusUpdates.size();
            Thread.sleep(6 * 1000);
            timeElapsed = System.currentTimeMillis() - startTime;

            // 2 minutes timeout waiting for connection to stabilized
            if (timeElapsed > timeout)
            {
                fail(Tools.buildExceptionMessage("Timed out waiting for a stable connection after error injection", hostname, deviceId, protocol.toString(), moduleId));
            }
        }

        System.out.println("Connection stabilized!");
    }
}

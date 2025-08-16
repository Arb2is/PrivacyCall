# SpeakPriv (PrivacyCall)

A Kotlin Multiplatform application designed to enhance call privacy by detecting and flagging when a call participant is using a speakerphone or external audio device.

## Quick Start Guide (Development)

This guide explains how to set up the local development environment to test the application on two Android devices.

### 1. Run the Signaling Server

The project includes a simple WebSocket signaling server required for the WebRTC DataChannel negotiation. 

**Prerequisites:**
- [Node.js](https://nodejs.org/)

**Steps:**
1. Open a terminal in the project root directory.
2. Install dependencies: `npm install ws`
3. Run the server: `node broker.js`

The server will start on `ws://localhost:8787`.

### 2. Expose the Server with LocalTunnel

For the two test devices to communicate, the local signaling server needs to be accessible from the internet.

**Prerequisites:**
- Node.js (for npx)

**Steps:**
1. Open a **new** terminal.
2. Run the following command:
   ```sh
   npx localtunnel --port 8787
   ```
3. `localtunnel` will provide you with a public URL, like `https://your-unique-subdomain.loca.lt`. 
4. **Important:** You need to update the `SecureFlagChannelRtc.kt` file in the Android app to use this public URL. For now, the app uses a stub. To implement the full WebRTC channel, you would replace `SecureFlagChannelStub` with your WebRTC implementation pointing to this URL.

### 3. Build and Run the Android App

**Prerequisites:**
- Android Studio
- Two Android devices (or emulators) with developer mode enabled.

**Steps:**
1. Open the project in Android Studio.
2. Let Gradle sync.
3. Build and run the `androidApp` configuration on both devices.
4. On each device, launch the app and tap **"Enable Protection"** to grant the `ROLE_CALL_SCREENING` permission.
5. Make a regular phone call from one device to the other.
6. The app's foreground service should start automatically and display a notification indicating the privacy status.

## How It Works

- **Call Screening:** The app uses Android's `CallScreeningService` to be notified of incoming and outgoing calls.
- **Foreground Service:** A foreground service (`PrivacyFgService`) is launched to monitor the call's duration.
- **Audio Monitoring:** `AudioDeviceCallback` detects if the audio is routed to the earpiece, speakerphone, or a Bluetooth device.
- **Secure Channel:** A WebRTC DataChannel is established between the two participants' apps (via the signaling server) to exchange a simple binary flag (`isExposed: true|false`) in real-time.
- **Privacy First:** No audio, contact lists, or personal information ever leaves the device. The server is stateless and only acts as a temporary broker.
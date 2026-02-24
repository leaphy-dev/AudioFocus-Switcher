# Audio Focus Switcher
恭喜你发现了矢山

An Xposed/LSPosed module that modifies Android's audio focus behavior, allowing multiple apps to play audio simultaneously.
## How it works

When enabled, the module intercepts audio focus requests and makes all apps believe they have successfully gained focus, allowing multiple audio streams to play at once.

## Requirements

- LSPosed framework (API 93+)
- Android 8.0+

## Usage

1. Enable the module in LSPosed and select **Android System** as the module scope
2. Open the app
3. Toggle "Enable Module" to activate
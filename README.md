# KarmaDefender for Android

[![Join the chat at https://gitter.im/eladnava/karmadefender-android](https://badges.gitter.im/eladnava/karmadefender-android.svg)](https://gitter.im/eladnava/karmadefender-android?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

KarmaDefender protects you from the [KARMA Wi-Fi attack](https://insights.sei.cmu.edu/cert/2015/08/instant-karma-might-still-get-you.html) by automatically scanning for and deleting your password-less Wi-Fi networks.

* [Google Play Listing](https://play.google.com/store/apps/details?id=com.eladnava.karmadefender)

> The KARMA attack takes advantage of clients that broadcast probes in an attempt to connect to a saved Wi-Fi network. An attacker can eavesdrop for these probes and create an access point with the same SSID name that was broadcasted in order to trick the device into thinking it's the legitimate access point. The attacker can then apply various man-in-the-middle exploits and phishing attempts to intercept and read your sensitive data. 

**Note:** Due to Android 6.0 limitations, apps can no longer programatically remove Wi-Fi network configurations created by the device owner. To work around this limitation, the app will alert you about the vulnerable networks via notification, instructing you on how to remove them manually.

# Screenshots

![Popup](https://raw.github.com/eladnava/karmadefender-android/master/preview/screenshot1.png)
![Main](https://raw.github.com/eladnava/karmadefender-android/master/preview/screenshot2.png)
![Settings](https://raw.github.com/eladnava/karmadefender-android/master/preview/screenshot3.png)

# Requirements

* Android 2.2+

# Development Requirements

* Android SDK
* Android Studio with Gradle Plugin
* A physical device to test on (recommended)

# Collaborating

* If you find a bug or wish to make some kind of change, please create an issue first
* Make your commits as tiny as possible - one feature or bugfix at a time
* Write detailed commit messages, in-line with the project's commit naming conventions
* Make sure your code conventions are in-line with the project

# License

Apache 2.0
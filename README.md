# smsish_guard

A new Flutter project.

## Getting Started

This project is a starting point for a Flutter application.

A few resources to get you started if this is your first Flutter project:

- [Lab: Write your first Flutter app](https://docs.flutter.dev/get-started/codelab)
- [Cookbook: Useful Flutter samples](https://docs.flutter.dev/cookbook)

For help getting started with Flutter development, view the
[online documentation](https://docs.flutter.dev/), which offers tutorials,
samples, guidance on mobile development, and a full API reference.

🛡️ SMSish Guard

A Flutter Android app that detects smishing (SMS phishing) attacks in real time using on-device machine learning and rule-based heuristics.

📖 Overview
Smishing (SMS phishing) is a growing cybersecurity threat where attackers send fraudulent text messages impersonating banks, payment platforms, or government bodies to steal credentials or personal information.
SMSish Guard runs silently in the background, scanning every incoming SMS through a multi-layer detection pipeline — on-device ML model + URL heuristics + trusted sender whitelist — and instantly alerts the user via push notification.
No internet connection is required for inference. Everything runs locally on the device.

✨ Features

📩 Real-time SMS scanning — detects smishing even when the app is in the background or closed
🤖 On-device ML classifier — logistic regression model bundled as a local JSON file; no cloud calls needed
🔗 URL heuristic analysis — multi-step URL inspection for suspicious keywords, insecure protocols, dangerous TLDs, and IP-based links
🏦 Trusted sender whitelist — major Indian banks and payment apps are pre-approved to reduce false positives
💳 Legit transaction pattern matching — standard debit/credit SMS formats are automatically recognized as safe
🔔 Push notifications — instant ⚠️ or ✅ alert for every classified message
📋 Inbox scanner — classifies all existing inbox messages on app launch
🔌 Native Android bridge — Kotlin SmsReceiver and MainActivity forward SMS events to Flutter via EventChannel and MethodChannel

🤖 ML Model
The classifier is a logistic regression model serialised as JSON and bundled at assets/ml/model.json.
Field                        Description
vocab                         Map<String, int> — word to feature index
coefficients                  List<double> — one weight per vocabulary term
intercept                     double — bias term
threshold                     double — decision boundary (default 0.5)

--> Inference steps:
Lowercase and tokenise the SMS text
Build a bag-of-words count vector using the bundled vocabulary
Compute score = intercept + Σ(coefficients[i] × vector[i])
Apply sigmoid: probability = 1 / (1 + e^-score)
Flag as smishing if probability ≥ threshold

🚀 Getting Started
Prerequisites

Flutter SDK >=3.9.0 <4.0.0
Android device or emulator (real device recommended for SMS features)
Android Studio or VS Code with Flutter extension

Installation
bash# 1. Clone the repository
git clone https://github.com/your-username/smsish_guard.git
cd smsish_guard

# 2. Install Flutter dependencies
flutter pub get

# 3. Run on a connected Android device
flutter run

📱 Permissions

The following Android permissions are required:
Permission                    Purpose
RECEIVE_SMS                   Listen for incoming SMS messages
READ_SMS                      Scan inbox messages on app launch
POST_NOTIFICATIONS            Display smishing alert notifications
Runtime permission for READ_SMS is requested automatically when the app starts via permission_handler.

📂 Key Files Reference

File                           Description
lib/main.dart                  Core app logic — SMS listener, heuristic filters, UI
lib/ml/classifier.dart         Logistic regression + sigmoid inference
lib/ml/model_loader.dart       Loads and parses model.json from assets
lib/ml/tokenize.dart           Text tokenisation (lowercase, strip punctuation)
lib/notification_service.dart  Push notification initialisation and display
android/.../SmsReceiver.kt     Native BroadcastReceiver — forwards SMS to Flutter
android/.../MainActivity.kt    EventChannel (live SMS) + MethodChannel (inbox read)
assets/ml/model.json           Serialised ML model (vocab, weights, threshold)

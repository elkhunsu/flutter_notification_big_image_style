# loop_app_flutter_v2

A new Flutter project.

## Getting Started

This project is a starting point for a Flutter application.

A few resources to get you started if this is your first Flutter project:

- [Lab: Write your first Flutter app](https://flutter.io/docs/get-started/codelab)
- [Cookbook: Useful Flutter samples](https://flutter.io/docs/cookbook)

For help getting started with Flutter, view our 
[online documentation](https://flutter.io/docs), which offers tutorials, 
samples, guidance on mobile development, and a full API reference.

I'm Newbie in flutter, start learning Feb 2019

Here Example flutter native firebase without plugin firebase message, for big image style notification
for example code in Android app/src/main/java/../MyFirebaseMessagingService

Raw Body hit Postman :
{
  "to" : "your_token",
  "collapse_key" : "type_a",
  "user": "test",
  "data" : {
  	"action": "FLUTTER_NOTIFICATION_CLICK",
    "status": "done",
    "screen": "screenA",
    "title":"aku ganteng",
    "image": "IMAGE_URL",
    "message":"nama saya elkin"
  }
}
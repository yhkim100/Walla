How to run App from a phyiscal android device.   
1) Clone a copy of Master branch onto local drive.   
2) Ensure device meets minimum requirements. App minimum SDK version 16, targetSDKVersion 23  
3) Load up android studio, load project, and plug in android device.  
4) Run -> Run "app" -> select physical device  
5) App should be up and running.   


How to run App from Emulator.   
1) Clone a copy of Master branch onto local drive.   
2) Load up android studio and load project.   
3) Tools -> Android -> AVD Manager   
4) Select Create Virtual Device  
5) Select Nexus 5, then Next  
6) Select Marshmallow API 23, Android 6.0 (with Google API's) as your system image, then Next  
7) Feel free to rename device and select finish.  
8) To run the app on the emulator, select Run -> Run "app" -> select the virutal device you created above -> select ok  
9) App should run as intended  
  
**Notes about running app on emulator**  
    The google maps api will not be able to obtain coordinates for the user's current location from the device unless the GPS coordinates are also emulated using the android device monitor, instead the coordinates will be set to lat:0, long: 0
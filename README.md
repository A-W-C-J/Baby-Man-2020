Trump Game based on [Klooni 1010!](https://github.com/LonamiWebs/Klooni1010)
============
Play Trump Game for free! Add your own themes! Contribute! Make the game yours!

1. Add  Trump themes
2. Add the sounds of Trump
3. Add the splash

[<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" alt="Get it on Google Play" height=
"80">](https://play.google.com/store/apps/details?id=com.vision.elimination)



<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/1theme.png" alt="1theme" width="150" height="267" /><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/2theme.png" alt="2theme" width="150" height="267" /><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/3theme.png" alt="3theme" width="150" height="267" /><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/4theme.png" alt="4theme" width="150" height="267" /><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/5theme.png" alt="5theme" width="150" height="267" />

[![Baby man 2020](http://img.youtube.com/vi/5vibNRa_J3g/0.jpg)](http://www.youtube.com/watch?v=5vibNRa_J3g "Baby man 2020")

Building
--------
Building the project should be very straight forward:

1. `git clone https://github.com/A-W-C-J/TrumpGame.git`.
2. `cd TrumpGame`
3. Now you can choose to either build for `desktop` or `android`:
   1. For desktop, use `./gradlew desktop:dist`
   2. For Android, use `./gradlew android:assembleRelease`
4. You're done! The generated files are under `build`:
   1. Desktop build is under `desktop/build/libs/*.jar`
   2. Android build is under `android/build/outputs/apk/*.apk`
5. Thx [LonamiWebs](https://github.com/LonamiWebs)

Playing
-------
If you're on desktop, you should be able to play the game by either double
clicking the built game `.jar` (Windows) or running `java -jar {file}.jar`.

If you want to play the game on Android, move the built `.apk` to your phone's
internal memory, find it with an Android file explorer and install it.
Make sure you have `Unknown sources` (`Settings -> Security`) enabled!

This project is licensed under [GPLv3+](LICENSE).


# CBA Exercise

Rod Bailey
Wednesday 3 Feb 2020

Note: The pictures and links in this document only work when this document is viewed on the Github web site.

# Summary

This is an Android application created as a technical exercise for Android Developers.

The requirements document is in a MarkDown file [here](/doc/cba_brief.md)

This app is written in **Kotlin** as are the instrumented and un-instrumented unit tests

# Building the app

- The app was developed in Android Studio 4.1.2
- The code (and this document) is stored in a Git repository at `https://github.com/replicant1/CBAExercise.git`
- The app will work on Android Oreo 8.1 (API level 26) upwards

# Screenshots

This is what the app looks like when it has successfully loaded the account summary that comes
bundled with it (contained in /assets/sample_account_data.json):

![Screenshot](/doc/cba_summary_screen.png)

Here is what the app looks like when you click on an ATM transaction on the above screen:

![Screenshot](/doc/cba_map_screen.png)

# Design Comments

Because this coding exercise is small in scope, I have not used the architecture I would for a
production app. Generally I would use `ViewModel`, `Repository` and some `Room` database tables to
cache the loaded data. I would also use Retrofit for HTTP request/response handling.

# Interpretive Issues

There are a few ambiguities in the requirements which have forced me to make some interpretive decisions:

### Days ago

The example screenshots show that each date header in the transaction list has the text '<X> days ago'
at right. For old data, this can result in useless info like `1287 days ago`. Perhaps the age
is meant to be in months or years as appropriate eg. `3 years ago`, `11 months ago`. Rather than
guess I have gone with the simplest interpretation to implement.

### Device Orientation

The requirements make no mention of how the app should handle changes in device orientation. For simplicity
I decided to lock it to portrait orientation.

# Future Improvements

Had I more time, I would've made the following improvements.

- Download the data from an external source, rather than just a hard-coded JSON sample file bundled
with the app.

- Implement an algorithm that will give the user a projected spend over the next two weeks

- Implement a disk-based caching strategy for the account and transaction info as described above

- Much more testing that bad JSON is handled gracefully.


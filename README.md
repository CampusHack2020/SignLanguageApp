# Sign Time App

## What's this project?
As part of the Southampton University Campus Hack 2020, we decided to create an app which would help to encourage children to have fun learning American Sign Language (ASL).

## What did we use?
We used Android Studio's to create an Android application for this project - as it uses Java, which runs natively on Androids. We implemented a Sign Language online Git repository (https://github.com/Mquinn960/sign-language) into our Android app, to help individuals spell words using ASL, and practice the ASL alphabet. To make use of this Sign Language Git repository, we had to create some training data for the machine learning model, to improve the software.

We decided to run keep this application server-less, which helped with simplicity and allowed us to keep make the app self-contained.

We wanted to make use of some Tello drones (which we had won at Hack The South 2020), so made use of Java UDP datagrams to send/receive data to/from the drone - this was a big challenge, however it payed off in the end! We implemented the drones into the program by making it start up when the user would load up the camera, and then made it do flips whenever the user finished spelling out a word in ASL correctly.

## The team
* Josh Hook (josh-hook) - Android Studio / GUI lead
* Ben Thompson (bt1g18) - Android Studio / GUI
* Leo Gamble (L-Gamble) - Android Studio / GUI
* Sohaib Karous (cleverman88) - Drone API lead
* Akhilesh Shastri (as14g18) - Drone API, and ML training data parsing
* Jack Parsons (jack-parsons) - Drone API, and ML training data lead

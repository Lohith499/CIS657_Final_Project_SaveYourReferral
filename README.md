# Save your Referrals

Save your Referrals is an Android application which can be used as a central location for all shares from other applications on an Android phone. It can be used as a central repository for all shared data, and allow for a quick launch to reshare data that had previously been shared. Inside there is an ability to add a new bookmark, add a current bookmark to a favorites list, as well as a helpful instructions screen to explain the functionality. 


## Getting Started

We believe this is an innovative application because we were unable to find many like in the iOS or Android store. The application uses a fully functional, multi-screen UI to access all of the features. Local storage is used to persist data between user sessions. Favorites and saved shares are listed in a scrollable view.

We have network integration utilizing the Shareable interface which allows our application to communicate with other applications. This is a core functionality of the app in which we can share things from one application which will be added to ours and can be further shared later. Further, we have Firebase integration which allows for all of the shares to be located in a cloud based database, in addition to the local storage in the embedded SQL database.

### Prerequisites

App should be able to have Usage and Phone permissions to run successfully



### Installing

Download the Apk file at the below link and install in your mobile

http://www.mediafire.com/file/cdwaqwiv8a3nr6w/SaveyourReferralv6.apk

## DataBase Storage

One of the most important aspects of the design of the application was to avoid being tied to any cloud based service and be fully functional even without an Internet connection. A share can be saved to be used later when a connection is present. 

The GreenDAO Android ORM for SQLite is the defining technology behind the application. The API allows for very easily written queries with a very small memory footprint and library size. Fairly large databases can be accessed with little to no overhead which is ideal for an application like ours. Also included is the addition of the Roboto font family which makes the application nicely integrated Android OS.


### FireBase Integration

Though it is not necessary for our app we still added the firebase integration to it. The data is being stored in both the RealTime Database and Cloud FireStore to see how data is being stored in each of these



## Privacy

From the beginning of planning we had discussions regarding privacy and the dependency that most applications have with social media and a persistent network connection.

## Network Integration

We wanted our application to function without a connection to the Internet, and have the freedom to run the application without tying into a social media account. Much like other system accessories, the primary user is expected to be the user on the phone, so a single account design is what we focused on. 

We had positive feedback regarding additional ideas that were not implemented in this version. We hope to continue to add features in the future as we explore new libraries and features of the Android SDK.



## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Brian Frankosky** - [bfrancosky](https://github.com/bfrancosky)
* **Lohith Nimmala** - [Lohith499](https://github.com/Lohith499)



## WireFrame

 
https://github.com/Lohith499/CIS657_Final_Project_SaveYourReferral/blob/master/Android%20Wireframe.png

## Additional Resources:

http://greenrobot.org/greendao/

https://fonts.google.com/specimen/Roboto

https://developer.android.com/training/sharing/shareaction

https://developer.android.com/training/implementing-navigation/lateral

https://firebase.google.com/docs/firestore/


## Acknowledgments

Below tutorials have helped us in implementing those features in our app


---Receving Intent Data----
1.sending - https://www.youtube.com/watch?v=ziqQzkpIeHA

2. receving - https://www.youtube.com/watch?v=2RwHUfmT6Kk
http://programmerguru.com/android-tutorial/how-to-receive-simple-data-from-other-apps/
https://developer.android.com/training/sharing/receive


---SQLLite tutorial---

6 videos-https://www.youtube.com/watch?v=cp2rL3sAFmI&list=PLS1QulWo1RIaRdy16cOzBO5Jr6kEagA07


---We will be using GRENDAO ORM for managing database to know about it visit below links---
http://greenrobot.org/greendao/documentation/updating-to-greendao-3-and-annotations/
http://greenrobot.org/greendao/documentation/ 
How to get started
Introduction – project setup
Modelling entities – schema and annotations
Sessions – identity scope and session cache
Queries – using the query builder
Joins – multi-table joins and self-joins

simple Tutorial at GREENDAO- 
https://www.youtube.com/watch?v=zDdu1MlXjZw



--Android Tab tutorial---

https://www.youtube.com/watch?v=bNpWGI_hGGg

--Android Naviagtion Drawer-- Slide Menu--

https://www.youtube.com/watch?v=AS92bq3XxkA
https://www.youtube.com/watch?v=dpE8kzZznAU


---SLiding tab layout---

https://www.youtube.com/watch?v=23vE3VUke5o


--Recycler View--

https://www.youtube.com/watch?v=0CCtgWVJVLs


---Roboto Font--

https://www.youtube.com/watch?v=rpy99RkPMOM


Cloud Firestore
https://firebase.google.com/docs/firestore/

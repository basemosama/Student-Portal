# Student Portal
<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/basemosama"><img alt="Profile" src="https://raw.githubusercontent.com/basemosama/basemosama/master/badges/github.svg"/></a> 
</p>
<p align="center">  
Student Portal is an app that help facilitate communication between students and professors.<br>
It's a demo application based on modern Android application tech-stacks and MVVM architecture.<br>
And fetching data from the network and integrating persisted data in the database via repository pattern.<br>
<img src="https://i.imgur.com/nfP9BuS.jpg" alt="Student Administration System" >
</p></br>

## Download
you can download the app and test it now from Google Play.

**To login as Studnet:**<br>
Username : hamdy@gmail.com<br>
Password : 123456789<br>

**To login as Professor:**<br>
Username : hala@gmail.com<br>
Password : 123456789<br>
<p align="center">
<a href="https://play.google.com/store/apps/details?id=com.basemosama.studentportal"><img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png"
      alt="Download from Google Play" 
      height="120"></a></p></br>

## ScreenShots

Login Screen                                      |                   Group                 |                   Subjects                   |                 Events                     |                   More Profile                
:--------------------------------------------------:|:---------------------------------------------------:|:----------------------------------------------------:|:-------------------------------------------------:|:--------------------------------------------
<img src="https://i.imgur.com/yvvi5Vm.png" /> | <img src="https://i.imgur.com/Q8ABYze.png" /> | <img src="https://i.imgur.com/G9EkjT6.png" /> | <img src="https://i.imgur.com/AdQ6BBF.png" /> |<img src="https://i.imgur.com/LyKf2Jt.png" />

## Features

### For Student:

 - [ ] Show group for current students.
 - [ ] Can post, comment or reply in the group.
 - [ ] Show current year subjects.
 - [ ] Show professor’s assignments, sources and marks.
 - [ ] Show current year table and results.
 - [ ] Show current events in the college.
### For Professor:	
 - [ ] Show group for each year.
 - [ ] Can post, comment or reply in the group.
 - [ ] Show assignments and source for professor’s subjects.
 - [ ] add Assignment and sources for professor’s subjects. 
 - [ ] Show lsit of students in each grade.
 - [ ] Show and edit student’s year work like attendance, midterm, etc.
 - [ ] Predict students’ final grade based on his year work.</br>

## Tech stack & Open-source libraries
- Minimum SDK level 21, built using Java.
- [JetPack](https://developer.android.com/jetpack)
  - [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started) - handle navigation of the app
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - notify domain layer data to views.
  - [Lifecycle](https://developer.android.com/reference/androidx/lifecycle/Lifecycle) - dispose of observing data when lifecycle state changes.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - UI related data holder, lifecycle aware.
  - [Room Persistence](https://developer.android.com/topic/libraries/architecture/room)- construct a database using the abstract layer.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
  - Repository pattern
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit) - construct the REST APIs and paging network data.
- [Glide](https://github.com/bumptech/glide) - loading images.
- [Photo View](https://github.com/chrisbanes/PhotoView) - help produce an easily usable implementation of a zooming Android Image .
- [Power Spinner](https://github.com/skydoves/PowerSpinner) - A lightweight dropdown popup spinner with fully customizable arrow and animations.
- [Pretty Time](https://www.ocpsoft.org/prettytime) - for createing human readable, relative timestamps.
- [Logger](https://github.com/orhanobut/logger) - logging.
- [Tab Layout Helper](https://github.com/h6ah4i/android-tablayouthelper) - A small library which helps to use Tab Layout with View Pager more easily..
- [Material Component](https://github.com/material-components/material-components-android) - Material design components like cardView.

## Architecture
The App is based on MVVM architecture and a repository pattern.

![architecture](https://user-images.githubusercontent.com/24237865/77502018-f7d36000-6e9c-11ea-92b0-1097240c8689.png)
## API
The app uses a RESTful API built by us using Laravel. <br> and has been dployed to Heroku. <br>

# License
```xml
Designed and developed by 2020 Basem Osama

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

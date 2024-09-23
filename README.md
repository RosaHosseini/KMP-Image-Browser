# ImageBrowser
I migrated the [android `ImageBrowser` project](https://github.com/RosaHosseini/ImageBrowser) to kotlin multiplatform. Which supports both `android` and `ios`.

An image browser supporting
 - Pagination for infinite scroll
 - Bookmarks
 - Photo detail
 - Search history
 - Offline first: It caches searched photos
 - Recycle outdated cached data (created 7 days ago) every day 
 
 <p float="center">
    <img src="screenshots/1.png" width ="200">
    <img src="screenshots/2.png" width ="200">
    <img src="screenshots/3.png" width ="200">
    <img src="screenshots/4.png" width ="200">
    <img src="screenshots/5.png" width ="200">
</p>
 
 ## Technologies
  - Kotlin
  - Clean Arch + MVI
  - Jetpack compose
  - coroutine + flow
  - Ktor
  - Koin
  - Modularization

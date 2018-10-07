# FoodFacts

### Intro
Build a small Android application that connects to [world.openfoodfacts.org](https://world.openfoodfacts.org) HTTP API.

This API provides product information based on barcode number.

In this application, the user should be able to: 

* see a product information by using its barcode number;
* see previously searched products while online and offline.

Target SDK : **28 AKA Android Pie** (min SDK 19, covering ~95% of Android devices).

### User stories

As an app user :

1. I should be able to search product information by the barcode number
2. If a product is found, I should be able to see the  product name, the picture of the products, the ingredients and the  energy value (kcal)  
3. I should be able to browse a history of the previously  searched products  
4. I should be able to see the information about a  product in my history of searches even If I am offline  

### Application design

This first release will be created by implementing this features step by step:

* Create and configure project
* Add library for barcode scanning and show result on secondary activity
* Add library for HTTP GET operation on API: for simplify the code, the app will save only desired data from JSON
* Add entity model of product and create database
* Save data found from GET operation on DB and show found data on detail activity
* Show saved data on a list / grid on main activity
* Create an UI (icons, launcher icons, activity interfaces, theme)
* Improve / create test

### TODO

* Add animations
* Add delete function of unwanted product
* Add option to allow manual bar code insertion
* Add placeholder text / images when data is not loaded correctly from server or is missing
* Add more info about products: additives, product quality, manufacturer
* Improve camera activity UI
* Improve tests
* Bug fix

### Knows bug

The following is a list of know bug to fix on next release:

* API search is not working on Android 7.0, see this [link](https://github.com/openfoodfacts/openfoodfacts-androidapp/issues/793) for more info
* Handle correctly missing internet connection
* Handle correctly product found from another country

### Android UI composition

The UI of the app is composed by:

* A main activity that show a history of previous research, done with **RecyclerView** as list (coupled with Android Architecture)
* A detail activity, which is used to show product details
* Each activity has is own fragment
* **CardView** for a lovely sight and content organization
* A secondary activity used to read the bar code and send results to detail activity


### Project structure

- **api** : contains API calls, using Retrofit
- **barcode** : contains Android Vision class which create an overlay showing found bar code
- **camera** : contains Android Vision class which handle camera and capturing frames for inspecting bar code
- **data** : contains the repository class, responsible for triggering API requests and saving the response in the database
- **db** : contains database cache for network data
- **model** : contains the data model, which is also a table in the Room database 
- **rawmodel** : contains the raw data model used by Retrofit in order to deserialize JSON data returned by API
- **ui** : contains all classes concerning UI
- **util** : package containing various data class
- **viewmodel** : contains all classes designed to store and manage UI-related data


### Pattern used so far
The app uses following patterns:

* Model-View-ViewModel [MVVM](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel) pattern for the presentation layer.
* [Repository pattern](https://medium.com/corebuild-software/android-repository-pattern-using-rx-room-bac6c65d7385), which handle all communications with DAO and search on API.
* [Observer pattern](https://en.wikipedia.org/wiki/Observer_pattern).

### Libraries list

You will find some code about listed libraries / framework / language:

* [Kotlin](https://kotlinlang.org/)
* [Room](https://developer.android.com/topic/libraries/architecture/room.html)
* [ViewModel](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html)
* [LiveData](https://developer.android.com/reference/android/arch/lifecycle/LiveData.html)
* [DataBinding](https://developer.android.com/topic/libraries/data-binding/index.html)
* [RxJava](https://github.com/ReactiveX/RxJava) and [RxAndroid](https://github.com/ReactiveX/RxAndroid) with _reactivenetwork-rx_ library
* [Glide](https://github.com/bumptech/glide) which will load and cache images from web
* [Gson](https://github.com/google/gson) coupled with Retrofit in order to create POJO of data searched on API
* [Retrofit](https://square.github.io/retrofit/) HTTP client for network operation
* [Mobile Vision API](https://developers.google.com/vision/) Google official code which provide bar code detection APIs that read and decode a myriad of different bar code types
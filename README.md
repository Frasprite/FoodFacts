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

* Create and configure project
* Add library for barcode scanning and show result on secondary activity
* Add library for HTTP GET operation on API
* Add entity model of product and create database
* Save data found from GET operation on DB and show found data on detail activity
* Show saved data on a list / grid on main activity
* Create an UI (icons, launcher icons, activity interfaces)
* Improve / create test

### Android UI composition


### Back office


### Pattern used so far

### Libraries list

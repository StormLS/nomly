# nomly
A Meal Tracking Application


## Prerequisites
Before you can run this project, you'll need:
1. Android Studio: The official IDE for Android development. Download it from developer.android.com/studio.
2. Java Development Kit (JDK): Android Studio usually comes with an embedded JDK, but having a standalone JDK (version 8 or higher, preferably 11 or 17 for modern Android development) installed is good practice.
3. Android SDK: Install the latest Android SDK platforms and build tools via the SDK Manager in Android Studio (Tools > SDK Manager).
4. Download the google-service.json and have it ready for 'Setting Up the Project'
5. (Optional but Recommended) Git: For version control if you cloned this project from a repository.

## Setting Up the Project
1. Clone or Download the Project:
        If you have Git: git clone <repository_url>
        Otherwise, download the project ZIP file and extract it.
2. Open in Android Studio:
        Launch Android Studio.
        Select "Open" (or "Open an Existing Project").
        Navigate to the root directory of the cloned/extracted project and select it.
3. Add the downloaded google-services.json to the project under the app directory
        This will be required to run the Firebase features of the application.
4. Gradle Sync: Android Studio will automatically try to sync the project with its Gradle files. This might take a few minutes as it downloads dependencies. If it doesn't start automatically, look for a "Sync Now" button in the notification bar at the top of the editor.

## Starting the Application
1. Connect a Device or Start an Emulator:
    Physical Device:
        Enable Developer Options and USB Debugging on your Android device.
        Connect your device to your computer via USB.
        You might see a prompt on your device to "Allow USB debugging." Accept it.
    Emulator:
        Open the AVD (Android Virtual Device) Manager in Android Studio (Tools > AVD Manager).
        Create a new virtual device or start an existing one. Ensure the emulator image includes Google APIs or Google Play Store if your app relies on Google Play services.

## Once the Application is Running
1. Login: 
    When the app launches, you will see the Login Screen.
    Enter your registered email and password, then tap Login.
    If you don’t have an account, tap the Register button to go to the registration screen.
2. Registration:
    On the Registration Screen, enter your details such as email, password, and any other required information.
    Submit the form to create a new account.
    After successful registration, you will see a message appear at the bottom of the screen that your account has been created.
3. On Login:
    After logging in, you will arrive at the Home Screen showing you a Meal List Screen showing your tracked meals (if any).
    Tap the + Add Meal floating action button at the bottom right of the screen.
4. Adding a new Meal:
    On the Add Meal Screen, enter meal details including:
        - Meal title/name
        - Description 
        - Time of consumption
        - Meal type (e.g., Breakfast, Lunch, Dinner)
        - Portion size
        - Macronutrients (Optional)
            Protein
            Carbs
            Fats
        - Add a photo by taking a picture or selecting one from the gallery (Disabled due to Firebase Storage limitation)
    Submit the meal to save it.
5. Successfully adding a meal:
    Once the meal has been added a message will appear at the bottom of your screen to notify that it was indeed successful
    After this you may navigate back to the Home Screen and find your newly added Meal
    Clicking on the Meal will navigate you to the Detail screen for that meal, allowing you to see all the captured details as well as share the details to another application via the top right button.
6. Filtering the Meal List:
    Once any meal has been added to the application you can either filter by Meal 'Title' via the search bar or you can use the Filter Chips to search by Meal Type (BREAKFAST, LUNCH, SUPPER) and/or the Macronutrients (Protein, Carbs, Fats)

## Additional features that could be added later that are currently out of scope 
1. Add the ability for the User to Update an already added item. When a meals macros are included the application can calculate the Calories for the User optionally.
2. Give the User the ability to request the removal of their details from the app database, basically to delete their account with us. 
3. When creating an account the user could Include more personal information such as their Height, Age, Weight and their level of activity. This could then be used to calculate their BMI, introducing more health options in the application.
4. Introduce additional Sign-In options for the user (Facebook etc).
5. Give the user the ability to use a ‘Remember Me’ feature so that they don’t have to re-login every time.
6. Implement more Animation into the application, in its current state it is very static.
7. Have more feature rich filtering.
8. Leverage Firebase and include in-app messaging with a ‘Goal’ system such as making sure you eat a certain number of meals/calories a day
9. Considering this app uses Firebase it would be beneficial to go and add ‘App Check’ as additional security measures.
10. Introduce offline support
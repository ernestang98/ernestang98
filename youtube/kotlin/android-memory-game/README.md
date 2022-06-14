## [Android Development Tutorial](https://www.youtube.com/watch?v=C2DBDZKkLss)

### General Stuff

Activities are like screens

Drawables are like images/icons/SVGs

onCreate() is a function which loads on creation of activity

startActivity() starts another activity

putExtra() sets the variable in the first parameter as the value of the second parameter

Intent refers to an operation to be performed

onRequestPermissionResult(): callback function when permission is requested from user

### Recycle View

Adapter refers to a bridge between a View and the underlying data for that view

  - https://developer.android.com/reference/android/widget/Adapter

  - Bridge between AdapterView and the data for that view

  - Provides access to the data items

  - Responsible for making a View for each item in the data set

onCreateViewHolder (allow you to alter the values of each data)

getItemCount (return total number of data)

onBindViewHolder (does some shit)

inner class ViewHolder (add logic to each card)

### Menu Bar things

onCreateOptions(): create options menu on menu bar

onOptionsSelectedItem: create logic for different options on the options menu

### *Have not done:*

- Add your firebase credentials file!

- Add Google Analytics (allows you to analyze you application)
  
  - https://github.com/rpandey1234/MyMemory/commit/dab215360a215a793159b2b54e0207697a36c6dd

- Publishing on Google Play/Build APK
  
  - Build -> Generate Signed Bundle or APK -> Android App Bundle 
  
  - https://developer.android.com/studio/publish/app-signing

### *Additional Links*

- [Source Code Repository](https://github.com/rpandey1234/MyMemory)

- [Activity in Android](https://developer.android.com/training/basics/firstapp/starting-activity)

- [Intent in Android](https://developer.android.com/reference/android/content/Intent)

- [Android gives error "Cannot fit requested classes in a single dex file"](https://stackoverflow.com/questions/51341627/android-gives-error-cannot-fit-requested-classes-in-a-single-dex-file)

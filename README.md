
# Chatiz - Chat Application
<p align="center">
  <img 
    width="500"
    height="393"
    src="https://user-images.githubusercontent.com/92651125/197267879-3c5e0b83-5f6a-4a4c-a414-80e77f2bb319.png"
  >
</p>

## Description
### ***For screenshots - skip to the bottom of the page.***  


Chatiz is a chat application that allows you to communicate with people from all over the world.  
From a more professional point of view, **Chatiz is a chat application for web browsers and Android devices, connected to a back-end RESTful server with a DB connection**: React.JS usage for the web application, Server side in ASP.NET core (C#) with a MariaDB connection (local database), and Android side in Java.  
This application was created as part of the course "Advanced Programming 2" and inspired by the application "Whatsapp Web". In the code we can find various techniques like ORM, MVC, Entity framework, web services and more.  

## Part 3: Android
The project is divided into 3 parts:

 1. Browser Side in React.JS. [link](https://github.com/ido106/Chatiz_Browser)
 2. Server Side in ASP.NET core (C#) with a MariaDB connection. [link](https://github.com/ido106/Chatiz_Server)
 3. **Android** side in Java. [link](https://github.com/ido106/Chatiz_Android)  

As marked, in this part I will show the **Android** side in **Java** under the [Android Studio](https://developer.android.com/studio?gclid=CjwKCAjwzNOaBhAcEiwAD7Tb6IlC-UqXarEgUcyvqjBqfaW5bQBfScI-sxX_qT2Pdep-jkNdh-jrIBoCnn4QAvD_BwE&gclsrc=aw.ds) workspace.  
In this task, we will create a client side but this time for Android devices. The android part is connected directly to the database server from [part 2](https://github.com/ido106/Chatiz_Server).
The new project is under the folder "*Android*", and the "*WebApp*" folder contains the server from the second part, after updates and improvements. 

The Android part includes a **login screen**, **registration screen**, **verifications**, **chats and messages screen**, **dark mode**, **horizontal screen** when sending messages and more.  
I used [**Firebase**](https://firebase.google.com/) to send notifications to the devices when they receive a message. I also used **ROOM**, **View Model**, **RetroFit**, **RecyclerView**, **LiveData**, **Dao**, **Adapters**, **API**, **services** and more.

*Chatiz* can be downloaded and used on Android devices, but I will show it to you under an emulator in Android Studio.



## Instruction Manual
In order to use *Chatiz* on android, the server must be run in the background before using the application.

### Downloading The Server
 1. Download [MariaDB](https://mariadb.org/download/?t=mariadb&p=mariadb&r=10.11.0&os=windows&cpu=x86_64&pkg=msi&m=truenetwork) and remember the password you choose.
 2. Right click on a new folder, then open the terminal.
 3. In the terminal, enter `git clone https://github.com/ido106/Chatiz_Android.git`.
 4. Open the `WebApp.sln`file in the `WebApp` folder.
 5. Under Solution Explorer, choose Repository -> WebAppContext.cs, then change the password in the `connection string`to your MariaDB password.
 6. Go to Tools -> NuGet Package manager -> Package Managaer Console.
 7. Enter `Update-Database;`.
 8. Run the server.

### Using Chatiz on Android
 1. Open the `Android` folder on [Android Studio](https://developer.android.com/studio?gclid=CjwKCAjwzNOaBhAcEiwAD7Tb6FYLIUSDCO7585dz_slTgxQxjOi-y8r5MoKIFEFzsNkYKv7G9n9NBBoCcocQAvD_BwE&gclsrc=aw.ds).
 2. Open a new emulator and run *Chatiz*.
 3. Enjoy !

## Screenshots
*For screenshots of the server side visit the [second part](https://github.com/ido106/Chatiz_Server).*


## **Enjoy	:smile:**

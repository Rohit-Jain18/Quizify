Note : To get the Whole View of App Open this[@AppShowcasingVideo.mp4 video](https://drive.google.com/file/d/1zxNcyUe1ub-K4eCFxqw1VXuiMO-efGiq/view?usp=drive_link)
--
 

Quizify: A Fun and Interactive Quiz App
--------------------------------------
Overview
--------
Welcome to Quizify, the ultimate quiz application designed to test your knowledge across various subjects and improve your learning experience. Whether you're a student looking to reinforce your studies, a trivia enthusiast, or someone who loves to challenge their knowledge, Quizify offers an engaging and interactive platform to quiz yourself on numerous topics. With a user-friendly interface, real-time score tracking, and a sleek design, Quizify makes learning fun and rewarding.

Key Features
------------
Diverse Question Bank:
-----------------------
Covers a wide range of subjects including history, science, mathematics, literature, and more, with questions categorized into different difficulty levels.
Interactive User Interface: Designed using Jetpack Compose for a modern, responsive, and flexible UI.

Timer-Based Quizzes:
-------------------
Each quiz is timed, adding an extra layer of challenge and excitement. Users can pause and resume the quiz without losing progress.

Real-Time Scoring:
-
Provides immediate feedback on answers, helping users learn and improve. Scores are tracked over time to monitor progress.
Review and Learn: At the end of each quiz, users can review the correct answers and compare them with their responses, with detailed explanations for some questions.

Customizable Experience:
--

Allows users to customize the app’s appearance with different themes and color schemes, and modify quiz settings such as question difficulty and timer length.


Technical Overview
-
Architecture
-
Quizify follows the Model-View-ViewModel (MVVM) architecture, which promotes a clear separation of concerns and enhances testability and maintainability. The architecture is composed of:

Model:
-
Handles data and business logic.

View: 
-
Represents the UI components.

ViewModel:
-
Manages UI-related data in a lifecycle-conscious way, acting as a bridge between the Model and the View.

Development Tools and Libraries
--
Kotlin:
-
The app is developed using Kotlin, which offers modern language features, better null safety, and more concise syntax compared to Java.

Jetpack Compose: 
-
Used for building the UI, providing a declarative approach that allows for more readable and maintainable code.

SharedPreferences:
-
Utilized for persisting user data such as the current question index, remaining time, and score, ensuring that users can resume their quizzes even after closing the app.

Gson:
-
Used for parsing JSON data, simplifying the process of converting JSON strings to Kotlin objects and vice versa.

User Interface
-
Jetpack Compose:
-
The app uses Jetpack Compose for all UI elements, providing a modern, responsive, and flexible user interface.

Theming and Customization:
-
The app’s theme is defined to ensure a consistent look and feel, with colors, typography, and dimensions managed through Compose’s theming system.

Splash Screen:
-
Features a customizable splash screen that displays an image for a few seconds on startup, using the new Android 12 Splash Screen API for a smooth and visually appealing startup experience.

Future Enhancements
-
Firebase Integration: Plans to integrate Firebase for user authentication, real-time database, and analytics.
Room Database: Considering migrating from SharedPreferences to Room for more robust local data storage.
Multiplayer Quizzes: Introducing a multiplayer mode using WebSockets for real-time quiz challenges.
Improved State Management: Using libraries like Hilt for dependency injection and StateFlow for state management to further improve code maintainability and testability.


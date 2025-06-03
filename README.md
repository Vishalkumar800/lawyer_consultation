# Lawyer Consultation App

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-purple.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.04.01-brightgreen.svg)](https://developer.android.com/jetpack/compose)
[![ZegoCloud](https://img.shields.io/badge/ZegoCloud-Voice%20%26%20Video-orange.svg)](https://www.zegocloud.com/)
[![Firebase Auth](https://img.shields.io/badge/Firebase-Auth-yellowgreen.svg)](https://firebase.google.com/docs/auth)
[![Dagger Hilt](https://img.shields.io/badge/Dagger-Hilt-red.svg)](https://dagger.dev/hilt/)
[![Razorpay](https://img.shields.io/badge/Razorpay-1.6.41-blue.svg)](https://github.com/razorpay/razorpay-android)



Welcome to the **Lawyer Consultation App**, a modern Android application designed to connect users with legal professionals for expert advice. Built with **Kotlin** and **Jetpack Compose**, this app offers seamless search, filtering, and real-time communication via voice and video calls powered by **ZegoCloud**.
Welcome to the Lawyer Consultation App, a modern Android application designed to connect users with legal professionals for expert advice. Built with Kotlin and Jetpack Compose, this app offers seamless search, filtering, and real-time communication via voice and video calls powered by ZegoCloud.

## 🌟 Features

- **Search Lawyers**: *Quickly find lawyers by name, specialty, or location.*
- **Category Filtering**: *Filter legal experts by categories like Crime, Marriage, Divorce, and more.*
- **Lawyer Profiles**: *View detailed lawyer info (name, expertise, rating, contact) in a sleek card-based UI.*

- **Voice & Video Calls**: *Connect instantly with lawyers using ZegoCloud's robust SDK for one-on-one communication.*

- **Responsive UI**: *Modern, declarative UI built with Jetpack Compose for a smooth, native experience.*

- **State Management**: *Powered by ViewModel and LiveData for efficient data handling.*

- **Offline Notifications**: *Integrated FCM for push notifications even when the app is offline.*

- **Error Handling**: *Robust loading states and error messages for a reliable user experience.*

## 📸 Screenshots
| Home Screen | Lawyer Profile |
|-------------|----------------|
| ![Home](https://github.com/Vishalkumar800/lawyer_consultation/blob/master/app/src/main/assets/image3.jpg) | ![Profile](https://github.com/Vishalkumar800/lawyer_consultation/blob/master/app/src/main/assets/image4.jpg) |

## 🎥 Demo Videos

### 🔐 1. Authentication Flow  
https://github.com/user-attachments/assets/3357592d-7cc7-4ef3-8ec6-7e522b74a18f

---

### 🎨 2. UI Preview Youtube Video (Jetpack Compose)  
[![UI Preview](https://img.youtube.com/vi/llQ_Kzk0HKg/0.jpg)](https://youtu.be/llQ_Kzk0HKg?si=vExq3AGsALPJxBC2)

---

### 📞 3. Calling Feature (ZegoCloud)  Youtube Video
[![Calling Feature](https://img.youtube.com/vi/uiDz9tMJu4Y/0.jpg)](https://https://youtube.com/shorts/uiDz9tMJu4Y?si=pg6IB42LzcuEARy_)




## 🛠️ Tech Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Communication**: ZegoCloud (Voice & Video Calling with ZegoUIKitPrebuiltCallService)
- **Push Notifications**: Firebase Cloud Messaging (FCM)
- **Dependencies**: Managed via Gradle
- **Other**: Coroutines for async operations, UUID for unique call IDs

## 📋 Prerequisites

Before running the app, ensure you have:
- Android Studio (Latest stable version, e.g., Android Studio Iguana)
- Kotlin 1.9.0 or higher
- A ZegoCloud account (Sign up at [ZegoCloud](https://www.zegocloud.com/))
- Firebase project for FCM setup
- Minimum SDK: 21 (Android 5.0 Lollipop)

## 🚀 Installation

1. **Clone the Repository**
```bash
git clone https://github.com/Vishalkumar800/lawyer_consultation.git
```

2. **Set Up ZegoCloud**
   - Log in to the [ZegoCloud Console](https://console.zegocloud.com/)
   - Create a project and obtain your `appID` and `appSign`
   - Update these values in `local.properties`

3. **Configure Firebase**
   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Enable FCM and download the `google-services.json` file
   - Place it in the `app/` directory
    
4. **Build the Project**
   - Open the project in Android Studio
   - Sync the project with Gradle
   - Build and run on an emulator or physical device (API 21+)

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


## 🙌 Acknowledgments

- **ZegoCloud** for powering voice and video calls
- **Jetpack Compose** for modern Android UI
- **Firebase** for reliable push notifications
- **Contributors** and the open-source community

## 📞 Contact

For issues, suggestions, or queries:
- GitHub: [Vishalkumar800](https://github.com/Vishalkumar800)
- Email: statusb130@gmail.com
- Linkedn: [Vishal]()

---

⭐ **Star this repository** if you find it helpful! We appreciate your support and feedback.

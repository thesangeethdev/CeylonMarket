# Ceylon Market

Building by - Sangeeth Amirthanathan

**Ceylon Market** A Kotlin Multiplatform mobile application that delivers daily vegetable, rice, fish, and other commodity prices from Sri Lankan markets. Built with Compose Multiplatform for both Android and iOS from a single shared codebase.

[<img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" alt="Get it on Google Play" height="80">](https://play.google.com/store/apps/details?id=com.sangeeth.ceylonmarket)

Time spent: TBA

## 📱 Platforms

| Android | iOS |
|---------|-----|
| ✅ Supported | ✅ Supported |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────-┐
│                        Shared Module                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐   │
│  │   UI Layer  │  │ ViewModels  │  │    Data Layer       │   │
│  │  (Compose)  │  │ (PreCompose)│  │  API (Ktor Client)  │   │
│  └─────────────┘  └─────────────┘  └─────────────────────┘   │
│         │                │                    │              │
│         ▼                ▼                    ▼              │
│  ┌────────────────────────────────────────────────────────┐  │
│  │              Kotlin Multiplatform (commonMain)         │  │
│  └────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────-┘
                              │
              ┌───────────────┼───────────────┐
              ▼               ▼               ▼
        ┌─────────┐                    ┌──────────┐
        │ Android │                    │ IOS      │
        │ (JVM)   │                    │ (Native) │
        └─────────┘                    └──────────┘
```

## Functionalities

**Completed** functionalities:

* [x] customized composable screens
* [x] loading components and screens
* [x] MVVM architecture lifecycle
* [x] Navigations using navigation3
* [x] fetch json api data


## Google Play Screenshots

Screen | Google Play |
--- |-------------|
Images | TBA         |

## Image Walkthrough

Here's a walkthrough of implemented user stories:

| Screen    | Home Screen - Andorid                                  | Detail View - Android                                          | Home Screen - IOS                                 | Detail View - IOS                                            |
|---|--------------------------------------------------------|----------------------------------------------------------------|---------------------------------------------------|--------------------------------------------------------------|
| Images    | <img src="/images/home_android.png" width="150" alt="Sign In"> | <img src="/images/detail_android.png" width="150" alt="home"> | <img src="/images/home_ios.png" width="150" alt="fab"> | <img src="/images/detail_ios.png" width="150" alt="orders"> |

# Screenshots Tablet
Screen | Landscape |
--- |-----------|
Images | TBA       |

## Workflow Diagram

TBA

## License

    Copyright 2026 Sangeeth Amirthanathan, Ceylon Market

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



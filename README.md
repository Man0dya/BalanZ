# 📱 BalanZ - Personal Finance Tracker

<div align="center">
  <img src="app/src/main/ic_launcher-playstore.png" alt="BalanZ Logo" width="100" height="100"/>
  
  ![Android](https://img.shields.io/badge/Platform-Android-green.svg)
  ![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)
  ![API](https://img.shields.io/badge/Min%20SDK-24-blue.svg)
  ![Target SDK](https://img.shields.io/badge/Target%20SDK-35-blue.svg)
  ![Version](https://img.shields.io/badge/Version-1.0-red.svg)
</div>

**BalanZ** is a modern, secure personal finance management Android application that helps users track their income, expenses, and spending patterns with intuitive visualizations and robust security features.

## 🌟 Key Features

### 🔐 Security First
- **PIN Authentication**: 6-digit PIN protection with AES-256 encryption
- **Secure Storage**: Uses Android's EncryptedSharedPreferences for sensitive data
- **Session Management**: Automatic PIN verification on app resume for enhanced security

### � Financial Management
- **Transaction Tracking**: Add, edit, and delete income/expense transactions
- **Category Management**: Organize transactions by customizable categories
- **Budget Monitoring**: Set monthly budgets with progress tracking and notifications
- **Real-time Balance**: Live updates of your financial status

### 📊 Data Visualization
- **Expense Analytics**: Interactive pie charts showing spending by category
- **Income Analysis**: Visual breakdown of income sources
- **Trend Charts**: Daily income/expense trends over the last 30 days
- **Progress Indicators**: Budget utilization with visual progress bars

### 🎨 User Experience
- **Modern UI**: Clean, Material Design-compliant interface
- **Dark Mode**: System-responsive dark/light theme support
- **Intuitive Navigation**: Bottom navigation with smooth fragment transitions
- **Data Export**: Export transaction history as PDF reports

### 📱 Data Management
- **Local Storage**: Secure local database with Room persistence
- **Backup & Restore**: Built-in data backup and restore functionality
- **Offline First**: Full functionality without internet connection

## 🏗️ Architecture & Tech Stack

**BalanZ** follows modern Android development best practices with a clean, maintainable architecture:

### Architecture Pattern
- **MVVM (Model-View-ViewModel)**: Separation of concerns with reactive UI
- **Repository Pattern**: Clean data access layer
- **LiveData & Data Binding**: Reactive UI updates

### Core Technologies
| Component | Technology |
|-----------|------------|
| **Language** | Kotlin 2.0.20 |
| **UI Framework** | Android Views with ViewBinding & DataBinding |
| **Database** | Room 2.6.1 (SQLite) |
| **Architecture** | MVVM + Repository Pattern |
| **Async** | Kotlin Coroutines + LiveData |
| **Security** | Android Security Crypto |
| **Charts** | MPAndroidChart 3.1.0 |
| **Navigation** | Navigation Component 2.8.3 |
| **PDF Export** | iText7 Core 7.2.5 |

### Development Tools
- **Build System**: Gradle with Kotlin DSL
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 35 (Android 15)
- **Compile SDK**: 35

## 📱 App Structure

The app consists of four main sections accessible via bottom navigation:

1. **🏠 Home**: Dashboard with balance overview and budget progress
2. **💳 Transactions**: List and manage all financial transactions
3. **📊 Statistics**: Visual analytics and spending insights
4. **⚙️ Settings**: App configuration, themes, and security settings

## � Getting Started

### Prerequisites
- Android Studio Arctic Fox or newer
- JDK 11 or higher
- Android SDK with API level 24+

### Installation
1. **Clone the repository**
   ```bash
   git clone https://github.com/Man0dya/BalanZ.git
   cd BalanZ
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Build and Run**
   - Let Gradle sync the project
   - Connect an Android device or start an emulator
   - Click "Run" or press `Ctrl+R`

### First Launch
1. The app will prompt you to set up a 6-digit PIN for security
2. Once set, you'll be taken to the main dashboard
3. Start adding your first transactions using the "+" button

## 🔧 Configuration

### Database Schema
The app uses Room database with automatic migrations. Schema files are stored in `app/schemas/` for version control.

### Security Configuration
- PIN hashes are stored using Android's EncryptedSharedPreferences
- AES-256 encryption ensures maximum security
- Session management requires PIN re-entry after app backgrounding

## � Project Structure

```
app/
├── src/main/
│   ├── java/com/example/balanz/
│   │   ├── data/              # Database entities, DAOs, and database
│   │   ├── model/             # Data models and categories
│   │   ├── repository/        # Data repositories
│   │   ├── ui/                # UI components (fragments, adapters)
│   │   │   ├── home/          # Home dashboard
│   │   │   ├── statistics/    # Charts and analytics
│   │   │   ├── transaction/   # Transaction management
│   │   │   └── settings/      # App settings
│   │   ├── util/              # Utility classes
│   │   ├── viewmodel/         # ViewModels for MVVM
│   │   ├── MainActivity.kt    # Main app container
│   │   └── PinActivity.kt     # PIN authentication
│   └── res/                   # Resources (layouts, strings, themes)
└── build.gradle.kts           # App-level build configuration
```

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. **Fork the repository**
2. **Create a feature branch** (`git checkout -b feature/AmazingFeature`)
3. **Commit your changes** (`git commit -m 'Add some AmazingFeature'`)
4. **Push to the branch** (`git push origin feature/AmazingFeature`)
5. **Open a Pull Request**

### Development Guidelines
- Follow Kotlin coding conventions
- Maintain MVVM architecture patterns
- Add unit tests for new features
- Update documentation as needed

## 🐛 Known Issues & Roadmap

### Current Limitations
- No cloud sync functionality
- Limited currency support
- No multi-account management

### Future Enhancements
- [ ] Cloud backup and sync
- [ ] Multiple currency support
- [ ] Recurring transactions
- [ ] Advanced budgeting features
- [ ] Spending goals and challenges
- [ ] Export to multiple formats (CSV, Excel)

## 📱 Screenshots

*Screenshots coming soon - the app features a clean, modern interface with intuitive navigation and beautiful data visualizations.*

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Man0dya** - [GitHub Profile](https://github.com/Man0dya)

## 🙏 Acknowledgments

- Material Design Guidelines for UI inspiration
- MPAndroidChart library for beautiful charts
- Android Jetpack libraries for modern Android development
- The Android developer community for continuous learning

---

<div align="center">
  <p>⭐ If you found this project helpful, please consider giving it a star!</p>
  <p>📧 For questions or suggestions, feel free to open an issue or reach out.</p>
</div>


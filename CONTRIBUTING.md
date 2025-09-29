# Contributing to BalanZ

Thank you for your interest in contributing to BalanZ! We welcome contributions from the community and are grateful for any help you can provide.

## 🤝 How to Contribute

### Reporting Issues

Before creating an issue, please:
- Check if the issue already exists in our [issue tracker](https://github.com/Man0dya/BalanZ/issues)
- Make sure you're using the latest version of the app
- Provide as much detail as possible about the problem

When creating an issue, please include:
- **Device Information**: Android version, device model
- **App Version**: The version of BalanZ you're using
- **Steps to Reproduce**: Clear steps to reproduce the issue
- **Expected Behavior**: What you expected to happen
- **Actual Behavior**: What actually happened
- **Screenshots**: If applicable, add screenshots to help explain the problem

### Suggesting Features

We welcome feature suggestions! Please:
- Check if the feature has already been requested
- Clearly describe the feature and its benefits
- Explain why this feature would be useful to BalanZ users
- Consider how it fits with the app's current design and philosophy

### Code Contributions

#### Prerequisites

Before you start contributing code, make sure you have:
- **Android Studio**: Arctic Fox or newer
- **JDK 11**: Or higher
- **Git**: For version control
- **Android SDK**: With API level 24+

#### Setting Up Your Development Environment

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/your-username/BalanZ.git
   cd BalanZ
   ```
3. **Add the original repository** as upstream:
   ```bash
   git remote add upstream https://github.com/Man0dya/BalanZ.git
   ```
4. **Open the project** in Android Studio
5. **Let Gradle sync** and resolve dependencies

#### Development Workflow

1. **Create a new branch** for your feature or fix:
   ```bash
   git checkout -b feature/your-feature-name
   # or
   git checkout -b fix/issue-description
   ```

2. **Make your changes** following our coding standards (see below)

3. **Test your changes** thoroughly:
   - Run the app on different devices/emulators
   - Test both light and dark themes
   - Verify PIN security still works
   - Test database operations

4. **Commit your changes** with clear, descriptive messages:
   ```bash
   git add .
   git commit -m "Add feature: brief description of what you added"
   ```

5. **Push to your fork**:
   ```bash
   git push origin feature/your-feature-name
   ```

6. **Create a Pull Request** from your fork to the main repository

#### Pull Request Guidelines

When creating a pull request:

- **Use a clear title** that describes what your PR does
- **Fill out the PR template** completely
- **Reference any related issues** using "Fixes #issue-number" or "Closes #issue-number"
- **Include screenshots** for UI changes
- **Keep changes focused** - one feature or fix per PR
- **Update documentation** if needed
- **Test on multiple devices** if possible

## 📋 Coding Standards

### Kotlin Code Style

Please follow these guidelines when writing Kotlin code:

- **Follow Android Kotlin Style Guide**: Use the official [Android Kotlin style guide](https://developer.android.com/kotlin/style-guide)
- **Use meaningful names**: Variables, functions, and classes should have descriptive names
- **Keep functions small**: Aim for functions that do one thing well
- **Add comments**: For complex logic or business rules
- **Use proper indentation**: 4 spaces (configured in Android Studio)

### Architecture Guidelines

BalanZ follows MVVM architecture. Please maintain this pattern:

- **Models**: Data classes and entities in `data/entity/`
- **Views**: Activities and Fragments in `ui/`
- **ViewModels**: Business logic in `viewmodel/`
- **Repository**: Data access layer in `repository/`
- **Database**: Room DAOs in `data/dao/`

### UI Guidelines

- **Follow Material Design**: Use Material Design components and guidelines
- **Support both themes**: Ensure your UI works in light and dark modes
- **Use ViewBinding**: For type-safe view references
- **Responsive design**: Test on different screen sizes
- **Accessibility**: Add content descriptions for UI elements

### Security Guidelines

BalanZ handles sensitive financial data. Please:

- **Never log sensitive data**: PIN codes, transaction amounts, etc.
- **Use EncryptedSharedPreferences**: For storing sensitive information
- **Validate inputs**: Always validate user inputs
- **Follow security best practices**: For data handling and storage

## 🧪 Testing

### Before Submitting

Please test your changes with:

1. **Manual Testing**:
   - Test the happy path (normal usage)
   - Test edge cases and error conditions
   - Test on different Android versions (API 24+)
   - Test both light and dark themes
   - Test with different screen sizes

2. **Security Testing**:
   - Verify PIN protection still works
   - Check that sensitive data isn't exposed
   - Test backup/restore functionality

3. **Performance Testing**:
   - Test with large numbers of transactions
   - Check for memory leaks
   - Verify smooth animations and transitions

### Unit Tests

If you're adding new business logic:
- Write unit tests for your code
- Place tests in `src/test/java/`
- Use meaningful test names that describe what's being tested
- Aim for good test coverage of critical paths

## 📝 Documentation

When contributing, please also update:

- **Code comments**: For complex business logic
- **README.md**: If you're adding new features
- **Inline documentation**: For public methods and classes

## 🏗️ Project Structure

Understanding the project structure will help you contribute effectively:

```
app/src/main/java/com/example/balanz/
├── data/                  # Database layer
│   ├── entity/           # Room entities
│   ├── dao/              # Data Access Objects
│   └── AppDatabase.kt    # Room database
├── model/                # Data models
├── repository/           # Repository pattern implementation
├── ui/                   # User Interface
│   ├── home/            # Home dashboard
│   ├── statistics/      # Charts and analytics
│   ├── transaction/     # Transaction management
│   └── settings/        # App settings
├── util/                # Utility classes
├── viewmodel/           # ViewModels (MVVM pattern)
├── MainActivity.kt      # Main app container
└── PinActivity.kt       # PIN authentication
```

## 🚀 Release Process

1. **Version Numbering**: We follow semantic versioning (MAJOR.MINOR.PATCH)
2. **Release Branches**: Features are merged to `main`, releases are tagged
3. **Testing**: All releases go through thorough testing
4. **Documentation**: Release notes are maintained for each version

## 💬 Getting Help

If you need help with contributing:

- **GitHub Issues**: Ask questions by creating an issue with the "question" label
- **Code Review**: Don't hesitate to ask for feedback during the PR process
- **Documentation**: Check existing code and documentation for examples

## 🎉 Recognition

Contributors will be:
- Listed in the project's contributors section
- Mentioned in release notes for significant contributions
- Invited to be maintainers for sustained, high-quality contributions

## 📄 License

By contributing to BalanZ, you agree that your contributions will be licensed under the same license as the project (MIT License).

---

Thank you for contributing to BalanZ! Your efforts help make personal finance management more accessible and secure for everyone. 🙏
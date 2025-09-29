# Security Policy

## 🔐 Security Commitment

BalanZ is a personal finance application that handles sensitive financial data. We take security seriously and are committed to protecting our users' financial information and privacy.

## 🛡️ Security Features

### Current Security Measures

- **PIN Authentication**: 6-digit PIN protection with secure hashing
- **Encrypted Storage**: Uses Android's EncryptedSharedPreferences with AES-256 encryption
- **Session Management**: Automatic re-authentication after app backgrounding
- **Local Data**: All data stored locally on device, no cloud transmission
- **Input Validation**: Comprehensive validation of all user inputs
- **Secure Coding**: Following OWASP mobile security guidelines

### Data Protection

- **Financial Data**: All transaction data encrypted at rest
- **Authentication**: PIN codes are hashed and salted before storage
- **No Network**: App functions entirely offline, reducing attack surface
- **Backup Security**: Data backups maintain encryption standards

## 🐛 Supported Versions

We provide security updates for the following versions:

| Version | Supported          |
| ------- | ------------------ |
| 1.0.x   | ✅ Yes             |
| < 1.0   | ❌ No              |

## 📢 Reporting a Vulnerability

If you discover a security vulnerability in BalanZ, please help us protect our users by reporting it responsibly.

### How to Report

**Please DO NOT report security vulnerabilities through public GitHub issues.**

Instead, please report security vulnerabilities by:

1. **Creating a private security advisory** on GitHub:
   - Go to the [Security tab](https://github.com/Man0dya/BalanZ/security)
   - Click "Report a vulnerability"
   - Fill out the security advisory form

2. **Direct contact** (if GitHub security advisories are not available):
   - Create a GitHub issue with the title "Security Issue - Private Discussion Needed"
   - Do not include vulnerability details in the public issue
   - Wait for maintainer response for private communication

### What to Include

When reporting a security vulnerability, please include:

- **Type of vulnerability** (e.g., encryption weakness, authentication bypass)
- **Affected components** (specific files, functions, or features)
- **Steps to reproduce** the vulnerability
- **Potential impact** on users and data
- **Suggested fix** (if you have one)
- **Your contact information** for follow-up questions

### Response Timeline

We are committed to addressing security vulnerabilities promptly:

- **Acknowledgment**: Within 48 hours of report
- **Initial Assessment**: Within 1 week
- **Fix Development**: Varies by complexity (1-4 weeks typically)
- **Release**: As soon as fix is tested and verified
- **Public Disclosure**: After fix is released (coordinated disclosure)

### Security Advisory Process

1. **Vulnerability received** and acknowledged
2. **Assessment and verification** of the issue
3. **Fix development** and internal testing
4. **Security patch release** with version update
5. **Public security advisory** published (after users have time to update)
6. **Recognition** of reporter (if they wish to be credited)

## 🏆 Security Recognition

We appreciate security researchers who help keep BalanZ secure:

- **Hall of Fame**: Contributors will be listed in our security acknowledgments
- **Coordinated Disclosure**: We follow responsible disclosure practices
- **Credit**: Security researchers will be credited in release notes (unless they prefer anonymity)

## 🔒 Security Best Practices for Users

To maximize your security when using BalanZ:

### For Users
- **Strong PIN**: Use a unique 6-digit PIN that others can't guess
- **Device Security**: Keep your Android device updated and secured
- **App Updates**: Install BalanZ updates promptly when available
- **Screen Lock**: Use additional device security (fingerprint, face unlock, etc.)
- **Backup Security**: Store app backups in secure locations

### For Developers
- **Code Review**: All security-related code changes require review
- **Dependency Updates**: Keep all dependencies updated for security patches
- **Static Analysis**: Use security scanning tools during development
- **Testing**: Include security testing in your development workflow

## 📋 Security Checklist

When contributing to BalanZ, please ensure:

- [ ] No hardcoded secrets or keys
- [ ] Input validation for all user data
- [ ] Sensitive data is encrypted before storage
- [ ] No sensitive information in logs
- [ ] Authentication mechanisms are not bypassed
- [ ] Error messages don't leak sensitive information
- [ ] Dependencies are up-to-date and secure

## 🔍 Security Audit

BalanZ undergoes regular security reviews:

- **Code Review**: All changes reviewed for security implications
- **Dependency Scanning**: Regular checks for vulnerable dependencies
- **Static Analysis**: Automated security scanning of codebase
- **Manual Testing**: Regular security testing of authentication and encryption

## 📚 Security Resources

For more information about mobile app security:

- [OWASP Mobile Security](https://owasp.org/www-project-mobile-security/)
- [Android Security Best Practices](https://developer.android.com/topic/security)
- [Kotlin Security Guidelines](https://kotlinlang.org/docs/security.html)

## 📞 Contact

For security-related questions or concerns:
- Use GitHub Security Advisories for vulnerabilities
- Create a GitHub issue for general security questions
- Check our documentation for security implementation details

---

**Remember**: The security of our users' financial data is our top priority. Thank you for helping us maintain the highest security standards.
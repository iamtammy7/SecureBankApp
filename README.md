SecureBankApp[H250595R]

Secure Banking Application

SecureBank is a Java desktop banking application that lets users create an account, log in securely, check their balance, deposit and withdraw funds, and view their transaction history. All data is persisted to files, and user passwords are hashed with SHA-256 and a random salt before storage.

Name: [OLGA TAMARI NHENDE]
Registration Number: [H250295R]

User registration with a unique auto-generated account number (SB2026NNNN)
Secure sign-in with SHA-256 + salted password hashing
Personal dashboard showing account number, account type, and status
Real-time account balance display
Deposit money with input validation
Withdraw money with insufficient-funds check
Full transaction history shown in a table
Per user data isolation :each customer's balance and transactions are stored in their own files
Graceful logout

Prerequisites
Java 8 or higher
Any terminal (PowerShell, CMD, or Bash)

From the project root, compile all sources:
Run the application:
The welcome screen appears. Choose CREATE ACCOUNT to register, or SIGN IN if you already have an account.

Passwords are never stored in plain text.
Each password is combined with a random 16-character salt and hashed with SHA-256.
Stored format is salt:hash, so identical passwords produce different stored values.

[OLGA TAMARI NHENDE] — [H250595R]

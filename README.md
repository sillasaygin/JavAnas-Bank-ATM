# 🏦 JavAnas Bank: Enterprise ATM & Financial Management Solution

**JavAnas Bank** is a robust, high-fidelity ATM management system designed with enterprise-grade scalability and Object-Oriented Programming (OOP) principles. This solution provides a seamless banking experience, handling complex financial transactions through a modular and secure architecture.

## 🌟 Key Features

* **Dual-Account Framework**: Integrated management for both **Checking** and **Saving** accounts within a unified interface.

* **Real-time Maturity Engine**: Automated daily interest projection for saving accounts to simulate long-term wealth growth.

* **Secure Transaction Logic**: Advanced handling for deposits, withdrawals, and multi-account transfers with strict validation.

* **Persistent Data Layer**: Efficient File I/O system ensuring all account states are preserved and restored across sessions via `accounts.txt`.

* **Granular Error Management**: Custom exception handling to manage financial risks such as insufficient funds or invalid inputs.

## 🛠️ Architecture & Core Principles

The system is built on a solid foundation of software engineering patterns:

* **Abstraction & Inheritance**: Utilizes a core `BankAccount` abstract class to enforce consistent behavior across different financial products.

* **Encapsulation**: Protects sensitive financial data through private access modifiers.

* **Polymorphism**: Implements specialized logic for interest calculation based on account types.

### 📈 Mathematical Projection

The system calculates daily maturity interest using the following formula:

$$
B_{new} = B_{current} \times 1.01
$$

## 👥 Engineering Team

This project was architected and developed by the **JavAnas Engineering Team**:

* **Sıla Saygın** 

* **Ege Can Okkaoğlu** 

* **Ege Efe Özkan** 

## 🚀 Deployment & Execution

The system is cross-platform and can be launched using the provided automated scripts.

### For Windows Users:

Simply double-click `Run.bat` or execute via terminal:

```bash
./Run.bat
```

### For Mac & Linux Users:

```bash
# 1. Grant execution permission (first time only):
chmod +x run.sh

# 2. Run the application:
./run.sh
```

*Note: Ensure you have Java JDK installed to compile and run the application.*

# 🛒 Epos2 — Point of Sale System

> A desktop Point of Sale (POS) application built in Java using NetBeans and Microsoft Access, designed for small to medium enterprises to manage transactions, stock, users, and sales monitoring.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Screenshots](#screenshots)
- [Database Design](#database-design)
- [Getting Started](#getting-started)
- [Technologies Used](#technologies-used)
- [Known Issues & Limitations](#known-issues--limitations)
- [Testing](#testing)
- [Developer Notes](#developer-notes)

---

## Overview

Small and large enterprises face challenges managing transactions, stock, user accounts, and reporting. **Epos2** addresses these by providing a role-based POS system where:

- **Employees** handle customer transactions — adding items to a cart, processing checkouts, and managing customer details.
- **Admins** oversee the entire system — managing users, monitoring sales activity across date ranges, viewing stock, and generating reports.

The system enforces security through a login window that validates credentials against a role (Employee or Admin), logs each login attempt with the device's MAC address, and locks out users after 3 failed attempts.

---

## Features

### 🔐 Login & Security
- Role-based login (Admin / Employee) with username and password validation.
- Tracks the MAC address of the device used to log in, adding an extra layer of device-level security.
- Locks the login window after **3 failed attempts** and prompts the user to contact an administrator.
- Auto-generates usernames and passwords for new users.

### 👤 Admin Window
- **User Management** — Create, update, and delete employee records. Retrieve user details by username. Auto-generate secure usernames and passwords.
- **Stock** — View and manage product stock levels.
- **Monitor** — Filter and view all sales transactions within a selected date range, showing employee name, customer, product, and total.
- **Report** — Generate sales reports based on a selected date range.

### 🧾 Employee Window
- Add products to a cart (table) during a transaction by selecting from a product combo box and specifying quantity.
- Remove items from the cart before checkout.
- Fill in customer details (CustomerID, Firstname, Surname, Email, Age, Gender).
- Complete the transaction with the **Checkout** button, which saves the order to the database.
- **Clear** and **Get** buttons for cart and order management.

---

## Screenshots

| Login | Admin Monitor | Employee Checkout |
|---|---|---|
| Role-based login with attempt tracking | Date-range sales filter with employee/customer/product breakdown | Cart management with add/remove/checkout |

> Screenshots from functional testing (August 2024) confirm all core flows work as expected — see the files in  [`docs/`](./docs) folder.

---

## Database Design

The system uses a **Microsoft Access (.accdb)** database with four tables, connected via UCanAccess JDBC.

### Customers
Stores customer information collected at the point of sale.

| Field | Type |
|---|---|
| CustomerID | AutoNumber (PK) |
| Firstname | Short Text |
| Surname | Short Text |
| Email | Short Text |
| Age | Number |
| Gender | Short Text |
| Nationality | Short Text |

### Orders
The central transaction table that links customers, employees, and products.

| Field | Type |
|---|---|
| OrderID | AutoNumber (PK) |
| CustomerID | Number (FK) |
| EmployeeID | Number (FK) |
| ProductID | Number (FK) |
| DatePurchased | Date/Time |
| ProductName | Long Text |
| Quantity | Large Number |
| Total | Currency |

### Employee
Stores employee credentials and profile information.

| Field | Type |
|---|---|
| EmployeeID | AutoNumber (PK) |
| IDNumber | Large Number |
| FirstName | Short Text |
| SurName | Short Text |
| Email | Short Text |
| Username | Short Text |
| Password | Short Text |
| Nationality | Short Text |
| Age | Number |
| Role | Short Text |

### Products
Stores the product catalogue available for sale.

| Field | Type |
|---|---|
| ProductID | AutoNumber (PK) |
| ProductName | Short Text |
| Category | Short Text |
| Stock | Number |
| Price | Currency |

---

## Getting Started

### Prerequisites

Before running the project, ensure you have the following installed:

- **Java JDK 11+**
- **Apache NetBeans IDE 20+** (or NetBeans 8.1)
- **Microsoft Access 365** (for the database file)

### Required Libraries

Download and add **all** of the following JAR files to your project's Libraries in NetBeans. UCanAccess will not work if any of these are missing:

| JAR | Purpose |
|---|---|
| `ucanaccess-5.0.1.jar` | Main JDBC driver for Access |
| `commons-lang3-3.8.1.jar` | Apache Commons dependency |
| `commons-logging-1.2.jar` | Apache Commons dependency |
| `hsqldb-2.5.0.jar` | In-memory database engine used by UCanAccess |
| `jackcess-3.0.1.jar` | Reads/writes the .accdb file format |
| `jcalendar-1.4.jar` | JDateChooser UI component |
| `AbsoluteLayout.jar` | NetBeans layout manager |

### Installation

1. Clone this repository:
   ```bash
   git clone https://github.com/tensorshinobi/Epos2.git
   ```

2. Open the project in **NetBeans** via `File > Open Project`.

3. Right-click the project → **Properties** → **Libraries** → **Add JAR/Folder**, and add all the JARs listed above.

4. Update the database connection path in your `DBM` class or connection initialisation to point to where `Employee.accdb` lives on your machine:
   ```java
   // Use forward slashes to avoid escape character issues in Java
   String url = "jdbc:ucanaccess://C:/Users/YourName/Documents/Epos2/Employee.accdb";
   ```

5. Run the project. The Login window will appear. Use the credentials stored in your Employee table to log in.

---

## Technologies Used

- **Java (Swing / AWT)** — Desktop GUI built with JFrame, JTable, JInternalFrame, and JDateChooser components.
- **UCanAccess** — Pure-Java JDBC driver that allows Java to connect to Microsoft Access databases without requiring Microsoft Office to be installed.
- **Microsoft Access** — Relational database storing all persistent data (employees, customers, orders, products).
- **NetBeans IDE** — Development environment used for GUI design and project management.
- **java.net (InetAddress / NetworkInterface)** — Used to retrieve the MAC address of the login device for security logging.
- **java.time (LocalDate / DateTimeFormatter)** — Used for date range parsing in the Monitor and Report tabs.

---

## Known Issues & Limitations

**Report generation** currently allows date-range selection rather than the originally planned preset periods (daily/weekly/monthly/yearly). This was an intentional design adjustment made during development.

**Checkout date formatting** had a known issue at the time of Phase 4 testing — converting the current date into a format acceptable by the Access `Date/Time` field type required careful handling. The recommended fix is to format the date using `yyyy-MM-dd` before inserting into the database:

```java
// When inserting DatePurchased during checkout, format it like this:
String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
// Then use it in your INSERT statement
```

**JDateChooser default state** — Both date pickers on the Monitor and Report tabs must be initialised with a default date in the constructor, otherwise `getDate()` returns `null` before the user interacts with them:

```java
// In the NewJFrame constructor, after initComponents():
dtcMonitorFrom.setDate(new java.util.Date());
dtcMonitorTo.setDate(new java.util.Date());
dtcReportFrom.setDate(new java.util.Date());
dtcReportTo.setDate(new java.util.Date());
```

**SQL injection vulnerability** — The current `CheckUserNamePassword` method builds the login query using string concatenation. For production use, this should be replaced with a `PreparedStatement` to prevent SQL injection attacks.
(NB: This part was entirely suggested by Claude and not me, thus further verification of its validility needs to done)
---

## Testing

Functional testing was carried out by two testers on **August 6, 2024**.

**David Dwumah** verified the Employee window — logging in with the `mei_chen / Employee` role, adding products to the cart, and successfully removing items using the combo box. The cart correctly updated after each remove action.

**Amanfo Dwumah** verified the Admin window — logging in with the `emma_johnson / Admin` role, using the Monitor tab to filter sales between August 1, 2023 and August 3, 2024 (results displayed correctly), creating a new user via User Management with auto-generated credentials, updating that user's email, and successfully deleting the user from the system.

Login testing covered three scenarios: standard valid credentials (granted access with welcome message), abnormal data with a wrong role/truncated password (rejected with attempt counter), and extreme/minimal input (correctly rejected with 2 attempts remaining shown).

---

## Developer Notes

### Externally Sourced Code

The `getMacaddress()` method uses `java.net.InetAddress` and `java.net.NetworkInterface` to retrieve the hardware MAC address of the machine. This was sourced externally and adapted for use in login logging. It formats the raw byte array into the standard `XX-XX-XX-XX-XX-XX` hexadecimal notation.

### Recommended Hardware

| | Developer Machine | Minimum for Users |
|---|---|---|
| CPU | 1.1 GHz dual core | 1.1 GHz dual core |
| RAM | 8 GB DDR4 | 4 GB DDR4 |
| Storage | 256 GB SSD | 2 GB SSD |
| OS | Windows | Windows |

---

## Contact & Support

For technical support or queries, contact the developer:

- 📧 dwumahdesmond@gmail.com
- 📞 +27 77 391 4111


---

## 📁 Project Documentation

The original phase documents used during planning and development are available in the [`docs/`](./docs) folder of this repository. These include the Phase 1 specification (problem summary, program functions, database design, and hardware/software requirements) and the Phase 4 report (externally sourced code, algorithm explanations, functional testing, and evaluation). They provide full context for the design decisions made throughout the project.

---

## 🤖 About This README

This README was generated with the assistance of [Claude](https://claude.ai) (Anthropic, claude-sonnet-4-6) using the following prompt:

> *"based on these files generate a nice readme for github"*

The two project PDF documents from the `docs/` folder were uploaded directly to Claude as context. The content, structure, known issues, and code snippets were all derived from those source documents and the development conversation that preceded the generation.
Although generated with Claude I have done my best to edit and fix some parts.
---

*Epos2 — Built with Java & NetBeans | Database: Microsoft Access via UCanAccess*

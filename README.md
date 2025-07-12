
# 📦 Inventory Management System (Java)

A desktop-based inventory management application built in Java using Swing and JDBC.  
This system allows users to manage categories, items, suppliers, transactions, and users with role-based access control.

> ✅ Project Status: Fully Functional  
> 🛠️ Technologies Used: Java, Swing, JDBC, MVC Architecture

---

## ✨ Features

- 🔐 **User Authentication**  
  - Login screen with role-based access
  - Admin/user roles with permission control

- 📦 **Item & Category Management**  
  - Add, update, delete items and categories
  - Real-time inventory updates

- 🚚 **Supplier Management**  
  - Manage supplier details
  - Track supplier-wise inventory

- 🔄 **Transaction Management**  
  - Record incoming/outgoing transactions
  - Maintain history and stock updates

- 👥 **User Management**  
  - Add/remove users
  - Assign roles and manage permissions

- 🎨 **Theme & State Handling**  
  - `ApplicationThemeConfig.java` and `ApplicationState.java` provide session and style control

## 🔧 How to Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/inventory-management-system-java.git
   cd inventory-management-system-java

2. **Open in IntelliJ IDEA / Eclipse**

3. **Set up the database (MySQL):**

   * Create necessary tables for `users`, `items`, `transactions`, etc.
   * Update DB config in your DAO classes or use a config file

4. **Run `Main.java`** to launch the app


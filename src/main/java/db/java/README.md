# Inventory Management API

A Java-based API for managing inventory, suppliers, locations, and transactions. Built with JDBC for database connectivity.

## 📦 Features
- CRUD operations for products, suppliers, users, and locations.
- Transaction logging for inventory movements.
- Modular design with extensible database classes.

## 💻 API Developers
- Berrached Maroua (also called marvi)
- Souane Abdenour (also called rayden)
[GITHUB Profile](https://github.com/abdorayden)
[Personnel Profile](https://rayden-six.vercel.app/)

## 🛠️ Setup

### Prerequisites
- Java 11+
- Maven (optional)

---

## 🚀 Usage

### Core Classes
| Class                     | Description                                |
|---------------------------|--------------------------------------------|
| `ProduitModeleDatabase`   | Manage product models (e.g., "Laptop-X200")|
| `FournisseurDatabase`     | Handle supplier records                    |
| `EmplacementDatabase`     | Track storage locations (warehouses, etc.) |
| `Operation`               | CRUD interface                             |
| `BonDatabase`             | Manage inventory orders/receipts           |

### initialisation of the class
```java
try {
    // Create the database access object
    // first parameter is for Connection interface or ConfigDatabase class if Connection already created , null will create connection by default
    // second for the id col null will take id col name by default
    // therd for the tablename null will take tablename by default
    FournisseurDatabase fournisseurDB = new FournisseurDatabase(null , null , null);
    
    // Or with custom ID column name
    // FournisseurDatabase fournisseurDB = new FournisseurDatabase("custom_id_column");
} catch (ConnectionFailedException e) {
    System.err.println("Failed to connect to database: " + e.getMessage());
} catch (LoadPropertiesException e) {
    System.err.println("Failed to load database properties: " + e.getMessage());
}
```

### Example: Create a Fournisseur (example table)
```java
Fournisseur newSupplier = new Fournisseur();
newSupplier.setIdF("F1001");
newSupplier.setNom("Tech Solutions Inc.");
newSupplier.setAdresse("123 Business Ave");
newSupplier.setNumeroTlph("0550123456");
newSupplier.setEmail("contact@techsolutions.dz");
newSupplier.setNIF("NIF123456789");
newSupplier.setNIS("NIS987654321");
newSupplier.setIA("IA456123789");
newSupplier.setRC("RC789456123");

try {
    fournisseurDB.add(newSupplier);
    System.out.println("Supplier added successfully!");
} catch (OperationFailedException e) {
    System.err.println("Failed to add supplier: " + e.getMessage());
}
```

### Example: Getting object by id in table
```java
try {
    Fournisseur foundSupplier = fournisseurDB.findById("F1001");
    if (foundSupplier != null) {
        System.out.println("Found supplier: " + foundSupplier.getNom());
        System.out.println("Email: " + foundSupplier.getEmail());
    } else {
        System.out.println("Supplier not found");
    }
} catch (OperationFailedException e) {
    System.err.println("Search failed: " + e.getMessage());
}
```
---

## 📚 Class Documentation

### `Read Comments`
check Operation interface , EntityCoreDatabase and FournisseurDatabase (as example) classes you will understand how this api works

**Key**:
- `Operation `: contains methods that implemented by EntityCoreDatabase class
- `EntityCoreDatabase `: abstract class that implement Operation interface based on tables feature (col count , table name , ...) and this features handled using abstract methods 
- `FournisseurDatabase`: Example class to see how it works , also contain comments to explain everything


---

## 🤝 Contributing
1. Fork the project.
2. Create a branch: `git checkout -b feature/your-feature`.
3. Commit changes: `git commit -m "Add awesome feature"`.
4. Push: `git push origin feature/your-feature`.
5. Open a pull request.

# db.java package

## TODO:
[ ] - create class Authuntication to check username and password of user account
[ ] - create classes that handle the data we needed to display in Views
[ ] - Handle ID privamry key for all <Tables>Database.java

```java
// abstract class that generate primary key for each class
public abstract String generatedIdPK() throws SQLException;

// Example:
// in FournisseurDatabase.java file
@Override
public String generatedIdPK() throws SQLException{
	long idx = super.countAll(); // we call countAll method from Operation interface that implemented By EntityCoreDatabase.java class
	return String.format("%02d" , idx); // return the generated id
}

// Where should I add this ID now
// there's other abstract method that add the data from object to database table in EntityCoreDatabase.java class 
protected abstract void setAddParameters(PreparedStatement statement, T obj) throws SQLException;
// for example in FournisseurDatabase.java file

protected void setAddParameters(PreparedStatement statement, Fournisseur obj) throws SQLException {
		// TODO: instead of getting id , we will generated
		statement.setString(1, this.generatedIdPK()); // <= here we add the generated id
		statement.setString(2, obj.getAdresse());
		statement.setString(3, obj.getNum_tel());
		statement.setString(4, obj.getNom_f());
		statement.setString(5, obj.getMail_f());
		statement.setString(6, obj.getNIF());
		statement.setString(7, obj.getNIS());
		statement.setString(8, obj.getIA());
		statement.setString(9, obj.getRC());
}
```

package db.utils;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.io.UnsupportedEncodingException;
import java.util.List;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import db.java.CompteDatabase;
import db.errors.OperationFailedException;
import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;

import testpackage.model.core.Compte;

public class AuthunticationVerification {

	private String username; 
	private String password;
	private CompteDatabase compte;

	public AuthunticationVerification(String username , String password){
		try{
			this.compte = new CompteDatabase(null , null);
		}catch(ConnectionFailedException | LoadPropertiesException error){
			this.compte = null;
		}
		this.username = username;
		this.password = password;
	}

	private String getHash256String(String password){
		try{
			MessageDigest diget = MessageDigest.getInstance("SHA-256");
			byte[] hash = diget.digest(password.getBytes("UTF-8"));
			StringBuilder builder = new StringBuilder();
			for(byte b : hash){
				String hex = Integer.toHexString(0xff & b);
				if(hex.length() == 1)	builder.append('0');
				builder.append(hex);
			}
			return builder.toString();
		}catch(NoSuchAlgorithmException | UnsupportedEncodingException e){
			return null;
		}
	}

	public boolean checkAuth(){

		if(this.compte == null)	return false;

		// size of comptes must be one because username is unique 
		List<Compte> comptes = null;
		try{

			comptes = compte.search(this.username);
			System.out.println(comptes.getFirst().getMot_de_passe()+comptes.getFirst().getNom_utilisateur());

			if(comptes == null || comptes.isEmpty())	return false;
		}catch(OperationFailedException error){
			// DEBUG ...
			System.out.println("auth check op failed error : " + error.getMessage());
			return false;
		}
		String hashedPassword = getHash256String(this.password);
		if(hashedPassword != null){
			System.out.println(hashedPassword.equals(comptes.getFirst().getMot_de_passe()));
			return hashedPassword.equals(comptes.getFirst().getMot_de_passe());
		}
		return false;
	}
}

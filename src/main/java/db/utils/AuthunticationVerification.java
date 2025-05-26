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

import stateMachin.Session;

public class AuthunticationVerification {

	private String username; 
	private String password;
	private CompteDatabase compte;

	private Compte currentCompt;

	public AuthunticationVerification(String username , String password){
		try{
			this.compte = new CompteDatabase(null , null);
		}catch(ConnectionFailedException | LoadPropertiesException error){
			this.compte = null;
		}
		this.username = username;
		this.password = password;

		this.currentCompt = null;
	}

	public Compte getCurrentCompte() {
		return this.currentCompt;
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
			if(comptes == null || comptes.isEmpty())	return false;
		}catch(OperationFailedException error){
			return false;
		}
		String hashedPassword = getHash256String(this.password);
		if(hashedPassword != null){
			Session.login(comptes.get(0));
			return hashedPassword.equals(comptes.get(0).getMot_de_passe());
		}
		return false;
	}

	public boolean newPassword(String pass){
		try {
			List<Compte> comptes = compte.search(username);

			if (comptes == null || comptes.isEmpty()) {
				return false;
			}

			Compte original = comptes.get(0);
			Compte updated = new Compte();
			updated.setId_c(original.getId_c());
			updated.setNom_utilisateur(original.getNom_utilisateur());
			updated.setMot_de_passe(getHash256String(pass));
			updated.setRoles(original.getRoles());

			compte.update(original, updated);
			Session.login(updated);
			return true;

		} catch (OperationFailedException e) {
			return false;
		}
	}
}

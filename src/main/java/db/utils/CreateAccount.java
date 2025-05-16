package db.utils;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.io.UnsupportedEncodingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import db.java.CompteDatabase;
import db.errors.OperationFailedException;
import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;

import testpackage.model.enumeration.Roles;
import testpackage.model.core.Compte;

public class CreateAccount {

	private String username;
	private String password;
	private CompteDatabase compte;

	public CreateAccount(String username , String password){
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

	public void login(){
		try{
			Compte forLogin = new Compte();
			forLogin.setNom_utilisateur(this.username);
			forLogin.setMot_de_passe(this.getHash256String(this.password));
			forLogin.setRoles(Roles.ADMINISTRATEUR);
			this.compte.add(forLogin);	
		}catch(NullPointerException error){
			// database init failed
			System.out.println("init Create Account" + error.getMessage());
			return ;
		}catch(OperationFailedException error){
			// username already exists
			System.out.println("add Create Account" + error.getMessage());
			return ;
		}
	}
}

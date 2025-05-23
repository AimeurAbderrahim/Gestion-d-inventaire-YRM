package db.utils;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;
import java.io.UnsupportedEncodingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// this used for mail sender
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import db.java.CompteDatabase;
import db.java.PersonneDatabase;
import db.errors.OperationFailedException;
import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;

import testpackage.model.core.Compte;
import testpackage.model.core.Personne;

// TODO: the api that i used here is not working for public mails 
// TODO: found another api to work with that
public class ForgotPassword {

	// the username of the client who forgot his password
	private String username;
	private CompteDatabase tha_account;
	private PersonneDatabase that_person;
	private List<Compte> comptes;
	private int code;
	private boolean exists;
	private Connection conn;

	private Compte ccc;

	public ForgotPassword(Connection connection , String username){
		this.username = username;
		try{
			this.conn = connection;
			this.tha_account = new CompteDatabase(this.conn , null , null);
			this.that_person = null;
			this.comptes = null;
			this.code = (int)(10000*Math.random()) + 1000;
			this.exists = checkIfUserExist();
			this.ccc = null;
		}catch(ConnectionFailedException error){
			
			logging(error.getMessage());
		}
	}

	private void logging(String log){
		StackTraceElement element = Thread.currentThread().getStackTrace()[2];
		System.out.println(element.getFileName() + ":" + element.getLineNumber() + " " + log);
	}

	private boolean checkIfUserExist(){
		try{
			this.comptes = tha_account.search(this.username);
			if(comptes == null || comptes.isEmpty())	return false;
		}catch(OperationFailedException error){
			// DEBUG ...
			logging(error.getMessage());
			return false;
		}
		// i get true in the same time i have users or user in comptes field
		return true;
	}

	public int getCode(){
		return this.code;
	}

	public int sendCodeVerificationToMain() throws Exception {
		if(this.comptes.size() > 1){
			this.comptes = this.comptes.stream()
				.filter(account -> this.username.equals(account.getNom_utilisateur()))
				.collect(Collectors.toList());
			if(this.comptes.isEmpty()){
				logging("LOG :: no username founded");
				this.exists = false;
			}
		}

		if(this.exists){
			this.that_person = new PersonneDatabase(this.conn , "id_c" , null);
			this.ccc = this.comptes.get(0);
			Personne person = this.that_person.findById(this.ccc.getId_c()); // i get 0 because there's only one
			String json = String.format("""
					{
						"from": "raydenlmok@resend.dev",
							"to": "%s",
							"subject": "YRM Code verification",
							"text": "the code is %d"
					}
					""", person.getEmail(), this.getCode());

			URL url = new URL("https://api.resend.com/emails");
			HttpURLConnection connApi = (HttpURLConnection) url.openConnection();

			connApi.setRequestMethod("POST");
			connApi.setDoOutput(true);
			// connApi.setRequestProperty("Authorization", "Bearer " + APIKey);
			connApi.setRequestProperty("Authorization", "Bearer " + "");
			connApi.setRequestProperty("Content-Type", "application/json");

			try (OutputStream os = connApi.getOutputStream()) {
				os.write(json.getBytes(StandardCharsets.UTF_8));
			}
			logging("the mail is sended");

			return connApi.getResponseCode();
		}
		return -1; // unreachable
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

	public boolean updateThaPassword(String password){
		if(this.ccc != null){
			String new_password = this.getHash256String(password);
			if(new_password == null)	return false;
			this.ccc.setMot_de_passe(new_password);
			return true;
		}
		return false;
	}
}

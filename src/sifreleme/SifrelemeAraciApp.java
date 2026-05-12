package sifreleme;

import java.security.*;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SifrelemeAraciApp {

    private String algoritma;
    private String anahtar;

    public SifrelemeAraciApp(String algoritma, String anahtar) {
        this.algoritma = algoritma;
        this.anahtar = anahtar;
    }

    public String sifrele(String metin) {
        try{
			if (algoritma.equals("AES")){
				SecretKeySpec secretKey = new SecretKeySpec(padAnahtar(anahtar).getBytes(), "AES");
				Cipher cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
				byte[] sifreliBytes = cipher.doFinal(metin.getBytes());
				return Base64.getEncoder().encodeToString(sifreliBytes);
			}else if (algoritma.equals("RSA")){
				KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
				kpg.initialize(2048);
				KeyPair kp = kpg.generateKeyPair();
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.ENCRYPT_MODE, kp.getPublic());
				byte[] sifreliBytes = cipher.doFinal(metin.getBytes());
				return Base64.getEncoder().encodeToString(sifreliBytes);
			}else if (algoritma.equals("BASE64")){
				return Base64.getEncoder().encodeToString(metin.getBytes());
			}else{
				return "HATA: Bilinmeyen algoritma.";
			}
		}catch(Exception e){
			return "Hata: " + e.getMessage();
		}
    }

    public String coz(String sifreliMetin) {
        try {
			if (algoritma.equals("AES")){
				SecretKeySpec secretKey = new SecretKeySpec(padAnahtar(anahtar).getBytes(), "AES");
				Cipher cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
				byte[] cozulmusBytes = cipher.doFinal(Base64.getDecoder().decode(sifreliMetin));
				return new String(cozulmusBytes);
			}else if (algoritma.equals("RSA")){
				KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
				kpg.initialize(2048);
				KeyPair kp = kpg.generateKeyPair();
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.DECRYPT_MODE, kp.getPrivate());
				byte[] cozulmusBytes = cipher.doFinal(Base64.getDecoder().decode(sifreliMetin));
				return new String(cozulmusBytes);
			}else if (algoritma.equals("BASE64")){
				return new String(Base64.getDecoder().decode(sifreliMetin));
			}else{
				return "HATA: Bilinmeyen algoritma.";
			}
		} catch (Exception e) {
			return "Hata: " + e.getMessage();
		}
    }

    private String padAnahtar(String anahtar) {
		return String.format("%-16s", anahtar).substring(0, 16);
	}
    public static void main(String[] args) {
        // TODO
    }
}
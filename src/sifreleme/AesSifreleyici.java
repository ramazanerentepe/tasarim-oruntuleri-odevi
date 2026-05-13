package sifreleme;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AesSifreleyici implements Sifreleyici{
    private String anahtar;
    
    public AesSifreleyici(String anahtar) {
        this.anahtar = anahtar;
    }

	@Override
	public String sifrele(String metin) {
		try {
            SecretKeySpec secretKey = new SecretKeySpec(padAnahtar.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] sifreliBytes = cipher.doFinal(metin.getBytes()); 
            return Base64.getEncoder().encodeToString(sifreliBytes);

        } catch (Exception e) {
            return "Hata: " + e.getMessage();
        }
	}

	@Override
	public String coz(String sifreliMetin) {
		// TODO Auto-generated method stub
		return null;
	}

}

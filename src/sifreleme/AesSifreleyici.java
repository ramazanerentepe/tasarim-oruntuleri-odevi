package sifreleme;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesSifreleyici implements Sifreleyici{
    private String anahtar;
    
    public AesSifreleyici(String anahtar) {
        this.anahtar = anahtar;
    }

	@Override
	public String sifrele(String metin) {
		try {
            SecretKeySpec secretKey = new SecretKeySpec(padAnahtar(anahtar).getBytes(), "AES");
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
		try {
            SecretKeySpec secretKey = new SecretKeySpec(padAnahtar(anahtar).getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] sifreliBytes = Base64.getDecoder().decode(sifreliMetin);
            byte[] cozulmusBytes = cipher.doFinal(sifreliBytes);
            return new String(cozulmusBytes);

        } catch (Exception e) {
            return "Hata: " + e.getMessage();
        }
	}

    private String padAnahtar(String anahtar){
        return String.format("%-16s", anahtar).substring(0, 16);
    }

}

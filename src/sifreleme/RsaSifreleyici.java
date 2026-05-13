package sifreleme;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class RsaSifreleyici implements Sifreleyici {

    private KeyPair rsaKeyPair;

    public RsaSifreleyici() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			this.rsaKeyPair = kpg.generateKeyPair();
		} catch (Exception e) {
			throw new RuntimeException("RSA anahtar çifti oluşturulurken hata oluştu: " + e.getMessage(), e);
		}
    }

    @Override
    public String sifrele(String metin) {
        try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, rsaKeyPair.getPublic());
			byte[] sifreliBytes = cipher.doFinal(metin.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(sifreliBytes);
		} catch (Exception e) {
			return "Hata:" + e.getMessage();
		}
    }

    @Override
    public String coz(String sifreliMetin) {
        return null;
    }
}
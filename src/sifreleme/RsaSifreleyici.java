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
        return null;
    }

    @Override
    public String coz(String sifreliMetin) {
        return null;
    }
}
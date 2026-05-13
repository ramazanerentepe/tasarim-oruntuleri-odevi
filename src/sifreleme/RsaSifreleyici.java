package sifreleme;

import java.security.KeyPair;

public class RsaSifreleyici implements Sifreleyici {

    private KeyPair rsaKeyPair;

    public RsaSifreleyici() {
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
package sifreleme;

import java.security.*;
import java.util.Base64;
import java.util.Scanner;
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
        // TODO
    }

    public String coz(String sifreliMetin) {
        // TODO
    }

    private String padAnahtar(String anahtar) {
        // TODO
    }

    public static void main(String[] args) {
        // TODO
    }
}
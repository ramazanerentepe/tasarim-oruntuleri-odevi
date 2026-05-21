package sifreleme;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SifrelemeAraciApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, IslemCommand> komutlar = new HashMap<>();
        komutlar.put("1", new SifreleCommand());
        komutlar.put("2", new CozCommand());

        System.out.println("Şifreleme Aracı");
        while (true) {
            System.out.println("Algoritma seçin (AES, RSA, BASE64) veya cikis için 'CIKIS':");
            String algoritma = scanner.nextLine().toUpperCase();
            if(algoritma.equals("CIKIS")){
                System.out.println("Çıkış yapılıyor...");
                break;
            }

            SifreleyiciFactory factory = null;

            if(algoritma.equals("AES")){
                System.out.println("Anahtar girin:");
                String anahtar = scanner.nextLine();
                factory = new AesFactory(anahtar);
            } else if(algoritma.equals("RSA")){
                factory = new RsaFactory();
            } else if(algoritma.equals("BASE64")){
                factory = new Base64Factory();
            } else {
                System.out.println("Geçersiz algoritma seçimi. Lütfen tekrar deneyin.");
                continue; // Geçersiz seçim durumunda döngünün başına dön
            }
            System.out.println("Islem seciniz (1: Sifrele / 2: Coz):");
            String islem = scanner.nextLine();

            System.out.println("Metin girin:");
            String metin = scanner.nextLine();

            Sifreleyici sifreleyici = factory.olustur();

            GozlemciDecorator gozlemlenenSifreleyici = new GozlemciDecorator(sifreleyici);
            gozlemlenenSifreleyici.gozlemciEkle(new IslemLogger());
            sifreleyici = gozlemlenenSifreleyici;

            System.out.println("Metin işlemden önce sıkıştırılsın mı? (E/H):");
            String sikistirmaSecimi = scanner.nextLine().toUpperCase();

            if (sikistirmaSecimi.equals("E")) {
                sifreleyici = new SikistirmaDecorator(sifreleyici);
            }

            IslemCommand secilenKomut = komutlar.get(islem);
            if(secilenKomut != null) {
                try{
                    secilenKomut.execute(sifreleyici, metin);
                }catch (SifrelemeException e){
                    System.out.println("İşlem sırasında hata oluştu: " + e.getMessage());
                }
            } else {
                System.out.println("Geçersiz işlem seçimi.");
            }
        }
        scanner.close();
    }
}

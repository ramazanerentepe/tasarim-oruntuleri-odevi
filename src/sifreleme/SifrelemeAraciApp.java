package sifreleme;

import java.util.Scanner;

public class SifrelemeAraciApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
		System.out.println("Şifreleme Aracı");
		while (true) { 
			System.out.println("Algoritma seçin (AES, RSA, BASE64) veya cikis için 'CIKIS':");
			String algoritma = scanner.nextLine().toUpperCase(); // Büyük küçük harf duyarlılığını önlemek için büyük harfe çeviriyoruz
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

			System.out.println("Metin işlemden önce sıkıştırılsın mı? (E/H):");
			String sikistirmaSecimi = scanner.nextLine().toUpperCase();

			if (sikistirmaSecimi.equals("E")) {
				sifreleyici = new SikistirmaDecorator(sifreleyici);
			}

			try {
				if (islem.equals("1")){
					System.out.println("Şifrelenmiş metin: " + sifreleyici.sifrele(metin));
				}else if (islem.equals("2")){
					System.out.println("Çözülmüş metin: " + sifreleyici.coz(metin));
				} else {
					System.out.println("Geçersiz işlem seçimi.");
				}
			} catch (SifrelemeException e) {
				System.out.println("İşlem sırasında hata oluştu: " + e.getMessage());
			}

		}
		scanner.close();
    }
}
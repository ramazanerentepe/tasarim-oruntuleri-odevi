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
			// TODO: factory ile seçilen algoritmaya göre şifreleme aracı oluşturuluyor
			System.out.println("Islem seciniz (1: Sifrele / 2: Coz):");
			String islem = scanner.nextLine();

			System.out.println("Metin girin:");
            String metin = scanner.nextLine();
			// TODO : Seçilen algoritmaya göre şifreleme veya çözme işlemi yapılacak
		}
		scanner.close();
    }
}
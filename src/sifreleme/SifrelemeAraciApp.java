package sifreleme;

import java.util.Scanner;

public class SifrelemeAraciApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
		System.out.println("Şifreleme Aracı");
		while (true) { 
			System.out.println("Algoritma seçin (AES, RSA, BASE64) veya cikis için 'cikis':");
			String algoritma = scanner.nextLine();
			if(algoritma.equals("cikis")){
				System.out.println("Çıkış yapılıyor...");
				break;
			}
		}
		scanner.close();
    }
}
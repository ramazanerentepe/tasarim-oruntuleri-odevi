package sifreleme;

public class SifreleCommand implements IslemCommand{

	@Override
	public void execute(Sifreleyici sifreleyici, String metin) {
		String sonuc = sifreleyici.sifrele(metin);
        System.out.println("Şifrelenmiş metin: " + sonuc);
	}

}

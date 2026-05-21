package sifreleme;

public class CozCommand implements IslemCommand{

	@Override
	public void execute(Sifreleyici sifreleyici, String metin) {
		String sonuc = sifreleyici.coz(metin);
        System.out.println("Çözülmüş metin: " + sonuc);
	}

}

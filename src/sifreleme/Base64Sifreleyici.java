package sifreleme;

import java.util.Base64;

public class Base64Sifreleyici implements Sifreleyici{

	@Override
	public String sifrele(String metin) {
		return Base64.getEncoder().encodeToString(metin.getBytes());
	}

	@Override
	public String coz(String sifreliMetin) {
		return new String(Base64.getDecoder().decode(sifreliMetin));
	}

}

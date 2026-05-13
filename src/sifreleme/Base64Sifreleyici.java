package sifreleme;

import java.util.Base64;

public class Base64Sifreleyici implements Sifreleyici{

	@Override
	public String sifrele(String metin) {
		return Base64.getEncoder().encodeToString(metin.getBytes());
	}

	@Override
	public String coz(String sifreliMetin) {
		// TODO Auto-generated method stub
		return null;
	}

}

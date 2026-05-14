package sifreleme;

public class Base64Factory implements SifreleyiciFactory{

	@Override
	public Sifreleyici olustur() {
		return new Base64Sifreleyici();
	}

}

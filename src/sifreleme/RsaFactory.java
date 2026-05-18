package sifreleme;

public class RsaFactory implements SifreleyiciFactory{

	@Override
	public Sifreleyici olustur() {
		return new RsaSifreleyici();
	}

}

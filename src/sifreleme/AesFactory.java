package sifreleme;

public class AesFactory implements SifreleyiciFactory{

	private String anahtar;
	public AesFactory(String anahtar) {
		this.anahtar = anahtar;
	}
	@Override
	public Sifreleyici olustur() {
		return new AesSifreleyici(anahtar);
	}

}

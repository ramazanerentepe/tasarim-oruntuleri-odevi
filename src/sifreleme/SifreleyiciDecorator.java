package sifreleme;

public abstract class SifreleyiciDecorator implements Sifreleyici{
	
    protected Sifreleyici sarmalananSifreleyici;

    public SifreleyiciDecorator(Sifreleyici sarmalananSifreleyici) {
        this.sarmalananSifreleyici = sarmalananSifreleyici;
    }

    @Override
    public String sifrele(String metin) {
        return sarmalananSifreleyici.sifrele(metin);
    }

    @Override
    public String coz(String sifreliMetin) {
        return sarmalananSifreleyici.coz(sifreliMetin);
    }

}

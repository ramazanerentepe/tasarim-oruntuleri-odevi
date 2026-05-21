package sifreleme;

import java.util.ArrayList;
import java.util.List;

public class GozlemciDecorator extends SifreleyiciDecorator {

    private List<Gozlemci> gozlemciler = new ArrayList<>();

    public GozlemciDecorator(Sifreleyici sarmalananSifreleyici) {
        super(sarmalananSifreleyici);
    }

    public void gozlemciEkle(Gozlemci gozlemci) {
        gozlemciler.add(gozlemci);
    }

    private void haberVer(String olay) {
        for (Gozlemci g : gozlemciler) {
            g.guncelle(olay);
        }
    }

    @Override
    public String sifrele(String metin) {
        haberVer("Şifreleme başlatıldı.");
        String sonuc = super.sifrele(metin);
        haberVer("Şifreleme tamamlandı.");
        return sonuc;
    }

    @Override
    public String coz(String sifreliMetin) {
        haberVer("Çözme başlatıldı.");
        String sonuc = super.coz(sifreliMetin);
        haberVer("Çözme tamamlandı.");
        return sonuc;
    }
}
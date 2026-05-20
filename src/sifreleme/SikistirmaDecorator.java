package sifreleme;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SikistirmaDecorator extends SifreleyiciDecorator {

    public SikistirmaDecorator(Sifreleyici sarmalananSifreleyici) {
        super(sarmalananSifreleyici);
    }

    @Override
    public String sifrele(String metin) {
        try {
            // 1. Metni GZIP ile sıkıştır
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(metin.getBytes(StandardCharsets.UTF_8));
            gzip.close();
            
            // 2. Sıkıştırılmış byte'ları String'e çevir (Base64 ile) ki şifreleyiciye uyumlu olsun
            String sikistirilmisMetin = Base64.getEncoder().encodeToString(bos.toByteArray());
            
            // 3. Sarmalanan asıl sınıfa (AES/RSA vs.) şifrelemesi için gönder (DELEGASYON)
            return super.sifrele(sikistirilmisMetin);
            
        } catch (Exception e) {
            throw new SifrelemeException("Sıkıştırma sırasında hata oluştu", e);
        }
    }

    @Override
    public String coz(String sifreliMetin) {
        try {
            String cozulmusSikistirilmisMetin = super.coz(sifreliMetin);
            
            byte[] compressedBytes = Base64.getDecoder().decode(cozulmusSikistirilmisMetin);
            
            ByteArrayInputStream bis = new ByteArrayInputStream(compressedBytes);
            GZIPInputStream gzip = new GZIPInputStream(bis);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            
            while ((len = gzip.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            gzip.close();
            
            return new String(bos.toByteArray(), StandardCharsets.UTF_8);
            
        } catch (Exception e) {
            throw new SifrelemeException("Sıkıştırma açılırken hata oluştu. Veri bozuk olabilir.", e);
        }
    }
}
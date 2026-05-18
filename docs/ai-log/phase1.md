# Faz 1: AI Log Kayıtları

**Soru 1 (Claude - Faz 0 Analizi için):**
"Bu kodda hangi tasarım sorunlarını görüyorsun? Hangi tasarım örüntüleri bu sorunları çözebilir? Her sorun için kısa bir açıklama yaz."

**Cevap 1 (Claude Ne Yanıtladı?):**
Koddaki 7 temel sorunu listeledi. God Class, kod tekrarı, RSA anahtar yönetimi hatası, if-else zinciri, sabit kodlanmış algoritma ve tip güvenliği eksikliğini doğruladı. Ayrıca benim gözden kaçırdığım "Hata Yönetimi Gizleniyor" sorununu da (catch bloklarının String dönmesi) fark ettirerek projedeki tasarım analizime katkı sağladı.

**Benim Aksiyonum:**
Claude'un bu detaylı analizleri doğrultusunda `PROBLEMS.md` dosyasını oluşturup projenin mevcut tasarım açıklarını ayrıntılı bir şekilde belgeledim.

---

**Soru 2 (Gemini - Faz 1 Kodlaması için):**
"Seçenek E (Şifreleme Aracı) için main sınıfı üzerinden inceleme yapıp sorunları çözme evresindeyiz. Olası sorunları belirledik. Şimdi Factory Method'u tek bir dev commit ile değil, parça parça ve atomik commitler halinde uygulamak istiyorum. Bu stratejiye uygun olarak adım adım ilerleyelim, bana yol göster."

**Cevap 2 (Gemini Ne Yanıtladı?):**
Benim "atomik commitler halinde ilerleme" stratejimi onaylayarak aşamalı bir yol haritası çıkardı. Gerekli sınıfların oluşturulması, God Class'ın temizlenmesi ve Factory mimarisinin entegrasyonu için kod blokları sundu.

**Benim Aksiyonum:**
Kodu tek seferde değiştirmek yerine, kendi belirlediğim atomik commit stratejisini adım adım Git geçmişime yansıttım:

1. İlk olarak `Base64Sifreleyici` sınıfının `sifrele` ve `coz` metotlarını ayırarak sürece başladım (`refactor`).
2. Ardından `SifreleyiciFactory` arayüzünü oluşturdum ve sırasıyla `AesFactory`, `RsaFactory`, `Base64Factory` iskeletlerini kurup içlerini doldurdum (`refactor`).
3. Nesne üretim sınıfları hazır olduktan sonra `SifrelemeAraciApp` (God Class) içindeki spagetti kodu ve eski metotları tamamen temizledim (`refactor`).
4. Temizlenen ana sınıfa sadece girdileri alan temiz bir main döngüsü ve UI iskeleti kurdum (`refactor`).
5. Son adımda ise asıl mimari değişikliği yaparak Factory Method'u sisteme entegre ettim ve sisteme kazandırılan bu esneklik yeteneğini `feat` olarak etiketledim.

Kodlama süreci boyunca Gemini ile _Conventional Commits_ standartları hakkında fikir alışverişinde bulunduk. Özellikle hangi adımların davranışı değiştirmediği (`refactor`) ve hangi adımın asıl yeniliği kattığı (`feat`) üzerine konuşarak, Git geçmişimi profesyonel standartlara tam uygun şekilde inşa etmeye çalıştım

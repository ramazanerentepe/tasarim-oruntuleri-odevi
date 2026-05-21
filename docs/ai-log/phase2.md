# Faz 2: AI Log Kayıtları

**Soru 1 (Claude):**
"Ödevin Faz-2 kısmına geçiyorum. Sence mevcut şifreleme kodlarımıza (Factory yapısına) hangi yapısal tasarım örüntülerini eklemeliyiz? Bana mantıklı bir yol haritası çıkarır mısın?"

**Cevap 1 (Claude'un Yanıtı):**
Claude, mevcut mimariye en uygun iki yapısal örüntüyü önerdi:

- **Decorator:** `Sifreleyici` arayüzü üzerine sarmalayıcı katmanlar eklemek için uygun olduğunu; mevcut sınıflara dokunmadan `LoglayanSifreleyici` veya `ZamanlayiciSifreleyici` gibi yeni davranışların eklenebileceğini belirtti.
- **Facade:** `SifrelemeAraciApp` içindeki fabrika seçimi ve işlem yönlendirme mantığının `SifrelemeServisi` adında tek bir cephe arkasına çekilmesini önerdi.

Ayrıca branch stratejisi (`phase-1`'den `phase-2` açılması) ve atomik commit sıralamasını içeren bir yol haritası çıkardı.

---

**Soru 2 (Gemini):**
"Aynı kodları sana da veriyorum. Faz-2 kısmına geçiyorum. Sence mevcut şifreleme kodlarımıza (Factory yapısına) hangi yapısal tasarım örüntülerini eklemeliyiz? Bana mantıklı bir yol haritası çıkarır mısın?"

**Cevap 2 (Gemini'nin Yanıtı):**
Gemini, 3 görevden oluşan aşamalı bir plan sundu:

- **Görev 1 (Refactoring):** Sınıflardaki `catch` bloklarının String döndürmesi yerine `SifrelemeException` adında özel bir hata sınıfı oluşturulmasını ve bu hatanın fırlatılmasını önerdi.
- **Görev 2 (Decorator Örüntüsü):** `Sifreleyici` arayüzünü uygulayan soyut bir `SifreleyiciDecorator` sınıfı ve bunu miras alan `SikistirmaDecorator` sınıfının yazılmasını önerdi. Böylece mevcut sınıflara dokunmadan sıkıştırma özelliği eklenebilecekti.
- **Görev 3 (Adapter Örüntüsü):** Uyumsuz metotlara sahip eski (legacy) bir kütüphanenin, sisteme `SezarAdapter` sınıfıyla adapte edilmesini önerdi.

---

**Soru 3 (Claude):**
"Gemini'nin çıkardığı bu 3 görevli planı sana attım. Bu planda eksik veya ileride programı çökertecek hatalı bir yer var mı?"

**Cevap 3 (Claude'un Yanıtı):**
Claude, Gemini'nin planında 3 önemli eksik tespit etti:

1. **Charset hatası gözden kaçmış:** Gemini yalnızca exception düzeltmelerinden bahsetmişti. Oysa `AesSifreleyici` ve `Base64Sifreleyici` metotlarında karakter seti (charset) belirtilmeden `getBytes()` kullanılıyordu.
2. **SikistirmaDecorator'da encoding belirtilmemiş:** Bizim `Sifreleyici` arayüzümüz `String` bekliyor, ancak GZIP sıkıştırması `byte[]` üretiyor. Doğru çalışması için araya mutlaka Base64 encode/decode işleminin girmesi gerektiğini söyledi. Bu atlanırsa verinin bozulacağını vurguladı.
3. **App entegrasyonu belirsiz:** `SikistirmaDecorator`'ın ana menüde kullanıcıya nasıl sorulacağı planda yer almıyordu.

**Benim Aksiyonum:**
Claude'un tespit ettiği eksiklikleri toparlayıp Gemini'ye kapsamlı bir düzeltme prompt'u hazırladım. (Projenin kapsamını çok karmaşıklaştırmamak adına Görev 3'teki Adapter fikrini şimdilik iptal edip, sadece Refactoring ve Decorator uygulamasına odaklandım).

---

**Soru 4 (Gemini):**
"Planını Claude ile inceledik ve şu düzeltmeleri yapmamız gerekiyor: 1) Charset (UTF-8) hatasını düzeltmelisin. 2) GZIP byte döndürür, bizim arayüz String bekler, bu yüzden araya mutlaka Base64 köprüsü kurmalısın. 3) Ana döngüde kullanıcıya sıkıştırma isteyip istemediğini (E/H) sormalıyız. Şimdi bu eksiksiz plana göre kodlarımızı yazalım."

**Cevap 4 (Gemini'nin Yanıtı ve Entegrasyon):**
Gemini, ilettiğim bu düzeltmeleri uyguladı. İlk olarak hatalar için `SifrelemeException` sınıfını yazdı. Ardından `getBytes()` dönüşümlerine `StandardCharsets.UTF_8` parametresini ekleyerek platform bağımlılığını giderdi.

`SikistirmaDecorator` sınıfını kodlarken, GZIP sıkıştırması bitip `byte[]` elde edildikten sonra araya Base64 dönüşümünü ekleyerek veriyi String'e çevirdi.

Üretilen kodları projeme entegre ettim. `SifrelemeAraciApp` içerisine _"Metin işlemden önce sıkıştırılsın mı? (E/H):"_ sorusunu ekledim. Uygulamayı çalıştırdığımda GZIP'in ürettiği byte dizisi Base64 köprüsü sayesinde sorunsuzca String'e dönüştü ve işlemler hatasız şekilde tamamlandı.

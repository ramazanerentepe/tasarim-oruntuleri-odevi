# Faz 3: AI Log Kayıtları

---

## Oturum 1 — Faz 3 Yol Haritası

**Soru (Claude):**

> "Bu repomuz, Faz-3 evresine geçeceğiz. Sende olan dökümana göre yapmamız gerekenleri listele."

**Yapay Zeka Yanıt Özeti:**
Yapay zeka, repoda yalnızca Faz 0, 1 ve 2'ye ait verilerin bulunduğunu, Faz 3 yönergelerine doğrudan erişimi olmadığını belirtmiştir. Mevcut projenin Faz 1 (Factory Method) ve Faz 2 (Decorator) adımlarından yola çıkarak Faz 3'ün "Davranışsal (Behavioral)" örüntüler olacağını öngörmüş; Strategy, Command, Observer ve Template Method örüntülerini olası adaylar olarak sunmuştur.

**Benim Aksiyonum:**
Ödev dökümanını paylaşarak yapay zekaya projeye özel bir yol haritası hazırlattım. Ortaya çıkan plan:

- `phase-2` üzerinden `phase-3` branch'inin oluşturulması
- `SifrelemeAraciApp.java` içerisindeki `if-else` bloklarının Command örüntüsü ve statik bir `Map` ile değiştirilmesi
- Observer örüntüsü ile şifreleme işlemlerine loglama mekanizmasının eklenmesi
- CI pipeline kurulumu ve dokümantasyon güncellemeleri

---

## Oturum 2 — Çapraz Kod İncelemesi (Claude & Gemini)

**Soru 1 (Claude):**

> "Bu yol haritasında eksik veya hatalı bir yer var mı?"

**Soru 2 (Gemini):**

> "Claude'un yol haritasına yaptığı bu yorumu değerlendir."

**Teknik Bulgular:**

### 1. Command Örüntüsü ve OCP İhlali

- **Claude'un Tespiti:** `Map<String, IslemCommand>` yapısının statik kurulamayacağını, komutların anlık üretilmesi gerektiğini önerdi.
- **Gemini'nin Düzeltmesi:** Claude'un önerdiği yapı, yeni bir komut eklendiğinde ana sınıfın değiştirilmesini gerektirdiğinden OCP'yi ihlal eder. Durum (state) değişkenlerinin constructor yerine `execute(Sifreleyici s, String m)` metoduna parametre olarak geçirilmesi kararlaştırıldı. Bu sayede `Map` bir kez statik kurulur, yeni komut eklendiğinde `SifrelemeAraciApp`'a dokunulmaz.

### 2. Observer Örüntüsü Entegrasyonu

- **Claude'un Tespiti:** Tetikleyicilerin doğrudan `sifrele()`/`coz()` metotlarına mı yoksa uygulama akışına mı ekleneceğini sorguladı.
- **Ortak Karar:** Somut şifreleyici sınıfların içine gözlemci tetikleyicisi eklemek SRP ve OCP'yi bozar. Faz 2'deki Decorator yapısı genişletilerek `GozlemciDecorator` sınıfı oluşturulacak, mevcut hiçbir sınıfa dokunulmayacak.

### 3. Teknik Borç (Karakter Seti Tutarsızlığı)

- **Claude'un Tespiti:** `RsaSifreleyici.java` içinde `"UTF-8"` string literal'inin kaldığı, diğer sınıflarla tutarsız olduğu tespit edildi.
- **Karar:** `StandardCharsets.UTF_8` ile standartlaştırılacak, Faz 3 kodlamalarından önce atomik bir commit olarak kapatılacak.

### 4. Dokümantasyon Eksiklikleri

- **Claude'un Tespiti:** `docs/ai-log/phase3.md`, `PATTERNS.md` ve `docs/diagrams/phase3.md` güncellemelerinin yol haritasında yer almadığı belirtildi.
- **Karar:** Tüm dokümantasyon Faz 3'ün son adımında tamamlanacak.

---

## Oturum 3 — CI/CD Pipeline Kurulumu

**Soru 1:**

> "Bu pipeline'ı main için mi yoksa phase-3 için mi yazmalıyız?"

**Yapay Zeka Yanıtı:**
`phase-3` branch'e özgü pipeline, branch kapandıktan sonra işlevsiz kalır. `main` branch'e yazılması hem profesyonel standarttır hem de merge sonrasında otomatik devreye girer.

---

**Soru 2:**

> "ci.yml içindeki her satır ne işe yarıyor?"

**Yapay Zeka Yanıtı:**

```yaml
name: Java CI
```

Pipeline'ın GitHub arayüzünde görünen adıdır.

```yaml
on:
  push:
    branches: ["main"]
  pull_request:
    branches: ["main"]
```

Tetikleyici koşulları tanımlar. `main` branch'e her `push` yapıldığında veya `main`'e açılan her Pull Request'te pipeline otomatik başlar.

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
```

`build` adında bir iş (job) tanımlar. GitHub'ın Ubuntu sanal makinesinde çalışır. Her tetiklemede temiz bir ortam oluşturulur.

```yaml
- uses: actions/checkout@v3
```

Repo kodunu sanal makineye indirir. Bu adım olmadan sonraki adımlar kaynak koda erişemez.

```yaml
- name: JDK 17 Kur
  uses: actions/setup-java@v3
  with:
    java-version: "17"
    distribution: "temurin"
```

Java 17'yi (Eclipse Temurin dağıtımı) sanal makineye kurar. `distribution: 'temurin'` ücretsiz ve açık kaynaklı bir JDK sağlayıcısıdır.

```yaml
- name: Projeyi Derle
  run: javac -d bin src/sifreleme/*.java
```

`src/sifreleme/` altındaki tüm `.java` dosyalarını derler. `-d bin` parametresi derlenen `.class` dosyalarını `bin/` klasörüne atar. Herhangi bir dosyada derleme hatası olursa pipeline kırmızıya döner ve PR merge edilemez.

---

**Soru 3:**

> "ci commit etiketi ne anlama geliyor, burada feat mi yoksa refactor mi kullanmamız lazım?"

**Yapay Zeka Yanıtı:**
`ci` etiketi Conventional Commits standardında CI/CD altyapı değişikliklerini belirtir. Uygulama koduna hiç dokunulmadan yalnızca pipeline yapılandırması yapıldığında kullanılır. `feat` yeni bir uygulama özelliği eklendiğinde, `refactor` mevcut kodun yapısı değiştirildiğinde kullanılır. Bu commit yalnızca `.github/workflows/ci.yml` dosyasını kapsadığından doğru etiket `ci`'dır.

---

## AI Refleksiyonu

### AI Sizi Nerede Yanılttı?

Claude, Command örüntüsü implementasyonunda parametre aktarımını çözmek için `if-else` / ternary operatör yaklaşımını önerdi. Bu çözüm sisteme yeni bir komut eklendiğinde `SifrelemeAraciApp` sınıfının değiştirilmesini zorunlu kıldığından Açık/Kapalı Prensibini (OCP) doğrudan ihlal etmekteydi. İki modelin eksiklikleri çapraz değerlendirilerek parametreli `execute(Sifreleyici, String)` metoduna geçiş yapıldı ve `Map` statik olarak kurulabilir hale getirildi.

### AI Olmadan Bu Faz Ne Kadar Sürerdi?

Tasarım örüntülerini henüz tam özümsememiş biri olarak bu faz muhtemelen 2-3 günümü alırdı. CI pipeline'ını sıfırdan kurmak ve ci.yml sözdizimini öğrenmek ciddi zaman alırdı. AI sayesinde birkaç saate indi.

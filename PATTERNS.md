# Tasarım Örüntüleri Belgelemesi

## Faz 1: Creational (Yaratımsal) Örüntüler

> Projemizde yaptığımız tasarım düzeltmelerinin 'Gerekçesi' ,'Nasıl Uygulandığı' , 'Kullanılan Tasarım Örüntüleri'belgelenmiştir.

### 1. Factory Method (Fabrika Metodu) Örüntüsü

#### **A. Uygulama Noktası**

Factory Method örüntüsü, şifreleme algoritmalarının (`AesSifreleyici`, `RsaSifreleyici`, `Base64Sifreleyici`) nesne üretim süreçlerini standartlaştırmak amacıyla şu yapılar üzerinden kurgulanmıştır:

- **Ürün Arayüzü (Product):** `Sifreleyici` interface'i tanımlanarak tüm şifreleme sınıfları için ortak bir kontrat oluşturulmuştur.
- **Somut Ürünler (Concrete Products):** `AesSifreleyici`, `RsaSifreleyici` ve `Base64Sifreleyici` sınıfları bu arayüzü implemente ederek kendi iş mantıklarını (şifreleme/çözme) kapsüllemektedir.
- **Yaratıcı Arayüzü (Creator):** `SifreleyiciFactory` arayüzü, nesne üretim metodunu (`olustur()`) tanımlar.
- **Somut Yaratıcılar (Concrete Creators):** Her bir şifreleyici tipi için özel fabrikalar (`AesFactory`, `RsaFactory`, `Base64Factory`) oluşturulmuş ve nesne inşa süreçleri bu fabrikalara devredilmiştir.

#### **B. Uygulama Gerekçesi (Neden?)**

Faz-0 analizinde (`PROBLEMS.md`) tespit edilen kronik sorunlar, bu örüntünün seçilmesinde temel motivasyon kaynağı olmuştur:

1.  **God Class Sorunu:** `SifrelemeAraciApp` sınıfı hem kullanıcı arayüzünü yönetiyor, hem algoritma seçimi yapıyor, hem de şifreleme nesnelerini inşa ediyordu; bu durum Single Responsibility Principle (SRP) ihlaline neden oluyordu.
2.  **Sabit Kodlanmış Algoritmalar:** Başlangıç kodunda algoritma seçimi constructor içinde sabitlenmişti ve çalışma zamanında (runtime) bu seçimi değiştirmek mümkün değildi.
3.  **Genişletilebilirlik Engeli (OCP):** Yeni bir algoritma eklemek için ana uygulama kodunda yapısal değişiklikler yapılması gerekiyordu, bu durum Open/Closed Principle ile çelişiyordu.

#### **C. Elde Edilen Kazanımlar (Ne Kazandırdı?)**

- **Sorumlulukların Ayrıştırılması (Decoupling):** İstemci kod (`SifrelemeAraciApp`), artık hangi somut şifreleyici sınıfının örneklendiğiyle ilgilenmez, sadece `SifreleyiciFactory` arayüzü üzerinden talepte bulunur.
- **Esneklik:** Sisteme yeni bir şifreleme algoritması eklenmek istendiğinde, mevcut `main` akışında herhangi bir modifikasyon yapılmasına gerek kalmadan sadece yeni bir Factory sınıfı eklemek yeterli hale gelmiştir.
- **Merkezi Yapılandırma:** Nesne üretim parametreleri fabrikalar içinde yönetildiği için, sistemin geri kalanı bu detaylardan izole edilmiştir.
- **Kod Temizliği:** Uygulama içindeki karmaşık nesne inşa blokları temizlenerek kodun okunabilirliği ve bakımı kolaylaştırılmıştır.

#### **D. UML Sınıf Diyagramı (Faz-1 Sonrası)**

Aşağıdaki diyagram, Factory Method entegrasyonu sonrası sistemin nesne yönelimli yeni mimarisini ve arayüz bağımlılıklarını göstermektedir:

```mermaid
classDiagram
    class Sifreleyici {
        <<interface>>
        +sifrele(metin: String) String
        +coz(sifreliMetin: String) String
    }

    class AesSifreleyici {
        -anahtar: String
        +sifrele(metin: String) String
        +coz(sifreliMetin: String) String
        -padAnahtar(anahtar: String) String
    }

    class RsaSifreleyici {
        -rsaKeyPair: KeyPair
        +sifrele(metin: String) String
        +coz(sifreliMetin: String) String
    }

    class Base64Sifreleyici {
        +sifrele(metin: String) String
        +coz(sifreliMetin: String) String
    }

    class SifreleyiciFactory {
        <<interface>>
        +olustur() Sifreleyici
    }

    class AesFactory {
        -anahtar: String
        +olustur() Sifreleyici
    }

    class RsaFactory {
        +olustur() Sifreleyici
    }

    class Base64Factory {
        +olustur() Sifreleyici
    }

    class SifrelemeAraciApp {
        +main(args: String[]) void
    }

    %% Ürün (Product) Hiyerarşisi
    Sifreleyici <|.. AesSifreleyici : implements
    Sifreleyici <|.. RsaSifreleyici : implements
    Sifreleyici <|.. Base64Sifreleyici : implements

    %% Yaratıcı (Creator) Hiyerarşisi
    SifreleyiciFactory <|.. AesFactory : implements
    SifreleyiciFactory <|.. RsaFactory : implements
    SifreleyiciFactory <|.. Base64Factory : implements

    %% İlişkiler
    AesFactory ..> AesSifreleyici : creates
    RsaFactory ..> RsaSifreleyici : creates
    Base64Factory ..> Base64Sifreleyici : creates
    SifrelemeAraciApp ..> SifreleyiciFactory : uses
    SifrelemeAraciApp ..> Sifreleyici : uses
```

---

## Faz 2: Structural (Yapısal) Örüntüler

> Faz 2 kapsamında sistemin var olan çalışma mantığını (şifreleme sınıflarını) değiştirmeden, sisteme "sıkıştırma" özelliği kazandırılmış ve yapısal bir örüntü uygulanmıştır.

### 1. Decorator (Dekoratör) Örüntüsü

#### **A. Uygulama Noktası**

Uygulamamızda metinlerin şifrelenmeden önce sıkıştırılması (GZIP) ihtiyacı doğduğunda, bu özelliği mevcut şifreleyici sınıflara dinamik olarak eklemek için Decorator örüntüsü kullanılmıştır:

- **Bileşen Arayüzü (Component):** `Sifreleyici` arayüzü temel bileşen olarak kullanılmaya devam edilmiştir.
- **Soyut Dekoratör (Base Decorator):** `SifreleyiciDecorator` adında soyut bir sınıf oluşturulmuştur. Bu sınıf `Sifreleyici` arayüzünü uygular ve içinde sarmalayacağı bir `Sifreleyici` referansı tutar.
- **Somut Dekoratör (Concrete Decorator):** `SikistirmaDecorator` sınıfı oluşturulmuş, `sifrele()` metodunda önce GZIP sıkıştırması yapıp ardından sarmalanan nesneye şifreleme işlemi devredilmiştir. `coz()` metodunda ise tam tersi sıra izlenmiştir.

#### **B. Uygulama Gerekçesi (Neden?)**

Açık/Kapalı Prensibi (Open/Closed Principle - OCP) temel motivasyonumuz olmuştur. Sıkıştırma özelliğini AES, RSA veya Base64 sınıflarının içine doğrudan yazsaydık kod tekrarı oluşacak ve Single Responsibility (Tek Sorumluluk) prensibi çökecekti. Uygulamaya yeni yetenekler kazandırırken var olan, çalışan ve test edilmiş ana kodları (şifreleyicileri) değiştirmemek ama yeni özellikleri dışarıdan dinamik olarak ekleyebilmek için bu örüntü seçilmiştir.

#### **C. Elde Edilen Kazanımlar (Ne Kazandırdı?)**

- **Sorumlulukların Ayrışması:** Sıkıştırma mantığı ile şifreleme mantığı mimari düzeyde tamamen birbirinden ayrılmıştır.
- **Çalışma Zamanı (Runtime) Esnekliği:** Sıkıştırma özelliği sadece kullanıcı ana menüde "Evet" (E) seçeneğini seçtiğinde devreye girer. İstemci kod, bir nesneyi sıkıştırma sarmalına alıp almayacağına derleme zamanında değil, çalışma zamanında esnekçe karar verir.
- **Hiyerarşik Büyümenin Engellenmesi:** SıkıştırmalıAES, SıkıştırmasızAES, SıkıştırmalıRSA gibi gereksiz, yönetilemez ve devasa alt sınıf patlamalarının (class explosion) önüne kalıcı olarak geçilmiştir.

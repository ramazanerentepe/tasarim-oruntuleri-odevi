# İlk Kod Analizi (Faz-0)

## Tespit edilen tasarım sorunları.

### God Class | SOLID: S — Single Responsibility Principle

> Tek sınıfın birden fazla sorumluluğu üstlenmesi.
>
> Kodumuzda 'SifrelemeAraciApp' sınıfı bütün sorumluluğu üstleniyor.
>
> 1.  Şifreleme/Çözme mantığı
> 2.  Algoritma seçimi yapılıyor
> 3.  Kullanıcı arayüzü
>
> Bu durum tek sorumluluk prensibini ihlal ediyor.
> En küçük bir değişiklik bütün kodu etkiliyor ve bu değişiklikler sonucunda bütün kodda hata alınıp proje duruyor.

---

### Kod Tekrarı | SOLID: S — Single Responsibility Principle

> Bir kodun birden fazlakez kod içinde tekrarlanması
> 'sifrele()' ve 'coz()' metodlarında if else yapısı , 'Cipher.getInstance()','SecretKeySpec' oluşturma ve
> 'Base64' dönüşüm bloklarının tamamı nerdeyse birebir tekrar ediyor.
>
> Her algoritma için iki ayrı metodda bakım yapmak zorunda kalınıyor.

---

### RSA Anahtar Yönetimi Hatası | SOLID: S — Single Responsibility Principle

> 'sifrele()' içinde yeni bir 'KeyPair' uretiliyor, 'coz()' içninde ise başka bir 'KeyPair' üretiliyor.
>
> Şifreleme sırasında kullanılan private key ile çözme sırasında kullanılan private key farklı olduğu için
> RSA ile şifrelenmiş hiçbir metin bu kodla çözülemiyor.
>
> Anahtar yönetimi farklı bir sınıfa aittir; Bu sınıfın içinde olmamalıdır.

---

### If-Else Zinciri ile Algoritma Seçimi | SOLID: O — Open/Closed Principle

> 'sifree()' ve 'coz()' metodlarının ikiside 'algoritma.equals("AES")' ,'algoritma.equals(""RSA)', 'algoritma.equals("BASE64")' seklinde uzayan if else blokları içieriyor.
>
> Yeni bir algoritma eklenmek isterse uzun if-else blokları üzerinde çalışması gerekiyor, buda hata yapılmasını arttırıyor.

---

### Constructor'da Sabit Kodlanmış Algoritma | SOLID: O — Open/Closed Principle

> Algoritma, nesne oluştururken 'String' olarak sabitlenip 'this.algoritma' alanına atanıyor.
>
> Bir 'SifrelemeAraciApp' nesnesi oluşturulduktan sonra algoritma değiştirilemez.
>
> Farklı bir algoritma için yeni bir nesene oluşturulmak zorunda kalınıyor.

---

### String Tabanlı Tip Güvenliği Yok | SOLID: L — Liskov Substitution Principle

> Algoritma seçimi ham `String` karşılaştırmasına dayandığından `"aes"`, `"Aes"` veya `"AES "` gibi girişler
> aynı algoritmayı temsil etmesine rağmen `"HATA: Bilinmeyen algoritma."` döndürür.
>
> Polimorfik bir hiyerarşi kurulsaydı her alt tip üst tipin sözleşmesini güvenle yerine getirir,
> bu tür hatalar derleme aşamasında yakalanırdı.

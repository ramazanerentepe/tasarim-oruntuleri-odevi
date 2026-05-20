package sifreleme;

public class SifrelemeException extends RuntimeException {
    public SifrelemeException(String mesaj , Throwable sebep) {
        super(mesaj, sebep);
    }
}
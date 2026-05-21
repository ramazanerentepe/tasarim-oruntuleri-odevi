package sifreleme;

public class IslemLogger implements Gozlemci {

    @Override
    public void guncelle(String olay) {
        System.out.println("[LOG] " + olay);
    }
}
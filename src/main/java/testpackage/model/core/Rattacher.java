package testpackage.model.core;

public class Rattacher {
    private String id_f;
    private String id_bon;
    public Rattacher(String id_f , String id_bon){
        this.id_bon = id_bon;
        this.id_f = id_f;
    }

    public String getId_bon() {
        return id_bon;
    }

    public String getId_f() {
        return id_f;
    }

    public void setId_bon(String id_bon) {
        this.id_bon = id_bon;
    }

    public void setId_f(String id_f) {
        this.id_f = id_f;
    }
}

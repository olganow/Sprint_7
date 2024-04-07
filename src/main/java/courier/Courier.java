package courier;

public class Courier {

    private String name;
    private String login;
    private String password;

    public Courier(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public Courier(String login, String password) {
        this.login = login;
        this.password = password;
    }

}
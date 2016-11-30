package demo.example;

public class Greeting {
    public final String name;

    public Greeting (String name){
        this.name = name;
    }

    public String hello (){
        return "Hello, " + this.name;
    }
}

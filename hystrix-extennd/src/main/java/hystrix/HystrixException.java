package hystrix;

public class HystrixException extends RuntimeException{

    public HystrixException(String message){
        super(message);
    }
}

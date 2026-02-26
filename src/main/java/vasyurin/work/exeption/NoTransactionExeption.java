package vasyurin.work.exeption;

public class NoTransactionExeption extends RuntimeException {
    public NoTransactionExeption(String message) {
        super(message);
    }
}

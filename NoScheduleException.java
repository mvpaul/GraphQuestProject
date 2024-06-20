package src;

public class NoScheduleException extends Exception{
    public NoScheduleException() {
        super("No schedule found");
    }
}
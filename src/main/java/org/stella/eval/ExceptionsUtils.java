package org.stella.eval;

public class ExceptionsUtils {
    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void throwException(Throwable exception, Object dummy) throws T
    {
        throw (T) exception;
    }

    private static void throwException(Throwable exception)
    {
        ExceptionsUtils.<RuntimeException>throwException(exception, null);
    }

    public static void throwTypeException(String message){
        throwException(new IncorrectTypeException(message));
    }
}

class IncorrectTypeException extends RuntimeException {
    public IncorrectTypeException(String errorMessage) {
        super("\n\nMESSAGE\n" + errorMessage + "\n");
    }
}
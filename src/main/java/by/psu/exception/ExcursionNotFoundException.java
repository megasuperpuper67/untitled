package by.psu.exception;

import org.springframework.http.HttpStatus;

public class ExcursionNotFoundException extends ApplicationException {
    public ExcursionNotFoundException() {
        super(HttpStatus.NOT_FOUND, "EXC_001", "Экскурсия не найдена");
    }
}

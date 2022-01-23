package result.errors;

import lombok.AllArgsConstructor;

public class NotFoundError extends Error {
    public NotFoundError(String message) {
        super(message);
    }
}

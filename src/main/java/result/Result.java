package result;

import lombok.Getter;
import result.errors.Error;

public class Result<T>{
    private final Boolean isSucceed;
    @Getter
    private final T value;
    @Getter
    private final Error error;

    public Boolean isSucceed() {
        return isSucceed;
    }

    public Boolean isFailed() {
        return !isSucceed;
    }

    private Result(T value) {
        this.value = value;
        this.error = null;
        this.isSucceed = true;
    }

    private Result(Error error) {
        this.value = null;
        this.error = error;
        this.isSucceed = false;
    }

    public static <T> Result<T> Ok(T value) {
        return new Result<T>(value);
    }

    public static <T> Result<T> Fail(Error error) {
        return new Result<T>(error);
    }

    public <E> boolean hasError(Class<E> classType) {
        return classType.isInstance(error);
    }
}

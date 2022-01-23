package result;

import result.errors.Error;

public class PlainResult {
    private final Boolean isSucceed;
    private final Error error;

    public Boolean isSucceed() {
        return isSucceed;
    }

    public Boolean isFailed() {
        return !isSucceed;
    }

    private PlainResult() {
        this.error = null;
        this.isSucceed = true;
    }

    private PlainResult(Error error) {
        this.error = error;
        this.isSucceed = false;
    }

    public static PlainResult Ok() {
        return new PlainResult();
    }

    public static PlainResult Fail(Error error) {
        return new PlainResult(error);
    }

    public <E> boolean hasError(Class<E> classType) {
        return classType.isInstance(error);
    }
}

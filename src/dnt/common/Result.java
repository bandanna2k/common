package dnt.common;

import java.util.function.Consumer;
import java.util.function.Function;

public class Result<S, E>
{
    private final S data;
    private final E error;

    private Result(S data, E errorMessage)
    {
        this.data = data;
        this.error = errorMessage;
    }

    public static <S, E> Result<S, E> failure(E errorMessage)
    {
        return new Result(null, errorMessage);
    }

    public static <S, E> Result<S, E> success(S data)
    {
        return new Result(data, null);
    }

    public void consume(Consumer<S> success, Consumer<E> error)
    {
        ifSuccess(success);
        ifFailure(error);
    }

    public <NewS, NewE> Result<NewS, NewE> fold(
            Function<S, Result<NewS, NewE>> success,
            Function<E, Result<NewS, NewE>> error)
    {
        if(isSuccess())
        {
            return success.apply(data);
        }
        else
        {
            return error.apply(this.error);
        }
    }

    public void ifSuccess(Consumer<S> successAction)
    {
        if(isSuccess())
        {
            successAction.accept(data);
        }
    }

    public void ifFailure(Consumer<E> failureAction)
    {
        if(isFailure())
        {
            failureAction.accept(error);
        }
    }

    public boolean isFailure()
    {
        return error != null;
    }

    public E failure()
    {
        assert isFailure();
        return error;
    }

    public S success()
    {
        assert isSuccess();
        return data;
    }

    public boolean isSuccess()
    {
        return error == null;
    }

    public <New> Result<New, E> map(Function<S, New> newp)
    {
        New newValue = newp.apply(data);
        return new Result<>(newValue, error);
    }

    @Override
    public String toString()
    {
        return isSuccess() ? "Success[" + data + "]" : "Failure[" + error + "]";
    }
}

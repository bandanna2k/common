package dnt.common;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import static dnt.common.Result.failure;
import static dnt.common.Result.success;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

public class ResultTest
{
    @Test
    public void testSuccess()
    {
        Result<Long, String> success = success(1L);
        assertTrue(success.isSuccess());

        {
            AtomicBoolean executed = new AtomicBoolean(false);
            success.ifSuccess(v -> executed.set(true));
            assertTrue(executed.get());
        }
        {
            AtomicBoolean executed = new AtomicBoolean(false);
            success.ifFailure(v -> executed.set(true));
            assertFalse(executed.get());
        }
        assertThat(success.success()).isEqualTo(1L);
    }

    @Test
    public void testFailure()
    {
        Result<Long, String> result = failure("epic fail");
        assertFalse(result.isSuccess());

        {
            AtomicBoolean executed = new AtomicBoolean(false);
            result.ifSuccess(v -> executed.set(true));
            assertFalse(executed.get());
        }
        {
            AtomicBoolean executed = new AtomicBoolean(false);
            result.ifFailure(v -> executed.set(true));
            assertTrue(executed.get());
        }
        assertThat(result.failure()).isEqualTo("epic fail");
    }

    @Test
    public void testMap()
    {
        Result<Long, String> success = success(1L);
        assertTrue(success.isSuccess());
        assertThat(success.success()).isEqualTo(1L);
        assertThat(success.success()).isNotEqualTo("1");

        Result<String, String> mapped = success.map(String::valueOf);
        assertTrue(mapped.isSuccess());
        assertThat(mapped.success()).isNotEqualTo(1L);
        assertThat(mapped.success()).isEqualTo("1");
    }

    @Test
    public void testFold()
    {
        Result<Long, String> success = success(1L);
        assertTrue(success.isSuccess());
        assertThat(success.success()).isEqualTo(1L);
        assertThat(success.success()).isNotEqualTo("1");

        Result<Long, String> folded1 = success.fold(
                success1 -> failure("Switching"),
                error1 -> success(2L)
        );
        assertTrue(folded1.isFailure());
        assertThat(folded1.failure()).isEqualTo("Switching");

        // Same fold
        Result<Long, String> folded2 = folded1.fold(
                success1 -> failure("Switching"),
                error1 -> success(2L)
        );
        assertTrue(folded2.isSuccess());
        assertThat(folded2.success()).isEqualTo(2L);
    }
}
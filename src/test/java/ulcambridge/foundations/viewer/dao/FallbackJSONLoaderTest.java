package ulcambridge.foundations.viewer.dao;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.EmptyResultDataAccessException;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FallbackJSONLoaderTest {

    @Mock
    private JSONLoader primary;

    @Mock
    private JSONLoader fallback;

    @Test
    public void returnsPrimaryResultWithoutConsultingFallback() {
        JSONObject result = new JSONObject("{\"key\":\"value\"}");
        when(primary.loadJSON("test")).thenReturn(result);

        JSONObject actual = new FallbackJSONLoader(primary, fallback).loadJSON("test");

        assertThat(actual).isSameInstanceAs(result);
        verify(fallback, never()).loadJSON(any());
    }

    @Test
    public void returnsFallbackWhenPrimaryThrowsEmptyResult() {
        JSONObject fallbackResult = new JSONObject("{\"key\":\"fallback\"}");
        when(primary.loadJSON("test")).thenThrow(new EmptyResultDataAccessException(1));
        when(fallback.loadJSON("test")).thenReturn(fallbackResult);

        JSONObject actual = new FallbackJSONLoader(primary, fallback).loadJSON("test");

        assertThat(actual).isSameInstanceAs(fallbackResult);
    }

    @Test
    public void propagatesOtherDataAccessExceptionWithoutCallingFallback() {
        DataAccessResourceFailureException ex = new DataAccessResourceFailureException("io error");
        when(primary.loadJSON("test")).thenThrow(ex);

        DataAccessResourceFailureException thrown = assertThrows(DataAccessResourceFailureException.class,
            () -> new FallbackJSONLoader(primary, fallback).loadJSON("test"));

        assertThat(thrown).isSameInstanceAs(ex);
        verify(fallback, never()).loadJSON(any());
    }
}

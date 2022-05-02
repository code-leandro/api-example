package com.leandro.apiexample.exception;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ResourceExceptionHandlerTest {

    @InjectMocks
    ResourceExceptionHandler resourceExceptionHandler;

    @Test
    void notFound() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String messageError = "Not Found";
        String path = "/my/path";
        when(request.getRequestURI()).thenReturn(path);
        ResponseEntity<StandardError> standardErrorResponseEntity = resourceExceptionHandler.notFound(new NotFoundException(messageError), request);
        assertThat(standardErrorResponseEntity).isNotNull();
        assertThat(standardErrorResponseEntity.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(standardErrorResponseEntity.getBody().getError()).isEqualTo(messageError);
        assertThat(standardErrorResponseEntity.getBody().getPath()).isEqualTo(path);
        assertThat(standardErrorResponseEntity.getBody().getTime()).isNotNull();
    }

    @Test
    void dataIntegrityViolation() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String messageError = "error with data integrity";
        DataIntegrityViolationException exception = new DataIntegrityViolationException(messageError);
        ResponseEntity<StandardError> standardErrorResponseEntity = resourceExceptionHandler
                                                                        .dataIntegrityViolation(exception, request);
        assertThat(standardErrorResponseEntity).isNotNull();
        assertThat(standardErrorResponseEntity.getBody().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(standardErrorResponseEntity.getBody().getError()).isEqualTo(messageError);
    }
}
package ru.job4j.site.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import ru.job4j.site.exception.ExternalServiceException;
import ru.job4j.site.exception.NotFoundException;

import java.io.IOException;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError()
                || response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new NotFoundException("Ресурс не найден во внешнем сервисе");
        }

        if (response.getStatusCode().is5xxServerError()) {
            throw new ExternalServiceException("Ошибка внешнего сервиса");
        }
    }
}

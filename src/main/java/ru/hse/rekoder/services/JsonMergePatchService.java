package ru.hse.rekoder.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.hse.rekoder.exceptions.MergePatchException;

import javax.json.JsonMergePatch;
import javax.json.JsonValue;
import javax.validation.Valid;
import javax.validation.Validator;

@Component
@Validated
public class JsonMergePatchService {
    private final ObjectMapper mapper;

    public JsonMergePatchService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Valid
    public <T> T mergePatch(JsonMergePatch mergePatch, T targetBean, Class<T> beanClass) {
        JsonValue target = mapper.convertValue(targetBean, JsonValue.class);
        JsonValue patched = mergePatch.apply(target);
        try {
            return mapper.convertValue(patched, beanClass);
        } catch (IllegalArgumentException ex) {
            throw new MergePatchException("Failed to patch the resource", ex);
        }
    }
}

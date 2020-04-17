package com.nadia.openplatfrom.isv.manage.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class JsonDateSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        String pattern = "d MMM, yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern, LocaleContextHolder.getLocale());
        try {

            String value = format.format(date);

            gen.writeString(value);
        }catch(Exception e) {
            log.error("pattern: [{}],date: [{}],locale: [{}]",
                    pattern,date, LocaleContextHolder.getLocale());
            throw new JsonProcessingException(e) {
                private static final long serialVersionUID = 3241752846490346649L;};
        }
    }
}

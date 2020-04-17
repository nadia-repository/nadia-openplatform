package com.nadia.openplatfrom.isv.manage.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class JsonDateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Date date= null;

        if(!org.apache.commons.lang3.StringUtils.isEmpty(p.getText())) {
            String dateString= p.getText();
            String pattern = "d MMM, yyyy";
            SimpleDateFormat format= new SimpleDateFormat(pattern, LocaleContextHolder.getLocale());
            try {
                date = format.parse(dateString);
            }catch(Exception e) {
                log.error("pattern: [{}],dateString: [{}],locale: [{}]",
                        pattern,p.getText(), LocaleContextHolder.getLocale());
                throw new JsonProcessingException(e) {
                    private static final long serialVersionUID = 3241752846490346649L;};
            }
        }
        return date;
    }
}

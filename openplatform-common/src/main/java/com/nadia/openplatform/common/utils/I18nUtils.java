package com.nadia.openplatform.common.utils;

import org.springframework.context.i18n.LocaleContextHolder;

final public class I18nUtils {

    final public static String I18N_PREFIX = "i18n:";

    private I18nUtils() {
    }

    public static String getMessage(String code) {
        return getMessage(code, new Object[0]);
    }

    public static String getMessage(String code, String defaultMessage) {
        return getMessage(code, new Object[0], defaultMessage);
    }

    public static String getMessage(String code, Object[] args) {
        return SpringUtils.getMessageSource().getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public static String getMessage(String code, Object[] args, String defaultMessage) {
        return SpringUtils.getMessageSource().getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
    }

    public static String getMessageByI18FullCode(String defaultMessage) {
        return I18nUtils.getMessage(defaultMessage.substring(I18nUtils.I18N_PREFIX.length()), "Parameter error");
    }
}

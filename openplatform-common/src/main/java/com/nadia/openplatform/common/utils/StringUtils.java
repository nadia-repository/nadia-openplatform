package com.nadia.openplatform.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.InvalidParameterException;
import java.text.Normalizer;
import java.util.*;

@Slf4j
public class StringUtils {

    public static final String EMPTY = "";

    public static boolean equals(Object o1, Object o2) {
        return o1 == null ? o2 == null ? true : false : o1.equals(o2);
    }

    public static boolean isEmpty(Object str) {
        return org.springframework.util.StringUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(Object str) {
        return !org.springframework.util.StringUtils.isEmpty(str);
    }

    public static boolean isBlank(String str) {
        return org.springframework.util.StringUtils.isEmpty(str) && !org.springframework.util.StringUtils.hasLength(org.springframework.util.StringUtils.trimAllWhitespace(str));
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String toString(Object obj) {
        // String.valueOf 会将null转成 "null", 有些情况不能转成字符串null
        return obj == null ? null : obj.toString();
    }

    public static boolean hasLength(CharSequence str) {
        return org.springframework.util.StringUtils.hasLength(str);
    }

    public static boolean hasLength(String str) {
        return org.springframework.util.StringUtils.hasLength(str);
    }

    public static boolean hasText(CharSequence str) {
        return org.springframework.util.StringUtils.hasText(str);
    }

    public static boolean hasText(String str) {
        return org.springframework.util.StringUtils.hasText(str);
    }


    public static boolean containsWhitespace(CharSequence str) {
        return org.springframework.util.StringUtils.containsWhitespace(str);
    }

    public static boolean containsWhitespace(String str) {
        return containsWhitespace((CharSequence) str);
    }

    public static String trimWhitespace(String str) {
        return org.springframework.util.StringUtils.trimWhitespace(str);
    }

    public static String trimAllWhitespace(String str) {
        return org.springframework.util.StringUtils.trimAllWhitespace(str);
    }

    public static String trimLeadingWhitespace(String str) {
        return org.springframework.util.StringUtils.trimLeadingWhitespace(str);
    }

    public static String trimTrailingWhitespace(String str) {
        return org.springframework.util.StringUtils.trimTrailingWhitespace(str);
    }

    public static String trimLeadingCharacter(String str, char leadingCharacter) {
        return org.springframework.util.StringUtils.trimLeadingCharacter(str, leadingCharacter);
    }

    public static String trimTrailingCharacter(String str, char trailingCharacter) {
        return org.springframework.util.StringUtils.trimTrailingCharacter(str, trailingCharacter);
    }

    /**
     * Returns a string whose value is this string or its part(the result string's maxLength is not greater
     * than the target maxLength), with any leading and trailing whitespace removed.
     *
     * @param source
     * @param maxLength
     * @return
     */
    public static String trimAndTruncate(String source, int maxLength) {
        if (source == null) {
            return null;
        }

        if (maxLength <= 0) {
            if (maxLength < 0) {
                throw new InvalidParameterException("maxLength can not be les");
            }
            return "";
        }
        int start = 0, end = source.length() - 1;
        while (start <= end && source.charAt(start) <= ' ') {
            start++;
        }

        while (start <= end &&source.charAt(end) <= ' ') {
            end--;
        }

        int subLen = end - start +1;
        if (subLen > maxLength) {
            end = start + maxLength - 1;
        }
        return (start > 0 || end < source.length()) ? source.substring(start, end + 1) : source;
    }

    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return org.springframework.util.StringUtils.startsWithIgnoreCase(str, prefix);
    }

    public static boolean endsWithIgnoreCase(String str, String suffix) {
        return org.springframework.util.StringUtils.endsWithIgnoreCase(str, suffix);
    }

    public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
        return org.springframework.util.StringUtils.substringMatch(str, index, substring);
    }

    public static int countOccurrencesOf(String str, String sub) {
        return org.springframework.util.StringUtils.countOccurrencesOf(str, sub);
    }

    public static String replace(String inString, String oldPattern, String newPattern) {
        return org.springframework.util.StringUtils.replace(inString, oldPattern, newPattern);
    }

    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, EMPTY);
    }

    public static String deleteAny(String inString, String charsToDelete) {
        return org.springframework.util.StringUtils.deleteAny(inString, charsToDelete);
    }

    public static String quote(String str) {
        return org.springframework.util.StringUtils.quote(str);
    }

    public static Object quoteIfString(Object obj) {
        return org.springframework.util.StringUtils.quoteIfString(obj);
    }

    public static String unqualify(String qualifiedName) {
        return org.springframework.util.StringUtils.unqualify(qualifiedName);
    }

    public static String unqualify(String qualifiedName, char separator) {
        return org.springframework.util.StringUtils.unqualify(qualifiedName, separator);
    }

    public static String capitalize(String str) {
        return org.springframework.util.StringUtils.capitalize(str);
    }

    public static String uncapitalize(String str) {
        return org.springframework.util.StringUtils.uncapitalize(str);
    }


    public static String getFilename(String path) {
        return org.springframework.util.StringUtils.getFilename(path);
    }

    public static String getFilenameExtension(String path) {
        return org.springframework.util.StringUtils.getFilenameExtension(path);
    }

    public static String stripFilenameExtension(String path) {
        return org.springframework.util.StringUtils.stripFilenameExtension(path);
    }

    public static String applyRelativePath(String path, String relativePath) {
        return org.springframework.util.StringUtils.applyRelativePath(path, relativePath);
    }

    public static String cleanPath(String path) {
        return org.springframework.util.StringUtils.cleanPath(path);
    }

    public static boolean pathEquals(String path1, String path2) {
        return cleanPath(path1).equals(cleanPath(path2));
    }

    public static Locale parseLocaleString(String localeString) {
        return org.springframework.util.StringUtils.parseLocaleString(localeString);
    }

    public static String toLanguageTag(Locale locale) {
        return org.springframework.util.StringUtils.toLanguageTag(locale);
    }

    public static TimeZone parseTimeZoneString(String timeZoneString) {
        return org.springframework.util.StringUtils.parseTimeZoneString(timeZoneString);
    }

    public static String[] addStringToArray(String[] array, String str) {
        return org.springframework.util.StringUtils.addStringToArray(array, str);
    }

    public static String[] concatenateStringArrays(String[] array1, String[] array2) {
        return org.springframework.util.StringUtils.concatenateStringArrays(array1, array2);
    }

    public static String[] sortStringArray(String[] array) {
        return org.springframework.util.StringUtils.sortStringArray(array);
    }

    public static String[] toStringArray(Collection<String> collection) {
        return org.springframework.util.StringUtils.toStringArray(collection);
    }

    public static String[] toStringArray(Enumeration<String> enumeration) {
        return org.springframework.util.StringUtils.toStringArray(enumeration);
    }

    public static String[] trimArrayElements(String[] array) {
        return org.springframework.util.StringUtils.trimArrayElements(array);
    }

    public static String[] removeDuplicateStrings(String[] array) {
        return org.springframework.util.StringUtils.removeDuplicateStrings(array);
    }

    public static String[] split(String toSplit, String delimiter) {
        return org.springframework.util.StringUtils.split(toSplit, delimiter);
    }

    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
        return splitArrayElementsIntoProperties(array, delimiter, null);
    }

    public static Properties splitArrayElementsIntoProperties(
            String[] array, String delimiter, String charsToDelete) {

        return org.springframework.util.StringUtils.splitArrayElementsIntoProperties(array, delimiter, charsToDelete);
    }

    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    public static String[] tokenizeToStringArray(
            String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        return org.springframework.util.StringUtils.tokenizeToStringArray(str, delimiters, trimTokens, ignoreEmptyTokens);
    }

    public static String[] delimitedListToStringArray(String str, String delimiter) {
        return delimitedListToStringArray(str, delimiter, null);
    }

    public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
        return org.springframework.util.StringUtils.delimitedListToStringArray(str, delimiter, charsToDelete);
    }

    public static String[] commaDelimitedListToStringArray(String str) {
        return delimitedListToStringArray(str, ",");
    }

    public static Set<String> commaDelimitedListToSet(String str) {
        return org.springframework.util.StringUtils.commaDelimitedListToSet(str);
    }

    public static String join(Collection<?> coll, String delim, String prefix, String suffix) {
        return org.springframework.util.StringUtils.collectionToDelimitedString(coll, delim, prefix, suffix);
    }

    public static String join(Collection<?> coll, String delim) {
        return join(coll, delim, EMPTY, EMPTY);
    }

    public static String join(Collection<?> coll) {
        return join(coll, ",");
    }

    public static String join(Object[] arr, String delim) {
        return org.springframework.util.StringUtils.arrayToDelimitedString(arr, delim);
    }

    public static String join(Object[] arr) {
        return join(arr, ",");
    }

    public static String removeAccent(String sb) {
        log.info("StringUtils.removeAccent paramter:{}",sb);
        // use Normalizer replace all characters with accents
        sb = Normalizer.normalize(sb, Normalizer.Form.NFD);
        sb = sb.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        // replace again with 2 exception
        sb = sb.replaceAll("Đ", "D");
        sb = sb.replaceAll("đ", "d");
        String result = sb.toUpperCase().replace(" ", "");
        log.info("StringUtils.removeAccent:{}",result);
        return result;
    }

    public static boolean compare(String name,String bankAccountName){
        log.info("StringUtils.compare name:{} ,bankAccountName:{}",name,bankAccountName);
        if(StringUtils.isBlank(name) || StringUtils.isBlank(bankAccountName)){
            return false;
        }
        String newName = StringUtils.removeAccent(name);
        String newBankAccountName = StringUtils.removeAccent(bankAccountName);
        log.info("StringUtils.compare result newName:{} ,newBankAccountName:{}",newName,newBankAccountName);
        if(newName.equals(newBankAccountName)){
            return true;
        }else {
            return false;
        }
    }

    public static void main(String[] args) {

        boolean compare = compare("Đàm Thị Thuỷ  ", "Đàm   Thị Thuỷ");
        System.out.println(compare);
    }
}
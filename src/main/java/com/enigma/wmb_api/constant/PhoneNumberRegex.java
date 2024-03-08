package com.enigma.wmb_api.constant;

import java.util.regex.Pattern;

public class PhoneNumberRegex {
    public static final Pattern TELKOMSEL_NUMBER_PATTERN = Pattern.compile("^(\\\\+62|\\\\+0|0|62)8(1[123]|52|53|21|22|23)[0-9]{5,9}$");
    public static final Pattern SIMPATI_NUMBER_PATTERN = Pattern.compile("^(\\\\+62|\\\\+0|0|62)8(1[123]|2[12])[0-9]{5,9}$");
    public static final Pattern AS_NUMBER_PATTERN = Pattern.compile("^(\\\\+62|\\\\+0|0|62)8(52|53|23)[0-9]{5,9}$");
    public static final Pattern INDOSAT_NUMBER_PATTERN = Pattern.compile("^(\\\\+62815|0815|62815|\\\\+0815|\\\\+62816|0816|62816|\\\\+0816|\\\\+62858|0858|62858|\\\\+0814|\\\\+62814|0814|62814|\\\\+0814)[0-9]{5,9}$");
    public static final Pattern IM3_NUMBER_PATTERN = Pattern.compile("^(\\\\+62855|0855|62855|\\\\+0855|\\\\+62856|0856|62856|\\\\+0856|\\\\+62857|0857|62857|\\\\+0857)[0-9]{5,9}$");
    public static final Pattern XL_NUMBER_PATTERN = Pattern.compile("^(\\\\+62817|0817|62817|\\\\+0817|\\\\+62818|0818|62818|\\\\+0818|\\\\+62819|0819|62819|\\\\+0819|\\\\+62859|0859|62859|\\\\+0859|\\\\+0878|\\\\+62878|0878|62878|\\\\+0877|\\\\+62877|0877|62877)[0-9]{5,9}$");
    public static final Pattern AXIS_NUMBER_PATTERN = Pattern.compile("^08(38|31)\\d{7,8}$");
    public static final Pattern TRI_NUMBER_PATTERN = Pattern.compile("^0896\\d{4,8}$");
    public static final Pattern SMARTFREN_NUMBER_PATTERN = Pattern.compile("^0887\\d{4,8}$");
    public static final Pattern BYU_NUMBER_PATTERN = Pattern.compile("^08(38|31)\\d{7,8}$");
    public static final Pattern OTHERS_NUMBER_PATTERN = Pattern.compile("^((081[123456789]\\d{7})|(082[123456789]\\d{7})|(083[123456789]\\d{7})|(085[123456789]\\d{7})|(086[123456789]\\d{7})|(087[123456789]\\d{7})|(088[123456789]\\d{7})|(089[123456789]\\d{7})|(090[123456789]\\d{7})|(091[123456789]\\d{7})|(092[123456789]\\d{7})|(093[123456789]\\d{7})|(094[123456789]\\d{7})|(095[123456789]\\d{7})|(096[123456789]\\d{7})|(097[123456789]\\d{7})|(098[123456789]\\d{7})|(099[123456789]\\d{7}))$\n");

}

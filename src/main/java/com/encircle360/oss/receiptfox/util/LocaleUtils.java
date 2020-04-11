package com.encircle360.oss.receiptfox.util;

import java.util.Currency;
import java.util.Locale;

public class LocaleUtils {

    public static String getCountryDisplayName(String countryCode) {
        Locale loc = new Locale("", countryCode);
        return loc.getDisplayCountry();
    }

    public static String getCurrencySymbol(String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        return currency.getSymbol();
    }
}

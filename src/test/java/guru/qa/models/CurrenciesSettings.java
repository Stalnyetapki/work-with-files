package guru.qa.models;

import java.util.ArrayList;

public class CurrenciesSettings {

    public boolean isFixedRate;
    public boolean isGameFiat;
    public boolean isViewFiat;
    public ArrayList<Currency> currencies;

    public static class Currency{
        public String currencyCode;
        public String id;

    }
}

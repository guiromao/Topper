package co.topper.configuration;

import java.util.List;

/**
 * Terms configuration that will be used to search for the "Track of the Hour"
 */
public final class TrendyTermsConfiguration {

    private static final List<String> trendyTerms = List.of(
            "love", "diamonds", "rich", "food", "soft", "peace",
            "guitars", "rock", "rap", "hip-hop", "one", "two", "three",
            "test", "dance", "trend", "sex", "savage", "godlike",
            "balance", "party", "bliss", "enjoy", "dogs", "sports",
            "hallway", "eagles", "foo", "program", "night", "day",
            "pillows", "beds", "house", "electro", "boat",
            "sea", "bowl", "boo", "blanket", "pop", "horsepower",
            "light", "stars", "prize", "dreams", "sing", "master",
            "know", "be", "forever", "together", "hug",
            "born", "immortal", "brave", "courage", "bold", "fly",
            "run", "pace", "grace", "place", "laugh", "live", "breathe",
            "punk", "lions", "growl", "scone"
    );

    private TrendyTermsConfiguration() {

    }

    public static List<String> getTrendyTerms() {
        return trendyTerms;
    }

}

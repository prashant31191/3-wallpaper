package com.azwallpaper;

import android.content.SearchRecentSuggestionsProvider;

/**

 */
public class SuggestionProvider extends SearchRecentSuggestionsProvider {

    // part of content uri which is defined in AndroidManifest.xml
    public static final String AUTHORITY = "com.azwallpaper" +
            ".SuggestionProvider";

    // suggestion mode which gives recent queries
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}

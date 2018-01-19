package com.google.android.apps.nexuslauncher.smartspace;

class SmartspaceController {
    enum Store {
        WEATHER("smartspace_weather"),
        CURRENT("smartspace_current");

        final String filename;

        Store(final String filename) {
            this.filename = filename;
        }
    }
}


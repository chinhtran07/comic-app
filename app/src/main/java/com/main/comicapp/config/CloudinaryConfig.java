package com.main.comicapp.config;

import android.content.Context;
import com.cloudinary.android.MediaManager;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryConfig {

    public static void initCloudinary(Context context) {
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", "dxxwcby8l");
        config.put("api_key", "448651448423589");
        config.put("api_secret", "ftGud0r1TTqp0CGp5tjwNmkAm-A");
        config.put("secure", true);
        MediaManager.init(context, config);
    }
}

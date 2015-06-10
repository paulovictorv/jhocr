package com.googlecode.jhocr.util;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by paulo on 10/06/15.
 */
public class ResourceLoader {

    public static URL asUrl(String resourceName) {
        return ResourceLoader.class.getClassLoader().getResource(resourceName);
    }

    public static InputStream asStream(String resourceName) {
        return ResourceLoader.class.getClassLoader().getResourceAsStream(resourceName);
    }

}

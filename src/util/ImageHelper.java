/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */
package util;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Image;

public class ImageHelper {
    /**
     * Loads an image from resources
     * @param path Path of the image file
     * @return Loaded image
     * @throws RuntimeException
     */
    public static Image loadImage(String path) throws RuntimeException {
        Image image = null;

        try {
            InputStream in = Image.class.getResourceAsStream(path);
            image = Image.createImage(in);
        } catch (IOException ioe) {
            throw new RuntimeException("ImageLoader failed to load image:" + path + " " + ioe.getMessage());
        }

        return image;
    }
}

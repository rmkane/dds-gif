package dds.gif.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dds.gif.util.ImageUtils;

public class ImageUtilsTest {
    @Test
    public void testStripExtension() {
        assertTrue("foo".equals(ImageUtils.stripExtension("foo.png")));
    }
}

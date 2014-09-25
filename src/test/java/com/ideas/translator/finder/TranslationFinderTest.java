package com.ideas.translator.finder;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;

public class TranslationFinderTest {

    public static final String G_3_EN_US_FILE_LOCATION = "C:\\Tetris\\pacman\\translator\\src\\test\\java\\resources_g3\\shared_en_US.properties";
    public static final String COMMON_EN_US_FILE_LOCATION = "C:\\Tetris\\pacman\\translator\\src\\test\\java\\resources_common\\en_US.properties";

    @Test
    public void shouldLoadEnUSPropertiesFilesWhenConstructorIsInvoked(){
        TranslationFinder translationFinder = null;
        try {
            translationFinder = new TranslationFinder(getPropertiesFor(G_3_EN_US_FILE_LOCATION), getPropertiesFor(COMMON_EN_US_FILE_LOCATION));

            assertEquals(5, translationFinder.getKeyCountForEnglishInG3());
            assertEquals(10, translationFinder.getKeyCountForEnglishInCommon());

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Should not throw exception while loading the files!");
        }
    }

    @Test
    public void shouldCreateMissingTranslationsFile(){
        TranslationFinder translationFinder = null;
        try {
            translationFinder = new TranslationFinder(getPropertiesFor(G_3_EN_US_FILE_LOCATION), getPropertiesFor(COMMON_EN_US_FILE_LOCATION));
            String localeCode = Locale.CHINA.toString();
            List<String> missingTranslationKeys =  translationFinder.getMissingTranslations(localeCode);

            translationFinder.createMissingFile(missingTranslationKeys, localeCode);
            assertEquals(3, missingTranslationKeys.size());

            String missingKeysFilePath = "C:\\Tetris\\pacman\\translator\\src\\test\\java\\resources_g3\\shared_To_Translate_" + localeCode + ".properties";
            Properties propertiesInMissingFile = getPropertiesFor(missingKeysFilePath);
            assertEquals(3, propertiesInMissingFile.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Properties getPropertiesFor(String fileLocation) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(fileLocation));
        return properties;
    }

}

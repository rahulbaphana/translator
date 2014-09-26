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

   public static final String G_3_EN_US_FILE_LOCATION = "C:\\GithHubRepo\\translator\\src\\test\\java\\resources_g3\\shared_en_US.properties";
   public static final String COMMON_EN_US_FILE_LOCATION = "C:\\GithHubRepo\\translator\\src\\test\\java\\resources_common\\shared_en_US.properties";

   @Test
   public void shouldPutMissingValuesFromCommonPoolTranslationToPutItInRespectiveTranslationForG3() throws IOException {
       //Given
       TranslationFinder translationFinder = new TranslationFinder(getPropertiesFor(G_3_EN_US_FILE_LOCATION), getPropertiesFor(COMMON_EN_US_FILE_LOCATION));

       //When
       Properties missingTranslationKeysChinese =  translationFinder.getMissingTranslations(Locale.CHINA.toString());
       String spanish = "es";
       Properties missingTranslationKeysSpanish =  translationFinder.getMissingTranslations(spanish);

       //Then
       String missingKeysFilePathChinese = "C:\\GithHubRepo\\translator\\src\\test\\java\\resources_g3\\shared_" + Locale.CHINA.toString() + ".properties";
       String missingKeysFilePathSpanish = "C:\\GithHubRepo\\translator\\src\\test\\java\\resources_g3\\shared_" + spanish + ".properties";
       assertEquals(1, missingTranslationKeysChinese.size());
       assertEquals(5, getPropertiesFor(missingKeysFilePathChinese).size());
       assertEquals(2, missingTranslationKeysSpanish.size());
       assertEquals(4, getPropertiesFor(missingKeysFilePathSpanish).size());

   }


    private Properties getPropertiesFor(String fileLocation) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(fileLocation));
        return properties;
    }

}

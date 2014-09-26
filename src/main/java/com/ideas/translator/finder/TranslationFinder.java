package com.ideas.translator.finder;

import java.io.*;
import java.util.*;

public class TranslationFinder {

    private Properties g3EnUs;
    private Properties commonEnUs;

    public TranslationFinder(Properties g3EnUs, Properties commonEnUs) throws IOException {
        this.g3EnUs = g3EnUs;
        this.commonEnUs = commonEnUs;
    }

    public Properties getMissingTranslations(String localeCode) throws IOException {
        Properties localeProperties = new Properties();
        Properties missingTranslations = new Properties();
        localeProperties.load(new FileInputStream("C:\\GithHubRepo\\translator\\src\\test\\java\\resources_g3\\shared_"+localeCode+".properties"));
        for (Object g3EnUsKey : g3EnUs.keySet()) {
            if(!localeProperties.containsKey(g3EnUsKey)){
                missingTranslations.put(g3EnUsKey, g3EnUs.get(g3EnUsKey));
            }
        }
        Properties commonPoolProperties = getTranslationsFromCommonPool(missingTranslations, localeCode);
        updateMissingTranslations(missingTranslations, commonPoolProperties, localeCode);
        createMissingFile(missingTranslations.keySet(), localeCode);
        return missingTranslations;
    }

    private void updateMissingTranslations(Properties missingTranslations, Properties commonPoolProperties, String localeCode) throws IOException {
        Properties localeProperties = new Properties();
        localeProperties.putAll(missingTranslations);
        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("C:\\GithHubRepo\\translator\\src\\test\\java\\resources_g3\\shared_" + localeCode + ".properties",true), "UTF8"));
        List keys = new ArrayList();
        for (Object key : commonPoolProperties.keySet()) {
            out.write(key.toString() + "=" + commonPoolProperties.get(key).toString() + "\n");
            if(missingTranslations.containsKey(key)){
                keys.add(key);
            }
        }
        missingTranslations.keySet().removeAll(keys);
        out.close();
    }

    private File createMissingFile(Set missingTranslationKeys, String localeCode) throws IOException {
        String missingKeysFile = "C:\\GithHubRepo\\translator\\src\\test\\java\\resources_g3\\missing_" + localeCode + ".properties";
        File missingFile = new File(missingKeysFile);
        if(missingFile.exists())
            missingFile.delete();
        missingFile.createNewFile();
        FileWriter fileWriter = new FileWriter(missingKeysFile);
        for (Object missingTranslationKey : missingTranslationKeys) {
            fileWriter.write(new StringBuilder((String)missingTranslationKey).append("=").append(g3EnUs.get((String)missingTranslationKey)).append("\n").toString());
        }
        fileWriter.close();
        return missingFile;
    }

    private Properties getTranslationsFromCommonPool(Properties propertiesInMissingFile, String localeCode) throws IOException {
        Properties tranlationFromCommonPool = new Properties();
        for (Object missingValue : propertiesInMissingFile.values()) {
            if(commonEnUs.containsValue(missingValue)){
                Object key = getKeyFrom(commonEnUs, missingValue);
                if(key!=null){
                   Object translationText = getTranslationTextFor(key, localeCode);
                   if(translationText!=null){
                       tranlationFromCommonPool.put(getKeyFrom(g3EnUs, missingValue), translationText);
                   }
                }
            }
        }
        return tranlationFromCommonPool;
    }

    private Object getTranslationTextFor(Object key, String localeCode) throws IOException {
        Properties localeProperties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("C:\\GithHubRepo\\translator\\src\\test\\java\\resources_common\\shared_" + localeCode + ".properties");
        InputStreamReader isr = new InputStreamReader(fileInputStream, "UTF-8");
        Reader reader = new BufferedReader(isr);
        localeProperties.load(reader);
        return localeProperties.get(key);
    }

    private Object getKeyFrom(Properties property, Object value) {
        for (Object propertyKey : property.keySet()) {
            if(property.get(propertyKey).equals(value)){
               return propertyKey;
            }
        }
        return null;
    }
}

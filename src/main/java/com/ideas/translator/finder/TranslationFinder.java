package com.ideas.translator.finder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class TranslationFinder {

    private Properties g3EnUs;
    private Properties commonEnUs;

    public TranslationFinder(Properties g3EnUs, Properties commonEnUs) throws IOException {
        this.g3EnUs = g3EnUs;
        this.commonEnUs = commonEnUs;
    }

    public int getKeyCountForEnglishInG3() {
        return g3EnUs.size();
    }

    public int getKeyCountForEnglishInCommon() {
        return commonEnUs.size();
    }

    public List<String> getMissingTranslations(String localeCode) throws IOException {
        Properties localeProperties = new Properties();
        List<String> missingKeys = new ArrayList<String>();
        localeProperties.load(new FileInputStream("C:\\Tetris\\pacman\\translator\\src\\test\\java\\resources_g3\\shared_"+localeCode+".properties"));
        for (Object g3EnUsKey : g3EnUs.keySet()) {
            if(!localeProperties.containsKey(g3EnUsKey)){
                missingKeys.add(g3EnUsKey.toString());
            }
        }
        return missingKeys;
    }

    public File createMissingFile(List<String> missingTranslationKeys, String localeCode) throws IOException {
        String missingKeysFile = "C:\\Tetris\\pacman\\translator\\src\\test\\java\\resources_g3\\shared_To_Translate_" + localeCode + ".properties";
        File file = new File(missingKeysFile);
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(missingKeysFile);
        for (String missingTranslationKey : missingTranslationKeys) {
            fileWriter.write(missingTranslationKey);
            fileWriter.write("\n");
        }
        fileWriter.close();
        return file;
    }
}

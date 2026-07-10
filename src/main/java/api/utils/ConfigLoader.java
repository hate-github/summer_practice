package api.utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();

    static {
        try(InputStream inputStream = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("config.properties")){
            if(inputStream == null){
                throw new RuntimeException("не найден конфиг");
            }
            properties.load(inputStream);
        }
        catch (Exception exception){
            throw  new RuntimeException("не смог загрузить конфиг");
        }
    }

    public static String get(String value){
        return properties.getProperty(value);
    }

}

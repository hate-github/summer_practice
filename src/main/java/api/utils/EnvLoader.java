package api.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {
    private static final Dotenv dotenv = Dotenv.configure()
            .filename(".env")
            .load();

    public static String get(String field){
        String i = dotenv.get(field);
        if (i != null){
            i = i.trim();
            if(i.endsWith(";")){
                i=i.substring(0,i.length()-1);
            }
        }
        return i;
    }
}

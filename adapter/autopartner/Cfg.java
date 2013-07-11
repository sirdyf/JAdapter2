package autopartner;

import java.util.*;
import java.io.*;

public class Cfg {

    // "user.dir" - папка программы
    // "user.home" - домашняя папка пользователя
    public String FileNameCfg = System.getProperty("user.dir") + System.getProperty("file.separator") + "Config.cfg";

    private static Cfg _instance = null;
    private Properties prop = null;

    private Cfg() {

        prop = new Properties();
    	try {
	    FileInputStream fis = new FileInputStream(new File(FileNameCfg));
	    prop.load(fis);
    	}
    	catch (Exception e) {
    	    System.out.println("Файл конфигурации не найден: " + FileNameCfg);
    	}
    }

    public synchronized static Cfg getInstance() {
        if (_instance == null)
            _instance = new Cfg();
        return _instance;
    }

    // получить значение свойства по имени
    public synchronized String Get(String key) {
        String value = null;
        if (prop.containsKey(key))
            value = (String) prop.get(key);
        else {
            return value;
			//System.out.println("Ключ " + key +  " не найден в " + FileNameCfg);
        }
        return value;
    }

    // Установить значение свойства по имени
    public synchronized void Set(String key, String val) {

        prop.setProperty(key, val);

        try {
            prop.store(new FileOutputStream(FileNameCfg), FileNameCfg);
        } catch (Throwable t){
            System.out.println("Ошибка записи конфиг файла: " + FileNameCfg);
        }
    }
}
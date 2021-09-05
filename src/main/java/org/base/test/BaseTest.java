package org.base.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class BaseTest {

	public Properties prop;

	public BaseTest() {

		try {
			prop = new Properties();
			FileInputStream fileInputStream = new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/config.properties");
			prop.load(fileInputStream);
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public Properties getProp() {
		return prop;
	}

}
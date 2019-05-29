/* Copyright © 2016 Oracle and/or its affiliates. All rights reserved. */

package com.example.db;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class DBConfigProperties {
	static Properties pp;
	
	static {
		pp = new Properties();
		InputStream fps = null;
		try	{
			Path basepath = Paths.get(".").toAbsolutePath().normalize();
			System.out.println(basepath);
			//fps = new BufferedInputStream(new FileInputStream(new File(basepath.toString()+"/src/main/resources/database.properties")));
			fps = new BufferedInputStream(new FileInputStream(new File("src/main/resources/database.properties")));
			pp.load(fps);
		} catch (IOException e) {
			System.out.println("Read database.properties file failed !");
			e.printStackTrace();
		} finally {
			try {
				if (fps != null) fps.close();
			} catch (IOException e) {
				System.out.println("Release database.properties file failed !");
				e.printStackTrace();
			}
		}
	}
	
	public static String values(String key) {
		String value = pp.getProperty(key);
		if (value != null) {
			return value.trim();
		} else {
			return null;
		}
	}
}


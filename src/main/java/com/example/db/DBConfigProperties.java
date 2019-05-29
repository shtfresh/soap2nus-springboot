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

import org.springframework.util.ResourceUtils;

public class DBConfigProperties {
	static Properties pp;
	
	static {
		pp = new Properties();
		InputStream fps = null;
		try	{
			/*(Path basepath = Paths.get(".").toAbsolutePath().normalize();
			System.out.println(basepath);
			File folder = new File(basepath.toString());
			for (String file : folder.list()) {
				System.out.println(file);
			}
			
			System.out.println("### main ###");
			for (String file : new File("main").list()) {
				System.out.println(file);
			}
			
			System.out.println("### src ###");
			for (String file : new File("src").list()) {
				System.out.println(file);
			}*/
			
			File path = new File(ResourceUtils.getURL("classpath:").getPath());
			String strDirPath = path.getAbsolutePath();
			System.out.println(strDirPath);
			//strDirPath=strDirPath.replace("\\target\\classes", "");
			System.out.println("### main ###");
			for (String file : new File(strDirPath).list()) {
				System.out.println(file);
			}
			System.out.println(strDirPath);
			fps = new BufferedInputStream(new FileInputStream(new File(strDirPath+"database.properties")));
			//fps = new BufferedInputStream(new FileInputStream(new File(basepath+"/src/main/resources/database.properties")));
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


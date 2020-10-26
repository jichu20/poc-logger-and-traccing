package com.poc.logger;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Test {

	private static Long unknown = new Long("1603473962996094");

	@Autowired
	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		System.out.println(TimeUnit.NANOSECONDS.convert(unknown, TimeUnit.MICROSECONDS));
		System.out.println(new Date(TimeUnit.NANOSECONDS.convert(unknown, TimeUnit.MICROSECONDS)));
	}

}

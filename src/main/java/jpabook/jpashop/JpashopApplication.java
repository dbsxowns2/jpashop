package jpabook.jpashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		// 줄 단위 삭제 : Ctrl + X
		Hello hello = new Hello();
		hello.setData("외않되?");
		String data = hello.getData(); // 변수 자동완성 : Ctrl + Alt + V
//		System.out.println("data = " + data); // soutv

		SpringApplication.run(JpashopApplication.class, args);
	}

}

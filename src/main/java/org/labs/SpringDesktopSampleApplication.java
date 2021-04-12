package org.labs;

import org.labs.app.MainFrame;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Properties;

@SpringBootApplication
@MapperScan("org.labs.mapper")
public class SpringDesktopSampleApplication implements CommandLineRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringDesktopSampleApplication.class).headless(false).run(args);
    }

    @Override
    public void run(String... args) {
        //new MainFrame();
    }
}
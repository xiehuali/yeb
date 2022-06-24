package com.syeet.server;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

/**
 * @author xhl
 * @date 2022/1/12
 */

@Slf4j
@SpringBootApplication
@MapperScan("com.syeet.server.mapper")
public class YebServerApplication {
    public static void main(String[] args) throws  Exception{
        ConfigurableApplicationContext application = SpringApplication.run(YebServerApplication.class, args);
        Environment env = application.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "External: \thttp://{}:{}\n\t"+
                        "Doc: 	\thttp://{}:{}/doc.html\n\t"+
                        "Local Doc: \thttp://localhost:{}/doc.html\n"+
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),

                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                env.getProperty("server.port"));

    }
}

package com.mgiglione;

import com.mgiglione.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

//代码扫描根路径
@SpringBootApplication(scanBasePackages = { "com.mgiglione" })
@EnableAsync
public class MangaApplication
{
    private static final Logger logger = LoggerFactory.getLogger(MangaApplication.class);

    @Bean
    protected RestTemplate getTemplate()
    {
        return new RestTemplate();
    }
    
    public static void main(String[] args)
    {
        logger.info("[[ CI_CD_Tool ]] main() spring application started!");

        new SpringApplicationBuilder(MangaApplication.class).run(args);
    }

}

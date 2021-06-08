package com.mgiglione.service.test.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mgiglione.model.Manga;
import com.mgiglione.service.MangaService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MangaServiceIntegrationTest
{

    private static final Logger logger = LoggerFactory.getLogger(MangaServiceIntegrationTest.class);
    
    @Autowired
    private MangaService mangaService;
    
    @Test
    public void testGetMangasByTitle()
    {
        logger.info("[[ CI_CD_Tool ]] testGetMangasByTitle() in");

        //集成测试，客户端测试service类，不需要经过spring mvc容器，直接调用MangaService的具体方法，由于MangaService又需要访问真实的第三方
        //接口，所以执行的是集成测试而不是单元测试
        List<Manga> mangasByTitle = mangaService.getMangasByTitle("ken");
        assertThat(mangasByTitle).isNotNull().isNotEmpty();

        logger.info(mangasByTitle.get(0).toString());

        logger.info("[[ CI_CD_Tool ]] testGetMangasByTitle() out");
    }

}

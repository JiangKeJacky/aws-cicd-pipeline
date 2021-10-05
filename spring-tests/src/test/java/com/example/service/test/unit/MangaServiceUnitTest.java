package com.mgiglione.service.test.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgiglione.model.Manga;
import com.mgiglione.model.MangaResult;
import com.mgiglione.service.MangaService;
import com.mgiglione.utils.JsonUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MangaServiceUnitTest
{

    @Autowired
    private MangaService mangaService;

    // MockBean is the annotation provided by Spring that wraps mockito one
    // Annotation that can be used to add mocks to a Spring ApplicationContext.
    // If any existing single bean of the same type defined in the context will be replaced by the mock,
    // if no existing bean is defined a new one will be added.
    @MockBean
    private RestTemplate template;

    private static final Logger logger = LoggerFactory.getLogger(MangaServiceUnitTest.class);

    @Test
    public void testGetMangasByTitle() throws IOException
    {
        logger.info("[[ CI_CD_Tool ]] testGetMangasByTitle() in");

        // Parsing mock file
        MangaResult mRs = JsonUtils.jsonFile2Object("Victor.json", MangaResult.class);

        // Mock template 对象（输入的两个任何参数，都返回来自自定义文件的，固定的JSON结果对象）
        when(template.getForEntity(any(String.class), any(Class.class))).thenReturn(new ResponseEntity(mRs, HttpStatus.OK));

        // I search for goku but system will use mocked response containing only ken, so I can check that mock is used.
        List<Manga> mangasByTitle = mangaService.getMangasByTitle("goku");

        assertThat(mangasByTitle).isNotNull()
                                 .isNotEmpty()
                                 .anyMatch(p -> p.getTitle()
                                 .toLowerCase()
                                 .contains("manga"));

        logger.info("[[ CI_CD_Tool ]] testGetMangasByTitle() out");

    }

}

package com.example.service.test.unit;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.example.model.MangaResult;
import com.example.model.Title;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.controller.MangaController;
import com.example.model.Manga;
import com.example.service.MangaService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MangaControllerUnitTest
{

    MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    MangaController mangaController;

    @MockBean
    MangaService mangaService;
    
    /**
     * List of samples mangas
     */
    private List<Manga> mangas;

    private MangaResult mangaResult;

    private static final Logger logger = LoggerFactory.getLogger(MangaControllerUnitTest.class);

    @Before
    public void setup() throws Exception
    {
        logger.info("[[ CI_CD_Tool ]] setup() in");

        //构造mvc mock对象，让controller在模拟的容器环境中执行
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.mangaController).build();//使用内置容器独立运行
        //this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); //使用外部运行的web容器运行

        //构造两个MANGA对象
        List<Title> titles = new ArrayList<Title>();
        titles.add(Title.builder().title("Hokuto no ken").type("english").build());
        Manga manga1 = Manga.builder()
                            .titles(titles)
                            .synopsis("The year is 199X. The Earth has been devastated by nuclear war...")
                            .build();

        titles = new ArrayList<Title>();
        titles.add(Title.builder().title("Yumekui Kenbun").type("english").build());
        Manga manga2 = Manga.builder()
                            .titles(titles)
                            .synopsis("For those who suffer nightmares, help awaits at the Ginseikan Tea House, "
                                    + "where patrons can order much more than just Darjeeling. "
                                    + "Hiruko is a special kind of a private investigator. He's a dream eater....")
                            .build();

        mangas = new ArrayList<>();
        mangas.add(manga1);
        mangas.add(manga2);

        mangaResult = MangaResult.builder().data(mangas).build();

        logger.info("[[ CI_CD_Tool ]] setup() out");

    }

    @Test
    public void testSearchSync() throws Exception
    {
        logger.info("[[ CI_CD_Tool ]] testSearchSync() in");

        //单元测试专注于验证Controller.getMangasByTitle()方法自己的逻辑，把它依赖的MangaService方法的调用过程利用mock对象模拟，保证总是能返回期望的结果
        when(mangaService.getMangasByTitle(any(String.class))).thenReturn(mangas);

        //实际测试的是MangaController的SearchSync()的逻辑是否正确，本例子比较特殊，只有一行代码需要被测试，就是调用service执行远程方法调用，利用MOCK对象模拟，
        //实际上单元测试需要测试的是除被MOCK方法以外的，属于本方法自己的逻辑实现，
        mockMvc.perform(get("/manga/sync/ken").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titles[0].title", is("Hokuto no ken")))
                .andExpect(jsonPath("$[1].titles[0].title", is("Yumekui Kenbun")));

        logger.info("[[ CI_CD_Tool ]] testSearchSync() out");
    }

    @Test
    public void testSearchASync() throws Exception
    {

        logger.info("[[ CI_CD_Tool ]] testSearchASync() in");

        // Mocking service （输入任意字符串，返回预先定义的MANGA列表对象）
        when(mangaService.getMangasByTitle(any(String.class))).thenReturn(mangas);

        //异常步调用将返回一个future对象
        MvcResult result = mockMvc.perform(get("/manga/async/ken").contentType(MediaType.APPLICATION_JSON))
                                  .andDo(print())
                                  .andExpect(request().asyncStarted())
                                  .andDo(print())
                                  // .andExpect(status().is2xxSuccessful()).andReturn();
                                  .andReturn();

        // result.getRequest().getAsyncContext().setTimeout(10000);

        mockMvc.perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titles[0].title", is("Hokuto no ken")));

        logger.info("[[ CI_CD_Tool ]] testSearchASync() out");

    }
}

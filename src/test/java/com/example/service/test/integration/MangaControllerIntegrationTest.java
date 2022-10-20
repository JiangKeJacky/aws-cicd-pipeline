package com.mgiglione.service.test.integration;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.mgiglione.controller.MangaController;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MangaControllerIntegrationTest
{

    // @Autowired
    MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    MangaController mangaController;


    private static final Logger logger = LoggerFactory.getLogger(MangaControllerIntegrationTest.class);

    @Before
    public void setup() throws Exception
    {
        logger.info("[[ CI_CD_Tool ]] setup() in");

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.mangaController).build();//使用内置容器独立运行
        //this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); //使用外部运行的web容器运行

        logger.info("[[ CI_CD_Tool ]] setup() out");
    }

    @Test
    public void testSearchSync() throws Exception
    {
        logger.info("[[ CI_CD_Tool ]] testSearchSync() in");

        //集成测试，客户端利用mock类模拟了对本地web容器的访问，然后通过MangaController类的同步业务方法实现了对真实第三方web服务的调用，
        //直接可以获取真实的返回结果
        mockMvc.perform(get("/manga/sync/ken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..title", hasItem(is("Sun-Ken Rock"))));

        //下面是一个会执行失败的测试用例（条件判断返回假）
//        mockMvc.perform(get("/manga/sync/ken")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.*.title", hasItem(is("Meng Victor is not exist.")))); //条件为假的失败用例

        logger.info("[[ CI_CD_Tool ]] testSearchSync() out");
    }

    @Test
    public void testSearchASync() throws Exception
    {
        logger.info("[[ CI_CD_Tool ]] testSearchASync() in");

        //集成测试，客户端利用mock类模拟了对本地web容器的访问，然后通过MangaController类的异步业务方法实现了对真实第三方web服务的调用，
        //获得的是可以获取最终结果的对象接口
        MvcResult result = mockMvc.perform(get("/manga/async/ken")
                                  .contentType(MediaType.APPLICATION_JSON))
                                  .andDo(print())
                                  .andExpect(request().asyncStarted())
                                  .andDo(print())
                                  .andReturn();

        //等待并最终获取异步操作返回的结果
        mockMvc.perform(asyncDispatch(result))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$..title", hasItem(is("Kenma Kensou Kensei Kenbu"))));

        //下面是一个会执行失败的测试用例（条件返回假）
//        mockMvc.perform(asyncDispatch(result))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.*.title", hasItem(is("Meng Victor is not exist.")))); //条件为假的失败用例

        logger.info("[[ CI_CD_Tool ]] testSearchASync() out");

    }



}

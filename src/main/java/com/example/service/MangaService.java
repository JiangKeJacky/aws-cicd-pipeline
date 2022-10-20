package com.mgiglione.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mgiglione.model.Manga;
import com.mgiglione.model.MangaResult;

@Service
public class MangaService
{

    private static final Logger logger = LoggerFactory.getLogger(MangaService.class);

    //互联网上现存的API服务
    private static final String MANGA_SEARCH_URL="https://api.jikan.moe/v4/search/manga?q=";

    @Autowired
    RestTemplate restTemplate;
    
    //远程调研第三方服务的restAPI方法
    public List<Manga> getMangasByTitle(String title)
    {
        logger.info("[[ CI_CD_Tool ]] getMangasByTitle()， "
                  + "url = " + MANGA_SEARCH_URL + title);

        return restTemplate.getForEntity(MANGA_SEARCH_URL + title, MangaResult.class).getBody().getResults();
    }

}

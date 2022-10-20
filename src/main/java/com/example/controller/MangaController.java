package com.mgiglione.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mgiglione.model.Manga;
import com.mgiglione.service.MangaService;

@RestController
@RequestMapping(value = "/manga")
public class MangaController
{

    private static final Logger logger = LoggerFactory.getLogger(MangaController.class);

    @Autowired
    private MangaService mangaService;   

    @RequestMapping(value = "/async/{title}", method = RequestMethod.GET)
    @Async
    public @ResponseBody CompletableFuture<List<Manga>> searchASync(@PathVariable(name = "title") String title)
    {
        logger.info("[[ CI_CD_Tool ]] searchASync() in");
        return CompletableFuture.completedFuture(mangaService.getMangasByTitle(title));
    }

    @RequestMapping(value = "/sync/{title}", method = RequestMethod.GET)
    public @ResponseBody List<Manga> searchSync(@PathVariable(name = "title") String title)
    {
        logger.info("[[ CI_CD_Tool ]] searchSync() in");
        return mangaService.getMangasByTitle(title);
    }

    @RequestMapping(value = "/helloworld", method = RequestMethod.GET)
    public @ResponseBody String sayHello()
    {
        return "Hello world!";
    }


}

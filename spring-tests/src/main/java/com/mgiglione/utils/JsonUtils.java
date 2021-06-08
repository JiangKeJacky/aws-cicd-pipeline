package com.mgiglione.utils;

import java.io.File;
import java.io.IOException;

import com.mgiglione.model.MangaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
 
/**
 *
 * 工具类方法，完成Json字符串和JAVA对象之间的转化
 *   
 */
public class JsonUtils
{
	
	//工具类，构造方法私有化
	private JsonUtils()
	{
	}

	private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

	public static <T> T json2Object(String str, Class<T> clazz) throws IOException
	{
		logger.info("com.mgiglione.utils.JsonUtils.json2Object() "
				  + "str = " + str + ", class = " + clazz.getName());

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		return objectMapper.readValue(str, clazz);
	}

	public static <T> String object2Json(T obj) throws IOException
	{
		logger.info("[[ CI_CD_Tool ]] object2Json() obj "
				  + "class = " + obj.getClass().getName() );

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		return objectMapper.writeValueAsString(obj);
	}

	public static <T> T jsonFile2Object(String fileName, Class<T> clazz) throws IOException
	{
		logger.info("[[ CI_CD_Tool ]] jsonFile2Object() "
				  + "fileName = " + fileName + ", class = " + clazz.getName());

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		//Ignoring missing fields in model objects
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		return objectMapper.readValue(new File(concatenate(fileName)), clazz);
	}

	//需要处理的JSON文件保存路径
	private static String concatenate(String fileName)
	{
		logger.info("[[ CI_CD_Tool ]] jsonFile2Object() "
				  + "fileName = " + "src/test/resources/"+fileName);

		return "src/test/resources/"+fileName;
	}

	public static void main(String[] args)
	{
		try
		{
			MangaResult mRs = JsonUtils.jsonFile2Object("Victor.json", MangaResult.class);
			System.out.print(mRs.getResults().size());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
}

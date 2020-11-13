package com.fsntest.restapi.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsntest.restapi.entity.Statistics;
import com.fsntest.restapi.repo.StatisticsJpaRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(StatisticsController.class)
class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatisticsJpaRepo statisticsJpaRepo;

    @Test
    public void findByStatisticsDateTest() throws Exception{
        String url = "/statistics/date/2020-11-10";
        mockMvc.perform(MockMvcRequestBuilders.get(url)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findByStatisticsDateErrorTest() throws Exception{
        String url = "/statistics/date/2020-11-1";
        mockMvc.perform(MockMvcRequestBuilders.get(url)).andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findByStatisticsDateErrorTest2() throws Exception{
        String url = "/statistics/date/2020-11-133";
        mockMvc.perform(MockMvcRequestBuilders.get(url)).andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findByStatisticsDateAndTime() throws Exception{
        String url = "/statistics/date/2020-11-10/time/10";
        mockMvc.perform(MockMvcRequestBuilders.get(url)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findByStatisticsDateAndTimeErrorTest() throws Exception{
        String url = "/statistics/date/2020-11-10/time/101";
        mockMvc.perform(MockMvcRequestBuilders.get(url)).andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findByStatisticsDateAndTimeErrorTest2() throws Exception{
        String url = "/statistics/date/200-11-1/time/10";
        mockMvc.perform(MockMvcRequestBuilders.get(url)).andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void saveStatisticsByFileTest() throws Exception{
        String url = "/statistics/upload/file";
        String content = "{\n" +
                "\t\"statisticsDate\" : \"2020-11-12\",\n" +
                "\t\"statisticsTime\" : 17,\n" +
                "\t\"requestCnt\" : 1,\n" +
                "\t\"responseCnt\" : 2,\n" +
                "\t\"clickCnt\" : 2\n" +
                "}";
        MockMultipartFile jsonFile = new MockMultipartFile("file", "statistics.json", "application/json", content.getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart(url).file(jsonFile)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void saveStatisticsByFileTest2() throws Exception{
        String url = "/statistics/upload/file";
        String content = "{\n" +
                "\t\"requestCnt\" : 1,\n" +
                "\t\"responseCnt\" : 2,\n" +
                "\t\"clickCnt\" : 2\n" +
                "}";
        MockMultipartFile jsonFile = new MockMultipartFile("file", "statistics.json", "application/json", content.getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart(url).file(jsonFile)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void saveStatisticsByFileErrorTest() throws Exception{
        String url = "/statistics/upload/file";
        String content = "{\n" +
                "\t\"d\" : \"2020-11-12\",\n" +
                "\t\"t\" : 17,\n" +
                "\t\"requestCnt\" : 1,\n" +
                "\t\"responseCnt\" : 2,\n" +
                "\t\"clickCnt\" : 2\n" +
                "}";
        MockMultipartFile jsonFile = new MockMultipartFile("file", "statistics.json", "application/json", content.getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart(url).file(jsonFile)).andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void saveStatisticsByFileErrorTest2() throws Exception{
        String url = "/statistics/upload/file";
        String content = "{\n" +
                "\t\"statisticsDate\" : \"2020-11-12\",\n" +
                "\t\"statisticsTime\" : 17,\n" +
                "\t\"requestCnt\" : 1,\n" +
                "\t\"responseCnt\" : 2,\n" +
                "\t\"clickCnt\" : 2\n" +
                "}";
        MockMultipartFile jsonFile = new MockMultipartFile("file", "statistics.txt", "application/json", content.getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart(url).file(jsonFile)).andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }


}
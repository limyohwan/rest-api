package com.fsntest.restapi.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsntest.restapi.entity.Statistics;
import com.fsntest.restapi.exception.ApiRequestException;
import com.fsntest.restapi.exception.ApiResponse;
import com.fsntest.restapi.repo.StatisticsJpaRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping(value = "/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsJpaRepo statisticsJpaRepo;

    //날짜로 통계조회
    @GetMapping(value ="/date/{statisticsDate}")
    public ApiResponse findByStatisticsDate(@PathVariable String statisticsDate, HttpServletRequest req){
        //날짜형식아닐시 에러
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            dateFormat.parse(statisticsDate);
        } catch (ParseException e) {
            throw new ApiRequestException("incorrect statisticsDate type yyyy-MM-dd");
        }
        if(statisticsDate.length() < 10){
            throw new ApiRequestException("incorrect statisticsDate type yyyy-MM-dd");
        }
        //통계리스트 조회후 합더하기
        List<Statistics> stList = statisticsJpaRepo.findByStatisticsDate(statisticsDate);
        Statistics st2;
        if(stList.size() > 0){
            int click = 0;
            int response = 0;
            int request = 0;
            for(Statistics st : stList){
                click = click + st.getClickCnt();
                response = response + st.getResponseCnt();
                request = request + st.getRequestCnt();
            }
            st2 = Statistics.builder().statisticsDate(statisticsDate).requestCnt(request).responseCnt(response).clickCnt(click).build();
        }else{
            st2 = Statistics.builder().statisticsDate(statisticsDate).requestCnt(0).responseCnt(0).clickCnt(0).build();
        }
        ApiResponse res = new ApiResponse(HttpStatus.OK, req.getRequestURI());
        res.getData().put("statistics", st2);

        return res;
    }

    //날짜와 시각으로 통계조회
    @GetMapping(value ="/date/{statisticsDate}/time/{statisticsTime}")
    public ApiResponse findByStatisticsDateAndTime(@PathVariable String statisticsDate, @PathVariable Integer statisticsTime, HttpServletRequest req){
        //날짜형식 아닐시 에러
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            dateFormat.parse(statisticsDate);
        } catch (ParseException e) {
            throw new ApiRequestException("incorrect statisticsDate type yyyy-MM-dd");
        }
        if(statisticsDate.length() < 10){
            throw new ApiRequestException("incorrect statisticsDate type yyyy-MM-dd");
        }
        //시각형식 아닐시 에러
        if(statisticsTime < 0 || statisticsTime >23){
            throw new ApiRequestException("incorrect statisticsTime type 0<= time < 24");
        }
        Statistics st = statisticsJpaRepo.findByStatisticsDateAndStatisticsTime(statisticsDate, statisticsTime);
        ApiResponse res = new ApiResponse(HttpStatus.OK, req.getRequestURI());
        res.getData().put("statistics", st);
        return res;
    }

    //json파일 업로드로 통계 업데이트
    @PostMapping(value="/upload/file")
    public ApiResponse saveStatisticsByFile(@RequestParam("file")MultipartFile file, HttpServletRequest req) throws IOException {
        //파일형식가져온후 json파일 아닐시 에러
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if(!ext.equals("json")){
            throw new ApiRequestException("not json file");
        }

        //json파일 통계객체로 파싱안 될 시 에러
        String fileStr = new String(file.getBytes());
        ObjectMapper mapper = new ObjectMapper();
        Statistics st;
        try{
            st = mapper.readValue(fileStr, Statistics.class);
        }catch(Exception e){
            throw new ApiRequestException("can not parse json file to object");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //날짜와 시각이 없을시 에러
        if(st.getStatisticsDate() == null && st.getStatisticsTime() == null) {
            //현재날짜, 시각 집어넣기
            Calendar cal = Calendar.getInstance();
            Statistics st2 = Statistics.builder().statisticsDate(dateFormat.format((cal.getTime()))).statisticsTime(cal.get(Calendar.HOUR_OF_DAY)).requestCnt(st.getRequestCnt()).responseCnt(st.getResponseCnt()).clickCnt(st.getClickCnt()).build();
            statisticsJpaRepo.save(st2);
        //날짜 또는 시각이 없을시 정확한 데이터요구 에러
        }else if(st.getStatisticsDate() == null || st.getStatisticsTime() == null){
            throw new ApiRequestException("incorrect statisticsDate and statistics time");
        //데이터 값이 있을시 받은데이터로 통계 업데이트
        }else{
            //날짜형식 에러
            try{
                dateFormat.setLenient(false);
                dateFormat.parse(st.getStatisticsDate());
            } catch (ParseException e) {
                throw new ApiRequestException("incorrect statisticsDate type yyyy-MM-dd");
            }
            if(st.getStatisticsDate().length() < 10){
                throw new ApiRequestException("incorrect statisticsDate type yyyy-MM-dd");
            }
            //시각형식 에러
            if(st.getStatisticsTime() < 0 || st.getStatisticsTime() >23){
                throw new ApiRequestException("incorrect statisticsTime type 0<= time < 24");
            }
            statisticsJpaRepo.save(st);
        }

        ApiResponse res = new ApiResponse(HttpStatus.OK, req.getRequestURI());
        return res;
    }

    /*type 1. request
           2. response
           3. click
     */
    /*@PostMapping(value="/type/{type}")
    public ApiResponse saveByType(@RequestBody Statistics statistics, @PathVariable String type, HttpServletRequest req){
        if(!(type.equals("request") || type.equals("response") || type.equals("click"))){
            throw new ApiRequestException("incorrect function type. type can be request or response or click.");
        }
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            dateFormat.parse(statistics.getStatisticsDate());
        } catch (ParseException e) {
            throw new ApiRequestException("incorrect statisticsDate type yyyy-MM-dd");
        }
        if(statistics.getStatisticsDate().length() < 10){
            throw new ApiRequestException("incorrect statisticsDate type yyyy-MM-dd");
        }
        if(statistics.getStatisticsTime() < 0 || statistics.getStatisticsTime() >23){
            throw new ApiRequestException("incorrect statisticsTime type 0<= time < 24");
        }
        Statistics st = statisticsJpaRepo.findByStatisticsDateAndStatisticsTime(statistics.getStatisticsDate(), statistics.getStatisticsTime());
        if(st == null){
            if(type.equals("request")){
                st = Statistics.builder().statisticsDate(statistics.getStatisticsDate()).statisticsTime(statistics.getStatisticsTime()).clickCnt(0).responseCnt(0).requestCnt(1).build();
            }else if(type.equals("response")){
                st = Statistics.builder().statisticsDate(statistics.getStatisticsDate()).statisticsTime(statistics.getStatisticsTime()).clickCnt(0).responseCnt(1).requestCnt(0).build();
            }else if(type.equals("click")){
                st = Statistics.builder().statisticsDate(statistics.getStatisticsDate()).statisticsTime(statistics.getStatisticsTime()).clickCnt(1).responseCnt(0).requestCnt(0).build();
            }
        }else{
            if(type.equals("request")){
                st = Statistics.builder().statisticsDate(statistics.getStatisticsDate()).statisticsTime(statistics.getStatisticsTime()).requestCnt(st.getRequestCnt() + 1).responseCnt(st.getResponseCnt()).clickCnt(st.getClickCnt()).build();
            }else if(type.equals("response")){
                st = Statistics.builder().statisticsDate(statistics.getStatisticsDate()).statisticsTime(statistics.getStatisticsTime()).requestCnt(st.getRequestCnt()).responseCnt(st.getResponseCnt() + 1).clickCnt(st.getClickCnt()).build();
            }else if(type.equals("click")){
                st = Statistics.builder().statisticsDate(statistics.getStatisticsDate()).statisticsTime(statistics.getStatisticsTime()).requestCnt(st.getRequestCnt()).responseCnt(st.getResponseCnt()).clickCnt(st.getClickCnt() + 1).build();
            }
        }
        statisticsJpaRepo.save(st);
        ApiResponse res = new ApiResponse(HttpStatus.OK, req.getRequestURI());
        return res;
    }*/

}

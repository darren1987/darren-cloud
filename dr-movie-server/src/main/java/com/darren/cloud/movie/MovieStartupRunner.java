package com.darren.cloud.movie;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动执行
 *
 * @author darren.ouyang
 * @version 2018/8/8 17:46
 */
@Slf4j
@Component
public class MovieStartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
       log.info(">>>>>>>>>>>>>>>movie服务启动完成<<<<<<<<<<<<<");

       List<Integer> list = new ArrayList<>();
       for (int numb=0; numb<10000; numb++) {
           list.add(numb);
       }

       // demo 1
       list.forEach((i)->{
           bigObject1(i);
       });

       // demo 2
       /*int i = 0;
       while (true) {
           if ((++i) > 10000) {
               break;
           }

           bigObject1();
       }*/
    }

    public void bigObject1 (int i){
        List<Object> list = new ArrayList<>();
        while (true){
            list.add(new Object());
            if (list.size() + i > (Integer.MAX_VALUE/100)){
                break;
            }
        }
        System.out.println(i);
    }

    public void bigObject2 (int i){
        List<Integer> intList = new ArrayList<>();
        for (int numb=0; numb<(Integer.MAX_VALUE/200); numb++){
            intList.add(numb);
        }

        List<Integer> list = intList.stream().map((value)->{
            return value + i;
        }).collect(Collectors.toList());

        if (list.size()>Integer.MAX_VALUE/1000){
            System.out.println(i + " - true");
        }
        else{
            System.out.println(i + " - false");
        }
    }

}
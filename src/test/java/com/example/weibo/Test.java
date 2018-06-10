package com.example.weibo;

import com.example.common.cache.RedisCache;
import com.example.weibo.dao.UserHandler;
import com.example.weibo.vo.User;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config.xml")
public class Test {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserHandler userHandler;

    volatile AtomicInteger val1 = new AtomicInteger(0);

    int val;


    @org.junit.Test
    public void test(){
        String setResult = redisCache.set("name", "xuebengang");
        String name = redisCache.get("name");
        System.out.println(setResult + "   "+name);
    }

    @org.junit.Test
    public void globalValTest(){
//        for (int j = 0; j < 100; j++) {
//            ((Runnable)() -> System.out.println(globalIncrVal.getAndIncrement())).run();
//        }

//        for (int i = 0; i < 100; i++) {
//            Thread thread = new Thread(()->val++);
//            thread.setName(String.valueOf(i));
//            thread.start();
//            System.out.println(thread.getName() + "  "+ val);
//        }

//        for (int i = 0; i < 100; i++) {
//            Thread thread = new Thread(val1::incrementAndGet);
//            thread.setName(String.valueOf(i));
//            thread.start();
//            System.out.println(thread.getName() + "  "+ val1.get());
//        }

//        Thread t1 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 100; i++) {
//                    val1.incrementAndGet();
//                    System.out.println(Thread.currentThread().getName()+ " "+val1.get());
//                }
//            }
//        });
//        Thread t2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 100; i++) {
//                    val1.incrementAndGet();
//                    System.out.println(Thread.currentThread().getName()+" " + val1.get());
//                }
//            }
//        });
//        t1.start();
//        t2.start();

        //最后值不会变
        for (int i = 0; i < 99; i++) {
            new Thread("" + i){
                @Override
                public void run(){
                    System.out.println("Thread: " + getName() + "running:   " + val1.getAndIncrement());
                }
            }.start();
        }
    }

    @org.junit.Test
    public void newUserTest(){
        Set<User> concerns = userHandler.concerns("1");
        System.out.println(concerns);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread("" + i){
                @Override
                public void run(){
                    System.out.println("Thread: " + getName() + "running");
                }
            }.start();
        }
    }

}

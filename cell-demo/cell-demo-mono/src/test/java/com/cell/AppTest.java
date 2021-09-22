package com.cell;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    @Test
    public void testFlux1()
    {
        Flux<String> flux = Flux.fromIterable(Arrays.asList("1", "2", "3"));
        Flux<String> combinations =
                flux.flatMap(id ->
                {
                    Mono<String> nameTask = Mono.just(id);
                    Mono<Integer> statTask = Mono.just(Integer.parseInt(id));

                    return nameTask.zipWith(statTask,
                            (name, stat) -> "Name " + name + " has stats " + stat);
                });

        Mono<List<String>> result = combinations.collectList();

        List<String> results = result.block();
        System.out.println(results);
    }

    @Test
    public void testMono()
    {
        Disposable subscribe = Mono.just(1).doOnError((e) -> System.out.println(e)).subscribe();
    }


    @Test
    public void testSubscribe()
    {
        Flux<Integer> range = Flux.range(1, 3);
        range.subscribe();
    }

    // 处理输入值
    @Test
    public void testSub2()
    {
        Flux<Integer> range = Flux.range(1, 3);
        range.subscribe(System.out::println);
    }

    // 处理输入值,并且异常的时候捕获处理
    @Test
    public void testSub3()
    {
        Flux<Integer> flux = Flux.range(1, 3)
                .map(v ->
                {
                    if (v < 2)
                    {
                        return v;
                    }
                    throw new RuntimeException("asd");
                });
        flux.subscribe((v) -> System.out.println(v), (e) -> System.out.println(e));
    }

    // 处理输入值+ 异常处理+ 结束通知
    @Test
    public void testSub4()
    {
        Flux<Integer> flux = Flux.range(1, 3);
        flux.map(v ->
        {
            return v;
        }).subscribe(v -> System.out.println(v), e -> System.out.println("err:" + e), () -> System.out.println("done"));
    }

    @Test
    public void testSub5()
    {
        Flux<Integer> flux = Flux.range(1, 3);
        flux.subscribe(v ->
        {
            System.out.println(v);
        }, e -> System.out.println("err:" + e), () -> System.out.println("done"), s -> s.request(2));
    }

    class MySubscriber extends BaseSubscriber<Integer>
    {
        @Override
        protected void hookOnNext(Integer value)
        {
            System.out.println(value);
        }

        @Override
        protected void hookOnSubscribe(Subscription subscription)
        {
            System.out.println("subscribe,限流1个");
            request(1);
        }
    }

    @Test
    public void testBaseSubscriper()
    {
        Flux<Integer> flux = Flux.range(1, 3);
        flux.subscribe(new MySubscriber());
    }

    @Test
    public void testFlux()
    {
        Flux.generate(() -> 1, (state, sink) ->
        {
            System.out.println(state);
            if (state == 10)
            {
                sink.complete();
            }
            sink.next("asdddd");
            return state + 1 + 2;
        }).doOnNext((v) -> System.out.println(v)).subscribe();
    }

    @Test
    public void testFlux2()
    {
        Flux<String> flux = Flux.generate(
                AtomicLong::new,
                (state, sink) ->
                {
                    long i = state.getAndIncrement();
                    sink.next("3 x " + i + " = " + 3 * i);
                    if (i == 10) sink.complete();
                    return state;
                }, (state) -> System.out.println("state: " + state));
    }

    @Test
    public void testHandler()
    {
        Flux<Integer> flux = Flux.just(-1, 30, 13, 9, 20);
        Flux<Object> handle = flux.handle((v, sink) ->
        {
            String letter = alphabet(v);
            if (letter != null)
            {
                sink.next(letter);
            }
        });
        handle.subscribe(v -> System.out.println(v));
    }

    public String alphabet(int letterNumber)
    {
        if (letterNumber < 1 || letterNumber > 26)
        {
            return null;
        }
        int letterIndexAscii = 'A' + letterNumber - 1;
        return "" + (char) letterIndexAscii;
    }

    @Test
    public void testMonoThread() throws Exception
    {
        Mono<String> hello = Mono.just("hello");

        Thread thread = new Thread(() ->
        {
            hello.map(v -> v + " thread ")
                    .subscribe(v -> System.out.println(v + Thread.currentThread().getName()));
        });
        thread.start();
        thread.join();
    }

    // 测试调度器
    @Test
    public void testSchekdual() throws Exception
    {
        Scheduler s = Schedulers.newParallel("test", 4);
        Flux<Integer> flux = Flux.range(1, 3);
        Flux<String> stringFlux = flux.map(v ->
        {
            System.out.println("currentThread:" + Thread.currentThread().getName());
            try
            {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e)
            {

            }
            return v + 10;
        }).publishOn(s)
                .map(v ->
                {
                    System.out.println("currentThread:" + Thread.currentThread().getName());
                    return "value:" + v;
                });
        Thread thread = new Thread(() -> stringFlux.subscribe(v -> System.out.println("currentThread:" + Thread.currentThread().getName() + "," + v)));
        thread.start();
        thread.join();
    }


    @Test
    public void testInterval() throws Exception
    {
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(2));
        Flux<String> asd = interval.map(v ->
        {
            v++;
            return v + "";
//            if (v <= 3)
//            {
//                return v;
//            }
//            throw new RuntimeException("asd");
        }).onErrorReturn("0");
        asd.subscribe(System.out::println);
        System.out.println(1);
        TimeUnit.SECONDS.sleep(200);
    }
}


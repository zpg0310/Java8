package com.zpg.foreach01;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ForEachTest {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);

        /*  list.forEach(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println(integer);
            }
        });*/
        //上面的可以用下面的替换
        //jdk1.8提供的迭代 内部迭代 不依赖于索引 其余迭代都为外部迭代 依赖索引
        list.forEach(a -> System.out.println(a));

        //方法引用 后面介绍
        list.forEach(System.out::println);

    }
}

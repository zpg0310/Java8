#  JDK8

#### 1.Grande介绍

.Grandle/Maven:目录结构一样的，约定优于配置

源代码:src/main/java

配置文件:src/main/resources

测试代码：src/text/java

测试配置文件：src/text/resources

页面相关:src/main/webapp

setting.gradle:项目名字

build.gradle:项目的描述，类似于pom.xml，包括项目所依赖的信息

```
sourceCompatibility = 1.8//源代码兼容性
targetCompatibility = 1.8//编译后代码兼容性
```

```
dependencies {
//    testCompile group: 'junit', name: 'junit', version: '4.12'
    testCompile(
            "junit:junit:4.11"
    )
}
```

#### 2.Lambda表达式基本格式

(param1,param2,param3)->{

}

```java
package com.tang.jdk8;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingTest {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("My JFrame");
        JButton jButton = new JButton("My JButton");

        //按按钮之后会自动调用方法
        //java为静态类型语言，e为类型推断后结果，
        jButton.addActionListener(e -> System.out.println("Button Pressed！"));
        jFrame.add(jButton);
        //正好是主键的大小
        jFrame.pack();
        jFrame.setVisible(true);
        //关闭的时候整个程序退出
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
```

#### 3.@FunctionalInterface	

```
package java.util.function;
```

```java
@FunctionalInterface
/*An informative annotation type used to indicate that an interface
 * type declaration is intended to be a <i>functional interface</i> as
 * defined by the Java Language Specification.
 ......
 
 *<p>Note that instances of functional interfaces can be created with
 * lambda expressions, method references, or constructor references.
 */
```

**1.函数式接口：如果一个接口只有一个抽象方法,那么他就是函数式接口。**

**2.如果我们在某个接口上声明了@FunctionalInterface注解，那么编译器就按照函数式接口的定义来要求该接口。**

**3.如果某个接口只有一个抽象方法,但我们没有给该接口声明，@FunctionalInterface注解，那么编译器依旧会将该接口看做函数式接口。**

```java
/*If an interface declares an abstract method overriding one of the
* public methods of {@code java.lang.Object}, that also does
* <em>not</em> count toward the interface's abstract method count
* since any implementation of the interface will have an
* implementation from {@code java.lang.Object} or elsewhere.
*/
```

```java
@FunctionalInterface
public interface MyInterface {
    void test();
    
    String isString();
}
```

==Multiple non-overriding abstract methods found in interface com.tang.jdk8.MyInterface==

```java
@FunctionalInterface
public interface MyInterface {
    void test(); 
    
    @Override
    String toString();
}
```

====toString 为Object类下的public方法，所以注解不认为他会一个新的抽象方法，因为任何接口都要直接或者间接继承Object类,继承里面的所有方法，由Object去去实现这个方法，而子类只实现一个方法==

```java
@FunctionalInterface
interface MyInterface {
    void test();

    @Override
    String toString();
}
public class Test2{
    public void myTest(MyInterface myInterface){
        System.out.println(1);
        myInterface.test();
        System.out.println(2);
    }

    public static void main(String[] args) {
        Test2 test2 = new Test2();

        test2.myTest(()-> System.out.println("mytest"));

        MyInterface myInterface=()-> System.out.println("hello");
        System.out.println(myInterface.getClass());
        System.out.println(myInterface.getClass().getSuperclass());
        System.out.println(myInterface.getClass().getInterfaces().length);
        System.out.println(myInterface.getClass().getInterfaces()[0]);
    }
}
1
mytest
2
class com.tang.jdk8.Test2$$Lambda$2/1078694789
class java.lang.Object 
1
interface com.tang.jdk8.MyInterface
```

#### 4.forEach

```
List extends Collection extends Iterable
```

```java
public interface Iterable<T> {
 
    Iterator<T> iterator();
    /* Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception.  Unless otherwise specified by the implementing class,
     * actions are performed in the order of iteration (if an iteration order
     * is specified).  Exceptions thrown by the action are relayed to the
     * caller.
     * ...
     */
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }
    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
```

所以list继承了Iterable接口，接口里有默认方法forEach

函数式接口加入，保证了向下兼容

#### 5.Comsumer

```java
package java.util.function;

import java.util.Objects;

/**
 * Represents an operation that accepts a single input argument and returns no
 * result. Unlike most other functional interfaces, {@code Consumer} is expected
 * to operate via side-effects.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Object)}.
 *
 * @param <T> the type of the input to the operation
 *
 * @since 1.8
 */
@FunctionalInterface
public interface Consumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t);

    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code Consumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default Consumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }
}
```



在将函数作为一等公民的语言中，Lambda表达式是函数(Python)。但在java中，Lambda表达式是对象（如MyInterface myInterface=()-> System.out.println("hello");），这就是把Lamada表达式赋值给一个对象），他们必须依附于一类特别的对象类型——函数式接口。

```java
@FunctionalInterface
interface TheInterface {

    void myMethod();
}
@FunctionalInterface
interface TheInterface2{

    void myMetho=d2();
}
```

```java
TheInterface i1=()->{ };
System.out.println(i1.getClass().getInterfaces()[0]);
TheInterface2 i2 =()->{};
System.out.println(i2.getClass().getInterfaces()[0]);
```

==()->{}是Lamada表达式，它必须有上下文，它只关心方法参数和返回类型（虽然方法名字实现对于接口很关键，但是对于Labmda表达式并不担心方法名字是什么）==

#### 6..方法引用（粗略）

通过方法引用的形式创建Lambda接口的实例 

```java
list.stream().map(String::toUpperCase).forEach(System.out::println);
```

stream是单线程完成的，parallelStream是由多线程完成的。

还可以分为中间流和结点流，中间流可以连续操作，结点流得到最终结果，不能再被操作 

```java
public String toUpperCase() {
    return toUpperCase(Locale.getDefault());
}
```

==调用toUpperCase方法的当前对象作为输入（所有实例的实例方法，第一个参数都是隐式的this，也就是stream流中的每一个String对象）==

==输出为执行完toUpperCase方法后的对象（也就是上面代码return的部分）==

#### 7..Function	

```java
Function<String,String> function=String::toUpperCase;
```

==不能写成String.toupperCase,因为toUpperCase不是静态方法==

==**如5所讲，一定会存在一个String的实例对象(假设为str)，去调用toUpperCase==**

**总结：如果一个类类型，直接通过 :: 引用实例方法，对应Lambda表达式的第一个参数,就是调用这个方法的对象（this）**

```java
package java.util.function;

import java.util.Objects;

/**
 * Represents a function that accepts one argument and produces a result.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object)}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 *
 * @since 1.8
 */
@FunctionalInterface
public interface Function<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    R apply(T t);

    /**
     * Returns a composed function that first applies the {@code before}
     * function to its input, and then applies this function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V> the type of input to the {@code before} function, and to the
     *           composed function
     * @param before the function to apply before this function is applied
     * @return a composed function that first applies the {@code before}
     * function and then applies this function
     * @throws NullPointerException if before is null
     *
     * @see #andThen(Function)
     */
    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V> the type of output of the {@code after} function, and of the
     *           composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     *
     * @see #compose(Function)
     */
    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T> the type of the input and output objects to the function
     * @return a function that always returns its input argument
     */
    static <T> Function<T, T> identity() {
        return t -> t;
    }
}
```

 

```java
public class FunctionTest {
    public static void main(String[] args) {
        FunctionTest functionTest=new FunctionTest();
        System.out.println(functionTest.compute(10, val->2*val));
        System.out.println(functionTest.convert( 10, val->String.valueOf(val+"你好")));
        System.out.println(functionTest.convert( 10, val->Integer.toString(val)+"hello"));
     	 System.out.println(functionTest.method1(10));
        
    }
    public int compute(int a, Function<Integer,Integer> function){
        int result = function.apply(a);
        return result;
    }
    public String convert(int a, Function<Integer,String> function){
        return function.apply(a);
    }
    public int method1(int a){
        return a*a;
    }
}
```

==Integer -->String  Integer.toString(a);  String.valueOf==

**结论：Lambda表达式传递的是行为，以前是行为提前定义好，调用行为**

```java
default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
    Objects.requireNonNull(before);
    return (V v) -> apply(before.apply(v));
}
```

```java
default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (T t) -> after.apply(apply(t));
}
```

**区别:**

**compose :先调用参数接口执行apply()方法，得到的结果作为参数再执行apply()方法**

**andThen:先调用本接口内的apply()方法，得到的接口再调用参数的apply接口的apply()方法**

```java
public class FunctionTest2 {
    public static void main(String[] args) {
        FunctionTest2 functionTest2 = new FunctionTest2();
        //4
        int compute = functionTest2.compute(1, val -> val * val, val -> val + 1);
        System.out.println("compute = " + compute);
        //2
        int compute2 = functionTest2.compute2(1, val -> val * val, val -> val + 1);
        System.out.println("compute2 = " + compute2);
    }
    public int compute(int a,Function<Integer,Integer> function1,Function<Integer,Integer> function2){
        return function1.compose(function2).apply(a);
    }
    public int compute2(int a,Function<Integer,Integer> function1,Function<Integer,Integer> function2){
        return function1.andThen(function2).apply(a);
    }
}
compute = 4
compute2 = 2
```

先执行function2.apply()再把得到的结果作为参数执行function1.apply()

##### 7.1高阶函数

如果一个函数接收一个函数作为参数，或者返回一个函数作为返回值，那么该函数称为高阶函数。

拓展：BiFuction

```java
@FunctionalInterface
public interface BiFunction<T, U, R> {

    R apply(T t, U u);

    default <V> BiFunction<T, U, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t, U u) -> after.apply(apply(t, u));
    }
}
```

```java
public static int compute4(int a, int b, BiFunction<Integer,Integer,Integer> biFunction,Function<Integer,Integer> function){
    return biFunction.andThen(function).apply(a,b);
}
```

```java
System.out.println(compute4(10, 20, (val1, val2) -> val1 + val2, val -> val * val));
```

输出：900



```java
public List<Person> getPersonByAge2(int age,List<Person> peoples,BiFunction<Integer,List<Person>,List<Person>> biFunction){
    return biFunction.apply(age, peoples);
}
```

```java
List<Person> personByAge2 = personTest.getPersonByAge2(20, peoples, (ageOfPerson, personList) -> personList.stream().filter(p -> p.getAge() > 20).collect(Collectors.toList()));
personByAge2.forEach(p-> System.out.println(p.getAge()));
```

21
23

#### 8.Predicate

```java
public class PredicateTest {
    public static void main(String[] args) {
        Predicate<String> predicate=p->p.length()>5;
        System.out.println(predicate.test("hello"));
    }
}
```

false

```java
package java.util.function;

import java.util.Objects;

/**
 * Represents a predicate (boolean-valued function) of one argument.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #test(Object)}.
 *
 * @param <T> the type of the input to the predicate
 *
 * @since 1.8
 */
@FunctionalInterface
public interface Predicate<T> {

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param t the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    boolean test(T t);

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * AND of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is {@code false}, then the {@code other}
     * predicate is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ANDed with this
     *              predicate
     * @return a composed predicate that represents the short-circuiting logical
     * AND of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    default Predicate<T> and(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) && other.test(t);
    }

    /**
     * Returns a predicate that represents the logical negation of this
     * predicate.
     *
     * @return a predicate that represents the logical negation of this
     * predicate
     */
    default Predicate<T> negate() {
        return (t) -> !test(t);
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * OR of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is {@code true}, then the {@code other}
     * predicate is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ORed with this
     *              predicate
     * @return a composed predicate that represents the short-circuiting logical
     * OR of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    default Predicate<T> or(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) || other.test(t);
    }

    /**
     * Returns a predicate that tests if two arguments are equal according
     * to {@link Objects#equals(Object, Object)}.
     *
     * @param <T> the type of arguments to the predicate
     * @param targetRef the object reference with which to compare for equality,
     *               which may be {@code null}
     * @return a predicate that tests if two arguments are equal according
     * to {@link Objects#equals(Object, Object)}
     */
    //判断两个参数是否相等
    static <T> Predicate<T> isEqual(Object targetRef) {
        return (null == targetRef)
                ? Objects::isNull//返回相同签名即可，满足输入一个变量，返回boolean。编译器会认为这是一个predicate的实现
                : object -> targetRef.equals(object);
    }
}
```

```java
public static boolean isNull(Object obj) {
    return obj == null;
}
```

```java
public boolean equals(Object obj) {
    return (this == obj);
}
```



##### 8.1面向对象和面向函数

**面向对象传递一个参数（对象），方法体里面定义行为，处理业务逻辑**

**函数式编程传递两个参数（对象+函数式接口），具体行为被调用者定义，在方法体里面没有具体表现（提供了更高层次的抽象化)**

```java
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PredicateTest2 {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        PredicateTest2 predicateTest2=new PredicateTest2();
        predicateTest2.conditionalFileter(list, num->num%2!=0);
        System.out.println();
        predicateTest2.conditionalFileter(list, num->num%2==0);
        System.out.println();
        predicateTest2.conditionalFileter(list, num->num>5);
        System.out.println();
        predicateTest2.conditionalFileter(list, num->num<3);
        predicateTest2.conditionalFileter(list, num->true);
        System.out.println();
        predicateTest2.conditionalFileter2(list, num->num>5,num->num%2==0);
        System.out.println();
        predicateTest2.conditionalFileter3(list, num->num>5,num->num%2==0);
        boolean test = predicateTest2.isEqual(new Date()).test(new Date());
        System.out.println(test);

    }

    //通用方法，具体行为使用者调用时自己定义并传递进去
    public void conditionalFileter(List<Integer> list, Predicate<Integer> predicate){
        for (Integer i : list) {
            if (predicate.test(i)){
                System.out.print(i+" ");
            }
        }
    }

    public void conditionalFileter2(List<Integer> list, Predicate<Integer> predicate1,Predicate<Integer> predicate2){
        for (Integer integer : list) {
            if (predicate1.and(predicate2).negate().test(integer)){
                System.out.print(integer+"");
            }
        }
    }
    public void conditionalFileter3(List<Integer> list, Predicate<Integer> predicate1,Predicate<Integer> predicate2){
        for (Integer integer : list) {
            if (predicate1.or(predicate2).test(integer)){
                System.out.print(integer+"");
            }
        }
    }

    public Predicate<Date> isEqual(Object object){
        return Predicate.isEqual(object);
    }
}
```

1 3 5 7 9 
2 4 6 8 10 
6 7 8 9 10 
1 2 1 2 3 4 5 6 7 8 9 10 
1234579
24678910false



#### 9.Supplier

```java
package java.util.function;

/**
 * Represents a supplier of results.
 *
 * <p>There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #get()}.
 *
 * @param <T> the type of results supplied by this supplier
 *
 * @since 1.8
 */
@FunctionalInterface
public interface Supplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get();
}
```



```java
public class Student {
    private String name="张三";
    private int age=20;
    //构造方法略
}
```

```java
public class StudentTest {
    public static void main(String[] args) {
        Supplier<Student> supplier=()->new Student();
        System.out.println(supplier.get().getName());
    }
}
```

张三



**BinaryOperator**  extends BiFunction

```java
package java.util.function;

import java.util.Objects;
import java.util.Comparator;

/**
 * Represents an operation upon two operands of the same type, producing a result
 * of the same type as the operands.  This is a specialization of
 * {@link BiFunction} for the case where the operands and the result are all of
 * the same type.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object, Object)}.
 *
 * @param <T> the type of the operands and result of the operator
 *
 * @see BiFunction
 * @see UnaryOperator
 * @since 1.8
 */
@FunctionalInterface
public interface BinaryOperator<T> extends BiFunction<T,T,T> {
    /**
     * Returns a {@link BinaryOperator} which returns the lesser of two elements
     * according to the specified {@code Comparator}.
     *
     * @param <T> the type of the input arguments of the comparator
     * @param comparator a {@code Comparator} for comparing the two values
     * @return a {@code BinaryOperator} which returns the lesser of its operands,
     *         according to the supplied {@code Comparator}
     * @throws NullPointerException if the argument is null
     */
    public static <T> BinaryOperator<T> minBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (a, b) -> comparator.compare(a, b) <= 0 ? a : b;
    }

    /**
     * Returns a {@link BinaryOperator} which returns the greater of two elements
     * according to the specified {@code Comparator}.
     *
     * @param <T> the type of the input arguments of the comparator
     * @param comparator a {@code Comparator} for comparing the two values
     * @return a {@code BinaryOperator} which returns the greater of its operands,
     *         according to the supplied {@code Comparator}
     * @throws NullPointerException if the argument is null
     */
    public static <T> BinaryOperator<T> maxBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (a, b) -> comparator.compare(a, b) >= 0 ? a : b;
    }
}
```



**用于没有输入参数的工厂**

```java
public class BinaryOperateorTest {
    public static void main(String[] args) {
        BinaryOperateorTest operateorTest = new BinaryOperateorTest();
        System.out.println( operateorTest.opearte(10, 20, (c,d)->c+d));
        System.out.println( operateorTest.opearte(10, 20, (c,d)->c-d));
        System.out.println( operateorTest.opearte(10, 20, (c,d)->c*d));
        System.out.println( operateorTest.opearte(10, 20, (c,d)->c+d));

        String aShort = operateorTest.getShort("tang", "yao", (a, b) -> a.length() -b.length());
        System.out.println("aShort = " + aShort);
        String aShort1 = operateorTest.getShort("tang", "yao", (a, b) -> a.getBytes()[0] - b.getBytes()[0]);
        System.out.println("aShort1 = " + aShort1);
    }
    public int opearte(int a,int b, BinaryOperator<Integer> binaryOperator){
       return binaryOperator.apply(a, b);
    }
    public String getShort(String a, String b, Comparator<String> comparator){
        return BinaryOperator.minBy(comparator).apply(a, b);
    }

}
30
-10
200
30
aShort = yao
aShort1 = tang
```



#### 10.Optional

NPE NullPointerException

```java
if(null!=person){
	Address address=new Address();
    if(null != address){
        ...
    }
}
```

使用Optional提高程序健壮性

```java
/**
 * A container object which may or may not contain a non-null value.
 * If a value is present, {@code isPresent()} will return {@code true} and
 * {@code get()} will return the value.
 *
 * <p>Additional methods that depend on the presence or absence of a contained
 * value are provided, such as {@link #orElse(java.lang.Object) orElse()}
 * (return a default value if value not present) and
 * {@link #ifPresent(java.util.function.Consumer) ifPresent()} (execute a block
 * of code if the value is present).
 *
 * <p>This is a <a href="../lang/doc-files/ValueBased.html">value-based</a>
 * class; use of identity-sensitive operations (including reference equality
 * ({@code ==}), identity hash code, or synchronization) on instances of
 * {@code Optional} may have unpredictable results and should be avoided.
 *
 * @since 1.8
 */
public final class Optional<T> {
    ...
}
```





创建Optional对象方法

```java
public static<T> Optional<T> empty() {
    @SuppressWarnings("unchecked")
    Optional<T> t = (Optional<T>) EMPTY;
    return t;
}
private static final Optional<?> EMPTY = new Optional<>(); private Optional() {
        this.value = null;
    }

```

```java
public static <T> Optional<T> of(T value) {
    return new Optional<>(value);
}
private Optional(T value) {
    this.value = Objects.requireNonNull(value);
}

public static <T> T requireNonNull(T obj) {
    if (obj == null)
        throw new NullPointerException();
    return obj;
}
```

```java
public static <T> Optional<T> ofNullable(T value) {
    return value == null ? empty() : of(value);
}
```

重要方法

```java
public boolean isPresent() {
    return value != null;
}
 public T get() {
    if (value == null) {
        throw new NoSuchElementException("No value present");
    }
    return value;
}
public T orElse(T other) {
    return value != null ? value : other;
}
public T orElseGet(Supplier<? extends T> other) {
    return value != null ? value : other.get();
}
```

```java
 //从数据库查出一个对象，不确定是否为空
Optional<String> optional=Optional.ofNullable("hello");
//        if (optional.isPresent()){
//            System.out.println(optional.get());
//        }
        optional.ifPresent(p-> System.out.println(p));
        System.out.println("------------");
        System.out.println(optional.orElse("world"));
        System.out.println("------------");
        System.out.println(optional.orElseGet(()->"你好"));

hello
------------
hello
------------
hello
```

```java
Optional<String> optional=Optional.empty();

------------
world
------------
你好

```

```java
public class Employee {
    private String name;

    public Employee() {
    }

    public Employee(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                '}';
    }
}
```

```java

import java.util.List;

public class Company {
    private String name;
    private List<Employee> employees;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
```

```
package com.tang.jdk8;

import java.util.*;

public class OptionalTest2 {
    public static void main(String[] args) {
        Employee employee1=new Employee();
        employee1.setName("zhangsan");
        Employee employee2=new Employee();
        employee2.setName("lisi");

        Company company = new Company();
        company.setName("company1");

        List<Employee> list= Arrays.asList(employee1,employee2);
//        company.setEmployees(list);
        Optional<Company> optional=Optional.ofNullable(company);
        optional.map(theCompany->theCompany.getEmployees()).orElse(Collections.emptyList());

    public void test(Optional optional){
		//Optional作为参数会出现问题
    }
}
```

Optional没有被序列化，做参数的时候会被警告

![1574091526764](C:\Users\10136\AppData\Roaming\Typora\typora-user-images\1574091526764.png)

#### 补充：Value-based Classes

Some classes, such as 

```java
java.util.Optional
```

 and 

```java
java.time.LocalDateTime
```

, are value-based. Instances of a value-based class:

- are final and immutable (though may contain references to mutable objects);
- have implementations of `equals`, `hashCode`, and `toString` which are computed solely from the instance's state and not from its identity or the state of any other object or variable;
- make no use of identity-sensitive operations such as reference equality (`==`) between instances, identity hash code of instances, or synchronization on an instances's intrinsic lock;
- are considered equal solely based on `equals()`, not based on reference equality (`==`);
- do not have accessible constructors, but are instead instantiated through factory methods which make no committment as to the identity of returned instances;
- are *freely substitutable* when equal, meaning that interchanging any two instances `x` and `y` that are equal according to `equals()` in any computation or method invocation should produce no visible change in behavior.

A program may produce unpredictable results if it attempts to distinguish two references to equal values of a value-based class, whether directly via reference equality or indirectly via an appeal to synchronization, identity hashing, serialization, or any other identity-sensitive mechanism. Use of such identity-sensitive operations on instances of value-based classes may have unpredictable effects and should be avoided.

#### 11.方法引用

方法引用实际上是Lambda表达式的语法糖 

我们可以将方法引用看做【函数指针】

**方法引用共分为四类**

**1.类名::静态方法名**

**2.引用名（对象名）::实例方法名**

**3.类名::实例方法名**

**4.构造方法引用：类名::new**

```java
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Student {
    private String name;
    private int score;
    
    //下面这两个静态方法虽然在语法角度都能够正确的编译和执行，但是从设计角度来说，这两个静态方法和Student没有任何关系，可以移除这个类，在这里是为了举 类名::静态方法名 这个例子
    public static int compareStudentScore(Student student1,Student student2){
        return student1.getScore()-student2.getScore();
    }
    public static int compareStudentName(Student student1,Student student2){
        return student1.getName().compareToIgnoreCase(student2.getName());
    }

   	//这么设计的好处：参数为传递进来待比较的Student类，比较对象是调用这个方法的Student实例的name或score
    public int compareByName(Student stu1){
        return this.getName().compareToIgnoreCase(stu1.getName());
    }

    public int compareByScore(Student stu1){
        return this.getScore()-stu1.getScore();
    }
    
}
```

```java
public class MethodRefencedTest {
    
    public String getString(Supplier<String> supplier){
        return supplier.get() + "test";
    }

    public String getString2(String str, Function<String,String> function){
        return function.apply(str);
    }

    public static void main(String[] args) {
        Student student1=new Student("zhangsan", 10);
        Student student2=new Student("lisi", 20);
        Student student3=new Student("wangwu", 30);
        Student student4=new Student("zhaoliu", 40);

        List<Student> students = Arrays.asList(student1, student2, student3, student4);
        //students.sort((stu1, stu2)->Student.compareStudentScore(stu1, stu2));
        //这里用了 1.类名::静态方法名 
        //Student::compareStudentScore和Student.compareStudentScore区别:，
        //前者可以类比为函数指针的形式，指向另外一个函数，不能传递任何参数，由运行时自动传进来，后者不能作为单独表达式存在，必须带（），里面有参数需要传递进去，表示方法调用的概念。
        students.sort(Student::compareStudentScore);
        students.forEach(student -> System.out.println(student.getScore()));
        //10
		//20
        //30
		//40
        //students.sort((stu1,stu2)->Student.compareStudentName(stu1,stu2));
        students.sort(Student::compareStudentName);
        students.forEach(student -> System.out.println(student.getName()));
        //lisi
		//wangwu
		//zhangsan
		//zhaoliu
        
        
		//这里用了 2.引用名（对象名）::实例方法名
        StudentComparator studentComparator = new StudentComparator();
        //students.sort((stu1, stu2)-> studentComparator.compareStudentByName(stu1, stu2));
        students.sort(studentComparator::compareStudentByName);
		students.forEach(System.out::println);
		//Student(name=lisi, score=20)
		//Student(name=wangwu, score=30)
		//Student(name=zhangsan, score=10)
		//Student(name=zhaoliu, score=40)

        
		
		//这里用了 3.类名::实例方法名
        //通过类名引用实例方法时，一定会有引用或者对象去调用这个方法，调用方法的对象是由lambda表达式第一个参数确定的，第一个参数后续所有参数，都是作为这个方法的调用参数传递进去的。对于compareByName这个方法来说，第一个参数调用这个方法的Student对象，第二个student则作为这个方法的第二个参数传进来的
//      students.sort((stu1,stu2)->stu1.compareByName(stu2));
        students.sort(Student::compareByName);
//      students.sort(Student::compareByScore);
        students.forEach(System.out::println);
        //Student(name=lisi, score=20)
		//Student(name=wangwu, score=30)
		//Student(name=zhangsan, score=10)
		//Student(name=zhaoliu, score=40)
        
        List<String> cities = Arrays.asList("tianjin", "beijing", "shagnhai", "guangzhou");
        Collections.sort(cities, (s, str) -> s.compareToIgnoreCase(str));
        Collections.sort(cities, String::compareToIgnoreCase);
        cities.forEach(System.out::println)
//          beijing
//			guangzhou
//			shagnhai
//			tianjin
            
        MethodRefencedTest methodRefencedTest=new MethodRefencedTest();
        System.out.println(methodRefencedTest.getString(() -> new String()));
        System.out.println(methodRefencedTest.getString(String::new));
        //test
        System.out.println(methodRefencedTest.getString2("hello", original -> new String(original)));
        System.out.println(methodRefencedTest.getString2("hello",String::new));
		//hello
    }
}
```

#### 12.System.out::println

```java
//引用名（对象名）::实例方法名
cities.forEach(System.out::println);
```

```java
public final class System {

    private static native void registerNatives();
	static {
        registerNatives();
    }
   	 public final static InputStream in = null;
    //out永远为空，找不到给out赋值的相应代码，真正给他赋值的是registerNatives();
    //静态代码块会在类加载的时候就会被执行，而它本身是native方法，由JNI调用，输入输出和硬件相关，由底层c语言完成，对其初始化
   
     public final static PrintStream out = null;
     public final static PrintStream err = null;
    ...
}
```



#### 13.默认方法

```java
public interface MyInterface1 {
    default void mythod(){
        System.out.println("MyInterface1");
    }
}
```

```java
public interface MyInterface2 {
    default void method(){
        System.out.println("MyInterface2");
    }
}
```

```java
public class MyClass implements MyInterface1,MyInterface2 {
    public static void main(String[] args) {
        MyClass myClass = new MyClass();
        myClass.mythod();
    }
    @Override
    public void method() {
        MyInterface2.super.mythod();
    }
}
```

实现了多个接口，如果默认方法名相同，则必须重写方法，如果想用其中一个方法，可以使用***接口名+super+默认方法名的方法***

```java
public class MyInterface1Impl implements MyInterface2 {

    @Override
    public void method() {
        System.out.println("MyClass2");
    }
}
```

```java
public class MyClass extends MyInterface1Impl implements MyInterface1 {
    public static void main(String[] args) {
        MyClass myClass = new MyClass();
        myClass.method();
    }

}
```

**java认为实现类比接口更为具体**，所以如果MyClass继承了MyInterface1Impl(重写了接口里面的method默认方法)和实现了Interface2，那么对于具有相同的默认方法，则**类MyClass优先调用继承类里面的方法。**

#### 14.为什么要有默认方法 

在 java 8 之前，接口与其实现类之间的 **耦合度** 太高了（**tightly coupled**），当需要为一个接口添加方法时，所有的实现类都必须随之修改。默认方法解决了这个问题，它可以为接口添加新的方法，而不会破坏已有的接口的实现。这在 lambda 表达式作为 java 8 语言的重要特性而出现之际，为升级旧接口且保持向后兼容（backward compatibility）提供了途径。 

```php
String[] array = new String[] {
        "hello",
        ", ",
        "world",
};
List<String> list = Arrays.asList(array);
list.forEach(System.out::println); // 这是 jdk 1.8 新增的接口默认方法
```

###### 这个 forEach 方法是 jdk 1.8 新增的接口默认方法，正是因为有了默认方法的引入，才不会因为 Iterable 接口中添加了 forEach 方法就需要修改所有 Iterable 接口的实现类。

#### 15.Stream

**三部分构成**

**1.source（源）**

**2.零个或多个中间操作**

**3.终止操作**

**流操作的分类**

**1.惰性求值（中间操作）**

**2.及早求值（终止求值**）

**构造stream的方法**

```java
public class StreamTest1 {
    public static void main(String[] args) {
        Stream stream1=Stream.of("hello","world","helloWorld!");
        String[] MyArray=new String[]{"hello","world","helloWorld!"};
        Stream stream2=Stream.of(MyArray);
        Stream stream3= Arrays.stream(MyArray);
        List<String> list=Arrays.asList(MyArray);
        list.stream();  
    }
}
```



```java
public class StreamTest2 {
    public static void main(String[] args) {
        IntStream.of(new int[]{1,3,5}).forEach(System.out::println);
        System.out.println("===========");
        //3,4,5,6,7
        IntStream.range(3, 8).forEach(System.out::println);
        System.out.println("===========");
        //3,4,5,6,7,8
        IntStream.rangeClosed(3, 8).forEach(System.out::println);
  	}
}
```



```java
public class StreamTest3 {
    public static void main(String[] args) {
        List<Integer> list= Arrays.asList(1,2,3,4,5);
        Integer reduce = list.stream().map(p -> p * 2).reduce(0, Integer::sum);
        //30,将集合每一个元素乘2，再相加
        System.out.println("reduce = " + reduce);
    }
}

```



**本质区别：函数式编程传递的是行为，根据行为对数据进行加工，面向对象编程传递的是数据**

- **Collection提供了新的stream()方法**
- **流不存储值，通过管道的方式获取值**
- **本质是函数式，对流的操作回生成一个结果，不过并不会修改底层数据源，集合可以作为流的底层数据源**
- **延迟查找，很多流操作(过滤，映射，排序等)都可以延迟实现**







##### 15.1.方法签名：

**方法签名由方法名称和一个参数列表（方法的参数的顺序和类型）组成。**

##### 15.2.stream.collect

```java
/**
 * Performs a <a href="package-summary.html#MutableReduction">mutable
 * reduction</a> operation on the elements of this stream.  A mutable
 * reduction is one in which the reduced value is a mutable result container,
 * such as an {@code ArrayList}, and elements are incorporated by updating
 * the state of the result rather than by replacing the result.  This
 * produces a result equivalent to:
 集合是根据更新结果状态而不是替换结果进行合并的
 * <pre>{@code
 *     R result = supplier.get();
 *     for (T element : this stream)
 *         accumulator.accept(result, element);
 *     return result;
 * }</pre>
 *
 * <p>Like {@link #reduce(Object, BinaryOperator)}, {@code collect} operations
 * can be parallelized without requiring additional synchronization.
 *
 * <p>This is a <a href="package-summary.html#StreamOps">terminal
 * operation</a>.
 *
 * @apiNote There are many existing classes in the JDK whose signatures are
 * well-suited for use with method references as arguments to {@code collect()}.
 * For example, the following will accumulate strings into an {@code ArrayList}:
 * <pre>{@code
 *     List<String> asList = stringStream.collect(ArrayList::new, ArrayList::add,
 *                                                ArrayList::addAll);
 * }</pre>
 *
 * <p>The following will take a stream of strings and concatenates them into a
 * single string:
 * <pre>{@code
 *     String concat = stringStream.collect(StringBuilder::new, StringBuilder::append,
 *                                          StringBuilder::append)
 *                                 .toString();
 * }</pre>
 *
 * @param <R> type of the result
 * @param supplier a function that creates a new result container. For a
 *                 parallel execution, this function may be called
 *                 multiple times and must return a fresh value each time.
 * @param accumulator an <a href="package-summary.html#Associativity">associative</a>,
 *                    <a href="package-summary.html#NonInterference">non-interfering冲突</a>,
 *                    <a href="package-summary.html#Statelessness">stateless</a>
 *                    function for incorporating an additional element into a result
 * @param combiner an <a href="package-summary.html#Associativity">associative</a>,
 *                    <a href="package-summary.html#NonInterference">non-interfering</a>,
 *                    <a href="package-summary.html#Statelessness">stateless</a>
 *                    function for combining two values, which must be
 *                    compatible with the accumulator function
 * @return the result of the reduction
 */
<R> R collect(Supplier<R> supplier,
              BiConsumer<R, ? super T> accumulator,
              BiConsumer<R, R> combiner);
```

```java
public class StreamTest4 {
    public static void main(String[] args) {
 
	    Stream<String> stream=Stream.of("hello","world","helloWorld!");
        //将stream转换为数组
        String[] stringArray=stream.toArray(length->new String[length]);
        stream.toArray(String[]::new);
        Arrays.asList(stringArray).forEach(System.out::println);
        
        //将stream转化为arrayList集合
        List<String> list = stream.collect(Collectors.toList());
        List<String> arrayList= stream.collect(() -> new ArrayList<String>(), (list1, item) -> list1.add(item),(list1, list2) -> list1.addAll(list2));
        List<String> collect = stream.collect(LinkedList::new, LinkedList::add,LinkedList::addAll);
        arrayList.forEach(System.out::println);
        
	}
}
```





```java
R result = supplier.get();
   for (T element : this stream)
       accumulator.accept(result, element);
    return result;
```

```java
List<String> list =
        stream.collect(()->new ArrayList<>(),(list1,item)->list1.add(item),(list1,list2)->list1.addAll(list2));
```

collect需要三个参数，第一个参数是调用supplier的get方法，也就是相当于得到一个list集合，例如：

**()->new ArrayList<>（）**

第二个参数是调用BiConSumer函数式接口的accept方法，该方法传入两个参数，不返回值，例如：

**（list1,item）->list1.add(item)**   item为stream流的每一个元素，添加到每一个ArrayList中

第三个参数也是调用BiConSumer函数式接口的accept方法，目的是将每次产生的Arraylist合并到一个新的ArrayList集合中

（list1，list2）->list1.addAll(list2)

转换为对应的**方法引用**为：

```
List<String> list=stream.collect(LinkedList::new,LinkedList::add,LinkedList::addAll);
list.forEach(System.out::println);
```

hello
world
helloWorld!

```java
/*
*
* @param <R> type of the result
* @param supplier a function that creates a new result container. For a
*                 parallel execution, this function may be called
*                 multiple times and must return a fresh value each time.
* @param accumulator an <a href="package-summary.html#Associativity">associative</a>,
*                    <a href="package-summary.html#NonInterference">non-interfering</a>,
*                    <a href="package-summary.html#Statelessness">stateless</a>
*                    function for incorporating an additional element into a result
* @param combiner an <a href="package-summary.html#Associativity">associative</a>,
*                    <a href="package-summary.html#NonInterference">non-interfering</a>,
*                    <a href="package-summary.html#Statelessness">stateless</a>
*                    function for combining two values, which must be
*                    compatible with the accumulator function
* @return the result of the reduction
*/
```

==============================================华丽的分割线

```
stream.collect(Collectors.toList());
```

```java
public static <T>
Collector<T, ?, List<T>> toList() {
    return new CollectorImpl<>((Supplier<List<T>>) ArrayList::new, List::add,
                               (left, right) -> { left.addAll(right); return left; },
                               CH_ID);
}
```

默认返回的是**ArrayList**，如果想返回**LinkedList**就需要看懂源代码操作

**Collections.toCollection();**

```java
public static <T, C extends Collection<T>>
Collector<T, ?, C> toCollection(Supplier<C> collectionFactory) {
    return new CollectorImpl<>(collectionFactory, Collection<T>::add,
                               (r1, r2) -> { r1.addAll(r2); return r1; },
                               CH_ID);
}
```

根据自己需要创建集合

```java
TreeSet<String> set = stream.collect(Collectors.toCollection(TreeSet::new));
System.out.println(set.getClass());
set.forEach(System.out::println);
```

hello
helloWorld!
world

**Collectors.joining（）**拼接stream中内容

```java
String string = stream.collect(Collectors.joining(" ")).toString();
System.out.println(string);
```

hello world helloWorld!

##### 15.3.map()

```java
List<String> list = Arrays.asList("hello", "world", "helloWorld", "test");
list.stream().map(String::toUpperCase).collect(Collectors.toList()).forEach(System.out::println);
List<Integer> list1=Arrays.asList(1,2,34,5,6);
list1.stream().map(i->i*2).collect(Collectors.toList()).forEach(System.out::println);
```

TEST
2
4
68
10
12

##### **15.4flatMap()**

```java
<R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);
```

```java
Stream<List<Integer>> stream = Stream.of(Arrays.asList(0), Arrays.asList(1, 2), Arrays.asList(3, 4, 5, 6));
stream.flatMap(theList->theList.stream()).map(i->i*i).forEach(System.out::println);
```

0
1
4
9
16
25
36

##### 15.5.generate()(获取流得方式)

**generate**根据**Supplier**获得对象得到带流

```java
public static<T> Stream<T> generate(Supplier<T> s) {
    Objects.requireNonNull(s);
    return StreamSupport.stream(
            new StreamSpliterators.InfiniteSupplyingSpliterator.OfRef<>(Long.MAX_VALUE, s), false);
}
```

```java
//findFirst返回的是Optional，为了规避空指针，可以将generate换为empty，什么也不会输出
Stream<String> stream = Stream.generate(UUID.randomUUID()::toString);
stream.findFirst().ifPresent(System.out::println);
```

01a7d356-448d-47f3-80b9-9c91e6eda6a7

##### **15.6.iterate**通过**种子（seed）**得到流

```
/**
 * Returns an infinite sequential ordered {@code Stream} produced by iterative
 * application of a function {@code f} to an initial element {@code seed},
 * producing a {@code Stream} consisting of {@code seed}, {@code f(seed)},
 * {@code f(f(seed))}, etc.
 *
 * <p>The first element (position {@code 0}) in the {@code Stream} will be
 * the provided {@code seed}.  For {@code n > 0}, the element at position
 * {@code n}, will be the result of applying the function {@code f} to the
 * element at position {@code n - 1}.
 *
 * @param <T> the type of stream elements
 * @param seed the initial element
 * @param f a function to be applied to to the previous element to produce
 *          a new element
 * @return a new sequential {@code Stream}
 */
 //   简单来说就是得到一个无限串顺序流，给定一个seed，然后f作用于seed生成新的f（seed）无限循环下去
   f（f(seed)）...
public static<T> Stream<T> iterate(final T seed, final UnaryOperator<T> f) {
    Objects.requireNonNull(f);
    final Iterator<T> iterator = new Iterator<T>() {
        @SuppressWarnings("unchecked")
        T t = (T) Streams.NONE;

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public T next() {
            return t = (t == Streams.NONE) ? seed : f.apply(t);
        }
    };
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
            iterator,
            Spliterator.ORDERED | Spliterator.IMMUTABLE), false);
}
```



```java
public static<T> Stream<T> iterate(final T seed, final UnaryOperator<T> f{
...
}
```

**UnaryOperator**继承**Function**，得到一个元素返回相同类型元素

```java
public interface UnaryOperator<T> extends Function<T, T> {
    ...
}
```

因为是**无限串行流**，所以需要限制一下（**limit**）流的长度

```java
Stream.iterate(2, item->item*2).limit(10).forEach(System.out::println);
```

2
4
8
16
32
64
128
256
512
1024

```java
//如果这里没用maptoInt，就不会有sum这个方法（map里面的function返回类型是泛型，自然没办法相加），之所以用mapToInt，是为了防止自动装箱拆箱而导致的一小部分性能损耗。而且sum本身调用的是reduce，默认从0开始
int sum = Stream.iterate(1, item -> item + 2).limit(6).filter(i -> i > 2).mapToInt(i -> i * 2).skip(2).limit(2).sum();
System.out.println("sum = " + sum);
```

sum = 32

**min的返回类型是OptionalInt，但sum返回类型是int，是否返回OptionalInt看能不能允许为空，**

```java
OptionalInt min = integerStream.filter(i -> i > 2).mapToInt(i -> i * 2).skip(2).limit(2).min();
```

**如果既想要最大值，又想要最小值或者其他，用summaryStatistics方法**

```java
IntSummaryStatistics summaryStatistics = Stream.iterate(1, item -> item + 2).limit(6).filter(i -> i > 2).mapToInt(i -> i * 2).skip(2).limit(2).summaryStatistics();
System.out.println(summaryStatistics.getMax());
System.out.println(summaryStatistics.getMin());
System.out.println(summaryStatistics.getSum());
System.out.println(summaryStatistics.getCount());
18
14
32
2
```

```
public class IntSummaryStatistics implements IntConsumer {
    private long count;
    private long sum;
    默认最小是最大int值，再比较时候肯定都小于等于它
    private int min = Integer.MAX_VALUE;
    private int max = Integer.MIN_VALUE;
    ...
}
```



```java
Stream.iterate(0, i->(i+1)%2).distinct().limit(6).forEach(System.out::println);
```

0
1

**distinct在前 程序没有终止**

```java
Stream.iterate(0, i->(i+1)%2).limit(6).distinct().forEach(System.out::println);
```

0
1

Process finished with exit code 0

#### 16.内部迭代和外部迭代的区别

![1577349043663](JDK8 .assets/1577349043663.png)

![1587885386232](assets/1587885386232.png)

**传统for循环像命题作文，从无到有，流像完形填空，将函数式接口内容填进去**

**传统for循环是串行的，并行化很难，需要自己写，处理并发操作带来问题，例如上锁和释放锁。但对流进行的所有操作会和流引擎/框架结合在一起，会很好利用并行化，和用户没关系。但是会有限制，如要求并行代码无状态。fork/join的处理方法**

**==集合关注的是数据与数据存储本身，流关注的是数据的计算。==**

**==流与迭代器类似的一点是：流是无法重复使用消费的==**

中间操作返回Stream对象，终止操作不返回Stream对象，可能不返回值，也可能返回某个类型的单个值 



#### 17.并发流与流的短路

```java
public class StreamTest9 {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>(500_0000);
        for (int i = 0; i < 500_0000; i++) {
            list.add(UUID.randomUUID().toString());
        }
        System.out.println("开始排序：");
        long startTime = System.nanoTime();
        //实测并行流1.318秒，串行流4.517秒
        list.parallelStream().sorted().count();
        long endTime = System.nanoTime();
        long mills = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("排序耗时 = " + mills);
    }
}
```

```java
public class StreamTest10 {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("hello", "world", "helloWorld", "test");
//        list.stream().filter(item->item.length()==5).findFirst().ifPresent(System.out::println);
//        list.stream().mapToInt(item->item.length()).filter(length->length==5).findFirst().ifPresent(System.out::println);
        list.stream().mapToInt(item->{
            int length=item.length();
            System.out.println(item);
            return length;
        }).filter(length->length==5).findFirst().ifPresent(System.out::println);
    }
}
```

输出结果：

hello
5

流存在短路运算，是对每一个流的元素进行所有操作后，再对流的下一个元素操作。对第一个元素执行了filter和findFirst发现符合全部条件，就不会对后面元素执行操作了，如果将hello改为hello1，那么会输出

hello1

world

5

如果用并行流会全部输出



#### 18.习题：去重

```java
public class StreamTest11 {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("hello welcome", "world hello", "hello world hello", "hello welcome");

//        List<String[]> collect = list.stream().map(item -> item.split(" ")).distinct()
//                .collect(Collectors.toList());
//        collect.forEach(item->Arrays.asList(item).forEach(System.out::println));
        list.stream().map(item->item.split(" ")).flatMap(Arrays::stream).distinct()
                .collect(Collectors.toList()).forEach(System.out::println);
    }
}
```

hello
welcome
world

#### 19.stream用作笛卡尔积

```java
public class StreamTest12 {
    public static void main(String[] args) {
        List<String> list1 = Arrays.asList("Hi", "Hello", "你好");
        List<String> list2 = Arrays.asList("ZhangSan", "lisi", "wangwu","zhaoliu");

        //这里flatMap返回的是Stream<String>,如果换为map返回类型为Stream<Stream<String>>
        list1.stream().flatMap(item1->list2.stream().map(item2->item2+" "+item1))
                .collect(Collectors.toList()).forEach(System.out::println);
    }
}
```

ZhangSan Hi
lisi Hi
wangwu Hi
zhaoliu Hi
ZhangSan Hello
lisi Hello
wangwu Hello
zhaoliu Hello
ZhangSan 你好
lisi 你好
wangwu 你好
zhaoliu 你好

==**flatMap是将结果打平,返回单个的Stream对象，map则不一定，可能是stream<Stream<T>>的对象，没有被打平，各自分散，需要被打平==**

#### 20.分区与分组

```java
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private String name;
    private int score;
    private int age;

}
```

将学生按照姓名分组

![1587916569619](http://static.jiebianjia.com/typora/75949c05b9ea750729131e5f8ddc84d5.png)

```java
public class StreamTest13 {
    public static void main(String[] args) {
        Student student1=new Student("lisi",100,23);
        Student student2=new Student("lisi",10,232);
        Student student3=new Student("wangwu",100,25);
        Student student4=new Student("zhaoliu",102,24);
        List<Student> students = Arrays.asList(student1, student2, student3, student4);

       Map<String,List<Student>> map=students.stream().collect(Collectors.groupingBy(Student::getName));
        System.out.println(map);
	}
//    {lisi=[com.tang.stream.Student@723279cf, com.tang.stream.Student@10f87f48], zhaoliu=[com.tang.stream.Student@b4c966a], wangwu=[com.tang.stream.Student@2f4d3709]}
    	Map<Integer, List<Student>> collect = students.stream().collect(Collectors.groupingBy(Student::getScore));
        System.out.println(collect);
//    {100=[com.tang.stream.Student@723279cf, com.tang.stream.Student@2f4d3709], 102=[com.tang.stream.Student@b4c966a], 10=[com.tang.stream.Student@10f87f48]
 		Map<String, Long> map1 = students.stream().collect(Collectors.groupingBy(Student::getName, Collectors.counting()));
        System.out.println(map1);
    //{lisi=2, zhaoliu=1, wangwu=1}
    Map<String, Double> map2 = students.stream().collect(Collectors.groupingBy(Student::getName, Collectors.averagingDouble(Student::getScore)));
        System.out.println(map2);
    //{lisi=55.0, zhaoliu=102.0, wangwu=100.0}
    
    
    Map<Boolean, List<Student>> collect1 = students.stream().collect(Collectors.partitioningBy(stu -> stu.getScore() <= 100));
        System.out.println(collect1);
        System.out.println(collect1.get(true));
    //{false=[com.tang.stream.Student@b4c966a], true=[com.tang.stream.Student@723279cf, com.tang.stream.Student@10f87f48, com.tang.stream.Student@2f4d3709]}
	//[com.tang.stream.Student@723279cf, com.tang.stream.Student@10f87f48, com.tang.stream.Student@2f4d3709]
    
	}
    
}
```

分组：group by

分区：partition by（根据true和false分区，是分组的特例）



#### 21.Collectors



**1.collect：收集器**

**2.Collector作为一个collect方法的参数**

**3.Collector是一个接口，它是一个可变的汇聚操作，将元素累积到一个可变的结果容器中，他会在所有的元素都处理完毕后，将累积的结果作为一个最终的表示（这是一个可选的操作），它支持串行与并行两种方式执行。**

**4.Collectors本身提供了Collector的常见汇聚实现，Collectors本身实际上是一个工厂。**

**5.为了确保串行与并行操作结果的等价性，Collector函数需要满足两个条件：identity（同一性）与adsociativy（结合性）。**

**6.a==combiner.apply(a, supplier.get())**

举例说明(List<String> list1,LIst<String> list2)->{list1.addAll(list2);return list1};

reduce是不可变的，reducing是可变的

**7.函数式编程最大的特点：表示做什么，而不是如何做。**



```java
Collectorpackage java.util.stream;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A <a href="package-summary.html#Reduction">mutable reduction operation</a> that
 * accumulates input elements into a mutable result container, optionally transforming
 * the accumulated result into a final representation after all input elements
 * have been processed.  Reduction operations can be performed either sequentially
 * or in parallel.
 *
 * <p>Examples of mutable reduction operations include:
 * accumulating elements into a {@code Collection}; concatenating
 * strings using a {@code StringBuilder}; computing summary information about
 * elements such as sum, min, max, or average; computing "pivot table" summaries
 * such as "maximum valued transaction by seller", etc.  The class {@link Collectors}
 * provides implementations of many common mutable reductions.
 *
 * <p>A {@code Collector} is specified by four functions that work together to
 * accumulate entries into a mutable result container, and optionally perform
 * a final transform on the result.  They are: <ul>
 *     <li>creation of a new result container ({@link #supplier()})</li>
 *     <li>incorporating a new data element into a result container ({@link #accumulator()})</li>
 *     <li>combining two result containers into one ({@link #combiner()})</li>
 *     <li>performing an optional final transform on the container ({@link #finisher()})</li>
 * </ul>
 *
 * <p>Collectors also have a set of characteristics, such as
 * {@link Characteristics#CONCURRENT}, that provide hints that can be used by a
 * reduction implementation to provide better performance.
 *
 * <p>A sequential implementation of a reduction using a collector would
 * create a single result container using the supplier function, and invoke the
 * accumulator function once for each input element.  A parallel implementation
 * would partition the input, create a result container for each partition,
 * accumulate the contents of each partition into a subresult for that partition,
 * and then use the combiner function to merge the subresults into a combined
 * result.
 *
 * <p>To ensure that sequential and parallel executions produce equivalent
 * results, the collector functions must satisfy an <em>identity</em> and an
 * <a href="package-summary.html#Associativity">associativity</a> constraints.
 *
 * <p>The identity constraint says that for any partially accumulated result,
 * combining it with an empty result container must produce an equivalent
 * result.  That is, for a partially accumulated result {@code a} that is the
 * result of any series of accumulator and combiner invocations, {@code a} must
 * be equivalent to {@code combiner.apply(a, supplier.get())}.
 *
 * <p>The associativity constraint says that splitting the computation must
 * produce an equivalent result.  That is, for any input elements {@code t1}
 * and {@code t2}, the results {@code r1} and {@code r2} in the computation
 * below must be equivalent:
 * <pre>{@code
 *     A a1 = supplier.get();
 *     accumulator.accept(a1, t1);
 *     accumulator.accept(a1, t2);
 *     R r1 = finisher.apply(a1);  // result without splitting
 *
 *     A a2 = supplier.get();
 *     accumulator.accept(a2, t1);
 *     A a3 = supplier.get();
 *     accumulator.accept(a3, t2);
 *     R r2 = finisher.apply(combiner.apply(a2, a3));  // result with splitting
 * } </pre>
 *
 * <p>For collectors that do not have the {@code UNORDERED} characteristic,
 * two accumulated results {@code a1} and {@code a2} are equivalent if
 * {@code finisher.apply(a1).equals(finisher.apply(a2))}.  For unordered
 * collectors, equivalence is relaxed to allow for non-equality related to
 * differences in order.  (For example, an unordered collector that accumulated
 * elements to a {@code List} would consider two lists equivalent if they
 * contained the same elements, ignoring order.)
 *
 * <p>Libraries that implement reduction based on {@code Collector}, such as
 * {@link Stream#collect(Collector)}, must adhere to the following constraints:
 * <ul>
 *     <li>The first argument passed to the accumulator function, both
 *     arguments passed to the combiner function, and the argument passed to the
 *     finisher function must be the result of a previous invocation of the
 *     result supplier, accumulator, or combiner functions.</li>
 *     <li>The implementation should not do anything with the result of any of
 *     the result supplier, accumulator, or combiner functions other than to
 *     pass them again to the accumulator, combiner, or finisher functions,
 *     or return them to the caller of the reduction operation.</li>
 *     <li>If a result is passed to the combiner or finisher
 *     function, and the same object is not returned from that function, it is
 *     never used again.</li>
 *     <li>Once a result is passed to the combiner or finisher function, it
 *     is never passed to the accumulator function again.</li>
 *     <li>For non-concurrent collectors, any result returned from the result
 *     supplier, accumulator, or combiner functions must be serially
 *     thread-confined.  This enables collection to occur in parallel without
 *     the {@code Collector} needing to implement any additional synchronization.
 *     The reduction implementation must manage that the input is properly
 *     partitioned, that partitions are processed in isolation, and combining
 *     happens only after accumulation is complete.</li>
 *     <li>For concurrent collectors, an implementation is free to (but not
 *     required to) implement reduction concurrently.  A concurrent reduction
 *     is one where the accumulator function is called concurrently from
 *     multiple threads, using the same concurrently-modifiable result container,
 *     rather than keeping the result isolated during accumulation.
 *     A concurrent reduction should only be applied if the collector has the
 *     {@link Characteristics#UNORDERED} characteristics or if the
 *     originating data is unordered.</li>
 * </ul>
 *
 * <p>In addition to the predefined implementations in {@link Collectors}, the
 * static factory methods {@link #of(Supplier, BiConsumer, BinaryOperator, Characteristics...)}
 * can be used to construct collectors.  For example, you could create a collector
 * that accumulates widgets into a {@code TreeSet} with:
 *
 * <pre>{@code
 *     Collector<Widget, ?, TreeSet<Widget>> intoSet =
 *         Collector.of(TreeSet::new, TreeSet::add,
 *                      (left, right) -> { left.addAll(right); return left; });
 * }</pre>
 *
 * (This behavior is also implemented by the predefined collector
 * {@link Collectors#toCollection(Supplier)}).
 *
 * @apiNote
 * Performing a reduction operation with a {@code Collector} should produce a
 * result equivalent to:
 * <pre>{@code
 *     R container = collector.supplier().get();
 *     for (T t : data)
 *         collector.accumulator().accept(container, t);
 *     return collector.finisher().apply(container);
 * }</pre>
 *
 * <p>However, the library is free to partition the input, perform the reduction
 * on the partitions, and then use the combiner function to combine the partial
 * results to achieve a parallel reduction.  (Depending on the specific reduction
 * operation, this may perform better or worse, depending on the relative cost
 * of the accumulator and combiner functions.)
 *
 * <p>Collectors are designed to be <em>composed</em>; many of the methods
 * in {@link Collectors} are functions that take a collector and produce
 * a new collector.  For example, given the following collector that computes
 * the sum of the salaries of a stream of employees:
 *
 * <pre>{@code
 *     Collector<Employee, ?, Integer> summingSalaries
 *         = Collectors.summingInt(Employee::getSalary))
 * }</pre>
 *
 * If we wanted to create a collector to tabulate the sum of salaries by
 * department, we could reuse the "sum of salaries" logic using
 * {@link Collectors#groupingBy(Function, Collector)}:
 *
 * <pre>{@code
 *     Collector<Employee, ?, Map<Department, Integer>> summingSalariesByDept
 *         = Collectors.groupingBy(Employee::getDepartment, summingSalaries);
 * }</pre>
 *
 * @see Stream#collect(Collector)
 * @see Collectors
 *
 * @param <T> the type of input elements to the reduction operation
 * @param <A> the mutable accumulation type of the reduction operation (often
 *            hidden as an implementation detail)
 * @param <R> the result type of the reduction operation
 * @since 1.8
 */
public interface Collector<T, A, R> {
    /**
     * A function that creates and returns a new mutable result container.
     *
     * @return a function which returns a new, mutable result container
     */
    Supplier<A> supplier();

    /**
     * A function that folds a value into a mutable result container.
     *
     * @return a function which folds a value into a mutable result container
     */
    BiConsumer<A, T> accumulator();

    /**
     * A function that accepts two partial results and merges them.  The
     * combiner function may fold state from one argument into the other and
     * return that, or may return a new result container.
     *
     * @return a function which combines two partial results into a combined
     * result
     */
    BinaryOperator<A> combiner();

    /**
     * Perform the final transformation from the intermediate accumulation type
     * {@code A} to the final result type {@code R}.
     *
     * <p>If the characteristic {@code IDENTITY_TRANSFORM} is
     * set, this function may be presumed to be an identity transform with an
     * unchecked cast from {@code A} to {@code R}.
     *
     * @return a function which transforms the intermediate result to the final
     * result
     */
    Function<A, R> finisher();

    /**
     * Returns a {@code Set} of {@code Collector.Characteristics} indicating
     * the characteristics of this Collector.  This set should be immutable.
     *
     * @return an immutable set of collector characteristics
     */
    Set<Characteristics> characteristics();

    /**
     * Returns a new {@code Collector} described by the given {@code supplier},
     * {@code accumulator}, and {@code combiner} functions.  The resulting
     * {@code Collector} has the {@code Collector.Characteristics.IDENTITY_FINISH}
     * characteristic.
     *
     * @param supplier The supplier function for the new collector
     * @param accumulator The accumulator function for the new collector
     * @param combiner The combiner function for the new collector
     * @param characteristics The collector characteristics for the new
     *                        collector
     * @param <T> The type of input elements for the new collector
     * @param <R> The type of intermediate accumulation result, and final result,
     *           for the new collector
     * @throws NullPointerException if any argument is null
     * @return the new {@code Collector}
     */
    public static<T, R> Collector<T, R, R> of(Supplier<R> supplier,
                                              BiConsumer<R, T> accumulator,
                                              BinaryOperator<R> combiner,
                                              Characteristics... characteristics) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(accumulator);
        Objects.requireNonNull(combiner);
        Objects.requireNonNull(characteristics);
        Set<Characteristics> cs = (characteristics.length == 0)
                                  ? Collectors.CH_ID
                                  : Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH,
                                                                           characteristics));
        return new Collectors.CollectorImpl<>(supplier, accumulator, combiner, cs);
    }

    /**
     * Returns a new {@code Collector} described by the given {@code supplier},
     * {@code accumulator}, {@code combiner}, and {@code finisher} functions.
     *
     * @param supplier The supplier function for the new collector
     * @param accumulator The accumulator function for the new collector
     * @param combiner The combiner function for the new collector
     * @param finisher The finisher function for the new collector
     * @param characteristics The collector characteristics for the new
     *                        collector
     * @param <T> The type of input elements for the new collector
     * @param <A> The intermediate accumulation type of the new collector
     * @param <R> The final result type of the new collector
     * @throws NullPointerException if any argument is null
     * @return the new {@code Collector}
     */
    public static<T, A, R> Collector<T, A, R> of(Supplier<A> supplier,
                                                 BiConsumer<A, T> accumulator,
                                                 BinaryOperator<A> combiner,
                                                 Function<A, R> finisher,
                                                 Characteristics... characteristics) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(accumulator);
        Objects.requireNonNull(combiner);
        Objects.requireNonNull(finisher);
        Objects.requireNonNull(characteristics);
        Set<Characteristics> cs = Collectors.CH_NOID;
        if (characteristics.length > 0) {
            cs = EnumSet.noneOf(Characteristics.class);
            Collections.addAll(cs, characteristics);
            cs = Collections.unmodifiableSet(cs);
        }
        return new Collectors.CollectorImpl<>(supplier, accumulator, combiner, finisher, cs);
    }

    /**
     * Characteristics indicating properties of a {@code Collector}, which can
     * be used to optimize reduction implementations.
     */
    enum Characteristics {
        /**
         * Indicates that this collector is <em>concurrent</em>, meaning that
         * the result container can support the accumulator function being
         * called concurrently with the same result container from multiple
         * threads.
         *
         * <p>If a {@code CONCURRENT} collector is not also {@code UNORDERED},
         * then it should only be evaluated concurrently if applied to an
         * unordered data source.
         */
        //list就不行，因为list里面元素是有序的
        CONCURRENT,

        /**
         * Indicates that the collection operation does not commit to preserving
         * the encounter order of input elements.  (This might be true if the
         * result container has no intrinsic order, such as a {@link Set}.)
         */
        UNORDERED,

        /**
         * Indicates that the finisher function is the identity function and
         * can be elided.  If set, it must be the case that an unchecked cast
         * from A to R will succeed.
         */
        IDENTITY_FINISH
    }
}
```



#### 22.多级分组和分区

```java
/**
 * Implementations of {@link Collector} that implement various useful reduction
 * operations, such as accumulating elements into collections, summarizing
 * elements according to various criteria, etc.
 *
 * <p>The following are examples of using the predefined collectors to perform
 * common mutable reduction tasks:
 *
 * <pre>{@code
 *     // Accumulate names into a List
 *     List<String> list = people.stream().map(Person::getName).collect(Collectors.toList());
 *
 *     // Accumulate names into a TreeSet
 *     Set<String> set = people.stream().map(Person::getName).collect(Collectors.toCollection(TreeSet::new));
 *
 *     // Convert elements to strings and concatenate them, separated by commas
 *     String joined = things.stream()
 *                           .map(Object::toString)
 *                           .collect(Collectors.joining(", "));
 *
 *     // Compute sum of salaries of employee
 *     int total = employees.stream()
 *                          .collect(Collectors.summingInt(Employee::getSalary)));
 *
 *     // Group employees by department
 *     Map<Department, List<Employee>> byDept
 *         = employees.stream()
 *                    .collect(Collectors.groupingBy(Employee::getDepartment));
 *
 *     // Compute sum of salaries by department
 *     Map<Department, Integer> totalByDept
 *         = employees.stream()
 *                    .collect(Collectors.groupingBy(Employee::getDepartment,
 *                                                   Collectors.summingInt(Employee::getSalary)));
 *
 *     // Partition students into passing and failing
 *     Map<Boolean, List<Student>> passingFailing =
 *         students.stream()
 *                 .collect(Collectors.partitioningBy(s -> s.getGrade() >= PASS_THRESHOLD));
 *
 * }</pre>
 *
 * @since 1.8
 */
public final class Collectors {
    
}
```

```java
package com.tang.stream2;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class StreamTest1 {
    public static void main(String[] args) {
        Student student1 = new Student("张三", 100);
        Student student2 = new Student("李四", 90);
        Student student3 = new Student("王五", 80);
        Student student4 = new Student("赵六", 20);
        Student student5 = new Student("赵六", 70);

        List<Student> students= Arrays.asList(student1,student2,student3,student4,student5);

        List<Student> student1s = students.stream().collect(toList());
        student1s.forEach(System.out::println);
        System.out.println("-----------");

        System.out.println(students.stream().collect(counting()));
        System.out.println(students.stream().count());
        System.out.println("===========");

        //分数最低的学生
        Optional<Student> collect = students.stream().collect(minBy(Comparator.comparingInt(Student::getScore)));
        collect.ifPresent(System.out::println);
        students.stream().collect(Collectors.maxBy(Comparator.comparingInt(Student::getScore))).ifPresent(System.out::println);
        System.out.println(students.stream().collect(averagingInt(Student::getScore)));

        System.out.println(students.stream().collect(summingInt(Student::getScore)));
        System.out.println("=======");
        IntSummaryStatistics intSummaryStatistics = students.stream().collect(summarizingInt(Student::getScore));
        System.out.println(intSummaryStatistics);

        System.out.println(students.stream().map(Student::getName).collect(joining()));
        System.out.println(students.stream().map(Student::getName).collect(joining(",")));
        System.out.println(students.stream().map(Student::getName).collect(joining(",","hello","world")));

        System.out.println("=========");
        Map<Integer, Map<String, List<Student>>> collect1 = students.stream().collect(groupingBy(Student::getScore, groupingBy(Student::getName)));
        System.out.println(collect1);
        System.out.println("==========");
        Map<Boolean, List<Student>> collect2 = students.stream().collect(partitioningBy(student -> student.getScore() >= 80));
        System.out.println(collect2);
        System.out.println("==========");
        Map<Boolean, Map<Boolean, List<Student>>> collect3 = students.stream().collect(partitioningBy(student -> student.getScore() >= 80, partitioningBy(student -> student.getScore() > 90)));
        System.out.println(collect3);
        System.out.println("=============");
        Map<Boolean, Long> collect4 = students.stream().
                collect(partitioningBy(student -> student.getScore() >= 80, counting()));
        System.out.println(collect4);
        System.out.println("==============");
        Map<String, Optional<Student>> collect5 = students.stream().collect(groupingBy(Student::getName, minBy(Comparator.comparingInt(Student::getScore))));
        System.out.println(collect5);
        System.out.println("==============");
        Map<String, Student> collect6 = students.stream().
                collect(groupingBy(Student::getName, collectingAndThen(minBy(Comparator.comparingInt(Student::getScore)), Optional::get)));
        System.out.println(collect6);

    }
}
```

```java
Student(name=张三, score=100)
Student(name=李四, score=90)
Student(name=王五, score=80)
Student(name=赵六, score=20)
Student(name=赵六, score=70)
-----------
5
5
===========
Student(name=赵六, score=20)
Student(name=张三, score=100)
72.0
360
=======
IntSummaryStatistics{count=5, sum=360, min=20, average=72.000000, max=100}
张三李四王五赵六赵六
张三,李四,王五,赵六,赵六
hello张三,李四,王五,赵六,赵六world
=========
{80={王五=[Student(name=王五, score=80)]}, 20={赵六=[Student(name=赵六, score=20)]}, 100={张三=[Student(name=张三, score=100)]}, 70={赵六=[Student(name=赵六, score=70)]}, 90={李四=[Student(name=李四, score=90)]}}
==========
{false=[Student(name=赵六, score=20), Student(name=赵六, score=70)], true=[Student(name=张三, score=100), Student(name=李四, score=90), Student(name=王五, score=80)]}
==========
{false={false=[Student(name=赵六, score=20), Student(name=赵六, score=70)], true=[]}, true={false=[Student(name=李四, score=90), Student(name=王五, score=80)], true=[Student(name=张三, score=100)]}}
=============
{false=2, true=3}
==============
{李四=Optional[Student(name=李四, score=90)], 张三=Optional[Student(name=张三, score=100)], 王五=Optional[Student(name=王五, score=80)], 赵六=Optional[Student(name=赵六, score=20)]}
==============
{李四=Student(name=李四, score=90), 张三=Student(name=张三, score=100), 王五=Student(name=王五, score=80), 赵六=Student(name=赵六, score=20)}

Process finished with exit code 0

```

 



#### 23.Comparator

```java
public class MyComparatorTest {
    public static void main(String[] args) {
        List<String> list= Arrays.asList("tang","yao","ni","hao");
        list.sort((string1,string2)->string1.length()-string2.length());
        System.out.println(list);
        //[ni, yao, hao, tang]

       list.sort(Comparator.comparingInt(String::length).reversed());
        System.out.println(list);
        //[tang, yao, hao, ni]
        
        //comaringInt参数为ToIntFunction<? super T> keyExtractor，所以T是String类型，？为Object类型，如果将（String item）换为（Boolean item）就会报错
        //这里推断参数类型不出来的原因：
        //sort方法接收的参数是reversed方法所返回的，reversed返回的是Comparator<T>,而sort参数先获取一个比较器，调用reversed获取另一个比较器，离上下文更远一些，因此item推断不出来是String类型，只能把它当做Object类型看待
        list.sort(Comparator.comparingInt((String item)->item.length()).reversed());
        System.out.println(list);
        //[tang, yao, hao, ni]
        
        list.sort(Comparator.comparingInt(String::length).thenComparing
                  (String.CASE_INSENSITIVE_ORDER));
        System.out.println(list);
        //[ni, hao, yao, tang]
        
        list.sort(Comparator.comparingInt(String::length)
                .thenComparing(Comparator.comparing(String::toLowerCase)));
        System.out.println(list);
        //[ni, hao, yao, tang]
        
        list.sort(Comparator.comparingInt(String::length)
                .thenComparing(Comparator.comparing(String::toLowerCase,
                                                    Comparator.reverseOrder())));
        System.out.println(list)
        //[ni, yao, hao, tang]
         
        list.sort(Comparator.comparingInt(String::length).reversed().
                thenComparing(String::toLowerCase,Comparator.reverseOrder()));
        System.out.println(list);
        //[tang, yao, hao, ni]
        list.sort(Comparator.comparingInt(String::length).reversed().
                thenComparing(String::toLowerCase,Comparator.reverseOrder()).
                thenComparing(Comparator.reverseOrder()));
        System.out.println(list);
        //[tang, yao, hao, ni]
            
        
    }
}
```



**Collections的sort方法**

```java
public static <T> void sort(List<T> list, Comparator<? super T> c) {
    list.sort(c);
}
为什么Comparator的泛型不仅仅设为T，而是用<? super T>

假设T为Student类型

Student implements A,B,C{

}
这里可以按照A接口方式，B接口方式，C接口方式进行排序，但是结果都是List<Student>类型
```

```java
default void sort(Comparator<? super E> c) {
	//将当前集合转换为数组
    Object[] a = this.toArray();
    //按照Comparator这个方式进行排序，这里E是Student，？可能是A,B,C
    Arrays.sort(a, (Comparator) c);
    ListIterator<E> i = this.listIterator();
    for (Object e : a) {
        i.next();
		//强制类型转换到真正类型Student
        i.set((E) e);
    }
}
```



**thenComparing**

```java
/**
 * Returns a lexicographic-order comparator with another comparator.
 * If this {@code Comparator} considers two elements equal, i.e.
 * {@code compare(a, b) == 0}, {@code other} is used to determine the order.
 *
 * <p>The returned comparator is serializable if the specified comparator
 * is also serializable.
 *
 * @apiNote
 * For example, to sort a collection of {@code String} based on the length
 * and then case-insensitive natural ordering, the comparator can be
 * composed using following code,
 *
 * <pre>{@code
 *     Comparator<String> cmp = Comparator.comparingInt(String::length)
 *             .thenComparing(String.CASE_INSENSITIVE_ORDER);
 * }</pre>
 *
 * @param  other the other comparator to be used when this comparator
 *         compares two objects that are equal.
 * @return a lexicographic-order comparator composed of this and then the
 *         other comparator
 * @throws NullPointerException if the argument is null.
 * @since 1.8
 */
default Comparator<T> thenComparing(Comparator<? super T> other) {
    Objects.requireNonNull(other);
    return (Comparator<T> & Serializable) (c1, c2) -> {
        //先用调用者的比较器比较
        int res = compare(c1, c2);
        //如果结果不为0就用调用者比较器结果，否则用other比较器的比较结果
        return (res != 0) ? res : other.compare(c1, c2);
    };
}
```

**总结：jdk8之前只有Comparator里面只有抽象方法compare，比较所接收的两个元素，的比较结果为0,负数，正数，来判断第一个元素和第二个元素的先后顺序，负数为正序，正数为逆序。jdk8之后，增加了若干个default方法reversed，thenComparing和多种重载方法，和若干static方法如reverseOrder，针对通用类型，提供通用提供comparing排序手段，针对原生数据类型int，long，double提供了三个特化的comparingInt，comparingLong，comparingDouble，有具体的特化实现要具体调用，避免不必要的装箱拆箱的性能损耗。**

#### 24.手写Collecor实现类

```java
package com.tang.stream2;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;
import static java.util.stream.Collector.Characteristics.UNORDERED;

public class MySetCollector<T> implements Collector<T,Set<T>,Set<T>> {

    @Override
    public Supplier<Set<T>> supplier() {
        System.out.println("Supplier invocked!");
        return HashSet<T>::new;
    }

    @Override
    public BiConsumer<Set<T>, T> accumulator() {
        System.out.println("accumulator invocked!");
        //如果用   return HashSet<T>::add;编译不通过，假设上面Supllier提供的容器是TreeSet<T>::new,两者类型不一致，
        //而Set<T>::add;能满足一切set实现类型的容器。
        return Set<T>::add;

    }

    @Override
    public BinaryOperator<Set<T>> combiner() {
        System.out.println("combiner invoked!");
        return (set,set1)->{set.addAll(set1);return set;};
    }

    @Override
    public Function<Set<T>, Set<T>> finisher() {
        System.out.println("finisher invoked!");
        //return t->t;
        //中间容器类型如果和返回结果类型一样的话可以直接抛出异常
        //throw new UnsupportedOperationException(); 
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        System.out.println("characteristics invoked!");
        //如果删掉IDENTITY_FINISH,结果就会多了finisher invoked!
        return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH,UNORDERED));
    }

    public static void main(String[] args) {
        List<String> list=Arrays.asList("tang","yao","ni","hao","tang");
        Set<String> set = list.stream().collect(new MySetCollector<>());
        System.out.println(set);
    }
}
```

Supplier invocked!
accumulator invocked!
combiner invoked!
characteristics invoked!
characteristics invoked!
[tang, yao, hao, ni]

这里虽然被调用但是不代表执行了方法，只是返回了接口

接下来看具体的实现代码

```java
@Override
@SuppressWarnings("unchecked")
public final <R, A> R collect(Collector<? super P_OUT, A, R> collector) {
    A container;
    if (isParallel()
            && (collector.characteristics().contains(Collector.Characteristics.CONCURRENT))
            && (!isOrdered() || collector.characteristics().contains(Collector.Characteristics.UNORDERED))) {
        container = collector.supplier().get();
        BiConsumer<A, ? super P_OUT> accumulator = collector.accumulator();
        forEach(u -> accumulator.accept(container, u));
    }
    else {
        container = evaluate(ReduceOps.makeRef(collector));
    }
    return 
 //characteristics出现第二次   
        //判断枚举类型是不是IDENTITY_FINISH，是的话直接进行强制类型转换，没调用finisher方法，否则调用finisher().apply方法，所以根据实际业务流程，认为中间结果容器类型就是返回结果类型，就在characteristics特性列表中加进来，就会在具体使用的时候就不会再去执行finisher，效率提升。但必须保证强制类型转换必须成功，因为在调用处没任何检查，只是通过标识进行判断，否则会抛出ClassCastException。
        collector.characteristics().contains
        (Collector.Characteristics.IDENTITY_FINISH)
           ? (R) container
           : collector.finisher().apply(container);
}
```





```java
public static <T, I> TerminalOp<T, I>
makeRef(Collector<? super T, I, ?> collector) {
    //这里调用了supplier，得到hashSet对象
    Supplier<I> supplier = Objects.requireNonNull(collector).supplier();
    //调用accumulator返回BiConsumer对象
    BiConsumer<I, ? super T> accumulator = collector.accumulator();
    //调用combine方法返回BinaryOperator对象
    BinaryOperator<I> combiner = collector.combiner();
    class ReducingSink extends Box<I>
            implements AccumulatingSink<T, I, ReducingSink> {
        @Override
        public void begin(long size) {
            state = supplier.get();
        }

        @Override
        public void accept(T t) {
            accumulator.accept(state, t);
        }

        @Override
        public void combine(ReducingSink other) {
            state = combiner.apply(state, other.state);
        }
    }
    return new ReduceOp<T, I, ReducingSink>(StreamShape.REFERENCE) {
        @Override
        public ReducingSink makeSink() {
            return new ReducingSink();
        }

        @Override
        public int getOpFlags() {
            return 
                //characteristics出现第一次
collector.characteristics().contains(Collector.Characteristics.UNORDERED)
                   ? StreamOpFlag.NOT_ORDERED
                   : 0;
        }
    };
}
```



Collectors的一个私有静态方法

```java
private static <I, R> Function<I, R> castingIdentity() {
    return i -> (R) i;
}
```



CollectorImpl实现类参数并没有提供finisher，而是用castingIdentity，直接强制类型转换成结果R类型

```java
CollectorImpl(Supplier<A> supplier,
              BiConsumer<A, T> accumulator,
              BinaryOperator<A> combiner,
              Set<Characteristics> characteristics) {
    this(supplier, accumulator, combiner, castingIdentity(), characteristics);
}
```
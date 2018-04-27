package net.sea.simple.rpc.data.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author chengwu.hua
 * @Date 2018/4/26 13:38
 * @Version 1.0
 */
public class Test {
    public static void main(String[] args) throws Exception {
        MarshallingEncoder encoder = new MarshallingEncoder();
        ByteBuf byteBuf = Unpooled.buffer();
        Demo d = new Demo();
        d.setAge(30);
        d.setName("Jack");
        d.setSs(Arrays.asList(new Book("Book1"),new Book("Book2")));
        encoder.encode(d, byteBuf);

        MarshallingDecoder decoder = new MarshallingDecoder();
        Object result = decoder.decode(byteBuf);
        System.out.println("result = [" + result + "]");
    }

    private static class Book implements Serializable{
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Book(String content) {
            this.content = content;
        }
    }

    private static class Demo implements Serializable{
        private String name;
        private int age;
        private List<Book> ss;

        public List<Book> getSs() {
            return ss;
        }

        public void setSs(List<Book> ss) {
            this.ss = ss;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Demo{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", ss=" + ss +
                    '}';
        }
    }

}


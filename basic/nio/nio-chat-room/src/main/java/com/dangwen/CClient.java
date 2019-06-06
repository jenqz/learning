package com.dangwen;

import java.io.IOException;

/**
 * @author czhan
 * @version 0.0.1
 * @description TODO
 * @date 2019年06月06日 下午2:06
 * @since 0.0.1
 */
public class CClient {
    public static void main(String[] args) throws IOException {
        new NioClient().start("C");
    }
}

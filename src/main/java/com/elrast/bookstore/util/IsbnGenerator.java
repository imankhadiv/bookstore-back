package com.elrast.bookstore.util;


import java.util.Random;

public class IsbnGenerator implements NumberGenerator {
    @Override
    public String generateNumber() {
        return "123-456" + Math.abs(new Random().nextInt());
    }
}

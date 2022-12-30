package org.but.feec.cinema.api.person;


import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class test_class {


    final private static Argon2 argon2 = Argon2Factory.create(
                Argon2Factory.Argon2Types.ARGON2id,16,64
        );
    public static void main(String [] args) {
       // iteration = 10
       // memory = 64m
        // parallelism = 1
        String hash = argon2.hash(22,65536,1 ,"batman".toCharArray());
        System.out.println(hash);



    }
}

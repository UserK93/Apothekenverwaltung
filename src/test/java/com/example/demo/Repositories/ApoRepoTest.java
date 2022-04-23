package com.example.demo.Repositories;

import com.example.demo.Apotheke;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ApoRepoTest {

    @Autowired
    private ApoRepo test;

    @Test
    void findApoByName() {

        //given
        Apotheke apotheke = new Apotheke(1,2,"test","test","test");

        test.save(apotheke);
        //when
        Optional<Apotheke> expected = test.findProductByName("test");

        //then
        //assertThat(expected).
    }
}
package com.example.demo.Repositories;

import com.example.demo.Apotheke;
import net.bytebuddy.dynamic.DynamicType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Queue;

//Für die Verbindung mit der Datenbank Id als Integer festgelegt, Datenbanktabelle = Apotheke
@Repository
public interface ApoRepo extends JpaRepository<Apotheke, Integer> {

    /**
     * select name form database and return it
     * @param name
     */
    @Query("SELECT s FROM Apotheke s WHERE s.produktname =?1")
    Optional<Apotheke> findProductByName(String name);

}

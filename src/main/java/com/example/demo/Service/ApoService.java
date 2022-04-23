package com.example.demo.Service;

import com.example.demo.Apotheke;
import com.example.demo.Repositories.ApoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class ApoService {

    @Autowired
    ApoRepo apoRepo;

    /**
     * list all products
     * @return all products in database as JSON
     */
    public List<Apotheke> getAllProducts() {

        if (apoRepo.findAll().isEmpty()){


        }

        return apoRepo.findAll();
    }

    /**
     * Display a specific product and check if is in Database
     * @param produktname
     * @return the specific product
     * @throws ResponseStatusException when product not found
     */

    public Optional<Apotheke> findProductByName(String produktname) {

        if (apoRepo.findProductByName(produktname).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "product" + produktname+" not found");

        }

        return apoRepo.findProductByName(produktname);
    }

    /**
     * add product by taking receiving data from JSON text and check if it is not smaller than 0
     * @param product
     * @return ResponseEntity with status OK, if adding was successful
     * @throws ResponseStatusException to check if amount is not smaller than 0
     */

    public ResponseEntity<String> addProduct(@RequestBody Apotheke product) {

        product.setAnzahl(product.getAnzahl());
        product.setHersteller(product.getHersteller());
        product.setProduktname(product.getProduktname());
        product.setWirkstoff(product.getWirkstoff());

        if (product.getAnzahl() < 0) {

            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Amount cannot be samller than 0!");
        }
        apoRepo.save(product);

        return new ResponseEntity<>(
                "adding succesful " ,
                HttpStatus.OK);

    }

    /**
     * Delete Product with given pharmazentralnummer
     * @param phnr
     * @throws  ResponseStatusException with status OK, if deleting was successful
     * @throws  ResponseStatusException to check if product exist
     */

    public ResponseEntity deleteProduct(@PathVariable("pharmazentralnummer") Integer phnr) {

        Optional<Apotheke> apotheke = apoRepo.findById(phnr);

        if (!apotheke.isPresent()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product doesn´t exist");
        }

        apoRepo.deleteById(phnr);

        return new ResponseEntity<>(
                "deleting successful " ,
                HttpStatus.OK);



    }

    /**
     * edit product with given id and data from JSON text
     * @param produktname
     * @param products
     * @return ResponseEntity with status OK, if ordering was successful
     * @throws  ResponseStatusException to check if product exist
     * @throws  ResponseStatusException to check if amount is smaller than 0
     */

    public ResponseEntity editProduct(@PathVariable("produktname") String produktname, @RequestBody Apotheke products) {

        Optional<Apotheke> apotheke = apoRepo.findProductByName(produktname);
        Apotheke apotheke2 = apotheke.get();

        if (!apotheke.isPresent()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product doesn´t exist");
        }

        apotheke2.setAnzahl(products.getAnzahl());
        apotheke2.setHersteller(products.getHersteller());
        apotheke2.setProduktname(products.getProduktname());
        apotheke2.setWirkstoff(products.getWirkstoff());

        if (apotheke2.getAnzahl() < 0) {

            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                 "Amount cannot be smaller than 0");


        }
        apoRepo.save(apotheke2);

        return new ResponseEntity<>(
                " editing successful" ,
                HttpStatus.OK);
    }

    /**
     * order from store, decreasing amount of products available in database
     * @param produktname
     * @param anzahl
     * @throws  ResponseStatusException to check if product exists
     * @throws  ResponseStatusException to check if amount is smaller than 0
     * @return  ResponseEntity with status OK, if ordering was successful
     */
    public ResponseEntity orderFromStore(@PathVariable("produktname") String produktname, @PathVariable("anzahl") int anzahl) {

        Optional<Apotheke> apotheke = apoRepo.findProductByName(produktname);
        Apotheke apotheke2 = apotheke.get();

        if (!apotheke.isPresent()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product doesn´t exist");
        }

        apotheke2.setAnzahl(apotheke.get().getAnzahl() - anzahl);
        if (apotheke2.getAnzahl() < 0) {

            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Only " + anzahl + " left, cannot order more than "+ anzahl);
        }
        apoRepo.save(apotheke2);
        return new ResponseEntity<>(
                " order successful" ,
                HttpStatus.OK);
    }



    /**
     * Aufstocken des Bestands, Ware bestellen
     * @param produktname
     * @param anzahl
     * @throws  ResponseStatusException to check if product exists
     * @throws  ResponseStatusException to check if amount is smaller than 0
     * @return  ResponseEntity with status OK, if ordering was successful
     */
    public ResponseEntity orderMore(@PathVariable("produktname") String produktname, @PathVariable("anzahl") int anzahl) {

        Optional<Apotheke> apotheke = apoRepo.findProductByName(produktname);
        Apotheke apotheke2 = apotheke.get();

        if (!apotheke.isPresent()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product doesn´t exist");
        }

        apotheke2.setAnzahl(apotheke.get().getAnzahl() + anzahl);
        apoRepo.save(apotheke2);

        return new ResponseEntity<>(
                " order successful" ,
                HttpStatus.OK);
    }

}

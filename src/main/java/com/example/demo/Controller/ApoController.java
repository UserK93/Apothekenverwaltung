package com.example.demo.Controller;

import com.example.demo.Apotheke;
import com.example.demo.Service.ApoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApoController {

    private final ApoService apoService;

    public ApoController(ApoService apoService) {
        this.apoService = apoService;
    }

    @GetMapping("/getAllProducts")
    public Iterable<Apotheke> getProducts() {

        return apoService.getAllProducts();
    }

    @GetMapping(path = "/getProduct/{produktname}")
    public Apotheke getName(@PathVariable("produktname") String produktname) {
        return apoService.findProductByName(produktname).orElse(null);

    }

    @PostMapping("/addProduct")
    public void addProduct(@RequestBody Apotheke apotheke) {

        apoService.addProduct(apotheke);
    }

    @PutMapping("/edit/{produktname}")
    public void editProduct(@PathVariable("produktname") String produktname, @RequestBody Apotheke products) {

        apoService.editProduct(produktname, products);
    }

    @PutMapping(path = "/orderFromStore/{produktname},{anzahl}")
    public void orderFromStore(@PathVariable("produktname") String produktname, @PathVariable("anzahl") int anzahl) {

        apoService.orderFromStore(produktname, anzahl);

    }

    @PutMapping(path = "/orderMore/{produktname},{anzahl}")
    public void orderMore(@PathVariable("produktname") String produktname, @PathVariable("anzahl") int anzahl) {

        apoService.orderMore(produktname, anzahl);

    }

    @DeleteMapping(path = "/deleteProduct/{pharmazentralnummer}")
    public void deleteProduct(@PathVariable("pharmazentralnummer") Integer pharmazentralnummer) {

        apoService.deleteProduct(pharmazentralnummer);
    }

}

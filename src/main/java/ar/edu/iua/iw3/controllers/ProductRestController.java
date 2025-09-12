package ar.edu.iua.iw3.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.iw3.model.Product;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.FoundException;
import ar.edu.iua.iw3.model.business.IProductBusiness;
import ar.edu.iua.iw3.util.IStandartResponseBusiness;

@RestController // Capa puente entre mundo http y java
@RequestMapping(Constants.URL_PRODUCTS)
public class ProductRestController {
    // Transformar el path de una url para ejecutar algo en java

    @Autowired
    private IProductBusiness productBusiness;

    @Autowired
    private IStandartResponseBusiness response;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> list() {
        try {
            return new ResponseEntity<>(productBusiness.list(), HttpStatus.OK);
        } catch (BusinessException e) {

            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    
    @PostMapping(value="")
    public ResponseEntity<?> add(@RequestBody Product product) {
        try{
            Product response = productBusiness.add(product);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("location", Constants.URL_PRODUCTS + "/" + response.getId());
            return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (FoundException e){
            return new ResponseEntity<>(response.build(HttpStatus.FOUND, e, e.getMessage()), HttpStatus.FOUND);
        }
    }
}

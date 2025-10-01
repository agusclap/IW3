package ar.edu.iua.iw3.integration.cli2.model.controllers;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import ar.edu.iua.iw3.controllers.Constants;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2SlimV1JsonSerializer;
import ar.edu.iua.iw3.integration.cli2.model.business.IProductCli2Business;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.FoundException;
import ar.edu.iua.iw3.util.IStandartResponseBusiness;
import ar.edu.iua.iw3.util.JsonUtiles;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Constants.URL_INTEGRATION_CLI2 + "/products")
@Slf4j
@Profile("cli2")
public class ProductCli2RestController {

    @Autowired
    private IProductCli2Business productBusiness;

    @Autowired
    private IStandartResponseBusiness response;

    @GetMapping(value = "list-expired", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listExpired(
            @RequestParam(name = "since", required = false, defaultValue = "1970-01-01 00:00:00") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date since,
                        @RequestParam(name = "slim", required = false, defaultValue = "v0") String slimVersion) throws JsonProcessingException {
                try {
                    Calendar c = Calendar.getInstance();
                    c.setTime(since);
                    if (c.get(Calendar.YEAR) == 1970) {
                        since = new Date();
                    }

                    StdSerializer<ProductCli2> ser = null;
                    if (slimVersion.equalsIgnoreCase("v1")) {
                        ser = new ProductCli2SlimV1JsonSerializer(ProductCli2.class, false);
                    } else {
                        return new ResponseEntity<>(productBusiness.listExpired(since), HttpStatus.OK);
                    }
                    String result = JsonUtiles.getObjectMapper(ProductCli2.class, ser, null)
                            .writeValueAsString(productBusiness.listExpired(since));


                    log.debug(since.toString());
                    return new ResponseEntity<>(productBusiness.listExpired(since), HttpStatus.OK);
                } catch (BusinessException e) {
                    return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                            HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

    @GetMapping(value = "list-by-price", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listByPrice(
            @RequestParam(name = "start-price", required = false) Double startPrice,
            @RequestParam(name = "end-price", required = false) Double endPrice) {
        try {
            return new ResponseEntity<>(productBusiness.listByPrice(startPrice, endPrice), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/b2b", consumes = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<?> addExternal(HttpEntity<String> httpEntity) {
        try {
            ProductCli2 saved = productBusiness.addExternal(httpEntity.getBody());

    public ResponseEntity<?> addExternal(@RequestBody ProductCli2 product) {
        try {
            ProductCli2 saved = productBusiness.add(product);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("location", Constants.URL_INTEGRATION_CLI2 + "/products/" + saved.getId());
            return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (FoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.FOUND, e, e.getMessage()), HttpStatus.FOUND);
        }
    }
}

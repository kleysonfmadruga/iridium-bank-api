package com.iridium.bankApi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iridium.bankApi.dtos.AccountDto;
import com.iridium.bankApi.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    // The GET HTTP method returns a representation of the resource on the specified URI
    // The response's body have the details of the resource
    @GetMapping(value = "/accounts/{number}", produces = "application/json")
    public ResponseEntity<Map<String, Object>> list(@PathVariable(value = "number") Integer number){
        AccountDto account;

        try {
            account = accountService.findAccount(number);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        var accountMap = new ObjectMapper().convertValue(account, Map.class);

        return new ResponseEntity<>(accountMap, HttpStatus.OK);
    }

    // The POST HTTP method creates a new resource. The details of the resource are in the request's body
    // This method also can be used to trigger other operations that doesn't really creates something
    @PostMapping(value = "/accounts", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> requestBody){

        AccountDto account = new AccountDto()
                .setHolderCpf(requestBody.get("cpf").toString())
                .setHolderName(requestBody.get("name").toString())
                .setBalance(0.0);

        AccountDto savedAccount = null;
        try {
            savedAccount = accountService.createAccount(account);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var accountMap = new ObjectMapper().convertValue(savedAccount, Map.class);

        return new ResponseEntity<>(accountMap, HttpStatus.CREATED);
    }

    // The PATCH method updates partially a resource. The request's body specifies the changes
    @PostMapping(value = "/accounts/{number}/deposit", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Object>> deposit(
            @PathVariable(value = "number") Integer number, @RequestBody Map<String, Double> requestBody
    ){
        AccountDto account;
        Double depositValue = requestBody.get("depositValue");

        try {
            account = accountService.deposit(number, depositValue);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        var accountMap = new ObjectMapper().convertValue(account, Map.class);

        return new ResponseEntity<>(accountMap, HttpStatus.OK);
    }

    @PostMapping(
            value = "/accounts/{number}/withdraw",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<Map<String, Object>> withdraw(
            @PathVariable(value = "number") Integer number, @RequestBody Map<String, Double> requestBody
    ){
        AccountDto account;
        Double withdrawValue = requestBody.get("withdrawValue");

        try {
            account = accountService.withdraw(number, withdrawValue);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        var accountMap = new ObjectMapper().convertValue(account, Map.class);

        return new ResponseEntity<>(accountMap, HttpStatus.OK);
    }

    @PostMapping(
        value = "/accounts/{fromAccountNumber}/transfer",
        consumes = "application/json", produces = "application/json"
    )
    public ResponseEntity<Map<String, Object>> transfer(
        @PathVariable(value = "fromAccountNumber") Integer fromAccountNumber,
        @RequestBody Map<String, Object> requestBody
    ){
        AccountDto fromAccount;
        Double transferenceValue = (Double) requestBody.get("transferenceValue");
        Integer toAccountNumber = (Integer) requestBody.get("toAccountNumber");

        try {
            fromAccount = accountService.transference(fromAccountNumber, toAccountNumber, transferenceValue);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        var accountMap = new ObjectMapper().convertValue(fromAccount, Map.class);

        return new ResponseEntity<>(accountMap, HttpStatus.OK);
    }

    // The DELETE method removes the resource of the specified URI
    @DeleteMapping(value = "/accounts/{number}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable(value = "number") Integer number){

        try {
            accountService.deleteAccount(number);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
            HttpStatus.NO_CONTENT
        );
    }
}

/*

 - The [Get|Post|Put|Patch|Delete]Mapping annotation marks a method to handle with the call of the
        endpoint defined in value attribute
 - The consumes and produces attributes signal that the method accepts or returns a specific type of data
 - The RequestBody annotation marks a parameter that's will be mapped to the JSON of the request's body
 - Request bodies also can be mapped to a Map<K, V> parameter
 - The PathVariable annotation marks a parameter that's will be mapped to a URL parameter
 - ResponseEntity provides HTTP response features such the status codes and bodies

*/

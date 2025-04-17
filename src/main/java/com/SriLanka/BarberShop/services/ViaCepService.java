package com.SriLanka.BarberShop.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

@Service
public class ViaCepService {

    public Map<String, String> buscarEnderecoPorCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        try (InputStream input = new URL(url).openStream();
             Scanner scanner = new Scanner(input)) {
            String json = scanner.useDelimiter("\\A").next();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar CEP", e);
        }
    }
}

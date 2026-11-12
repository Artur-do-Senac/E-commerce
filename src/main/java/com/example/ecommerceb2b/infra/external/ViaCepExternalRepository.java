package com.example.ecommerceb2b.infra.external;

import com.example.ecommerceb2b.domain.interfaces.IViaCepExternalRepository;
import com.example.ecommerceb2b.infra.dto.ViaCepResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

// Adaptador: implementa a porta IViaCepExternalRepository chamando a API externa do ViaCEP
@Component
public class ViaCepExternalRepository implements IViaCepExternalRepository {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10)).build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Lança IllegalArgumentException quando o CEP não existe ou a consulta falha (a Controller transforma em 400 com a mensagem)
    @Override
    public ViaCepResponse buscarEnderecoViaCepPorCep(String cep) {

        var url = "https://viacep.com.br/ws/" + cep.replaceAll("[^0-9]", "") + "/json/";

        HttpResponse<String> response;

        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao consultar CEP!");
        }

        if (response.statusCode() != 200) {
            throw new IllegalArgumentException("Erro ao consultar CEP!");
        }

        ViaCepResponse viaCepResponse;

        try {
            viaCepResponse = objectMapper.readValue(response.body(), ViaCepResponse.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao consultar CEP!");
        }

        if (viaCepResponse.getErro() != null && viaCepResponse.getErro()) {
            throw new IllegalArgumentException("CEP invalido!");
        }

        return viaCepResponse;
    }
}

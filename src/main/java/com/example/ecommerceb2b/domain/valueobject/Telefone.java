package com.example.ecommerceb2b.domain.valueobject;

public class Telefone {

    private String telefone;

    public Telefone(){
        this.telefone = "";
    }

    public Telefone(String telefone){

        if(!isValidTelefone(telefone)){
            throw new IllegalArgumentException("Telefone Invalido!");
        }

        this.telefone = telefone;
    }

    public static boolean isValidTelefone(String telefone) {
        if (telefone == null) {
            return false;
        }

        // Remove tudo o que não for dígito
        telefone = telefone.replaceAll("\\D", "");

        // Telefone com DDD: 10 dígitos (fixo) ou 11 dígitos (celular)
        if (telefone.length() != 10 && telefone.length() != 11) {
            return false;
        }

        // Descarta telefones com todos os dígitos iguais (ex: 11 99999-9999)
        if (telefone.matches("(\\d)\\1{9,10}")) {
            return false;
        }

        // DDD válido: de 11 a 99 e que não termine em 0 (não existem os DDDs 10, 20, 30...)
        int ddd = Integer.parseInt(telefone.substring(0, 2));
        if (ddd < 11 || ddd % 10 == 0) {
            return false;
        }

        char primeiroDigito = telefone.charAt(2);

        // Celular (11 dígitos) começa com 9 depois do DDD
        if (telefone.length() == 11) {
            return primeiroDigito == '9';
        }

        // Fixo (10 dígitos) começa com 2, 3, 4 ou 5 depois do DDD
        return primeiroDigito >= '2' && primeiroDigito <= '5';
    }

    @Override
    public String toString(){
        String digitos = telefone.replaceAll("\\D","");

        if (digitos.length() == 11) {
            return digitos.replaceAll("(\\d{2})(\\d{5})(\\d{4})","($1) $2-$3");
        }
        return digitos.replaceAll("(\\d{2})(\\d{4})(\\d{4})","($1) $2-$3");
    }

}

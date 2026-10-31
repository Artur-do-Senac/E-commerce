package com.example.back;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UsuarioController {

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtCpf;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtSenha;

    // Mensagem que o back devolveu quando a resposta não foi 200 (ex.: "Ja existe um administrador cadastrado!")
    private String mensagemErro = "";



    @FXML
    protected void onSalvarButtonClick(ActionEvent event) throws Exception {
        if (!txtNome.getText().isEmpty() && !txtCpf.getText().isEmpty() && !txtEmail.getText().isEmpty() && !txtSenha.getText().isEmpty()) {


            // escapar() evita que aspas ou barras digitadas nos campos quebrem o JSON
            String jsonRequest = String.format("{\n" +
                    "  \"nome\": \"%s\",\n" +
                    "  \"email\": \"%s\",\n" +
                    "  \"senha\": \"%s\",\n" +
                    "  \"cpf\": \"%s\",\n" +
                    "  \"secretKey\": \"%s\"\n" +
                    "}", escapar(txtNome.getText()), escapar(txtEmail.getText()), escapar(txtSenha.getText()), escapar(txtCpf.getText()), "4fdmnbwer382j3blkdbf8932");

            int respostaAPI;

            try {
                respostaAPI = ExecutarMetodoAPI("http://localhost:8080/usuarios/admin", jsonRequest, "POST");
            } catch (IOException e) {
                showMessage(Alert.AlertType.ERROR, "Não foi possível conectar ao servidor. Verifique se o back-end está rodando!");
                return;
            }

            if(respostaAPI==200) {
                showMessage(Alert.AlertType.INFORMATION, "Usuário foi salvo com sucesso!");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/back/menu-view.fxml"));

                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                stage.setScene(scene);
            }else {
                showMessage(Alert.AlertType.ERROR, mensagemErro.isBlank() ? "Erro ao salvar usuário!" : "Erro ao salvar usuário: " + mensagemErro);
            }
        } else {
            showMessage(Alert.AlertType.ERROR, "Erro ao salvar usuário!");
        }
    }

    private void showMessage(Alert.AlertType type, String msg) {
        Alert alerta = new Alert(type);

        alerta.setTitle("Mensagem do sistema.");
        alerta.setHeaderText(null);
        alerta.setContentText(msg);

        alerta.showAndWait();
    }


    @FXML
    public void salvar(ActionEvent event) throws IOException {
        voltarParaMenu(event);
    }

    @FXML
    public void sair(ActionEvent event) throws IOException {
        voltarParaMenu(event);
    }

    private void voltarParaMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/back/menu-view.fxml"));

        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
    }

    private int ExecutarMetodoAPI(String url, String json, String protocoloHttp) throws Exception {
        URL urlAPI = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlAPI.openConnection();
        connection.setRequestMethod(protocoloHttp);
        connection.setDoOutput(true);

        if (!json.isEmpty()) {
            connection.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = connection.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

        }

        int codigo = connection.getResponseCode();

        // Quando o back recusa (400, 401...), a mensagem vem no corpo do erro
        mensagemErro = "";
        if (codigo != 200) {
            try (InputStream erro = connection.getErrorStream()) {
                if (erro != null) {
                    mensagemErro = new String(erro.readAllBytes(), StandardCharsets.UTF_8);
                }
            }
        }

        return codigo;

    }

    private String escapar(String texto) {
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}












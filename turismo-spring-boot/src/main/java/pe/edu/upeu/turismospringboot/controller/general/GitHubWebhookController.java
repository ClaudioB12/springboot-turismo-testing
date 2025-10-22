package pe.edu.upeu.turismospringboot.controller.general;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitHubWebhookController {

    @PostMapping("/github-webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload) {
        // Aquí puedes parsear el JSON recibido o imprimirlo en logs
        System.out.println("Webhook payload: " + payload);

        // Siempre responder 200 OK a GitHub
        return ResponseEntity.ok("Received");
    }
}

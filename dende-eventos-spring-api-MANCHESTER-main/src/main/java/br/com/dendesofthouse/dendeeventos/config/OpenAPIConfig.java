package br.com.dendesofthouse.dendeeventos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Dendê Eventos API")
                        .version("1.0.0")
                        .description("""
                                API para gerenciamento de eventos, usuários, organizadores e ingressos.
                                
                                ## Funcionalidades:
                                * Cadastro e gerenciamento de usuários
                                * Cadastro e gerenciamento de organizadores
                                * Criação e gerenciamento de eventos
                                * Compra e cancelamento de ingressos
                                * Feed de eventos disponíveis
                                
                                ## Regras de Negócio:
                                * Um organizador pode criar múltiplos eventos
                                * Evento pode ser principal ou sub-evento
                                * Ingressos podem ser cancelados com taxa
                                * Eventos inativos não permitem venda
                                """)
                        .contact(new Contact()
                                .name("Dendê Eventos Team")
                                .email("suporte@dendeventos.com")
                                .url("https://www.dendeventos.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://api.dendeventos.com")
                                .description("Servidor de Produção")
                ))
                .tags(List.of(
                        new Tag().name("Usuários").description("Operações relacionadas a usuários comuns"),
                        new Tag().name("Organizadores").description("Operações relacionadas a organizadores de eventos"),
                        new Tag().name("Eventos").description("Gerenciamento de eventos"),
                        new Tag().name("Feed").description("Feed público de eventos"),
                        new Tag().name("Ingressos").description("Compra e cancelamento de ingressos")
                ));
    }
}

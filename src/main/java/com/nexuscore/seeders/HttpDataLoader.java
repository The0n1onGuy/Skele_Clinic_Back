//package com.nexuscore.seeders;
//
//import com.nexuscore.models.system.HttpStatusCode;
//import com.nexuscore.repositories.system.IHttpStatusCodeRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//@Component
//@Order(2) // Debe ejecutarse antes de cualquier módulo que use códigos HTTP
//public class HttpDataLoader implements CommandLineRunner {
//
//    private final IHttpStatusCodeRepository repository;
//
//    public HttpDataLoader(IHttpStatusCodeRepository repository) {
//        this.repository = repository;
//    }
//
//    @Override
//    @Transactional
//    public void run(String... args) {
//
//        if (repository.count() == 0) {
//
//            // 🔹 1xx
//            save(100, "Continue", "El cliente debe continuar con la solicitud");
//            save(101, "Switching Protocols", "El servidor cambia de protocolo");
//            save(102, "Processing", "El servidor está procesando la solicitud");
//
//            // 🔹 2xx
//            save(200, "OK", "Solicitud exitosa");
//            save(201, "Created", "Recurso creado correctamente");
//            save(202, "Accepted", "Solicitud aceptada pero no finalizada");
//            save(203, "Non-Authoritative Information", "Información no autorizada");
//            save(204, "No Content", "Sin contenido");
//            save(205, "Reset Content", "Reiniciar contenido");
//            save(206, "Partial Content", "Contenido parcial");
//
//            // 🔹 3xx
//            save(300, "Multiple Choices", "Múltiples opciones");
//            save(301, "Moved Permanently", "Recurso movido permanentemente");
//            save(302, "Found", "Recurso encontrado temporalmente");
//            save(303, "See Other", "Ver otro recurso");
//            save(304, "Not Modified", "No modificado");
//            save(305, "Use Proxy", "Usar proxy");
//            save(307, "Temporary Redirect", "Redirección temporal");
//            save(308, "Permanent Redirect", "Redirección permanente");
//
//            // 🔹 4xx
//            save(400, "Bad Request", "Solicitud incorrecta");
//            save(401, "Unauthorized", "No autorizado");
//            save(402, "Payment Required", "Pago requerido");
//            save(403, "Forbidden", "Acceso prohibido");
//            save(404, "Not Found", "Recurso no encontrado");
//            save(405, "Method Not Allowed", "Método no permitido");
//            save(406, "Not Acceptable", "No aceptable");
//            save(407, "Proxy Authentication Required", "Autenticación de proxy requerida");
//            save(408, "Request Timeout", "Tiempo de espera agotado");
//            save(409, "Conflict", "Conflicto");
//            save(410, "Gone", "Recurso eliminado");
//            save(411, "Length Required", "Longitud requerida");
//            save(412, "Precondition Failed", "Precondición fallida");
//            save(413, "Payload Too Large", "Carga demasiado grande");
//            save(414, "URI Too Long", "URI demasiado larga");
//            save(415, "Unsupported Media Type", "Tipo no soportado");
//            save(416, "Range Not Satisfiable", "Rango inválido");
//            save(417, "Expectation Failed", "Expectativa fallida");
//            save(418, "I'm a teapot", "Código de broma HTTP");
//            save(422, "Unprocessable Entity", "Entidad no procesable");
//            save(425, "Too Early", "Demasiado pronto");
//            save(426, "Upgrade Required", "Actualización requerida");
//            save(429, "Too Many Requests", "Demasiadas solicitudes");
//
//            // 🔹 5xx
//            save(500, "Internal Server Error", "Error interno del servidor");
//            save(501, "Not Implemented", "No implementado");
//            save(502, "Bad Gateway", "Puerta de enlace incorrecta");
//            save(503, "Service Unavailable", "Servicio no disponible");
//            save(504, "Gateway Timeout", "Tiempo de espera agotado");
//            save(505, "HTTP Version Not Supported", "Versión no soportada");
//
//            System.out.println(">>> HTTP Status Codes: Carga inicial completada correctamente.");
//        }
//    }
//
//    /**
//     * Método defensivo: evita duplicados incluso si se ejecuta más de una vez.
//     */
//    private void save(int code, String name, String description) {
//
//        boolean exists = repository.findAll().stream()
//                .anyMatch(c -> c.getCode() == code);
//
//        if (!exists) {
//            HttpStatusCode status = new HttpStatusCode();
//            status.setCode(code);
//            status.setName(name);
//            status.setDescription(description);
//            repository.save(status);
//        }
//    }
//}
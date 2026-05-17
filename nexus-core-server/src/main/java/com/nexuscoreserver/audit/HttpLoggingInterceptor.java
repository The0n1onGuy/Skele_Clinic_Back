package com.nexuscoreserver.audit;
import com.nexuscoreserver.services.system.HttpStatusService;
import com.nexuscoreserver.services.system.Systemlogservice;

// Importaciones para manejar solicitudes HTTP
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Importaciones de Spring
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// Indica que esta clase es un componente gestionado por Spring
@Component
public class HttpLoggingInterceptor implements HandlerInterceptor {

    // Servicio encargado de guardar logs
    private final Systemlogservice logService;

    // Servicio para consultar información de códigos HTTP
    private final HttpStatusService httpStatusService;

    // Constructor con inyección de dependencias
    public HttpLoggingInterceptor(Systemlogservice logService,
                                  HttpStatusService httpStatusService) {
        this.logService = logService;
        this.httpStatusService = httpStatusService;
    }

    // Metodo que se ejecuta antes de que la petición llegue al controlador
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        // Guarda el tiempo de inicio de la petición
        request.setAttribute("startTime", System.currentTimeMillis());

        // Permite que la petición continúe su flujo
        return true;
    }

    // Metodo que se ejecuta al finalizar completamente la petición (incluyendo errores)
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {

        // Recupera el tiempo de inicio guardado previamente
        long startTime = (long) request.getAttribute("startTime");

        // Calcula la duración total de la petición
        long duration = System.currentTimeMillis() - startTime;

        // Obtiene el código de estado HTTP de la respuesta
        int statusCode = response.getStatus();



        // Obtiene el metodo HTTP (GET, POST, etc.)
        String method = request.getMethod();

        // Obtiene la URI solicitada
        String uri = request.getRequestURI();

        // Construye el detalle del log
        String detail = String.format(
                "Método: %s | URI: %s | Tiempo: %d ms",
                method,
                uri,
                duration
        );

        // Si ocurrió una excepción, se agrega al detalle
        if (ex != null) {
            detail += " | Error: " + ex.getMessage();
        }
    }
}
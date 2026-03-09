package com.expedienteclinico.expedienteclinico;


import com.expedienteclinico.expedienteclinico.services.audit.AuditLogsService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class GlobalAuditAspect {

    @Autowired
    private AuditLogsService auditService;
    //LISTA NEGRA PARA CONTROLADORES
    private final List<String> BLACKLIST_CONTROLLERS = Arrays.asList(
            "SystemAuthController"
            // , "NombreDelControlador" USA ESTO COMO PLANTILLA
    );

    //Obten el directorio de los controladores (lo que necesitamos que vigile)
    @Pointcut("execution(* com.expedienteclinico.expedienteclinico.controllers..*.*(..))")
    public void allControllerMethods() {}

    //Funcion que maneja el registro para culpar y subir
    @AfterReturning(pointcut = "allControllerMethods()", returning = "result")
    public void interceptByMapping(JoinPoint joinPoint, Object result) {
        //Primero identificamos de donde proviene por nombre
        String className = joinPoint.getTarget().getClass().getSimpleName();

        // Si el controlador está en la lista negra, salimos de inmediato y no registramos nada
        if (BLACKLIST_CONTROLLERS.contains(className)) {
            return;
        }

        // Obtenemos la firma del método para analizar sus anotaciones
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String action = "";
        String statusLabel = "Active"; // Estado por defecto

        // LÓGICA BASADA EN ANOTACIONES (MAPPINGS)
        if (method.isAnnotationPresent(PostMapping.class)) {
            statusLabel = "Added";
        } else if (method.isAnnotationPresent(PutMapping.class) || method.isAnnotationPresent(PatchMapping.class)) {
            statusLabel = "Edited";
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            statusLabel = "Deleted";
        } else {
            // Casos Ignoramos GetMapping y cualquier otro que no sea de escritura
            return;
        }

        // EXTRACCIÓN DE NOMBRES DINÁMICOS
        // Obtenemos el nombre del controlador (ej: DepartmentsController -> Departments)
        String controllerName = joinPoint.getTarget().getClass().getSimpleName().replace("Controller", "");

        // Construimos el concepto para el log
        //String concepto = String.format("[%s] Acción realizada en el módulo %s", action, controllerName);
        String concepto = String.format("Módulo afectado: %s", controllerName);

        // PERSISTENCIA
        // Enviamos al servicio que ya tiene el DBUSER (culpable) configurado
        auditService.logAction(concepto, statusLabel);
    }

}
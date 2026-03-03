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

@Aspect
@Component
public class GlobalAuditAspect {

    @Autowired
    private AuditLogsService auditService;


    @Pointcut("execution(* com.expedienteclinico.expedienteclinico.controllers..*.*(..))")
    public void allControllerMethods() {}

    @AfterReturning(pointcut = "allControllerMethods()", returning = "result")
    public void interceptByMapping(JoinPoint joinPoint, Object result) {
        // Obtenemos la firma del método para analizar sus anotaciones
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String action = "";
        String statusLabel = "Activo"; // Estado por defecto

        // LÓGICA BASADA EN ANOTACIONES DE SPRING WEB (MAPPINGS)
        if (method.isAnnotationPresent(PostMapping.class)) {
//            action = "CREACION";
            statusLabel = "Agregado";
        } else if (method.isAnnotationPresent(PutMapping.class) || method.isAnnotationPresent(PatchMapping.class)) {
            //          action = "CAMBIOS";
            statusLabel = "Editado";
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            //        action = "ELIMINACION";
            statusLabel = "Eliminado";
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
        auditService.registrarAccion(concepto, statusLabel);
    }

}
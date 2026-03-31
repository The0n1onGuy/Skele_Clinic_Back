<?php
// Config/endpoints.php

return [
    'baseUrl' => 'http://localhost:2026', // Cambia al puerto de tu Spring Boot
    'routes' => [
        'auth'        => '/api/auth/login',
        'rrhh'        => '/api/rrhh/departments',
        'patients'    => '/api/patients',
        'expedientes' => '/api/expedientes',
    ]
];
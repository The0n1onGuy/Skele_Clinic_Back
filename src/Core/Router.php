<?php
namespace Core;
use Services\RrhhService;
use Services\authService;
class Router {
    private $apiClient;
    private $config;
    public function __construct($apiClient, $config) {
        $this->apiClient = $apiClient;
        $this->config = $config;
    }
    public function resolve($action, $method, $inputData, $id)
    {
        $basePath = $this->config['routes'][$action] ?? null;

        if (!$basePath) {
            return ['status' => 404, 'data' => ['error' => "Acción '$action' no encontrada"]];
        }
        return match ($action) {
            'auth' => (new authService($this->apiClient, $basePath))->handle($method, $inputData),
            'rrhh' => (new RrhhService($this->apiClient, $basePath))->handle($method, $inputData, $id),
            // 'patients' => (new PatientService($this->apiClient, $basePath))->handle($method, $inputData, $id),
            default => ['status' => 404, 'data' => ['error' => 'Servicio no implementado']]
        };
    }
}

<?php
namespace Services;

class authService {
    private $apiClient;
    private $basePath;

    public function __construct($apiClient, $basePath) {
        $this->apiClient = $apiClient;
        $this->basePath = $basePath;
    }

    public function handle($method, $inputData) {
        if ($method !== 'POST') {
            return ['status' => 405, 'data' => ['error' => 'Login solo permite POST']];
        }
        // Llamamos directamente al endpoint de Spring Boot
        return $this->apiClient->post($this->basePath, $inputData);
    }
}
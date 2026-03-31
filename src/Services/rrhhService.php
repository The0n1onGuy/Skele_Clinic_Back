<?php

namespace Services;

class rrhhService{
    private $apiClient;
    private $basePath;

    public function __construct($apiClient, $basePath) {
        $this->apiClient = $apiClient;
        $this->basePath = $basePath;
    }

    public function handle($method, $inputData, $id = null) {
        return match ($method) {
            'GET'    => $this->apiClient->get($this->basePath . '/all'),
            'POST'   => $this->apiClient->post($this->basePath . '/post', $inputData),
            'PUT'    => $id ? $this->apiClient->put($this->basePath . "/update/{$id}", $inputData) : ['status' => 400, 'data' => ['error' => 'ID requerido']],
            'DELETE' => $id ? $this->apiClient->delete($this->basePath . "/delete/{$id}") : ['status' => 400, 'data' => ['error' => 'ID requerido']],
            default  => ['status' => 405, 'data' => ['error' => 'Método no permitido']]
        };
    }
}
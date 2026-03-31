<?php

class ApiClient {
    private string $baseUrl;
    private array $headers;

    /**
     * Constructor de ApiClient.
     *
     * @param string $baseUrl URL base del API (ej. 'http://localhost:8080').
     * @param array $headers Cabeceras adicionales opcionales.
     */
    public function __construct(string $baseUrl, array $headers = []) {
        $this->baseUrl = rtrim($baseUrl, '/');
        $this->headers = [
            'Content-Type: application/json',
            'Accept: application/json',
            ...$headers
        ];
    }

    /**
     * Permite inyectar un token JWT dinámicamente.
     *
     * @param string|null $token El token JWT o null para no agregarlo si no se proporciona autorización.
     */
    public function setBearerToken(?string $token): void {
        if (!empty($token)) {
            $this->headers[] = "Authorization: Bearer {$token}";
        }
    }

    /**
     * Realiza una solicitud GET.
     *
     * @param string $endpoint El endpoint relativo (ej. '/api/patients').
     * @param array $queryParams Parámetros de consulta opcionales.
     * @return array Respuesta del API con 'status' y 'data'.
     */
    public function get(string $endpoint, array $queryParams = []): array {
        if (!empty($queryParams)) {
            $endpoint .= '?' . http_build_query($queryParams);
        }
        return $this->makeRequest('GET', $endpoint);
    }

    /**
     * Realiza una solicitud POST.
     *
     * @param string $endpoint El endpoint relativo.
     * @param array $data Datos a enviar en el cuerpo.
     * @return array Respuesta del API.
     */
    public function post(string $endpoint, array $data = []): array {
        return $this->makeRequest('POST', $endpoint, $data);
    }

    /**
     * Realiza una solicitud PUT.
     *
     * @param string $endpoint El endpoint relativo.
     * @param array $data Datos a enviar en el cuerpo.
     * @return array Respuesta del API.
     */
    public function put(string $endpoint, array $data = []): array {
        return $this->makeRequest('PUT', $endpoint, $data);
    }

    /**
     * Realiza una solicitud DELETE.
     *
     * @param string $endpoint El endpoint relativo.
     * @return array Respuesta del API.
     */
    public function delete(string $endpoint): array {
        return $this->makeRequest('DELETE', $endpoint);
    }

    private function makeRequest(string $method, string $endpoint, ?array $data = null): array {
        $url = "{$this->baseUrl}/" . ltrim($endpoint, '/');
        $ch = curl_init();

        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $this->headers);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, $method);
        curl_setopt($ch, CURLOPT_TIMEOUT, 30); // Timeout de 30 segundos
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, true); // Verificar certificados SSL

        if ($data !== null && in_array($method, ['POST', 'PUT', 'PATCH'])) {
            curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
        }

        try {
            $response = curl_exec($ch);

            if (curl_errno($ch)) {
                $error = curl_error($ch);
                return [
                    'status' => 500,
                    'error' => true,
                    'mensaje' => "Error de conexión hacia API: {$error}"
                ];
            }

            $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
            $decodedResponse = json_decode($response, true, 512, JSON_THROW_ON_ERROR);

            return [
                'status' => $httpCode,
                'data' => $decodedResponse
            ];
        } catch (JsonException $e) {
            return [
                'status' => 502, // Bad Gateway, ya que el backend envió respuesta no JSON
                'error' => true,
                'mensaje' => "Respuesta del backend no es JSON válido: {$e->getMessage()}"
            ];
        } finally {
            curl_close($ch);
        }
    }
}
<?php
declare(strict_types=1);

// =========================================================================
// 1. CONFIGURACIÓN DE CABECERAS Y CORS (Crucial para el Frontend)
// =========================================================================
header('Access-Control-Allow-Origin: *'); // En producción, cambia '*' por la URL de tu frontend (ej. http://localhost:3000)
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Authorization');
header('Content-Type: application/json; charset=utf-8');

// Manejo de la petición OPTIONS (Pre-flight request de los navegadores)
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

// =========================================================================
// 2. CLASE DEL CLIENTE API
// =========================================================================
/**
 * Clase ApiClient
 * Cliente cURL robusto para consumir APIs REST externas.
 */
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

// =========================================================================
// 3. CONTROLADOR FRONTAL (El "Servidor" PHP que atiende a tu Frontend)
// =========================================================================

// Configuración de la URL de tu backend Spring Boot
$apiUrl = 'http://localhost:2026';
$cliente = new ApiClient($apiUrl);

// Extraer el token de autorización que envía el frontend y pasarlo a Spring Boot
$requestHeaders = getallheaders();
if (isset($requestHeaders['Authorization']) && is_string($requestHeaders['Authorization'])) {
    $token = str_replace('Bearer ', '', $requestHeaders['Authorization']);
    $cliente->setBearerToken($token);
}

// Variables de la petición entrante
$action = $_GET['action'] ?? '';
$method = $_SERVER['REQUEST_METHOD'];

// Leer el cuerpo de la petición (Payload JSON) para POST y PUT
$inputData = json_decode(file_get_contents('php://input'), true);
if ($inputData === null && json_last_error() !== JSON_ERROR_NONE) {
    http_response_code(400);
    echo json_encode(['error' => true, 'mensaje' => 'JSON inválido en el cuerpo de la petición']);
    exit();
}

// Enrutador
try {
    $resultado = match ($action) {
        'rrhh' => (function() use ($cliente, $method, $inputData) {
            $basePath = '/api/rrhh/departments';

            return match ($method) {
                'GET'    => $cliente->get($basePath . '/all'),
                'POST'   => $cliente->post($basePath . '/post', $inputData),
                'PUT'    => $cliente->put($basePath . "/update/" . ($_GET['id'] ?? ''), $inputData),
                'DELETE' => $cliente->delete($basePath . "/delete/" . ($_GET['id'] ?? '')),
                default  => null,
            };
        })(),
        'patients' => match ($method) {
            'GET' => $cliente->get('/api/patients'),
            'POST' => $cliente->post('/api/patients', $inputData),
            default => null,
        },
        'expedientes' => match ($method) {
            'GET' => (function() use ($cliente) {
                $endpoint = '/api/expedientes';
                if (isset($_GET['id']) && is_numeric($_GET['id']) && (int)$_GET['id'] > 0) {
                    $endpoint .= '/' . (int)$_GET['id'];
                }
                return $cliente->get($endpoint);
            })(),
            'POST' => $cliente->post('/api/expedientes', $inputData),
            default => null,
        },
        default => null,
    };

    if ($resultado === null) {
        http_response_code(400);
        echo json_encode([
            'error' => true, 
            'mensaje' => 'Acción o método HTTP no válido. Acciones soportadas: patients, expedientes'
        ]);
    } else {
        http_response_code($resultado['status']);
        echo json_encode($resultado);
    }
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode([
        'error' => true, 
        'mensaje' => "Error interno del servidor PHP: {$e->getMessage()}"
    ]);
}
<?php

header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

require_once '../src/Core/ApiClient.php';
require_once '../src/Core/Router.php';
require_once '../src/Services/rrhhService.php';
require_once '../src/Services/authService.php';
$config = require_once '../src/Config/endpoints.php';

// 1. Inicializar Motor
$cliente = new ApiClient($config['baseUrl']);

// 2. Capturar datos de la petición
$action    = $_GET['action'] ?? '';
$id        = $_GET['id'] ?? null;
$method    = $_SERVER['REQUEST_METHOD'];
$inputData = json_decode(file_get_contents('php://input'), true) ?? [];

// 3. Pasar el Token JWT si viene del Frontend
$headers = getallheaders();
if (isset($headers['Authorization'])) {
    $cliente->setBearerToken(str_replace('Bearer ', '', $headers['Authorization']));
}

// 4. Ejecutar Enrutador
$router = new \Core\Router($cliente, $config);
$resultado = $router->resolve($action, $method, $inputData, $id);

// 5. Responder al Frontend
http_response_code($resultado['status'] ?? 200);
echo json_encode($resultado['data'] ?? $resultado);

//Composer version
//require_once '../vendor/autoload.php';
//
//$router = new Router();
//$response = $router->resolve($_GET['action'], $_SERVER['REQUEST_METHOD']);
//
//echo json_encode($response);
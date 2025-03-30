package eci.ieti.safezone.config;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;

public class DynamoDBConnection {
    public static void main(String[] args) {
        try {
            // Obtener credenciales de variables de entorno
            String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
            String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
            
            if (accessKey == null || secretKey == null) {
                throw new IllegalArgumentException("Las credenciales de AWS no están configuradas correctamente.");
            }
            
            // Configurar cliente DynamoDB
            DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                    .region(Region.US_EAST_2) // Cambia esto según tu región
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)
                    ))
                    .build();
            
            // Crear cliente mejorado para manejar tablas
            DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(dynamoDbClient)
                    .build();
            
            System.out.println("Conexión exitosa a DynamoDB");
        } catch (Exception e) {
            System.err.println("Error al conectar con DynamoDB: " + e.getMessage());
        }
    }
}

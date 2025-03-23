package eci.ieti.safezone.service;

import eci.ieti.safezone.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class UserService {
    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<User> userTable;
    private final DynamoDbClient dynamoDbClient;

    @Autowired
    public UserService(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient; // Inicializar el campo
        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        this.userTable = enhancedClient.table("users", TableSchema.fromBean(User.class));
    }

    public void save(User user) {
        userTable.putItem(user);
    }

    public Optional<User> findById(String id) {
        return Optional.ofNullable(userTable.getItem(r -> r.key(k -> k.partitionValue(id))));
    }
    public void deleteUserById(String id) {
        userTable.deleteItem(r -> r.key(k -> k.partitionValue(id)));
    }
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        userTable.scan(ScanEnhancedRequest.builder().build())
                .items()
                .forEach(users::add);

        return users;
    }
    public User validateUser(String username, String password) {
        return findAll().stream()
                .filter(user -> user.getName().equals(username) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public List<User> findByName(String name) {
        List<User> users = new ArrayList<>();

        userTable.scan(ScanEnhancedRequest.builder().build())
                .items()
                .forEach(user -> {
                    if (user.getName().equalsIgnoreCase(name)) {
                        users.add(user);
                    }
                });

        return users;
    }

}

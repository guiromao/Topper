package co.topper.domain.data.repository;

import co.topper.domain.data.entity.Role;
import co.topper.domain.data.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class UserRepositoryCustomImplTests {

    @Mock
    MongoTemplate mongoTemplate;

    UserRepositoryCustomImpl userRepositoryCustom;

    @BeforeEach
    void setup() {
        userRepositoryCustom = new UserRepositoryCustomImpl(mongoTemplate);
    }

    @Test
    void testUpdateUser() {
        Mockito.when(mongoTemplate.findAndModify(any(Query.class), any(Update.class),
                any(FindAndModifyOptions.class), any(Class.class)))
                .thenReturn(Mockito.mock(UserEntity.class));

        Update update = UserEntity.UpdateBuilder.create()
                .setAvailableVotes(10000L)
                .setUsername("new-username")
                .setRoles(Set.of(Role.USER, Role.ADMIN))
                .build().orElseThrow(() -> new RuntimeException("Error creating User update"));

        userRepositoryCustom.updateUser("user@mail.com", update);

        Mockito.verify(mongoTemplate, Mockito.times(1))
                .findAndModify(any(Query.class), any(Update.class),
                        any(FindAndModifyOptions.class), any(Class.class));
    }

}

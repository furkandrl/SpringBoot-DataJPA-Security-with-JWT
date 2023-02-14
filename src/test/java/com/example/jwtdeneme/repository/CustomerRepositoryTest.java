package com.example.jwtdeneme.repository;

import com.example.jwtdeneme.model.Customer;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;




import static org.junit.jupiter.api.Assertions.assertEquals;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CustomerRepositoryTest {

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    void itShouldFindCustomerByName_WhenCustomerExists() throws Exception{
        String username = "username";

        Customer expected = new Customer();
        expected.setUsername(username);
        expected.setEmail("email@email.com");
        expected.setPassword("password");

        customerRepository.save(expected);

        Customer actual = customerRepository.findByUsername(username).get();

        assertEquals(expected, actual);
    }

}
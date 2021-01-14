package com.bank.account.service;

import com.bank.account.model.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ClientServiceIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Test
    public void testCreateAndFind() {
        // Given
        Client client = Client.builder()
                .firstname("Anduin")
                .lastname("Wrynn")
                .build();

        // When
        Client clientCreated = clientService.createClient(client);
        Client clientFound = clientService.findClient(clientCreated.getId());

        // Then
        assertThat(clientCreated.getId()).isNotEmpty();
        assertThat(clientCreated.getFirstname()).isEqualTo(client.getFirstname());
        assertThat(clientCreated.getLastname()).isEqualTo(client.getLastname());
        assertThat(clientFound).isEqualTo(clientCreated);
    }

}

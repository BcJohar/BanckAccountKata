package com.bank.account.service;

import com.bank.account.model.Client;
import com.bank.account.repository.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.bank.account.util.ValidatorUtil.validate;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.Validate.notEmpty;

/**
 * Service for {@link Client}s.
 */
@Service
public class ClientService {

    private ClientRepository clientRepository;

    /**
     * Constructor with parameter.
     * @param clientRepository instance of {@link ClientRepository}
     */
    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = requireNonNull(clientRepository);
    }

    /**
     * Create a new client.
     * @param client to create
     * @return created {@link Client}
     */
    public Client createClient(Client client) {
        return clientRepository.save(validate(client));
    }

    /**
     * Finds a client by id.
     * @param id as search criteria
     * @return {@link Client}
     */
    public Client findClient(String id) {
        return clientRepository.findOne(notEmpty(id));
    }

}

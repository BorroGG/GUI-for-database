package org.labs.service;

import org.labs.mapper.CustomerMapper;
import org.labs.model.Customer;
import org.labs.model.Mission;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private CustomerMapper customerMapper;

    public void save(Customer saveDTO) {
        Customer customer = mapper.map(saveDTO, Customer.class);
        customerMapper.save(customer);
    }

    public void update(Customer updateDTO) {
        Customer customer = mapper.map(updateDTO, Customer.class);
        customerMapper.update(customer);
    }

    public List<Customer> selectAllCustomers() {
        return customerMapper.selectAllCustomers();
    }

    public Customer selectCustomerById(Customer selectDTO) {
        Customer customer = mapper.map(selectDTO, Customer.class);
        return customerMapper.selectCustomerById(customer);
    }

    public void delete(Customer deleteDTO) {
        Customer customer = mapper.map(deleteDTO, Customer.class);
        customerMapper.delete(customer);
    }
}

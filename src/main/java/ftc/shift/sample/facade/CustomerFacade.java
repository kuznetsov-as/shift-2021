package ftc.shift.sample.facade;

import ftc.shift.sample.dto.CustomerDtoRequest;
import ftc.shift.sample.dto.CustomerDtoResponse;
import ftc.shift.sample.entity.Customer;
import ftc.shift.sample.exception.DataNotFoundException;
import ftc.shift.sample.mapper.CustomerMapper;
import ftc.shift.sample.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerFacade {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerFacade(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    public CustomerDtoResponse createCustomer(CustomerDtoRequest customer) {
        Customer creatingCustomer = customerMapper.dtoRequestToCustomer(customer);
        Customer createdCustomer = customerService.createCustomer(creatingCustomer);
        return customerMapper.customerToDtoResponse(createdCustomer);
    }

    public CustomerDtoResponse getCustomer(Long customerId) throws DataNotFoundException {
        return customerMapper.customerToDtoResponse(customerService.getCustomer(customerId));
    }

    public CustomerDtoResponse updateCustomer(CustomerDtoRequest customer, Long customerId) throws DataNotFoundException {
        Customer updatingCustomer = customerMapper.dtoRequestToCustomer(customer);
        Customer updatedCustomer = customerService.updateCustomer(updatingCustomer, customerId);
        return customerMapper.customerToDtoResponse(updatedCustomer);
    }

    public void deleteCustomer(Long customerId) {
        customerService.deleteCustomer(customerId);
    }

    public List<CustomerDtoResponse> getAllCustomers() {
        List<Customer> customerList = customerService.getAllCustomers();
        List<CustomerDtoResponse> customerDtoResponseList = new ArrayList<>();
        customerList.forEach(user -> customerDtoResponseList.add(customerMapper.customerToDtoResponse(user)));
        return customerDtoResponseList;
    }
}

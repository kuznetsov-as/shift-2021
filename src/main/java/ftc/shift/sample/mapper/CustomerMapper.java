package ftc.shift.sample.mapper;

import ftc.shift.sample.dto.CustomerDtoRequest;
import ftc.shift.sample.dto.CustomerDtoResponse;
import ftc.shift.sample.entity.Customer;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    Customer dtoRequestToCustomer(CustomerDtoRequest customerDtoRequest);

    Customer dtoResponseToCustomer(CustomerDtoResponse customerDtoResponse);

    CustomerDtoResponse customerToDtoResponse(Customer customer);
}

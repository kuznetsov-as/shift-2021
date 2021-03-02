package ftc.shift.sample.mapper;

import ftc.shift.sample.dto.UserDtoRequest;
import ftc.shift.sample.dto.UserDtoResponse;
import ftc.shift.sample.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User dtoRequestToUser(UserDtoRequest userDtoRequest);

    User dtoResponseToUser(UserDtoResponse userDtoResponce);

    UserDtoResponse userToDtoResponse(User user);
}

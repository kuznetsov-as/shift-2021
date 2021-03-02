package ftc.shift.sample.mapper;

import ftc.shift.sample.dto.UserDtoRequest;
import ftc.shift.sample.dto.UserDtoResponse;
import ftc.shift.sample.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-02T15:39:45+0700",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User dtoRequestToUser(UserDtoRequest userDtoRequest) {
        if ( userDtoRequest == null ) {
            return null;
        }

        User user = new User();

        user.setType( userDtoRequest.getType() );
        user.setName( userDtoRequest.getName() );
        user.setRegistrationDate( userDtoRequest.getRegistrationDate() );

        return user;
    }

    @Override
    public User dtoResponseToUser(UserDtoResponse userDtoResponse) {
        if ( userDtoResponse == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDtoResponse.getId() );
        user.setType( userDtoResponse.getType() );
        user.setName( userDtoResponse.getName() );
        user.setRegistrationDate( userDtoResponse.getRegistrationDate() );

        return user;
    }

    @Override
    public UserDtoResponse userToDtoResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserDtoResponse userDtoResponse = new UserDtoResponse();

        if ( user.getId() != null ) {
            userDtoResponse.setId( user.getId() );
        }
        userDtoResponse.setType( user.getType() );
        userDtoResponse.setName( user.getName() );
        userDtoResponse.setRegistrationDate( user.getRegistrationDate() );

        return userDtoResponse;
    }
}

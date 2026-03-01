package org.example.mapper;

import org.example.dto.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserHateoasModelAssemblerTest {
    private final UserHateoasModelAssembler assembler = new UserHateoasModelAssembler();

    private final String name = "testName";
    private final String email = "testEmail";
    private final int age = 13;
    private final int id = 4;
    private final UserResponseDto testUserResponseDto = new UserResponseDto(id,
            name,
            email,
            age,
            LocalDate.now());

    @Test
    void toModelTest_LinksAndFieldsAreCorrect() {
        EntityModel<UserResponseDto> testModel = assembler.toModel(testUserResponseDto);

        assertTrue(testModel.getLinks().hasSize(2));
        assertNotNull(testModel.getLink("self"));
        assertNotNull(testModel.getLink("users"));
        assertNotNull(testModel.getContent());
        assertEquals(age, testModel.getContent().age());
        assertEquals(id, testModel.getContent().id());
        assertEquals(email, testModel.getContent().email());
        assertEquals(name, testModel.getContent().name());
    }
}
package br.com.hcp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocationUserMapperTest {

    private LocationUserMapper locationUserMapper;

    @BeforeEach
    public void setUp() {
        locationUserMapper = new LocationUserMapperImpl();
    }
}

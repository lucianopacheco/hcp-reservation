package br.com.hcp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.hcp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationUserDTO.class);
        LocationUserDTO locationUserDTO1 = new LocationUserDTO();
        locationUserDTO1.setId(1L);
        LocationUserDTO locationUserDTO2 = new LocationUserDTO();
        assertThat(locationUserDTO1).isNotEqualTo(locationUserDTO2);
        locationUserDTO2.setId(locationUserDTO1.getId());
        assertThat(locationUserDTO1).isEqualTo(locationUserDTO2);
        locationUserDTO2.setId(2L);
        assertThat(locationUserDTO1).isNotEqualTo(locationUserDTO2);
        locationUserDTO1.setId(null);
        assertThat(locationUserDTO1).isNotEqualTo(locationUserDTO2);
    }
}

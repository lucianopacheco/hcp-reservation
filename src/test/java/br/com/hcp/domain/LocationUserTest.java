package br.com.hcp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.hcp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationUser.class);
        LocationUser locationUser1 = new LocationUser();
        locationUser1.setId(1L);
        LocationUser locationUser2 = new LocationUser();
        locationUser2.setId(locationUser1.getId());
        assertThat(locationUser1).isEqualTo(locationUser2);
        locationUser2.setId(2L);
        assertThat(locationUser1).isNotEqualTo(locationUser2);
        locationUser1.setId(null);
        assertThat(locationUser1).isNotEqualTo(locationUser2);
    }
}

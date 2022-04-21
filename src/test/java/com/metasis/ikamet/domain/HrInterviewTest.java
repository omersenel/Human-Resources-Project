package com.metasis.ikamet.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.metasis.ikamet.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HrInterviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HrInterview.class);
        HrInterview hrInterview1 = new HrInterview();
        hrInterview1.setId(1L);
        HrInterview hrInterview2 = new HrInterview();
        hrInterview2.setId(hrInterview1.getId());
        assertThat(hrInterview1).isEqualTo(hrInterview2);
        hrInterview2.setId(2L);
        assertThat(hrInterview1).isNotEqualTo(hrInterview2);
        hrInterview1.setId(null);
        assertThat(hrInterview1).isNotEqualTo(hrInterview2);
    }
}

package com.metasis.ikamet.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.metasis.ikamet.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TechnicalInterviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TechnicalInterview.class);
        TechnicalInterview technicalInterview1 = new TechnicalInterview();
        technicalInterview1.setId(1L);
        TechnicalInterview technicalInterview2 = new TechnicalInterview();
        technicalInterview2.setId(technicalInterview1.getId());
        assertThat(technicalInterview1).isEqualTo(technicalInterview2);
        technicalInterview2.setId(2L);
        assertThat(technicalInterview1).isNotEqualTo(technicalInterview2);
        technicalInterview1.setId(null);
        assertThat(technicalInterview1).isNotEqualTo(technicalInterview2);
    }
}

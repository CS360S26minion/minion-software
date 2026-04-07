package com.example.seproj;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.example.seproj.model.Counselor;

public class CounselorTest {

    @Test
    public void counselorConstructorAndGetters_workCorrectly() {
        Counselor counselor = new Counselor(
                "c1",
                "Dr. Sarah Khan",
                "sarah@test.com",
                "Stress Management",
                "Experienced counselor",
                true
        );

        assertEquals("c1", counselor.getCounselorId());
        assertEquals("Dr. Sarah Khan", counselor.getName());
        assertEquals("sarah@test.com", counselor.getEmail());
        assertEquals("Stress Management", counselor.getSpecialization());
        assertEquals("Experienced counselor", counselor.getBio());
        assertTrue(counselor.isActive());
    }

    @Test
    public void counselorSetters_workCorrectly() {
        Counselor counselor = new Counselor();

        counselor.setCounselorId("c2");
        counselor.setName("Dr. Ali");
        counselor.setEmail("ali@test.com");
        counselor.setSpecialization("Academic Anxiety");
        counselor.setBio("Test bio");
        counselor.setActive(false);

        assertEquals("c2", counselor.getCounselorId());
        assertEquals("Dr. Ali", counselor.getName());
        assertEquals("ali@test.com", counselor.getEmail());
        assertEquals("Academic Anxiety", counselor.getSpecialization());
        assertEquals("Test bio", counselor.getBio());
        assertFalse(counselor.isActive());
    }
}
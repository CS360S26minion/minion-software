package com.example.seproj;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.example.seproj.model.Student;

public class StudentTest {

    @Test
    public void studentConstructorAndGetters_workCorrectly() {
        Student student = new Student(
                "s1",
                "Eman",
                "eman@test.com",
                "slot123"
        );

        assertEquals("s1", student.getStudentId());
        assertEquals("Eman", student.getName());
        assertEquals("eman@test.com", student.getEmail());
        assertEquals("slot123", student.getActiveAppointmentId());
    }

    @Test
    public void studentSetters_workCorrectly() {
        Student student = new Student();

        student.setStudentId("s2");
        student.setName("Ali");
        student.setEmail("ali@test.com");
        student.setActiveAppointmentId("slot999");

        assertEquals("s2", student.getStudentId());
        assertEquals("Ali", student.getName());
        assertEquals("ali@test.com", student.getEmail());
        assertEquals("slot999", student.getActiveAppointmentId());
    }

    @Test
    public void defaultConstructor_initializesEmptyObject() {
        Student student = new Student();

        assertNull(student.getStudentId());
        assertNull(student.getName());
        assertNull(student.getEmail());
        assertNull(student.getActiveAppointmentId());
    }
}
package service;

import domain.Grade;
import domain.Homework;
import domain.Pair;
import domain.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ServiceMockTest {
    @Mock
    StudentXMLRepository studentXmlRepoMock;
    @Mock
    HomeworkXMLRepository homeworkXmlRepoMock;
    @Mock
    GradeXMLRepository gradeXmlRepoMock;

    Service service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        service = new Service(studentXmlRepoMock, homeworkXmlRepoMock, gradeXmlRepoMock);
    }

    @Test
    public void saveStudent_Valid_InExistent() {
        when(studentXmlRepoMock.save(new Student("1", anyString(), 531)))
                .thenReturn(null);
        Assertions.assertEquals(1, service.saveStudent("1", "Anett", 531));
        verify(studentXmlRepoMock).findAll();
        verify(studentXmlRepoMock).save(new Student("1", "Anett", 531));
    }

    @Test
    public void saveStudent_Valid_Existent() {
        when(studentXmlRepoMock.findAll()).thenReturn(Collections.singletonList(new Student("1", "Anett", 531)));
        Assertions.assertEquals(0, service.saveStudent("1", "Anett", 531));
        verify(studentXmlRepoMock).findAll();
        verify(studentXmlRepoMock, never()).save(new Student("1", "Anett", 531));
    }

    @Test
    public void saveHomework_Valid_InExistent() {
        when(homeworkXmlRepoMock.save(new Homework("4", "ketto", 6,5)))
                .thenReturn(null);
        Assertions.assertEquals(1, service.saveHomework("4", "ketto", 6,5));
        verify(homeworkXmlRepoMock).findAll();
        verify(homeworkXmlRepoMock).save(new Homework("4", "ketto", 6,5));
    }

    @Test
    public void saveHomework_Valid_Existent() {
        when(homeworkXmlRepoMock.findAll()).thenReturn(
                Collections.singletonList(new Homework("4", "ketto", 6,5)));
        Assertions.assertEquals(0, service.saveHomework("4", "ketto", 6,5));
        verify(homeworkXmlRepoMock).findAll();
        verify(homeworkXmlRepoMock, never()).save(new Homework("4", "ketto", 6,5));
    }

    @Test
    void saveGrade_Valid_NotExistent() {
        when(studentXmlRepoMock.findOne(anyString()))
                .thenReturn(new Student("2", "Anna", 531));
        when(homeworkXmlRepoMock.findOne(anyString()))
                .thenReturn(new Homework("2", "alma", 6, 4));
        when(gradeXmlRepoMock.save(new Grade(new Pair<>("2", "2"), 4.5, 7, "ugyes")))
                .thenReturn(null);
        Assertions.assertEquals(1,
                service.saveGrade("2", "2", 7, 7, "ugyes"));
        verify(studentXmlRepoMock).findOne("2");
        verify(homeworkXmlRepoMock, times(2)).findOne("2");
        verify(gradeXmlRepoMock).save(new Grade(any(), 4.5, 7, "ugyes"));
    }

    @Test
    void saveGrade_InvalidStudent() {
        when(studentXmlRepoMock.findOne(anyString())).thenReturn(null);
        Assertions.assertEquals(-1,
                service.saveGrade("555", "2", 7, 7, "ugyes"));
        verify(studentXmlRepoMock).findOne("555");
    }

    @Test
    void saveGrade_InvalidHomework() {
        when(studentXmlRepoMock.findOne(anyString())).thenReturn(new Student("2", "", 531));
        when(homeworkXmlRepoMock.findOne(anyString())).thenReturn(null);
        Assertions.assertEquals(-1,
                service.saveGrade("2", "555", 7, 7, "ugyes"));
        verify(studentXmlRepoMock).findOne("2");
        verify(homeworkXmlRepoMock).findOne("555");
    }
}

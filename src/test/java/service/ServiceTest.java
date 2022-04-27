package service;

import domain.Grade;
import domain.Homework;
import domain.Student;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;
import validation.*;

import java.util.Arrays;
import java.util.List;

class ServiceTest {
    private StudentXMLRepository studentXmlRepo;
    private HomeworkXMLRepository homeworkXmlRepo;
    private GradeXMLRepository gradeXmlRepo;

    private Service service;

    List<Student> students = Arrays.asList(
            new Student("1", "Maria", 222),
            new Student("2", "Maria1", 223),
            new Student("4", "Ion", 227),
            new Student("5", "Jozsa Tihamer", 531),
            new Student("6", "Tihamer Attila", 532),
            new Student("7", "Attila Ancsa", 533),
            new Student("8", "Ancsa Jozsa", 534)
    );

    @BeforeAll
    static void init() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Homework> homeworkValidator = new HomeworkValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        StudentXMLRepository studentXmlRepo = new StudentXMLRepository(studentValidator, "students.xml");
        HomeworkXMLRepository homeworkXmlRepo = new HomeworkXMLRepository(homeworkValidator, "homework.xml");
        GradeXMLRepository gradeXmlRepo = new GradeXMLRepository(gradeValidator, "grades.xml");

        Service service = new Service(studentXmlRepo, homeworkXmlRepo, gradeXmlRepo);

        service.saveStudent("5", "Jozsa Tihamer", 531);
        service.saveStudent("6", "Tihamer Attila", 532);
        service.saveStudent("7", "Attila Ancsa", 533);
        service.saveStudent("8", "Ancsa Jozsa", 534);
    }

    @BeforeEach
    void setUp() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Homework> homeworkValidator = new HomeworkValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        this.studentXmlRepo = new StudentXMLRepository(studentValidator, "students.xml");
        this.homeworkXmlRepo = new HomeworkXMLRepository(homeworkValidator, "homework.xml");
        this.gradeXmlRepo = new GradeXMLRepository(gradeValidator, "grades.xml");

        this.service = new Service(this.studentXmlRepo, this.homeworkXmlRepo, this.gradeXmlRepo);
    }

    @AfterAll
    static void clean() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Homework> homeworkValidator = new HomeworkValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        StudentXMLRepository studentXmlRepo = new StudentXMLRepository(studentValidator, "students.xml");
        HomeworkXMLRepository homeworkXmlRepo = new HomeworkXMLRepository(homeworkValidator, "homework.xml");
        GradeXMLRepository gradeXmlRepo = new GradeXMLRepository(gradeValidator, "grades.xml");

        Service service = new Service(studentXmlRepo, homeworkXmlRepo, gradeXmlRepo);

        service.deleteStudent("9");
        service.deleteHomework("4");
    }

    @Test
    void findAllStudents() {
        Iterable<Student> allStudents = service.findAllStudents();
        allStudents.forEach(student ->
                Assertions.assertTrue("Kadosa".equals(student.getName()) || this.students.contains(student))
        );
    }

    @Test
    void saveStudent_ValidData() {
        Assertions.assertEquals(1, service.saveStudent("9", "Kadosa", 631));
    }

    @Test
    void saveStudent_InvalidData() {
        Assertions.assertThrows(ValidationException.class, () -> service.saveStudent("10","Kadosa", 5551));
    }

    @Test
    void saveHomework_Valid_InExistent() {
        Assertions.assertEquals(1, service.saveHomework("4", "ketto", 6,5));
    }

    @Test
    void saveHomework_Valid_Existing() {
        Assertions.assertEquals(0, service.saveHomework("1", "egy", 6,7));
    }

    @Test
    void saveHomework_Invalid_TimePeriod() {
        Assertions.assertThrows(ValidationException.class,
                () -> service.saveHomework("4", "egy", 6,7));
    }

    @Test
    void saveHomework_Invalid_DeadLine_StartLine() {
        Assertions.assertThrows(ValidationException.class,
                () -> service.saveHomework("4", "egy", 15,15));
    }

    //        delete from grades after use since there is no deletegrade option
    @Test
    void saveGrade_Valid_NotExistent() {
        Assertions.assertEquals(1,
                service.saveGrade("2", "2", 7, 7, "ugyes"));
    }
    @Test
    void saveGrade_Valid_Existent() {
        Assertions.assertEquals(0,
                service.saveGrade("1", "2", 7, 7, "ugyes"));
    }
    @Test
    void saveGrade_InvalidStudent() {
        Assertions.assertEquals(-1,
                service.saveGrade("555", "2", 7, 7, "ugyes"));
    }
    @Test
    void saveGrade_InvalidHomework() {
        Assertions.assertEquals(-1,
                service.saveGrade("2", "555", 7, 7, "ugyes"));
    }
    @Test
    void saveGrade_InvalidDelivered() {
        Assertions.assertThrows(ValidationException.class,
                () -> service.saveGrade("2", "2", 7, 5, "ugyes"));
    }
    @Test
    void saveGrade_InvalidGrade() {
        Assertions.assertThrows(ValidationException.class,
                () -> service.saveGrade("2", "2", 15, 7, "ugyes"));
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 6, 7, 8})
    void deleteStudent_CleanInit(Integer id) {
        Assertions.assertEquals(1, service.deleteStudent(id.toString()));
        id = -id;
        Assertions.assertNotEquals(1, service.deleteStudent(id.toString()));
    }
}
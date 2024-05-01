package com.lehuutri.studentmanagement.views.list;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.lehuutri.studentmanagement.domain.entities.CourseEntity;
import com.lehuutri.studentmanagement.domain.entities.CourseStudentEntity;
import com.lehuutri.studentmanagement.domain.entities.StudentEntity;
import com.lehuutri.studentmanagement.services.CourseService;
import com.lehuutri.studentmanagement.services.CourseStudentService;
import com.lehuutri.studentmanagement.services.StudentService;
import com.lehuutri.studentmanagement.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

@Route(value = "courses", layout = MainLayout.class)
public class CourseDetailView extends VerticalLayout implements HasUrlParameter<String> {
    private TextField name = new TextField("Course Name");
    private TextField lecturer = new TextField("Lecturer");
    private IntegerField year = new IntegerField("Year");
    private TextArea note = new TextArea("Note");
    private Grid<CourseStudentEntity> studentInCourseGrid;
    private Grid<StudentEntity> studentNotInCourseGrid;
    private TextField filter = new TextField("Filter");
    private VerticalLayout studentInCourseLayout;
    private TextField studentInCourseFilter = new TextField("Filter");
    private TextField studentNotInCourseFilter = new TextField("Filter");
    private VerticalLayout studentNotInCourseLayout;
    private String courseId;
    private transient CourseService courseService;
    private transient StudentService studentService;
    private transient CourseStudentService courseStudentService;
    private CourseEntity courseEntity;
    private List<CourseStudentEntity> attendees;
    private List<CourseStudentEntity> originalAttendees;
    private List<StudentEntity> students;
    
    public CourseDetailView(CourseService courseService, StudentService studentService, CourseStudentService courseStudentService) {
        this.courseService = courseService;
        this.courseStudentService = courseStudentService;
        this.studentService = studentService;
        
        attendees = new ArrayList<>();
        originalAttendees = new ArrayList<>();
        students = new ArrayList<>();

        studentInCourseGrid = new Grid<>(CourseStudentEntity.class);
        studentNotInCourseGrid = new Grid<>(StudentEntity.class);
        setSizeFull();

        configureStudentInCourseGrid();
        configureStudentNotInCourseGrid();
    }

    private void configureStudentInCourseGrid() {
        studentInCourseGrid.setSizeFull();
        studentInCourseGrid.removeAllColumns();
        studentInCourseGrid.addColumn(courseStudent -> courseStudent.getStudent().getStudentId())
                            .setSortable(true)
                            .setHeader("Student Id");
        
        studentInCourseGrid.addColumn(courseStudent -> courseStudent.getStudent().getName())
                            .setSortable(true)
                            .setHeader("Name");

        ((GridMultiSelectionModel<?>) studentInCourseGrid
            .setSelectionMode(Grid.SelectionMode.MULTI))
            .setSelectionColumnFrozen(true);

        studentInCourseGrid.addColumn("grade");
    }

    private void configureStudentNotInCourseGrid() {
        studentNotInCourseGrid.setSizeFull();
        studentNotInCourseGrid.setColumns("studentId", "name");

        ((GridMultiSelectionModel<?>) studentNotInCourseGrid
            .setSelectionMode(Grid.SelectionMode.MULTI))
            .setSelectionColumnFrozen(true);
    }

    private void configureForm() {
        removeAll();
        HorizontalLayout firstComponent = new HorizontalLayout(name, lecturer, year, note);
        name.setValue(courseEntity.getName());
        lecturer.setValue(courseEntity.getLecturer());
        year.setValue(courseEntity.getYear());
        note.setValue(courseEntity.getNote());
        firstComponent.setAlignItems(Alignment.BASELINE);
        note.setWidth("20em");

        configureStudentInCourseToolBar();

        configureStudentNotInCourseToolBar();

        HorizontalLayout secondComponent = new HorizontalLayout(studentInCourseLayout, studentNotInCourseLayout);
        secondComponent.setFlexGrow(1, studentInCourseGrid);
        secondComponent.setFlexGrow(1, studentNotInCourseGrid);
        secondComponent.setSizeFull();

        Button save = new Button("Save", e -> validateAndSave());
        Button delete = new Button("Delete", e -> deleteCourse());
        Button cancel = new Button("Cancel", e -> this.getUI().ifPresent(ui -> ui.navigate(CourseListView.class)));

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        add(new H3("Course: " + courseId), firstComponent, secondComponent, new Hr(), new HorizontalLayout(save, delete, cancel));

        updateAttendeeGrid();
        updateStudentGrid();
    }

    private void deleteCourse() {
        originalAttendees.forEach(cs -> courseStudentService.deleteByCourseAndStudent(courseEntity, cs.getStudent()));

        courseService.deleteById(courseId);

        this.getUI().ifPresent(ui -> ui.navigate(CourseListView.class));
    }

    private void validateAndSave() {
        if (name.isEmpty() || name.getValue().strip().length() == 0) {
            raiseError("Name must not be empty");

            return;
        }

        if (lecturer.isEmpty() || lecturer.getValue().strip().length() == 0) {
            raiseError("Lecturer must not be empty");

            return;
        }


        if (year.isEmpty() || year.getValue() < 0 || LocalDate.now().getYear() < year.getValue()) {
            raiseError("Input year is invalid");

            return;
        }

        courseEntity.setName(name.getValue());
        courseEntity.setLecturer(lecturer.getValue());
        courseEntity.setYear(year.getValue());
        courseEntity.setNote(note.getValue());

        courseService.saveCourse(courseEntity);

        originalAttendees.forEach(attendee -> {
            if (attendees.contains(attendee)) {
                Optional<CourseStudentEntity> cs = courseStudentService.findByCourseAndStudent(courseEntity, attendee.getStudent());
                if (cs.isPresent()) {
                    CourseStudentEntity e = cs.get();
                    e.setGrade(attendee.getGrade());
                    courseStudentService.saveCourseStudent(e);
                }

            } else {
                courseStudentService.deleteByCourseAndStudent(courseEntity, attendee.getStudent());            
            }
        });

        attendees.forEach(attendee -> courseStudentService.saveCourseStudent(attendee));

        UI.getCurrent().getPage().reload();
    }

    private void raiseError(String error) {
        Dialog errDialog = new Dialog();
        errDialog.setHeaderTitle("ERROR");
        errDialog.setModal(true);

        Button closeButton = new Button(
            new Icon("lumo", "cross"),
            event -> errDialog.close());

        errDialog.getHeader().add(closeButton);

        errDialog.add(new Div(error));
        errDialog.open();
    }

    private void updateStudentGrid() {
        studentNotInCourseGrid.setItems(students);
    }

    private void updateAttendeeGrid() {
        studentInCourseGrid.setItems(attendees);
    }

    private void configureStudentNotInCourseToolBar() {
        studentNotInCourseLayout = new VerticalLayout();
        studentNotInCourseLayout.add(new H4("Student(s)"));
        Button add = new Button("Add", event -> {
            studentNotInCourseGrid.getSelectedItems().forEach(student -> {
                attendees.add(CourseStudentEntity.builder()
                    .course(courseEntity)
                    .student(student)
                    .build());

                students.remove(student);

                updateAttendeeGrid();
                updateStudentGrid();
            });
        });
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout studentNotInCourseToolBar = new HorizontalLayout(studentNotInCourseFilter, add);
        studentNotInCourseToolBar.setAlignItems(Alignment.BASELINE);
        studentNotInCourseLayout.add(studentNotInCourseToolBar);
        studentNotInCourseLayout.add(studentNotInCourseGrid);
    }

    private void configureStudentInCourseToolBar() {
        studentInCourseLayout = new VerticalLayout();
        studentInCourseLayout.add(new H4("Attendee(s)"));
        Button remove = new Button("Remove", event -> {
            studentInCourseGrid.getSelectedItems().forEach(cs -> {
                students.add(cs.getStudent());
                attendees.remove(cs);

                updateAttendeeGrid();
                updateStudentGrid();
            });
        });
        remove.addThemeVariants(ButtonVariant.LUMO_ERROR);
        HorizontalLayout studentInCourseToolBar = new HorizontalLayout(studentInCourseFilter, getGradeSetter(), remove);
        studentInCourseToolBar.setAlignItems(Alignment.BASELINE);
        studentInCourseLayout.add(studentInCourseToolBar);
        studentInCourseLayout.add(studentInCourseGrid);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        courseId = parameter;

        if (courseId == null) {
            add(getNotFoundPage());

            return;
        }

        Optional<CourseEntity> result = courseService.findOneCourse(parameter);

        if (result.isEmpty()) {
            add(getNotFoundPage());

            return;
        }

        courseEntity = result.get();
        courseStudentService.findByCourse(courseEntity).forEach(cs -> {
            attendees.add(cs);
            originalAttendees.add(cs);
        });

        studentService.findStudentNotInCourse(courseEntity).forEach(students::add);
        
        configureForm();
    }

    private HorizontalLayout getGradeSetter() {
        HorizontalLayout gradeToolBar = new HorizontalLayout();
        gradeToolBar.setWidth("25em");

        NumberField numberField = new NumberField("Grade");
        numberField.setMax(10.0);
        numberField.setMin(0.0);
        
        Button confirm = new Button("Confirm", event -> {
            if (studentInCourseGrid.getSelectedItems().isEmpty()) {
                raiseError("Please select at least an attendee(s)");

                return;
            }

            if (numberField.isInvalid()) {
                raiseError("Grade must be in range 0 to 10.0");

                return;
            }

            attendees.forEach(attendee -> {
                if (studentInCourseGrid.getSelectedItems().contains(attendee)) {
                    attendee.setGrade(numberField.getValue());
                }
            });

            updateAttendeeGrid();
        });

        gradeToolBar.add(numberField, confirm);
        gradeToolBar.setAlignItems(Alignment.BASELINE);

        return gradeToolBar;
    }

    public VerticalLayout getNotFoundPage() {
        return new VerticalLayout(
            new H1("NOT FOUND!"),
            new H5("No course with id: " + courseId + " found!")
        );
    }
}

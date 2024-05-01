package com.lehuutri.studentmanagement.views.list;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.lehuutri.studentmanagement.domain.entities.CourseEntity;
import com.lehuutri.studentmanagement.domain.entities.CourseStudentEntity;
import com.lehuutri.studentmanagement.domain.entities.StudentEntity;
import com.lehuutri.studentmanagement.services.CourseService;
import com.lehuutri.studentmanagement.services.CourseStudentService;
import com.lehuutri.studentmanagement.services.StudentService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class AddCourseForm extends FormLayout {
    Binder<CourseEntity> courseBinder = new BeanValidationBinder<>(CourseEntity.class);
    TextField courseId = new TextField("Course Id");
    TextField name = new TextField("Course Name");
    TextField lecturer = new TextField("Lecturer");
    IntegerField year = new IntegerField("Year");
    TextArea note = new TextArea("Note");
    Grid<StudentEntity> studentGrid;
    TextField filter = new TextField("Filter");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");
    StudentService studentService;
    CourseService courseService;
    CourseStudentService courseStudentService;
    private CourseEntity courseEntity;

    public AddCourseForm(StudentService studentService, CourseStudentService courseStudentService) {

        this.studentService = studentService;
        this.courseService = courseService;
        this.courseStudentService = courseStudentService;

        studentGrid = new Grid<>(StudentEntity.class);
        setSizeFull();
        courseBinder.bindInstanceFields(this);

        VerticalLayout formLayout = getFormLayout();

        add(formLayout);
    }

    public void setCourse(CourseEntity courseEntity) {
        this.courseEntity = courseEntity;
        courseBinder.readBean(courseEntity);
    }

    private Component getButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, cancel);
    }

    private void validateAndSave() {
        try {
            courseBinder.writeBean(courseEntity);

            List<CourseStudentEntity> students = new ArrayList<>();
            
            studentGrid.getSelectedItems().forEach(student -> {
                students.add(CourseStudentEntity.builder()
                    .course(courseEntity)
                    .student(student)
                    .build());
            });

            courseEntity.setStudents(students);

            fireEvent(new SaveEvent(this, courseEntity));
        } catch (ValidationException e) {
            Dialog errDialog = new Dialog();
            errDialog.setHeaderTitle("ERROR");
            errDialog.setModal(true);

            Button closeButton = new Button(
                new Icon("lumo", "cross"),
                event -> errDialog.close());

            errDialog.getHeader().add(closeButton);

            errDialog.add(new Div("Operation failed!"));
            errDialog.open();
        }
    }

    private VerticalLayout getFormLayout() {
        VerticalLayout formLayout = new VerticalLayout();

        formLayout.setWidthFull();
        courseId.setWidthFull();
        name.setWidthFull();
        lecturer.setWidthFull();
        year.setWidthFull();
        note.setWidthFull();

        configureStudentGrid();

        formLayout.add(
            courseId,
            name,
            lecturer,
            year,
            note,
            filter, 
            studentGrid,
            getButtons()
        );

        return formLayout;
    }

    private void configureStudentGrid() {
        studentGrid.setColumns("studentId", "name");
        ((GridMultiSelectionModel<?>) studentGrid
            .setSelectionMode(Grid.SelectionMode.MULTI))
            .setSelectionColumnFrozen(true);
        studentGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        studentGrid.setWidthFull();

        studentGrid.setItems(StreamSupport.stream(
                studentService.findAllStudent()
                .spliterator(), false)
                .collect(Collectors.toList()));
    }

    // Events
    public abstract static class AddCourseFormEvent extends ComponentEvent<AddCourseForm> {
        private CourseEntity entity;

        protected AddCourseFormEvent(AddCourseForm source, CourseEntity student) { 
            super(source, false);
            this.entity = student;
        }

        public CourseEntity getCourse() {
            return this.entity;
        }
    }

    public static class SaveEvent extends AddCourseFormEvent {
        SaveEvent(AddCourseForm source, CourseEntity entity) {
            super(source, entity);
        }
    }

    public static class DeleteEvent extends AddCourseFormEvent {
        DeleteEvent(AddCourseForm source, CourseEntity entity) {
            super(source, entity);
        }
    }

    public static class CloseEvent extends AddCourseFormEvent {
        CloseEvent(AddCourseForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
        ComponentEventListener<T> listener) { 
        return getEventBus().addListener(eventType, listener);
    }
}
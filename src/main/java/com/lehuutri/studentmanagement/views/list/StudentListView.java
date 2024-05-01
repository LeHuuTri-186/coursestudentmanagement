package com.lehuutri.studentmanagement.views.list;

import java.time.format.DateTimeFormatter;

import com.lehuutri.studentmanagement.domain.entities.StudentEntity;
import com.lehuutri.studentmanagement.services.CourseStudentService;
import com.lehuutri.studentmanagement.services.StudentService;
import com.lehuutri.studentmanagement.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("students")
@Route(value = "students", layout = MainLayout.class)
public class StudentListView extends VerticalLayout {
    private transient StudentService studentService;
    private transient CourseStudentService courseStudentService;
    private Grid<StudentEntity> studentGrid;
    private TextField filterText = new TextField("Filter");
    private StudentForm studentForm;

    public StudentListView(final StudentService studentService, final CourseStudentService courseStudentService) {
        this.courseStudentService = courseStudentService;
        this.studentService = studentService;
        this.studentGrid = new Grid<>(StudentEntity.class);
        setSizeFull();

        configureStudentGrid();
        configureEditForm();

        setupStudentsPage();
    }

    private void setupStudentsPage() {
        add(
            getToolbar(),
            getContent()
        );

        updateGrid();

        closeEditForm();
    }

    private void closeEditForm() {
        studentForm.setStudentEntity(null);
        studentForm.setVisible(false);
        removeClassName("editing");
    }

    private void updateGrid() {
        studentGrid.setItems(studentService.findStudentByName(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(studentGrid, studentForm);
        content.setFlexGrow(2, studentGrid);
        content.setFlexGrow(1, studentForm);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureEditForm() {
        studentForm = new StudentForm(studentService);
        studentForm.setWidth("20em");

        studentForm.addListener(StudentForm.SaveEvent.class, this::saveStudent);
        studentForm.addListener(StudentForm.DeleteEvent.class, this::deleteStudent);
        studentForm.addListener(StudentForm.CloseEvent.class, e -> closeEditForm());
    }

    private void deleteStudent(StudentForm.DeleteEvent event) {
        courseStudentService.deleteByStudent(event.getStudent());
        boolean deleteSuccessfully = studentService.deleteStudent(event.getStudent().getStudentId());

        if (deleteSuccessfully)
        {
            updateGrid();
            closeEditForm();

            return;
        }

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Deletion failed");
        dialog.setText(new Html(
        "<p>Fail operation!</p> <p>Please make sure the student exists!</p>"));
        dialog.open();
    }

    private void saveStudent(StudentForm.SaveEvent event) {
        studentService.saveStudent(event.getStudent());
        updateGrid();
        closeEditForm();
    }

    private void configureAddStudentForm() {
        studentGrid.asSingleSelect().clear();
        editStudent(new StudentEntity());
        studentForm.studentId.setEnabled(true);
        studentForm.delete.setVisible(false);
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        Button addStudent = new Button("Add student");
        addStudent.addClickListener(event -> configureAddStudentForm());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addStudent);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        toolbar.addClassName("toolbar");
        filterText.addValueChangeListener(event -> updateGrid());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        return toolbar;
    }

    private void configureStudentGrid() {
        studentGrid.addClassName("student-grid");
        studentGrid.setSizeFull();
        studentGrid.setColumns("studentId", "name", "address");
        studentGrid.addColumn(student -> student.getBirthday().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).setHeader("Birthday")
                    .setSortable(true)
                    .setComparator((o1, o2) -> o1.getBirthday().compareTo(o2.getBirthday()));
        studentGrid.addColumn("note");
        studentGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        studentGrid.asSingleSelect().addValueChangeListener(event -> {
            editStudent(event.getValue()); 
            studentForm.studentId.setEnabled(false);
            studentForm.delete.setVisible(true);
        });
    }

    private void editStudent(StudentEntity value) {
        if (value == null) {
            closeEditForm();

            return;
        }

        studentForm.setStudentEntity(value);
        studentForm.setVisible(true);
        addClassName("editing");
    }
}

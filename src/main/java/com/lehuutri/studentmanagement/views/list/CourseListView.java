package com.lehuutri.studentmanagement.views.list;

import java.util.ArrayList;
import java.util.List;

import com.lehuutri.studentmanagement.domain.entities.CourseEntity;
import com.lehuutri.studentmanagement.domain.entities.CourseStudentEntity;
import com.lehuutri.studentmanagement.domain.entities.StudentEntity;
import com.lehuutri.studentmanagement.services.CourseService;
import com.lehuutri.studentmanagement.services.CourseStudentService;
import com.lehuutri.studentmanagement.services.StudentService;
import com.lehuutri.studentmanagement.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("courses")
@Route(value = "courses", layout = MainLayout.class)
public class CourseListView extends VerticalLayout {
    private Grid<CourseEntity> courseGrid;
    private Grid<StudentEntity> studentGrid;
    private Grid<CourseStudentEntity> studentInCourseGrid;
    private transient CourseService courseService;
    private transient StudentService studentService;
    private transient CourseStudentService courseStudentService;
    private AddCourseForm addCourseForm;
    private TextField filterByName = new TextField("Filter by Name");
    private IntegerField filterByYear = new IntegerField("Filter by Year");
    private H4 courseId = new H4("Course Id: ");
    private VerticalLayout courseStudents;
    private String selectedCourseId = null;
    private ComboBox<String> filterBy = new ComboBox<>("Filter by");
    
    public CourseListView(CourseService courseService, StudentService studentService, CourseStudentService courseStudentService) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.courseStudentService = courseStudentService;
        this.addCourseForm = new AddCourseForm(studentService, courseStudentService);
        courseGrid = new Grid<>(CourseEntity.class);
        studentGrid = new Grid<>(StudentEntity.class);
        studentInCourseGrid = new Grid<>(CourseStudentEntity.class);
        setSizeFull();

        configureCourseGrid();
        configureStudentInCourseGrid();

        closeAddCourseForm();

        add(getCourseToolBar(), getContent());

        updateCourseGrid();
    }


    private void closeAddCourseForm() {
        addCourseForm.setVisible(false);
    }


    private void updateCourseGrid() {
        if (filterBy.getValue().equals("Year")) {
            try {
                Integer year = filterByYear.getValue();
                courseGrid.setItems(courseService.findByYear(year));
            } catch (Exception e) {
                courseGrid.setItems(courseService.findByYear(null));
            }

            return;
        }

        courseGrid.setItems(courseService.findByNameIsLike(filterByName.getValue()));
    }

    private void saveCourse(AddCourseForm.SaveEvent event) {
        CourseEntity course = event.getCourse();

        courseService.saveCourse(course);
        course.getStudents().forEach(student -> courseStudentService.saveCourseStudent(student));

        updateCourseGrid();
        closeAddCourseForm();
        closeStudentInCourse();
    }

    private void closeStudentInCourse() {
        studentInCourseGrid.setItems(new ArrayList<>());
        courseGrid.deselectAll();
        selectedCourseId = null;
        courseStudents.setVisible(false);
    }

    private VerticalLayout getCourseInfoComponent() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.add(courseId);
        verticalLayout.add(studentInCourseGrid);

        Button delete = new Button("Delete", event -> deleteCourse());
        Button edit = new Button("Edit", event -> {
            this.getUI().ifPresent(ui -> {
                ui.navigate(CourseDetailView.class, selectedCourseId);
            });
        });
        Button close = new Button("Close", 
        e -> closeStudentInCourse());
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        verticalLayout.add(new HorizontalLayout(edit, delete, close));

        return verticalLayout;
    }

    private void deleteCourse() {
        if (selectedCourseId == null) {
            return;
        }

        courseStudentService.findByCourse(courseGrid.getSelectedItems().iterator().next()).forEach(cs ->
            courseStudentService.deleteByCourseAndStudent(cs.getCourse(), cs.getStudent())
        );

        courseService.deleteById(selectedCourseId);

        closeStudentInCourse();

        updateCourseGrid();
    }

    private void showStudentsInCourse(CourseEntity courseEntity) {
        courseId.setText("Course Id: ");
        if (courseEntity == null) {
            return;
        }
        List<CourseStudentEntity> students = courseStudentService.findByCourse(courseEntity);
        courseId.add(courseEntity.getCourseId());
        studentInCourseGrid.setItems(students);
        selectedCourseId = courseEntity.getCourseId();
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

        studentInCourseGrid.addColumn("grade");
    }


    private void configureAddCourseForm() {
        addCourseForm.setWidth("30em");
        addCourseForm.addListener(AddCourseForm.SaveEvent.class, this::saveCourse);

        addCourseForm.setVisible(true);
        addCourseForm.setCourse(new CourseEntity());
        closeStudentInCourse();
    }

    private Component getCourseToolBar() {
        Button addStudent = new Button("Add course", event -> configureAddCourseForm());
        filterBy.setItems("Name", "Year");
        filterBy.setValue("Name");
        filterBy.setAllowCustomValue(false);
        filterByYear.setVisible(false);
        filterByYear.setPlaceholder("Filter by year...");
        filterByName.setPlaceholder("Filter by name...");
        HorizontalLayout toolBar = new HorizontalLayout(filterBy, filterByName, filterByYear, addStudent);
        toolBar.setAlignItems(Alignment.BASELINE);

        filterByName.addValueChangeListener(event -> updateCourseGrid());
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);
        filterByYear.addValueChangeListener(event -> updateCourseGrid());
        filterByYear.setValueChangeMode(ValueChangeMode.LAZY);

        filterBy.addValueChangeListener(event -> {
            updateCourseGrid();

            if (filterByName.isVisible()) {
                filterByName.setVisible(false);
                filterByYear.setVisible(true);

                return;
            }

            filterByName.setVisible(true);
            filterByYear.setVisible(false);
        });
            
        return toolBar;
    }

    private void configureCourseGrid() {
        courseGrid.setSizeFull();
        courseGrid.addClassName("course-grid");
        courseGrid.setColumns("courseId", "name", "lecturer", "year", "note");
        courseGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        courseGrid.asSingleSelect().addValueChangeListener(event -> {
            showStudentsInCourse(event.getValue());
            List<CourseStudentEntity> result = courseStudentService.findByCourse(event.getValue());

            courseStudents.setVisible(true);
            addCourseForm.setVisible(false);
        });
    }

    private Component getContent() {
        courseStudents =  getCourseInfoComponent();
        HorizontalLayout content = new HorizontalLayout(courseGrid, addCourseForm, courseStudents);
        content.setFlexGrow(2, courseGrid);
        content.setFlexGrow(1, addCourseForm);
        content.setFlexGrow(2, courseStudents);
        content.addClassName("content");
        content.setSizeFull();
        courseStudents.setVisible(false);

        return content;
    }
}

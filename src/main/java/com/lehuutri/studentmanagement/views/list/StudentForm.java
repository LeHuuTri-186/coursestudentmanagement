package com.lehuutri.studentmanagement.views.list;

import java.time.LocalDate;

import com.lehuutri.studentmanagement.domain.entities.StudentEntity;
import com.lehuutri.studentmanagement.services.StudentService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;


public class StudentForm extends FormLayout {
    Binder<StudentEntity> studentFormBinder = new BeanValidationBinder<>(StudentEntity.class);

    TextField studentId = new TextField("Student Id");
    TextField name = new TextField("Student Name");
    TextField address = new TextField("Address");
    DatePicker birthday = new DatePicker("birthday");
    TextArea note = new TextArea("Note");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");

    private transient StudentEntity studentEntity;
    private transient StudentService studentService;

    public StudentForm(StudentService studentService) {
        this.studentService = studentService;
        addClassName("student-form");
        studentFormBinder.bindInstanceFields(this);  

        birthday.setMax(LocalDate.now());
        DatePicker.DatePickerI18n singleFormatI18n = new DatePicker.DatePickerI18n();
        singleFormatI18n.setDateFormat("dd/MM/yyyy");

        birthday.setI18n(singleFormatI18n);

        add(
            studentId,
            name,
            address,
            birthday,
            note, 
            getButtonLayout()
        );
    }

    public void setStudentEntity(StudentEntity entity) {
        this.studentEntity = entity;
        studentFormBinder.readBean(entity);
    }

    private Component getButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, studentEntity)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, delete, cancel);
    }

    private void validateAndSave() {
        if (studentId.isEnabled() && 
        studentService.findOneStudent(studentId.getValue()).isPresent()) {
            Dialog errDialog = new Dialog();
            errDialog.setHeaderTitle("ERROR");
            errDialog.setModal(true);

            Button closeButton = new Button(
                new Icon("lumo", "cross"),
                event -> errDialog.close());

            errDialog.getHeader().add(closeButton);

            errDialog.add(new Div("Student Id already exists in database!"));
            errDialog.open();

            return;
        }

        try {
            studentFormBinder.writeBean(studentEntity);
            fireEvent(new SaveEvent(this, studentEntity));
        } catch (ValidationException e) {
            raiseError();
        }
    }

    private void raiseError() {
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

    // Events
    public abstract static class StudentFormEvent extends ComponentEvent<StudentForm> {
        private StudentEntity entity;

        protected StudentFormEvent(StudentForm source, StudentEntity student) { 
            super(source, false);
            this.entity = student;
        }

        public StudentEntity getStudent() {
            return this.entity;
        }
    }

    public static class SaveEvent extends StudentFormEvent {
        SaveEvent(StudentForm source, StudentEntity entity) {
            super(source, entity);
        }
    }

    public static class DeleteEvent extends StudentFormEvent {
        DeleteEvent(StudentForm source, StudentEntity entity) {
            super(source, entity);
        }
    }

    public static class CloseEvent extends StudentFormEvent {
        CloseEvent(StudentForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
        ComponentEventListener<T> listener) { 
        return getEventBus().addListener(eventType, listener);
    }
}

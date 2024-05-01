package com.lehuutri.studentmanagement.views;



import com.lehuutri.studentmanagement.views.list.CourseListView;
import com.lehuutri.studentmanagement.views.list.StudentListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("")
public class MainLayout extends AppLayout {
    
    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createDrawer() {
        RouterLink studentLink = new RouterLink("Students", StudentListView.class);
        RouterLink coursesLink = new RouterLink("Courses", CourseListView.class);
        studentLink.setHighlightCondition(HighlightConditions.sameLocation());
        coursesLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(studentLink, coursesLink));
    }

    private void createHeader() {
        H1 logo = new H1("Student - Course Management");
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }
}

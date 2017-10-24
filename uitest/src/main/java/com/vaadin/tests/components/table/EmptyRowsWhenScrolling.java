package com.vaadin.tests.components.table;

import java.util.Random;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ClassResource;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.data.util.BeanContainer;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.ColumnGenerator;

/**
 * This test cannot be automated. http://dev.vaadin.com/ticket/6160, base code
 * by user radosdesign.
 *
 * The test fails if even occasionally empty rows appear in the table. A
 * relatively good way to get them (before the fix) is to wait for the page to
 * load, move the scrollbar down, press 'R' before the rows have time to be
 * rendered, and then move the scrollbar up when no rows have been rendered yet.
 * After this, when you scroll down slowly there may be empty rows. This doesn't
 * happen always, and you may need to force the threads slower to get it to
 * happen at all. On a slow 32-bit Windows 7 with Chrome version 22.0.1229.94 m
 * (no GWT dev mode) this test has managed to reproduce the problem maybe nine
 * times out of ten.
 *
 * @author Anna Koskinen / Vaadin Oy
 *
 */
public class EmptyRowsWhenScrolling extends UI {

    @Override
    public void init(VaadinRequest request) {
        getPage().setTitle("Simpletable Application");
        AppView appView = new AppView(this);
        setContent(appView);
        addAction(new Button.ClickShortcut(appView.getBtnRefreshTable(),
                KeyCode.R));
    }

    private class AppView extends CustomComponent {

        @AutoGenerated
        private AbsoluteLayout mainLayout;
        @AutoGenerated
        private VerticalLayout verticalLayout_1;
        @AutoGenerated
        private Table table;
        @AutoGenerated
        private HorizontalLayout horizontalLayout_1;
        @AutoGenerated
        private Button btnRefreshTable;

        /*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

        /*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

        /**
         * The constructor should first build the main layout, set the
         * composition root and then do any custom initialization.
         *
         * The constructor will not be automatically regenerated by the visual
         * editor.
         */
        public AppView(final UI application) {
            buildMainLayout();
            setCompositionRoot(mainLayout);

            // Container with sample data
            BeanContainer<Integer, SimpleBean> container = new BeanContainer<>(
                    SimpleBean.class);
            container.setBeanIdProperty("id");
            for (int i = 1; i <= 50; ++i) {
                container.addBean(new SimpleBean(i, "image", "Column1 row " + i,
                        "Column2 row " + i, "Column3 row " + i,
                        "Column4 row " + i));
            }
            table.setContainerDataSource(container);
            table.setEditable(true);
            table.setColumnReorderingAllowed(true);
            table.setVisibleColumns("image", "id", "col1",
                    "col2", "col3", "col4");
            table.addGeneratedColumn("image", new ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId,
                        Object columnId) {
                    int imgNum = new Random().nextInt(5) + 1;
                    try {
                        // Simulate background work
                        System.out.println(
                                "Generated column for image /com/example/simpletable/img/px50-"
                                        + imgNum + ".jpg");
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Resource resource = new ClassResource(
                            "/com/example/simpletable/img/px50-" + imgNum
                                    + ".jpg");
                    Embedded image = new Embedded("", resource);
                    image.setWidth("50px");
                    image.setHeight("50px");
                    image.addClickListener(
                            event -> Notification.show("Image clicked!"));
                    return image;
                }
            });

            // Refresh table button
            getBtnRefreshTable()
                    .addClickListener(event -> table.refreshRowCache());
        }

        @AutoGenerated
        private AbsoluteLayout buildMainLayout() {
            // common part: create layout
            mainLayout = new AbsoluteLayout();
            mainLayout.setWidth("100%");
            mainLayout.setHeight("100%");

            // top-level component properties
            setWidth("100.0%");
            setHeight("100.0%");

            // verticalLayout_1
            verticalLayout_1 = buildVerticalLayout_1();
            mainLayout.addComponent(verticalLayout_1, "top:0.0px;left:0.0px;");

            return mainLayout;
        }

        @AutoGenerated
        private VerticalLayout buildVerticalLayout_1() {
            // common part: create layout
            verticalLayout_1 = new VerticalLayout();
            verticalLayout_1.setWidth("100.0%");
            verticalLayout_1.setHeight("100.0%");
            verticalLayout_1.setMargin(false);

            // horizontalLayout_1
            horizontalLayout_1 = buildHorizontalLayout_1();
            verticalLayout_1.addComponent(horizontalLayout_1);

            // table_1
            table = new Table();
            table.setImmediate(false);
            table.setWidth("100.0%");
            table.setHeight("100.0%");
            verticalLayout_1.addComponent(table);
            verticalLayout_1.setExpandRatio(table, 1.0f);

            return verticalLayout_1;
        }

        @AutoGenerated
        private HorizontalLayout buildHorizontalLayout_1() {
            // common part: create layout
            horizontalLayout_1 = new HorizontalLayout();
            horizontalLayout_1.setWidth("100.0%");
            horizontalLayout_1.setHeight("-1px");
            horizontalLayout_1.setMargin(false);

            // btnRefreshTable
            setBtnRefreshTable(new Button());
            getBtnRefreshTable().setCaption("Reload table row cache");
            getBtnRefreshTable().setWidth("-1px");
            getBtnRefreshTable().setHeight("-1px");
            horizontalLayout_1.addComponent(getBtnRefreshTable());
            horizontalLayout_1.setComponentAlignment(getBtnRefreshTable(),
                    new Alignment(33));

            return horizontalLayout_1;
        }

        public Button getBtnRefreshTable() {
            return btnRefreshTable;
        }

        public void setBtnRefreshTable(Button btnRefreshTable) {
            this.btnRefreshTable = btnRefreshTable;
        }

    }

    protected class SimpleBean {
        private int id;
        private String image;
        private String col1;
        private String col2;
        private String col3;
        private String col4;

        public SimpleBean(int id, String image, String col1, String col2,
                String col3, String col4) {
            super();
            this.id = id;
            this.image = image;
            this.col1 = col1;
            this.col2 = col2;
            this.col3 = col3;
            this.col4 = col4;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCol1() {
            return col1;
        }

        public void setCol1(String col1) {
            this.col1 = col1;
        }

        public String getCol2() {
            return col2;
        }

        public void setCol2(String col2) {
            this.col2 = col2;
        }

        public String getCol3() {
            return col3;
        }

        public void setCol3(String col3) {
            this.col3 = col3;
        }

        public String getCol4() {
            return col4;
        }

        public void setCol4(String col4) {
            this.col4 = col4;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

}

# PrimeFaces HTML Exporter

This project provides a custom PrimeFaces `Exporter` class to export `DataTable` components into an HTML file with a structured format and styling.

## Features
- Exports PrimeFaces `DataTable` to an HTML file.
- Preserves table styling and structure.
- Easy integration into PrimeFaces projects.
- Supports custom filename and encoding.

## Prerequisites

Ensure you have the following installed:
- Java 8 or higher
- PrimeFaces 11 or compatible version

## Structure for Placement

```
|-- src/main/java/your_package/
|   |-- HTMLExporter.java    # The HTML exporter class
|   |-- YourBean.java       #Your Bean file
```

## Setup Instructions

### Step 1: Add the Exporter Class

Copy the `HTMLExporter.java` file into your project's package structure, for example, under `customExporter`.

### Step 2: Using the Exporter in Your Bean

To use the HTML exporter in your project, add the following method in your managed bean:

```java
import javax.faces.context.FacesContext;
import org.primefaces.component.datatable.DataTable;
import java.io.IOException;

public void exportHTML() throws IOException {
    HTMLExporter exporter = new HTMLExporter();
    DataTable dataTable = (DataTable) FacesContext.getCurrentInstance()
            .getViewRoot().findComponent("form:dataTable");  // Replace with your form and DataTable ID
    exporter.export(FacesContext.getCurrentInstance(),
                   dataTable,
                   "exported_data",  // Desired filename
                   false, false, "UTF-8", null, null, null);
}
```

### Step 3: Add Export Button in Your XHTML Page

```xml
<h:form id="form">
    <p:dataTable id="dataTable" value="#{myBean.dataList}" var="item">
        <p:column headerText="ID">
            #{item.id}
        </p:column>
        <p:column headerText="Name">
            #{item.name}
        </p:column>
    </p:dataTable>

    <p:commandButton value="Export to HTML"
                     actionListener="#{myBean.exportHTML}"
                     ajax="false" />
</h:form>
```

### Step 4: Running the Application

1. Deploy your application on a Jakarta EE compatible server such as Tomcat or WildFly.
2. Open your web page containing the DataTable.
3. Click the "Export to HTML" button to download the table as an HTML file.

## Usage for Global Project Scope

To use this exporter across your entire project, follow these steps:

1. Place the `HTMLExporter.java` file in a central utility package (e.g., `com.myproject.utils`).
2. Create a utility method to handle DataTable exports globally:

```java
public class ExportUtil {
    public static void exportDataTableToHTML(String componentId, String fileName) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        DataTable dataTable = (DataTable) context.getViewRoot().findComponent(componentId);
        HTMLExporter exporter = new HTMLExporter();
        exporter.export(context, dataTable, fileName, false, false, "UTF-8", null, null, null);
    }
}
```

3. Use this method across your project wherever needed:

```java
ExportUtil.exportDataTableToHTML("form:dataTable", "exported_data");
```

## Troubleshooting

- Ensure the correct ID is used in `findComponent("form:dataTable")`.
- Check that PrimeFaces is properly configured in your project.
- Verify the encoding type matches your project requirements.


## License

This project is open-source and available under the MIT License.


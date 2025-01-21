package YourPackage;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.ExportConfiguration;
import org.primefaces.component.export.Exporter;
import org.primefaces.component.export.ExporterOptions;


import org.primefaces.component.api.UIColumn;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.el.ValueExpression;

public class HTMLExporter extends Exporter<DataTable> {
	public void export(FacesContext context, DataTable table, String filename, 
            boolean pageOnly, boolean selectionOnly, 
            String encodingType, MethodExpression preProcessor, 
            MethodExpression postProcessor, ExporterOptions options) 
  throws IOException {
		String htmlContent = generateHtmlContent(context, table, filename, encodingType);
		

		
		ExternalContext externalContext = context.getExternalContext();
		byte[] content = htmlContent.getBytes(encodingType);
		
		externalContext.setResponseContentType("text/html");
		externalContext.setResponseHeader("Content-Disposition", 
		  "attachment;filename=\"" + filename + ".html\"");
		externalContext.setResponseContentLength(content.length);
		
		externalContext.getResponseOutputStream().write(content);
		context.responseComplete();
		}


    
    
    public String generateHtmlContent(FacesContext context, DataTable table, String filename, String encodingType) {
        StringBuilder builder = new StringBuilder();
        
        builder.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\">");
        builder.append("<style>");
        builder.append("table { border-collapse: collapse; width: 100%; }");
        builder.append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        builder.append("th { background-color: #f2f2f2; }");
builder.append("</style></head><body>");
        
        builder.append("<table>");
        
        builder.append("<thead><tr>");
        for (UIColumn col : table.getColumns()) {
            if (col.isExportable() == false) {
                continue;
            }
            UIComponent headerFacet = col.getFacet("header");
            String headerText = "";
            if (headerFacet != null) {
                headerText = headerFacet.toString();
            } else {
                headerText = col.getHeaderText();
            }
            builder.append("<th>").append(headerText).append("</th>");
        }
        builder.append("</tr></thead>");

        

        builder.append("<tbody>");
        List<?> data = (List<?>) table.getValue();

        int rowIndex = 0;
        
        for (Object item : data) {
            builder.append("<tr>");
            table.setRowIndex(rowIndex);  
            

            for (UIColumn col : table.getColumns()) {
                if (col.isExportable() == false) {
                    continue;
                }
                
                Object cellValue;
                UIComponent component = col.getFacet("output");
                if (component == null) {
                    component = col.getChildren().get(0);
                }
                
                if (component instanceof ValueHolder) {
                    cellValue = ((ValueHolder) component).getValue();
                } else {
                    cellValue = "";
                }
                
                builder.append("<td>").append(cellValue != null ? cellValue.toString() : "").append("</td>");
            }



            builder.append("</tr>");
            rowIndex++;
        }

        
        
        table.setRowIndex(-1);
        builder.append("</tbody></table></body></html>");
        return builder.toString();
    }
    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public String getFileExtension() {
        return ".html";
    }




	@Override
	public void export(FacesContext facesContext, List<DataTable> component, OutputStream outputStream,
			ExportConfiguration exportConfiguration) throws IOException {
		// TODO Auto-generated method stub
		
	}
	//Add this commented method in bean
//    public void exportHTML() throws IOException {
//        HTMLExporter exporter = new HTMLExporter();
//        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance()
//                .getViewRoot().findComponent("form:dataTable"); // fill this with your id's
//        exporter.export(FacesContext.getCurrentInstance(), 
//                       dataTable, 
//                       "file_name", // fill with your file name 
//                       false, false, "UTF-8", null, null, null);
//    }
}

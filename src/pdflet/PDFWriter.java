package pdflet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */

public class PDFWriter {
	
	
	private VelocityEngine engine;
	private VelocityContext context;	
	
	static ResourceBundle rb;
	static String tempLocation = "/Users/Admin/temp";
	
	static {
		try {
			rb = ResourceBundle.getBundle("pdflet");
			tempLocation = rb.getString("tempLocation");
			if ( !tempLocation.endsWith("/") ) tempLocation = tempLocation + "/";
		} catch ( MissingResourceException e ) {
			System.out.println("MissingResourceException: " + e.getMessage());	
		}
	}

	public PDFWriter(VelocityEngine engine, VelocityContext context) {
		this.engine = engine;
		this.context = context;
	}
	
	
	public String createContent(String templateName) throws Exception {
		StringBuffer sb = new StringBuffer("");
		try {
			Template template = engine.getTemplate(templateName); 
			StringWriter writer = new StringWriter();
			template.merge(context, writer);
			writer.close();
			sb = writer.getBuffer();
		} catch ( Exception ex ) {
		    ex.printStackTrace();
			throw ex;			
		}
		return sb.toString();
	}		
	
	
	public void writeContent(String templateName, HttpServletResponse response) throws Exception {
		
		String content = createContent(templateName);
		String filename = tempLocation + UniqueID.getUID() + ".html";
		
		InputStream contentStream = new ByteArrayInputStream(content.getBytes());
		BufferedInputStream in = new BufferedInputStream(contentStream);
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filename));
		
		Tidy tidy = new Tidy();
		tidy.parse(in, out);
		in.close();
		out.close();
				
		ServletOutputStream os = response.getOutputStream();
		ITextRenderer renderer = new ITextRenderer();
		File file = new File(filename);
		renderer.setDocument(file);
		renderer.layout();
		renderer.createPDF(os);     
		os.flush();
		os.close();
		
		file.delete();
		
	}



}

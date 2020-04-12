package pdflet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;


/**
 * 
 * @author Shamsul Bahrin
 *
 */

public class ServletTemplate extends HttpServlet {
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)  throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  {
		ServletContext context = this.getServletConfig().getServletContext();
        String pathInfo = req.getPathInfo();
        pathInfo = pathInfo != null ? pathInfo.substring(1) : "";
		String module = pathInfo != null ? pathInfo : "";	    	
		if ( !"".equals(module) ) {
			Object object = null;
			try {
				Class klazz = Class.forName(module);
				object = klazz.newInstance();	
				((IServlet) object).doService(req, res, getServletConfig(), getServletContext());
				
			} catch ( ClassNotFoundException cnfex ) {
				System.out.println(cnfex.getMessage());
			} catch ( InstantiationException iex ) {
				System.out.println(iex.getMessage());
			} catch ( IllegalAccessException illex ) {
				System.out.println(illex.getMessage());
			} catch ( Exception ex ) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}	
		}

	}
	
}

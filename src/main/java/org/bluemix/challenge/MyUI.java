package org.bluemix.challenge;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 *
 */
@Theme("mytheme")
@Widgetset("org.bluemix.challenge.MyAppWidgetset")
public class MyUI extends UI 
{
	private static final long serialVersionUID = 1L;
	private Navigator navigator;
	protected static final String LOGINVIEW = "Login",
								  DASHBOARD = "Dashboard";

	@Override
    protected void init(VaadinRequest vaadinRequest) 
    {
    	navigator = new Navigator( this, this );
    	navigator.addView( LOGINVIEW, new LoginView() );
    	navigator.addView( DASHBOARD, new Dashboard() );
    	navigator.navigateTo( LOGINVIEW );
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet 
    {
		private static final long serialVersionUID = 1L;
    }
    
    
}

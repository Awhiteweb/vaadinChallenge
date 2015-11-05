package org.bluemix.challenge;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;


public class LoginView extends Task3 implements View 
{
	private static final long serialVersionUID = 1L;

	private TextField username;
	private PasswordField password;
	private Button loginButton;
	
	@Override
	public void enter( ViewChangeEvent event )
	{
		this.username = super.username;
		this.password = super.password;
		this.loginButton = super.loginButton;
		username.focus();
		loginButton.addClickListener( new ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick( ClickEvent event )
			{
				validate( event );
			}			
		});
	}
	
	private void validate( ClickEvent event )
	{
		if (!username.isValid() || !password.isValid()) 
		{
			return;
		}
		
		String user = username.getValue();
		String pass = password.getValue();
		
		boolean isValid = user.equals("vaadin") && pass.equals("bluemix");

		if ( isValid ) 
		{
			getSession().setAttribute( "user", user );
			getUI().getNavigator().navigateTo( "MyUI.java" );
		} 
		else 
		{
			password.setValue( null );
			password.focus();
		}

	}
}

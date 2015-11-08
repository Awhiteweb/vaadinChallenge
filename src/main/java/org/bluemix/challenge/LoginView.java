package org.bluemix.challenge;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class LoginView extends Task3 implements View 
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void enter( ViewChangeEvent event )
	{
		username.focus();
		super.loginButton.addClickListener( e -> {
			if ( !username.isValid() || !password.isValid() ) 
			{
				errorLabel.setValue( "Please enter some valid credentials" );
				return;
			}
			String user = username.getValue();
			String pass = password.getValue();
			boolean isValid = user.equals("vaadin") && pass.equals("bluemix");
			if ( isValid ) 
			{
				getSession().setAttribute( "user", user );
				username.clear();
				password.clear();
				errorLabel.setValue("");
				getUI().getNavigator().navigateTo( MyUI.DASHBOARD );
			} 
			else 
			{
				password.setValue( null );
				username.focus();
			}
		});
	}
}

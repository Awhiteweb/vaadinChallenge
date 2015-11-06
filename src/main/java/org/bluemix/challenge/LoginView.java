package org.bluemix.challenge;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


public class LoginView extends Task3 implements View 
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void enter( ViewChangeEvent event )
	{
		username.focus();
		super.loginButton.addClickListener( e -> validate( e ) );
	}
	
	private void validate( ClickEvent event )
	{
		System.out.println( event.getButton() );
		Label error = new Label( "Please enter some credentials" );
		error.addStyleName( "alert-error" );
		
		if ( !username.isValid() || !password.isValid() ) 
		{
			addComponent( error );
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
			getUI().getNavigator().navigateTo( MyUI.DASHBOARD );
		} 
		else 
		{
			error.setCaption( "error" );
			addComponent( error );
			password.setValue( null );
			username.focus();
		}
	}
	
	private Window errorModal( String comment )
	{
		Window modal = new Window( "Invalid Credentials" );
		modal.center();
		modal.addStyleName( "infoModal" );
		modal.setModal( true );
		modal.setResizable( false );
		VerticalLayout content = new VerticalLayout();
		content.setMargin( true );
		content.addComponent( new Label( comment ) );
		modal.setContent( content );
		return modal;
 	}
}

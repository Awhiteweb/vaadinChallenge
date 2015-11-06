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
		loginButton.addClickListener( e -> validate( e ) );
	}
	
	private void validate( ClickEvent event )
	{
		
			if ( !username.isValid() || !password.isValid() ) 
			{
				errorModal( "Please enter some credentials" );
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
				errorModal( "Please enter some valid credentials" );
				password.setValue( null );
				username.focus();
			}
	}
	
	private void errorModal( String comment )
	{
		Window modal = new Window( "Invalid Credentials" );
		modal.center();
		modal.addStyleName( "infoModal" );
		modal.setModal( true );
		modal.setResizable( false );
		VerticalLayout content = new VerticalLayout();
		content.setMargin( true );
		content.addComponent( new Label( comment ) );
//		Button button = new Button( "close" );
//		button.addClickListener( e -> modal.close() );
//		content.addComponent( button );
		modal.setContent( content );
		UI.getCurrent().addWindow( modal );
 	}
}

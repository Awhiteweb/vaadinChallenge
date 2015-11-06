package org.bluemix.challenge;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.bluemix.challenge.backend.Customer;
import org.bluemix.challenge.backend.DummyDataService;

import com.vaadin.data.Item;
import com.vaadin.data.sort.Sort;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SingleSelectionModel;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.DateRenderer;

public class Dashboard extends VerticalLayout implements View 
{
	private ComboBox comboBox;
	private Grid grid;
	private Button button;
	private List<Customer> customerList;

	public Dashboard()
	{
		setMargin( true );
	}
	
	@Override
	public void enter( ViewChangeEvent event )
	{
        BeanContainer<String, Customer> customerContainer = generateCustomerList( DummyDataService.createDemoService() );        
        createComboBox( customerContainer );
        createGrid( customerContainer );
        logout();
		addComponent( comboBox );
		addComponent( grid );
		addComponent( button );
	}

	private BeanContainer<String, Customer> generateCustomerList( DummyDataService dds )
	{
		BeanContainer<String, Customer> customerContainer = new BeanContainer<String, Customer>( Customer.class );
		customerList = dds.findAll();
        customerList.sort( new Comparator<Customer>()
        		{
					@Override
					public int compare( Customer o1, Customer o2 )
					{
						if ( o1.getFirstName().equals( o2.getFirstName() ) )
							return o1.getLastName().compareToIgnoreCase( o2.getLastName() );
						return o1.getFirstName().compareToIgnoreCase( o2.getFirstName() );
					}
        		});         
        for ( Customer c : customerList )
        {
        	customerContainer.addItem( c.toString() + " (" + c.getId() + ")", c );
        }
        return customerContainer;
	}
	
	private void createComboBox( BeanContainer<String, Customer> customerContainer )
	{
		comboBox = new ComboBox( "Customers" );
        comboBox.addItems( customerContainer.getItemIds() );
        comboBox.addValueChangeListener( event -> { 
        	String selection = event.getProperty().getValue().toString();
        	customerContainer.getItem( selection );
            if (selection != null)
                UI.getCurrent().addWindow( infoModal( customerContainer.getItem( selection ) ) );
        	});
	}

	private void createGrid( BeanContainer<String, Customer> customerContainer )
	{
		grid = new Grid();
        grid.setSizeFull();
        grid.setContainerDataSource( customerContainer );
               
        grid.getDefaultHeaderRow().getCell( "address" ).setHtml( "Address" );
        grid.getDefaultHeaderRow().getCell( "birthDate" ).setHtml( "Birthday" );
        grid.getDefaultHeaderRow().getCell( "city" ).setHtml( "City" );
        grid.getDefaultHeaderRow().getCell( "email" ).setHtml( "Email" );
        grid.getDefaultHeaderRow().getCell( "firstName" ).setHtml( "First name" );
        grid.getDefaultHeaderRow().getCell( "gender" ).setHtml( "Gender" );
        grid.getDefaultHeaderRow().getCell( "id" ).setHtml( "ID" );
        grid.getDefaultHeaderRow().getCell( "lastName" ).setHtml( "Last name" );
        grid.getDefaultHeaderRow().getCell( "phone" ).setHtml( "Phone" );
        grid.getDefaultHeaderRow().getCell( "zipCode" ).setHtml( "Zip code" );
        grid.setColumnOrder( "firstName", "lastName", "birthDate", "gender", "phone", "email", 
        		"address", "city", "zipCode", "id" );
        
        grid.getColumn( "birthDate" ).setRenderer( new DateRenderer( "%1$td %1$tb %1$tY", Locale.ENGLISH) );
        
        grid.sort( Sort.by( "id", SortDirection.ASCENDING ) );
        grid.sort( Sort.by( "birthDate", SortDirection.ASCENDING ).then( "firstName", SortDirection.ASCENDING ) );
        grid.sort( Sort.by( "email", SortDirection.ASCENDING ).then( "firstName", SortDirection.ASCENDING ) );
        grid.sort( Sort.by( "lastName", SortDirection.ASCENDING ).then( "firstName", SortDirection.ASCENDING ) );
        grid.sort( Sort.by( "firstName", SortDirection.ASCENDING ).then( "lastName", SortDirection.ASCENDING ) );
        
        grid.addSelectionListener(selectionEvent -> {
        	Object selected = ( ( SingleSelectionModel ) grid.getSelectionModel()).getSelectedRow();
            if (selected != null)
                UI.getCurrent().addWindow( infoModal( grid.getContainerDataSource().getItem(selected) ) );
        });

	}
	
	private Window infoModal( Item item )
	{
		String title = String.format( "Customer ID: %s", item.getItemProperty( "id" ).getValue() );
		Window modal = new Window( title );
		modal.center();
		modal.addStyleName( "infoModal" );
		modal.setModal( true );
		modal.setResizable( false );
		VerticalLayout content = new VerticalLayout();
		content.setMargin( true );
		String name = String.format( "Name: %s %s", item.getItemProperty("firstName").getValue(), 
				item.getItemProperty("lastName").getValue() );
		content.addComponent( new Label( name ) );
		content.addComponent( new Label( 
				String.format( "Date of Birth: %1$td %1$tb %1$tY", item.getItemProperty("birthDate").getValue() ) ) );
		content.addComponent( new Label( "Email: " + item.getItemProperty( "email" ).getValue() ) );
		content.addComponent( new Label( "Phone: " + item.getItemProperty( "phone" ).getValue() ) );
		if ( item.getItemProperty( "address" ).getValue() != null )
			content.addComponent( new Label( "Gender: " + item.getItemProperty( "gender" ).getValue() ) );
		content.addComponent( new Label( "Address:" ) );
		if ( item.getItemProperty( "address" ).getValue() != null )
			content.addComponent( new Label( String.format( "%t%s", item.getItemProperty( "address" ).getValue() ) ) );
		if ( item.getItemProperty( "city" ).getValue() != null )
			content.addComponent( new Label( String.format( "%t%s", item.getItemProperty( "city" ).getValue() ) ) );
		if ( item.getItemProperty( "zipCode" ).getValue() != null )
			content.addComponent( new Label( String.format( "%t%s", item.getItemProperty( "zipCode" ).getValue() ) ) );
		modal.setContent( content );
		return modal;
	}
	
	private void logout()
	{
		button = new Button( "Logout" );
		button.setStyleName( "closeButton" );
		button.addClickListener( event -> {
			getSession().close();
			UI.getCurrent().getNavigator().navigateTo( MyUI.LOGINVIEW );
		});
	}
	
}

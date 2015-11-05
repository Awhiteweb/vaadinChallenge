package org.bluemix.challenge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.annotation.WebServlet;

import org.bluemix.challenge.backend.Customer;
import org.bluemix.challenge.backend.DummyDataService;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.sort.Sort;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.SingleSelectionModel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.DateRenderer;

/**
 *
 */
@Theme("mytheme")
@Widgetset("org.bluemix.challenge.MyAppWidgetset")
public class MyUI extends UI 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ComboBox comboBox;
	private Grid grid;
	
    @Override
    protected void init(VaadinRequest vaadinRequest) 
    {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        comboBox = new ComboBox( "Customers" );
       
        DummyDataService dds = DummyDataService.createDemoService();

        BeanContainer<String, Customer> customerContainer = new BeanContainer<String, Customer>( Customer.class );
        
        List<Customer> customerList = dds.findAll();
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
        
        comboBox.addItems( customerContainer.getItemIds() );
        createGrid( customerContainer );
       
        comboBox.addValueChangeListener( event -> { 
        	String selection = event.getProperty().getValue().toString();
        	customerContainer.getItem( selection );
            if (selection != null)
                UI.getCurrent().addWindow( infoModal( customerContainer.getItem( selection ) ) );
        	});
        
        grid.addSelectionListener(selectionEvent -> {
        	Object selected = ( ( SingleSelectionModel ) grid.getSelectionModel()).getSelectedRow();
            if (selected != null)
                UI.getCurrent().addWindow( infoModal( grid.getContainerDataSource().getItem(selected) ) );
        });

        layout.addComponent( comboBox );
        layout.addComponent( grid );
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

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
    }
    
    
}

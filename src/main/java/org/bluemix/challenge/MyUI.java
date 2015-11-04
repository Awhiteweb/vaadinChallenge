package org.bluemix.challenge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import org.bluemix.challenge.backend.Customer;
import org.bluemix.challenge.backend.DummyDataService;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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
        
        grid.addSelectionListener( new SelectionListener() {
			
			@Override
			public void select( SelectionEvent event )
			{
				event.getSelected();				
			}
		} );
        
        createGrid( customerContainer );
        
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
        grid.setColumnOrder( "firstName", "lastName", "birthDate", "gender", "phone", "email", "address", "city", "zipCode", "id" );
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

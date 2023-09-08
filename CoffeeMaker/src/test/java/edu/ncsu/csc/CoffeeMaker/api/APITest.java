package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {
	
	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST
	 * API
	 */
	private MockMvc mvc;

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private InventoryService iService;

	private static Inventory localInv;
	
	/**
	 * Sets up the tests.
	 */
	@BeforeEach
	public void setup () {
	    mvc = MockMvcBuilders.webAppContextSetup( context ).build();
		localInv = iService.getInventory();
		localInv.addIngredient(new Ingredient("Coffee", 100));
		localInv.addIngredient(new Ingredient("Milk", 100));
		localInv.addIngredient(new Ingredient("Chocolate", 100));
		localInv.addIngredient(new Ingredient("Sugar", 100));
	

	}
	
	/**
	 * Method to add inventory for makeCoffee testing
	 * @throws Exception in case something goes wrong
	 * @author dgrant
	 */
	public void addInventory() throws Exception {
		final Inventory i = new Inventory();
		i.addIngredient(new Ingredient("Coffee", 50));
		i.addIngredient(new Ingredient("Milk", 50));
		i.addIngredient(new Ingredient("Sugar", 50));
		i.addIngredient(new Ingredient("Chocolate", 50));
		mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
	        	    .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );
	}
	
	/**
	 * Tests the Inventory Api controller, adding inventory and getting inventory
	 * @throws UnsupportedEncodingException in case invenotry object was falsely created.
	 * @throws Exception In case anything goes wrong with the reqeust
	 * @author lspieler
	 */
	@Test
	@Transactional
	public void testAddInventory() throws UnsupportedEncodingException, Exception {
		
		final Inventory i = new Inventory();
		i.addIngredient(new Ingredient("Coffee", 50));
		i.addIngredient(new Ingredient("Milk", 50));
		i.addIngredient(new Ingredient("Sugar", 50));
		i.addIngredient(new Ingredient("Chocolate", 50));
		
		mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
				.content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );
		
		// now, actually see if these items are in the inventory:
		final String inventory_ret = mvc.perform( get( "/api/v1/inventory") ).andDo( print() ).andExpect( status().isOk() )
			    .andReturn().getResponse().getContentAsString();		
		
	}
	
	/**
	 * Tests the recipe Api endpoint. Specifically Adding multiple
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 * @author dgrant
	 */
	@Test
	@Transactional
	public void testAddRecipe() throws UnsupportedEncodingException, Exception {
		final Recipe r1 = new Recipe();
				r1.addIngredient("Coffee", 3, localInv);
				r1.addIngredient("Milk", 4, localInv);
				r1.addIngredient("Sugar", 8, localInv);
				r1.addIngredient("Chocolate", 5, localInv);
		        r1.setPrice( 10 );
		        r1.setName( "Mocha" );
		try {
			mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
		        	    .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );
		}
		catch (final Exception e) {
			Assertions.fail("should have been able to add this recipe");
		}
		
		// now, trying to re-add existing recipe—should fail
		try {
			mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
		        	    .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().is4xxClientError() );
		}
		catch (final Exception e) {
			// this should not have thrown an exception
		}

		final Recipe r2 = new Recipe();
				r2.addIngredient("Coffee", 3, localInv);
				r2.addIngredient("Milk", 4,localInv);
				r2.addIngredient("Sugar", 8,localInv);
				r2.addIngredient("Chocolate", 5,localInv);
		        r2.setPrice( 10 );
		        r2.setName( "Latte" );
		try {
			mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
		        	    .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().isOk() );
		}
		catch (final Exception e) {
			Assertions.fail("should have been able to add this recipe");
		}

	}

	/**
	 * Tests the Recipe Api endpoint, spcifically getting recipies
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 * @author lspieler
	 */
	@Test
	@Transactional
	public void testGetRecipe() throws UnsupportedEncodingException, Exception {
		
		final String recipe = mvc.perform( get( "/api/v1/recipes") ).andDo( print() ).andExpect( status().isOk() )
			    .andReturn().getResponse().getContentAsString();
		
		if (!recipe.contains("Mocha")) {
			
			final Recipe r1 = new Recipe();
				r1.addIngredient("Coffee", 3, localInv);
				r1.addIngredient("Milk", 4,localInv);
				r1.addIngredient("Sugar", 8, localInv);
				r1.addIngredient("Chocolate", 5, localInv);
		        r1.setPrice( 10 );
		        r1.setName( "Mocha" );
		        
		        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
		        	    .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );
			  
			}

		if (!recipe.contains("Latte")) {
			
			final Recipe r2 = new Recipe();
				r2.addIngredient("Coffee", 3, localInv);
				r2.addIngredient("Milk", 4, localInv);
				r2.addIngredient("Sugar", 8,localInv);
				r2.addIngredient("Chocolate", 5, localInv);
		        r2.setPrice( 10 );
		        r2.setName( "Latte" );
		        
		        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
		        	    .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().isOk() );
			  
			}

		final String recipe_ret = mvc.perform( get( "/api/v1/recipes") ).andDo( print() ).andExpect( status().isOk() )
			    .andReturn().getResponse().getContentAsString();
		
		// check if added recipes are in the String
		Assertions.assertTrue(recipe_ret.contains("Mocha"));
		Assertions.assertTrue(recipe_ret.contains("Latte"));

		// make sure that recipes that should not be in the string are not
		Assertions.assertFalse(recipe_ret.contains("Cappuccino"));
		
		// now, try getting individual recipes:
		Assertions.assertNotNull(mvc.perform( get( "/api/v1/recipes/Mocha") ).andDo( print() ).andExpect( status().isOk() )
			    .andReturn().getResponse().getContentAsString());
		Assertions.assertNotNull(mvc.perform( get( "/api/v1/recipes/Latte") ).andDo( print() ).andExpect( status().isOk() )
			    .andReturn().getResponse().getContentAsString());
		
		// Cappuccino was never added, so should give a 404 exception:
		mvc.perform( get( "/api/v1/recipes/Cappuccino") ).andDo( print() ).andExpect( status().is4xxClientError() ) 
				.andReturn().getResponse().getContentAsString();
			   
	}
	
	/**
	 * Tests recipe Api endpoint, specifically deleting recipies.
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 * @author dgrant
	 */
	@Test
	@Transactional
	public void testDeleteRecipe() throws UnsupportedEncodingException, Exception {
		final String recipe = mvc.perform( get( "/api/v1/recipes") ).andDo( print() ).andExpect( status().isOk() )
			    .andReturn().getResponse().getContentAsString();
		
		if (!recipe.contains("Mocha")) {
			
			final Recipe r1 = new Recipe();
				r1.addIngredient("Coffee", 3, localInv);
				r1.addIngredient("Milk", 4,localInv);
				r1.addIngredient("Sugar", 8,localInv);
				r1.addIngredient("Chocolate", 5, localInv);
		        r1.setPrice( 10 );
		        r1.setName( "Mocha" );
		        
		        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
		        	    .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );
			  
			}
		Assertions.assertNotNull("Mocha");
		
		// now, try deleting the recipe--should give 2xx code
		try {
			mvc.perform( delete( "/api/v1/recipes/Mocha") ).andDo( print() ).andExpect( status().isOk() )
			.andReturn().getResponse().getContentAsString();
		} catch (final Exception e) {
			Assertions.fail("Should not have thrown an exception");
		}
		
		// try deleting recipe that doesnt exist--should give 404 code
		try {
			mvc.perform( delete( "/api/v1/recipes/Mocha") ).andDo( print() ).andExpect( status().is4xxClientError() )
			.andReturn().getResponse().getContentAsString();
		} catch (final Exception e) {
			Assertions.fail("Should not have thrown an exception");
		}

	}
	
	
	/**
	 * Test the MakeCoffee Api controller
	 * @throws Exception
	 * @author lspieler
	 */
	@Test
	@Transactional
	public void testMakeCoffee() throws UnsupportedEncodingException, Exception {
		final String recipe = mvc.perform( get( "/api/v1/recipes") ).andDo( print() ).andExpect( status().isOk() )
			    .andReturn().getResponse().getContentAsString();
		
		if (!recipe.contains("Mocha")) {
			//Create mocha recipe
			final Recipe r1 = new Recipe();
				r1.addIngredient("Coffee", 3, localInv);
				r1.addIngredient("Milk", 4, localInv);
				r1.addIngredient("Sugar", 8, localInv);
				r1.addIngredient("Chocolate", 5, localInv);
				r1.setPrice( 10 );
				r1.setName( "Mocha" );
	
		try {
			mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
			    	    .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );
		} catch (final Exception e) {
			Assertions.fail("Should have created mocha recipies sucessfully but failed for some reason.");
		}
	}
	Assertions.assertNotNull("Mocha");
		
		//Adds inventory so we can make drinks
		addInventory();
		
		//Try making Mocha dring, should succeeed
		try {
			mvc.perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
				    .content( TestUtils.asJsonString( 10 ) ) ).andExpect( status().isOk() );
		} catch (final Exception e) {
			Assertions.fail("Tried to make valid recipie, but failed");
		}
		
		//Failure cases
		
		//Did not pay enough
		mvc.perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
				   .content( TestUtils.asJsonString( 5 ) ) ).andExpect( status().is(409) );

		//Null Recipe in URL
		mvc.perform( post( "/api/v1/makecoffee/null" ).contentType( MediaType.APPLICATION_JSON )
				   .content( TestUtils.asJsonString( 10 ) ) ).andExpect( status().is(404) );
		
		//Null payment json body
		mvc.perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
				   .content( TestUtils.asJsonString( null ) ) ).andExpect( status().is(409) );
		
		//Creating recipe that consumes too much inventory
		final Recipe r2 = new Recipe();
	        r2.setPrice( 50 );
			r2.addIngredient("Coffee", 50, localInv);
			r2.addIngredient("Milk", 50, localInv);
			r2.addIngredient("Sugar", 50, localInv);
			r2.addIngredient("Chocolate", 50, localInv);

	        r2.setName( "Impossible" );
	      
	    //Try creating drink with impossible recipe
		try {
			mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
			    	    .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().isOk() );
		} catch (final Exception e) {
			Assertions.fail("Should have created mocha recipies sucessfully but failed for some reason.");
		}
		
		mvc.perform( post( "/api/v1/makecoffee/Impossible" ).contentType( MediaType.APPLICATION_JSON )
				   .content( TestUtils.asJsonString( 10 ) ) ).andExpect( status().is4xxClientError() );
		
		
	
	}

}

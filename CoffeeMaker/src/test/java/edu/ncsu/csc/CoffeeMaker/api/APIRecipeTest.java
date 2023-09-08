package edu.ncsu.csc.CoffeeMaker.api;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIRecipeTest {
    @Autowired
    private RecipeService service;
    @Autowired
    private MockMvc       mvc;
    
   
	
	@Autowired
	private InventoryService iService;

	private Inventory localInv;
	
	 @BeforeEach
		public void setup () {
			localInv = iService.getInventory();
			localInv.addIngredient(new Ingredient("Coffee", 100));
			localInv.addIngredient(new Ingredient("Milk", 100));
			localInv.addIngredient(new Ingredient("Chocolate", 100));
			localInv.addIngredient(new Ingredient("Sugar", 100));
		

		}
	 
    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        service.deleteAll();
        final Recipe r = new Recipe();
        r.addIngredient("Coffee", 4, localInv);
        r.addIngredient("Milk", 4,localInv);
        r.addIngredient("Sugar", 8, localInv);
        r.addIngredient("Chocolate", 5, localInv);
        r.setPrice( 10 );
        r.setName( "Mocha" );
        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient("Coffee", 1, localInv);
        recipe.addIngredient("Milk", 20, localInv);
        recipe.addIngredient("Sugar", 5, localInv);
        recipe.addIngredient("Chocolate", 10, localInv);
        recipe.setPrice( 5 );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );
        Assertions.assertEquals( 1, (int) service.count() );
    }
    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {
        service.deleteAll();
        /* Tests a recipe with a duplicate name to make sure it's rejected */
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("Coffee", 3));
        ingredients.add(new Ingredient("Milk", 1));
        ingredients.add(new Ingredient("Sugar", 1));
        final Recipe r1 = createRecipe( name, 50, ingredients );
        service.save( r1 );
        final Recipe r2 = createRecipe( name, 50, ingredients );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );
        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
    }
    @Test
    @Transactional
    public void testAddRecipe15 () throws Exception {
        service.deleteAll();
        /* Tests to make sure that our cap of 3 recipes is enforced */
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final List<Ingredient> ingredients1 = new ArrayList<Ingredient>();
        ingredients1.add(new Ingredient("Coffee", 3));
        ingredients1.add(new Ingredient("Milk", 1));
        ingredients1.add(new Ingredient("Sugar", 1));
        final Recipe r1 = createRecipe( "Coffee", 50, ingredients1 );
        service.save( r1 );
        final List<Ingredient> ingredients2 = new ArrayList<Ingredient>();
        ingredients2.add(new Ingredient("Coffee", 3));
        ingredients2.add(new Ingredient("Milk", 1));
        ingredients2.add(new Ingredient("Sugar", 1));
        ingredients2.add(new Ingredient("Chocolate", 1));
        final Recipe r2 = createRecipe( "Mocha", 50, ingredients2 );
        service.save( r2 );
        final List<Ingredient> ingredients3 = new ArrayList<Ingredient>();
        ingredients3.add(new Ingredient("Coffee", 3));
        ingredients3.add(new Ingredient("Milk", 2));
        ingredients3.add(new Ingredient("Sugar", 2));
        final Recipe r3 = createRecipe( "Latte", 60, ingredients3 );
        service.save( r3 );
        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );
        final List<Ingredient> ingredients4 = new ArrayList<Ingredient>();
        ingredients4.add(new Ingredient("Chocolate", 1));
        ingredients4.add(new Ingredient("Milk", 2));
        ingredients4.add(new Ingredient("Sugar", 2));
        final Recipe r4 = createRecipe( "Hot Chocolate", 75, ingredients4 );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );
        Assertions.assertEquals( 3, service.count(), "Creating a fourth recipe should not get saved" );
    }
    
    @Test
    @Transactional
    public void testEditRecipe() throws Exception {
    	service.deleteAll();
        /* Tests to make sure that our cap of 3 recipes is enforced */
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final List<Ingredient> ingredients1 = new ArrayList<Ingredient>();
        ingredients1.add(new Ingredient("Coffee", 3));
        ingredients1.add(new Ingredient("Milk", 1));
        ingredients1.add(new Ingredient("Sugar", 1));
        final Recipe r1 = createRecipe( "Coffee", 50, ingredients1 );
        service.save( r1 );
        
        final List<Ingredient> ingredients2 = new ArrayList<Ingredient>();
        ingredients2.add(new Ingredient("Coffee", 3));
        ingredients2.add(new Ingredient("Milk", 1));
        ingredients2.add(new Ingredient("Sugar", 1));
        ingredients2.add(new Ingredient("Chocolate", 1));
        final Recipe r2 = createRecipe( "Coffee", 10, ingredients2 );

        
        mvc.perform( put( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is2xxSuccessful());
        Assertions.assertEquals( 1, service.count(), "Editing recipe should have not changed number of recipes" );
        
        final List<Ingredient> ingredients3 = new ArrayList<Ingredient>();
        ingredients3.add(new Ingredient("Coffee", 3));
        ingredients3.add(new Ingredient("Milk", 2));
        ingredients3.add(new Ingredient("Sugar", 2));
        final Recipe r3 = createRecipe( "Latte", 60, ingredients3 );
        
    
        mvc.perform( put( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                     .content( TestUtils.asJsonString( r3 ) ) ).andExpect( status().isNotFound());

    	
    }
    
    private Recipe createRecipe ( final String name, final Integer price, final List<Ingredient> ingredients ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        for ( final Ingredient ingredient : ingredients ) {
            recipe.addIngredient( ingredient, localInv );
        }
        return recipe;
    }
}
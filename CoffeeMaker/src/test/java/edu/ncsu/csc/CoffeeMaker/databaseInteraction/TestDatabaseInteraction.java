package edu.ncsu.csc.CoffeeMaker.databaseInteraction;



import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )

public class TestDatabaseInteraction {
	
	@Autowired
	private RecipeService recipeService;
	
	@Autowired
	private InventoryService inventoryService;
	

	
	
	@Test
	@Transactional
	public void testRecipeAndInventoryDB() {
		
		recipeService.deleteAll();
		inventoryService.deleteAll();
		
		final Recipe r1 = new Recipe("Iced Coffee", 4);
		
		final Inventory i = new Inventory();
		
		final Ingredient coffee = new Ingredient("Coffee", 5);
		final Ingredient ice = new Ingredient("Ice", 20);
		
		
		final Ingredient coffeeForRecipe = new Ingredient("Coffee", 3);
		final Ingredient iceForRecipe = new Ingredient("Ice", 2);
		
		
		i.addIngredient(coffee);
		i.addIngredient(ice);
		
		r1.addIngredient(coffeeForRecipe, i);
		r1.addIngredient(iceForRecipe,i);
		
//		does not work yet
//		inventoryService.save(i);
		recipeService.save(r1);
		
		final List<Recipe> dbRecipes = recipeService.findAll();
		

//		List<Inventory> dbInventories = inventoryService.findAll();
		
		
		Assertions.assertEquals(1, dbRecipes.size(), "Only 1 recipe should be in DB");
//		Assertions.assertEquals(1, dbInventories.size(), "Only 1 inventory should be in DB");

		
		
	}
	

//  Copy and pasted from write up
//	@Test
//    @Transactional
//    public void testRecipesII () {
//        recipeService.deleteAll();
//
//        final Recipe r = new Recipe();
//
//        r.setName( "Special Drink" );
//
//        r.setChocolate( 5 );
//        r.setCoffee( 6 );
//        r.setMilk( 7 );
//        r.setSugar( 8 );
//
//        r.setPrice( 2 );
//
//        recipeService.save( r );
//
//        final List<Recipe> dbRecipes = (List<Recipe>) recipeService.findAll();
//
//        assertEquals( 1, dbRecipes.size() );
//
//        final Recipe dbRecipe = dbRecipes.get( 0 );
//
//        /*
//         * Note we are _not_ using the `Recipe.equals(Object)` method here
//         * because it only checks the name!
//         */
//        assertEquals( r.getName(), dbRecipe.getName() );
//        assertEquals( r.getChocolate(), dbRecipe.getChocolate() );
//        assertEquals( r.getMilk(), dbRecipe.getMilk() );
//        assertEquals( r.getSugar(), dbRecipe.getSugar() );
//        assertEquals( r.getPrice(), dbRecipe.getPrice() );
//
//        final Recipe dbRecipeByName = recipeService.findByName( "Special Drink" );
//
//        assertEquals( r.getChocolate(), dbRecipeByName.getChocolate() );
//
//        dbRecipe.setPrice( 15 );
//        dbRecipe.setSugar( 12 );
//        recipeService.save( dbRecipe );
//
//        assertEquals( 1, recipeService.count() );
//
//        assertEquals( 15, (int) ( (Recipe) recipeService.findAll().get( 0 ) ).getPrice() );
//
//    }

}

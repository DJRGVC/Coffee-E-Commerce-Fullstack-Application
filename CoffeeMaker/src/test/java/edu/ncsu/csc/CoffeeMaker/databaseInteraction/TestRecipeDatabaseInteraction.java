package edu.ncsu.csc.CoffeeMaker.databaseInteraction;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)
public class TestRecipeDatabaseInteraction {

	@Autowired
	private RecipeService recipeService;
	
	private Inventory localInv;

	
	@Test
	@Transactional
	public void testCreateRecipes() {

		recipeService.deleteAll();

		final Recipe r1 = new Recipe();

		r1.setName("Black Coffee");
		r1.setPrice(1);
		
	
		final Recipe r2 = new Recipe();

		r2.setName("Mocha");
		r2.setPrice(3);


		recipeService.save(r1);
		recipeService.save(r2);

		Assert.assertEquals("Creating two recipes should results in two recipes in the database", 2,
				recipeService.count());

	}
	
	/**
	 * 
	 * Tests recipe saving and retrieval from database.
	 * 
	 * @author lspieler
	 */
	@Test
	@Transactional
	public void testSaveandRetrieveRecipe(){
	    
		recipeService.deleteAll();
		localInv = new Inventory();
		localInv.addIngredient(new Ingredient("Coffee", 100));
		localInv.addIngredient(new Ingredient("Milk", 100));
		localInv.addIngredient(new Ingredient("Chocolate", 100));
		localInv.addIngredient(new Ingredient("Sugar", 100));
		
	    final Recipe recipe = new Recipe();
	    recipe.addIngredient("Sugar", 1, localInv);
	    recipe.addIngredient("Milk", 1, localInv);
	    recipe.addIngredient("Coffee", 2, localInv);
	    recipe.addIngredient("Chocolate", 1, localInv);
	    recipe.setName("Mocha");
	    
	    recipeService.save(recipe);
	    
	    final List<Recipe> dbRecipes = recipeService.findAll();

	    Assertions.assertEquals(1, dbRecipes.size());

	    Recipe dbRecipe = dbRecipes.get(0);

	    Assertions.assertEquals(recipe.getName(), dbRecipe.getName());
	    Assertions.assertEquals(recipe.getPrice(), dbRecipe.getPrice());
	    Assertions.assertEquals(recipe.getAmount("Sugar"), dbRecipe.getAmount("Sugar"));
	    Assertions.assertEquals(recipe.getAmount("Coffee"), dbRecipe.getAmount("Coffee"));
	    Assertions.assertEquals(recipe.getAmount("Chocolate"), dbRecipe.getAmount("Chocolate"));
	    Assertions.assertEquals(recipe.getAmount("Milk"), dbRecipe.getAmount("Milk"));
	    
	 /* Other fields would get tested one at a time here too */
	    
	    dbRecipe = recipeService.findByName("Mocha");

	    Assertions.assertEquals(recipe.getName(), dbRecipe.getName());
	    Assertions.assertEquals(recipe.getPrice(), dbRecipe.getPrice());
	    Assertions.assertEquals(recipe.getAmount("Sugar"), dbRecipe.getAmount("Sugar"));
	    Assertions.assertEquals(recipe.getAmount("Coffee"), dbRecipe.getAmount("Coffee"));
	    Assertions.assertEquals(recipe.getAmount("Chocolate"), dbRecipe.getAmount("Chocolate"));
	    Assertions.assertEquals(recipe.getAmount("Milk"), dbRecipe.getAmount("Milk"));
	    
	}
	
	/**
	 * 
	 * Tests edit recipe interaction with db.
	 * 
	 * @author Leonard
	 */
	@Test
	@Transactional
	public void testEditRecipe(){
	
		recipeService.deleteAll();
		localInv = new Inventory();
		localInv.addIngredient(new Ingredient("Coffee", 100));
		localInv.addIngredient(new Ingredient("Milk", 100));
		localInv.addIngredient(new Ingredient("Chocolate", 100));
		localInv.addIngredient(new Ingredient("Sugar", 100));
	    
	    final Recipe recipe = new Recipe();
	    final Recipe recipe2 = new Recipe();
	    
	    
	    recipe2.addIngredient("Coffee", 2, localInv);
	    recipe2.setPrice(350);
	    
	   
	    recipe.updateRecipe(recipe2, localInv.getIngNames());
	    
	    
	   
	    recipe.setName("Mocha");
	    
	    recipeService.save(recipe);
	    
	    List<Recipe> dbRecipes = recipeService.findAll();

	    Assertions.assertEquals(1, dbRecipes.size());

	    Recipe dbRecipe = dbRecipes.get(0);
	    
	    
	    dbRecipe.setPrice(450);
	    localInv.addIngredient(new Ingredient("Ice", 100));
	    dbRecipe.addIngredient("Ice", 2, localInv);
	    dbRecipe.setName("Iced Coffee");
	    

	    recipeService.save(dbRecipe);
	    
	    dbRecipes = recipeService.findAll();

	    Assertions.assertEquals(1, dbRecipes.size());
	    
	    dbRecipe = dbRecipes.get(0);
	    
	    Assertions.assertEquals(recipe.getPrice(), dbRecipe.getPrice());
	    Assertions.assertEquals(recipe.getAmount("Coffee"), dbRecipe.getAmount("Coffee"));
	    Assertions.assertEquals(recipe.getName(), dbRecipe.getName());
	    
	    
	    
	}
	
	/**
	 * 
	 * Tests find recipe from name.
	 * 
	 * @author Brian
	 */
	@Test
	@Transactional
	public void testFindByName(){
		
		recipeService.deleteAll();
		localInv = new Inventory();
		localInv.addIngredient(new Ingredient("Coffee", 100));
		localInv.addIngredient(new Ingredient("Milk", 100));
		localInv.addIngredient(new Ingredient("Chocolate", 100));
		localInv.addIngredient(new Ingredient("Sugar", 100));
		
	    
	    final Recipe recipe = new Recipe();
	    recipe.setPrice(350);
	    recipe.addIngredient("Coffee", 3, localInv);
	    recipe.setName("Black Coffee");
	    
	    recipeService.save(recipe);
	    
	    final Recipe dbRecipe = recipeService.findByName("Black Coffee");

	    Assertions.assertEquals(recipe.getName(), dbRecipe.getName());
	    Assertions.assertEquals(recipe.getPrice(), dbRecipe.getPrice());
	    Assertions.assertEquals(recipe.getAmount("Coffee"), dbRecipe.getAmount("Coffee"));
	
	}
	
	/**
	 * 
	 * Tests delete from database.
	 * 
	 * @author Brian
	 */
	@Test
	@Transactional
	public void testDelete(){
	
		recipeService.deleteAll();
		localInv = new Inventory();
		localInv.addIngredient(new Ingredient("Coffee", 100));
		localInv.addIngredient(new Ingredient("Milk", 100));
		localInv.addIngredient(new Ingredient("Chocolate", 100));
		localInv.addIngredient(new Ingredient("Sugar", 100));
	    
	    final Recipe recipe = new Recipe();
	    recipe.setPrice(350);
	    recipe.addIngredient("Coffee", 2, localInv);
	    recipe.setName("Black Coffee");
	    
	    recipeService.save(recipe);
	    
	    recipeService.delete(recipe);
	    
	    final List<Recipe> dbRecipes = recipeService.findAll();

	    Assertions.assertEquals(0, dbRecipes.size());
	}

}
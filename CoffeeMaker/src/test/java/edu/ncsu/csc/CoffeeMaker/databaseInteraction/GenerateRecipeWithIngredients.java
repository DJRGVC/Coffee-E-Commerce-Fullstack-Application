package edu.ncsu.csc.CoffeeMaker.databaseInteraction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.DomainObject;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)
public class GenerateRecipeWithIngredients {

	@Autowired
	private RecipeService recipeService;
	
	@Autowired 
	private InventoryService iService;

	@BeforeEach
	public void setup() {
		recipeService.deleteAll();
		localInv = iService.getInventory();
		localInv.addIngredient(new Ingredient("Coffee", 100));
		localInv.addIngredient(new Ingredient("Milk", 100));
		localInv.addIngredient(new Ingredient("Chocolate", 100));
		localInv.addIngredient(new Ingredient("Sugar", 100));
	}
	
	private static Inventory localInv;

	@BeforeEach
	public void setupInv() {
		
	}
	

	@Test
	public void createRecipe() {
		final Recipe r1 = new Recipe();
		r1.setName("Delicious Coffee");

		r1.setPrice(50);
		localInv.addIngredient(new Ingredient("Pumpkin Spice", 100));
		r1.addIngredient("Coffee", 10, localInv);
		r1.addIngredient("Pumpkin Spice", 3, localInv);
		r1.addIngredient("Milk", 2, localInv);

		recipeService.save(r1);

		printRecipes();
	}

	private void printRecipes() {
		for (final DomainObject r : recipeService.findAll()) {
			System.out.println(r);
		}
	}

}
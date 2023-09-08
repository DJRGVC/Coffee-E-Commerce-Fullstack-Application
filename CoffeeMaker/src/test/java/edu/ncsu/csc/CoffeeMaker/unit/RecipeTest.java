package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)
public class RecipeTest {

	@Autowired
	private RecipeService service;
	
	@Autowired
	private InventoryService iService;
	
	/* Local (non-database) Recipes for testing */
	Recipe rp1;
	Recipe rp2;

	@BeforeEach
	public void setup() {
		service.deleteAll();
	
	}
	
	
	@Test
	@Transactional
	public void testNewRecipe() {
				

		final Inventory inv = iService.getInventory();
		
		inv.addIngredient(new Ingredient("Coffee", 100));
		inv.addIngredient(new Ingredient("Milk", 100));

		rp1 = new Recipe("Black Coffee", 4);
		
		Assertions.assertTrue(rp1.checkRecipe());
		
		rp1.addIngredient("Coffee", 2, inv);
		
		rp2 = new Recipe("Latte", 5);
		rp2.addIngredient("Coffee", 6, inv);
		
		Assertions.assertEquals("Black Coffee", rp1.getName());
		Assertions.assertEquals("Latte", rp2.getName());
		
		Assertions.assertEquals(4, rp1.getPrice());
		Assertions.assertEquals(5, rp2.getPrice());
		
		Assertions.assertEquals(2, rp1.getAmount("Coffee"));
		try {
			rp1.getAmount("Jamaican Coffee");
			Assertions.fail();
		} catch (final Exception e) {
			
		}


		

		
	}
	
	@Test
	@Transactional
	public void testUpdateRecipeAndEquals() {
				
		
		final Inventory inv = iService.getInventory();
		
		inv.addIngredient(new Ingredient("Coffee", 100));
		inv.addIngredient(new Ingredient("Milk", 100));

		rp1 = new Recipe("Black Coffee", 4);
		rp1.addIngredient("Coffee", 2, inv);
	
		rp2 = new Recipe("Latte", 5);
		rp2.addIngredient("Coffee", 6, inv);
		
		
		rp1.updateRecipe(rp2, inv.getIngNames());
		
	
		
		Assertions.assertEquals("Latte", rp1.getName());
		Assertions.assertEquals(5, rp1.getPrice());
		Assertions.assertEquals(rp2.getIngredients(), rp1.getIngredients());
		
		
		Assertions.assertEquals(rp1, rp2);
		

		Assertions.assertEquals(rp1, rp1);
		Assertions.assertFalse(rp1.equals(null));
		Assertions.assertFalse(rp1.equals("not an ingredient object"));
		Assertions.assertEquals(rp1.hashCode(), rp2.hashCode());
		
		final String expected = "Latte contains the following ingredients:\nIngredient [id=null, name=Coffee, amount=6]";
		
		Assertions.assertEquals(expected, rp1.toString(), "toString does not match");
		
		
	}
	
	@Test
	@Transactional
	public void TestValidateRecipe() {
		rp1 = new Recipe("Black Coffee", 4);
		final Inventory inv = iService.getInventory();
		
		inv.addIngredient(new Ingredient("Coffee", 100));
		inv.addIngredient(new Ingredient("Milk", 100));
		rp1.addIngredient("Coffee", 2, inv);
		
		Assertions.assertTrue(rp1.validateIngredients());
		
		rp1.addIngredient("Coffee", 2, inv);
		
		Assertions.assertFalse(rp1.validateIngredients());
		
	}
	
	@Test
	@Transactional
	public void testUpdateRecipeAndEqualsFail() {
				
	
		final Inventory inv = new Inventory();


		rp1 = new Recipe("Black Coffee", 4);
		rp2 = new Recipe("Latte", 5);
		

		try {
			rp1.addIngredient("Coffee", 2, inv);
			Assertions.fail();
		}
		catch (final IllegalArgumentException e) {
			//Expected
		}
		
		try {
			rp1.addIngredient(new Ingredient("Coffee", 2), inv);
			Assertions.fail();
		}
		catch (final IllegalArgumentException e) {
			
		}
		
		try {
			rp1.updateRecipe( rp2, inv.getIngNames());
			Assertions.fail();
		}
		catch (final IllegalArgumentException e) {
			
		}
		
		inv.addIngredient(new Ingredient("Milk", 10));
		
		try {
			rp1.addIngredient("Coffee", 2, inv);
			Assertions.fail();
		}
		catch (final IllegalArgumentException e) {
			//Expected
		}
		
		try {
			rp1.addIngredient(new Ingredient("Coffee", 2), inv);
			Assertions.fail();
		}
		catch (final IllegalArgumentException e) {
			
		}
		
		try {
			
			rp1.updateRecipe(rp2, inv.getIngNames());
			Assertions.fail();
		}
		catch (final IllegalArgumentException e) {
			
		}
		
		
		
		
	}
	


//	@Test
//	@Transactional
//	public void testAddRecipe() {
//
//		final Recipe r1 = new Recipe();
//		r1.setName("Black Coffee");
//		r1.setPrice(1);
//		r1.setCoffee(1);
//		r1.setMilk(0);
//		r1.setSugar(0);
//		r1.setChocolate(0);
//		service.save(r1);
//
//		final Recipe r2 = new Recipe();
//		r2.setName("Mocha");
//		r2.setPrice(1);
//		r2.setCoffee(1);
//		r2.setMilk(1);
//		r2.setSugar(1);
//		r2.setChocolate(1);
//		service.save(r2);
//
//		final List<Recipe> recipes = service.findAll();
//		Assertions.assertEquals(2, recipes.size(), "Creating two recipes should result in two recipes in the database");
//
//		Assertions.assertEquals(r1, recipes.get(0), "The retrieved recipe should match the created one");
//	}
//
//	@Test
//	@Transactional
//	public void testNoRecipes() {
//		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
//
//		final Recipe r1 = new Recipe();
//		r1.setName("Tasty Drink");
//		r1.setPrice(12);
//		r1.setCoffee(-12);
//		r1.setMilk(0);
//		r1.setSugar(0);
//		r1.setChocolate(0);
//
//		final Recipe r2 = new Recipe();
//		r2.setName("Mocha");
//		r2.setPrice(1);
//		r2.setCoffee(1);
//		r2.setMilk(1);
//		r2.setSugar(1);
//		r2.setChocolate(1);
//
//		final List<Recipe> recipes = List.of(r1, r2);
//
//		try {
//			service.saveAll(recipes);
//			Assertions.assertEquals(0, service.count(),
//					"Trying to save a collection of elements where one is invalid should result in neither getting saved");
//		} catch (final Exception e) {
//			Assertions.assertTrue(e instanceof ConstraintViolationException);
//		}
//
//	}
//
//	@Test
//	@Transactional
//	public void testAddRecipe1() {
//
//		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
//		final String name = "Coffee";
//		final Recipe r1 = createRecipe(name, 50, 3, 1, 1, 0);
//
//		service.save(r1);
//
//		Assertions.assertEquals(1, service.findAll().size(), "There should only one recipe in the CoffeeMaker");
//		Assertions.assertNotNull(service.findByName(name));
//
//	}
//
//	/* Test2 is done via the API for different validation */
//
//	@Test
//	@Transactional
//	public void testAddRecipe3() {
//		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
//		final String name = "Coffee";
//		final Recipe r1 = createRecipe(name, -50, 3, 1, 1, 0);
//
//		try {
//			service.save(r1);
//
//			Assertions.assertNull(service.findByName(name), "A recipe was able to be created with a negative price");
//		} catch (final ConstraintViolationException cvee) {
//			// expected
//		}
//
//	}
//
//	@Test
//	@Transactional
//	public void testAddRecipe4() {
//		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
//		final String name = "Coffee";
//		final Recipe r1 = createRecipe(name, 50, -3, 1, 1, 2);
//
//		try {
//			service.save(r1);
//
//			Assertions.assertNull(service.findByName(name),
//					"A recipe was able to be created with a negative amount of coffee");
//		} catch (final ConstraintViolationException cvee) {
//			// expected
//		}
//
//	}
//
//	@Test
//	@Transactional
//	public void testAddRecipe5() {
//		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
//		final String name = "Coffee";
//		final Recipe r1 = createRecipe(name, 50, 3, -1, 1, 2);
//
//		try {
//			service.save(r1);
//
//			Assertions.assertNull(service.findByName(name),
//					"A recipe was able to be created with a negative amount of milk");
//		} catch (final ConstraintViolationException cvee) {
//			// expected
//		}
//
//	}
//
//	@Test
//	@Transactional
//	public void testAddRecipe6() {
//		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
//		final String name = "Coffee";
//		final Recipe r1 = createRecipe(name, 50, 3, 1, -1, 2);
//
//		try {
//			service.save(r1);
//
//			Assertions.assertNull(service.findByName(name),
//					"A recipe was able to be created with a negative amount of sugar");
//		} catch (final ConstraintViolationException cvee) {
//			// expected
//		}
//
//	}
//
//	@Test
//	@Transactional
//	public void testAddRecipe7() {
//		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
//		final String name = "Coffee";
//		final Recipe r1 = createRecipe(name, 50, 3, 1, 1, -2);
//
//		try {
//			service.save(r1);
//
//			Assertions.assertNull(service.findByName(name),
//					"A recipe was able to be created with a negative amount of chocolate");
//		} catch (final ConstraintViolationException cvee) {
//			// expected
//		}
//
//	}
//
//	@Test
//	@Transactional
//	public void testAddRecipe13() {
//		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
//
//		final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
//		service.save(r1);
//		final Recipe r2 = createRecipe("Mocha", 50, 3, 1, 1, 2);
//		service.save(r2);
//
//		Assertions.assertEquals(2, service.count(),
//				"Creating two recipes should result in two recipes in the database");
//
//	}
//
//	@Test
//	@Transactional
//	public void testAddRecipe14() {
//		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
//
//		final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
//		service.save(r1);
//		final Recipe r2 = createRecipe("Mocha", 50, 3, 1, 1, 2);
//		service.save(r2);
//		final Recipe r3 = createRecipe("Latte", 60, 3, 2, 2, 0);
//		service.save(r3);
//
//		Assertions.assertEquals(3, service.count(),
//				"Creating three recipes should result in three recipes in the database");
//
//	}
//
//	@Test
//	@Transactional
//	public void testDeleteRecipe1() {
//		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
//
//		final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
//		service.save(r1);
//
//		Assertions.assertEquals(1, service.count(), "There should be one recipe in the database");
//
//		service.delete(r1);
//		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
//	}
//
//	@Test
//	@Transactional
//	public void testDeleteRecipe2() {
//		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
//
//		final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
//		service.save(r1);
//		final Recipe r2 = createRecipe("Mocha", 50, 3, 1, 1, 2);
//		service.save(r2);
//		final Recipe r3 = createRecipe("Latte", 60, 3, 2, 2, 0);
//		service.save(r3);
//
//		Assertions.assertEquals(3, service.count(), "There should be three recipes in the database");
//
//		service.deleteAll();
//
//		Assertions.assertEquals(0, service.count(), "`service.deleteAll()` should remove everything");
//
//	}
//
//	@Test
//	@Transactional
//	public void testEditRecipe1() {
//		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
//
//		final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
//		service.save(r1);
//
//		r1.setPrice(70);
//
//		service.save(r1);
//
//		final Recipe retrieved = service.findByName("Coffee");
//
//		Assertions.assertEquals(70, (int) retrieved.getPrice());
//		Assertions.assertEquals(3, (int) retrieved.getCoffee());
//		Assertions.assertEquals(1, (int) retrieved.getMilk());
//		Assertions.assertEquals(1, (int) retrieved.getSugar());
//		Assertions.assertEquals(0, (int) retrieved.getChocolate());
//
//		Assertions.assertEquals(1, service.count(), "Editing a recipe shouldn't duplicate it");
//
//	}
//
//	private Recipe createRecipe(final String name, final Integer price, final Integer coffee, final Integer milk,
//			final Integer sugar, final Integer chocolate) {
//		final Recipe recipe = new Recipe();
//		recipe.setName(name);
//		recipe.setPrice(price);
//		recipe.setCoffee(coffee);
//		recipe.setMilk(milk);
//		recipe.setSugar(sugar);
//		recipe.setChocolate(chocolate);
//
//		return recipe;
//	}
//
//
//	/**
//	 * 
//	 * Tests check recipe (all ingredients are 0)
//	 * 
//	 * @author Brian
//	 * 
//	 */
//	@Test
//	@Transactional
//	public void testCheckRecipe() {
//		Recipe r1 = new Recipe();
//		r1.setChocolate(0);
//		r1.setCoffee(0);
//		r1.setMilk(0);
//		r1.setSugar(0);
//		Assertions.assertTrue(r1.checkRecipe());
//	}
//
//	/**
//	 * 
//	 * Tests check recipe (all ingredients are not 0)
//	 * 
//	 * @author Brian
//	 * 
//	 */
//	@Test
//	@Transactional
//	public void testCheckRecipe2() {
//		Recipe r1 = new Recipe();
//		r1.setChocolate(1);
//		r1.setCoffee(1);
//		r1.setMilk(1);
//		r1.setSugar(1);
//		Assertions.assertFalse(r1.checkRecipe());
//	}
//
//	/**
//	 * 
//	 * Test toString method
//	 * 
//	 * @author Brian
//	 * 
//	 */
//	@Test
//	@Transactional
//	public void testToString() {
//		Recipe r1 = new Recipe();
//		r1.setName("Coffee");
//		Assertions.assertEquals("Coffee", r1.toString());
//	}
//	
//	/**
//	 * 
//	 * Test hashing method
//	 * 
//	 * @author Brian
//	 */
//	@Test
//	@Transactional
//	public void testHashCode() {
//		Recipe r1 = new Recipe();
//		Recipe r2 = new Recipe();
//		r1.setName("Coffee");
//		r2.setName("Coffee");
//		Assertions.assertEquals(r1.hashCode(), r2.hashCode());
//	}
//
//
//	/**
//	 * 
//	 * Test for equals method
//	 * 
//	 * @author Brian
//	 * 
//	 */
//	@Test
//	@Transactional
//	public void testEquals() {
//		Recipe r1 = new Recipe();
//		Recipe r2 = new Recipe();
//		r1.setName("Coffee");
//		r2.setName("Coffee");
//		Assertions.assertTrue(r1.equals(r2));
//	}
//
//	/**
//	 * 
//	 * Test for equals method
//	 * 
//	 * @author Brian
//	 * 
//	 */
//	@Test
//	@Transactional
//	public void testEquals2() {
//		Recipe r1 = new Recipe();
//		Recipe r2 = null;
//		r1.setName("Coffee");
//		Assertions.assertFalse(r1.equals(r2));
//	}
//
//	/**
//	 * 
//	 * Test for equals method
//	 * 
//	 * @author Brian
//	 * 
//	 */
//	@Test
//	@Transactional
//	public void testEquals3() {
//		Recipe r1 = new Recipe();
//		String r2 = "Coffee";
//		r1.setName("Coffee");
//		Assertions.assertFalse(r1.equals(r2));
//	}
}

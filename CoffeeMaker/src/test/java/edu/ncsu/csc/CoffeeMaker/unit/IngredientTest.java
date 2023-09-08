package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

public class IngredientTest {
	
	@Test 
	public void testNewIngredient() {
		
		final Ingredient ice = new Ingredient("Ice", 20);
		
		Assertions.assertEquals("Ice", ice.getName());
		Assertions.assertEquals(20, ice.getAmount());
		
		ice.setAmount(15);
		
		Assertions.assertEquals(15, ice.getAmount());
		
		
	}
	
	
	@Test
	public void testNewIngredientAndEquals() {
		final Ingredient water = new Ingredient("WatEr", 20);
		final Ingredient alsoWater = new Ingredient();
		alsoWater.setIngredient("WatEr");
		alsoWater.setAmount(20);
		
		alsoWater.setId(50);
		water.setId(50);
		
		
//		Check all branches of .equals()
		Assertions.assertEquals(water, alsoWater);
		Assertions.assertEquals(water, water);
		Assertions.assertFalse(water.equals(null));
		Assertions.assertFalse(water.equals("not an ingredient object"));
		Assertions.assertEquals(water.hashCode(), alsoWater.hashCode());
		
		// Auto-generated toString
		final String expected = "Ingredient [id=50, name=WatEr, amount=20]";
		
		Assertions.assertEquals(expected, water.toString(), "toString does not match");
		
		

		
		
	}
	

}

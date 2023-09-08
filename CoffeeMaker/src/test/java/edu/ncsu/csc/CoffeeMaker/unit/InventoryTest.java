package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    private Inventory localIvt;

    /**
     * Setup adds 100 of Coffee, Milk, Chocolate, and Sugar.
     */
    @BeforeEach
    public void setup () {
    	// local inventory for testing
        localIvt = new Inventory();

        final Ingredient chocolate = new Ingredient("Chocolate", 100);
        final Ingredient milk = new Ingredient("Milk", 100);
        final Ingredient sugar = new Ingredient("Sugar", 100);
        final Ingredient coffee = new Ingredient("Coffee", 100);
        
        localIvt.addIngredient(chocolate);
        localIvt.addIngredient(milk);
        localIvt.addIngredient(sugar);
        localIvt.addIngredient(coffee);
        
//        inventoryService.save( ivt );
//        
//        /* Local (non-database) inventory class for testing */
//        localIvt = new Inventory();
//        localIvt.addIngredient(chocolate);
//        localIvt.addIngredient(milk);
//        localIvt.addIngredient(sugar);
//        localIvt.addIngredient(coffee);
    }
    
    @Test
    @Transactional
    public void testConsumeIngredients () {
        final Recipe recipe = new Recipe();
//        recipe.setName( "Delicious Not-Coffee" );
        
        localIvt.setId(20l);
        Assertions.assertEquals(20l, localIvt.getId());

        
        recipe.addIngredient("Chocolate", 10, localIvt);
        recipe.addIngredient("Milk", 20, localIvt);
        recipe.addIngredient("Sugar", 5, localIvt);
        recipe.addIngredient("Coffee", 1, localIvt);

//        recipe.setPrice( 5 );

        localIvt.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 90, localIvt.checkIngredient("Chocolate"));
        Assertions.assertEquals( 80, localIvt.checkIngredient("Milk"));
        Assertions.assertEquals( 95, localIvt.checkIngredient("Sugar"));
        Assertions.assertEquals( 99, localIvt.checkIngredient("Coffee"));
        
    }

    @Test
    @Transactional
    public void testUpdateInventory () {
    	
        try {
        	localIvt.updateInventory("Coffee", 10);
        } catch (final Exception e) {
        	Assertions.fail("this ingredient does exist, should not throw exception");
        }
        
        try {
        	localIvt.updateInventory("Coffee", -1);
        	Assertions.fail("amount can not be -1");
        } catch (final Exception e) {
        	// fine
        }
        
        try {
        	localIvt.updateInventory(null, 10);
        	Assertions.fail("name cannot be null");
        } catch (final Exception e) {
        	// fine
        }
        
        try {
        	localIvt.updateInventory("oranges", 10);
        	Assertions.fail("ingredient does not exist");
        } catch (final Exception e) {
        	// fine
        }
        
//    	ivt.updateInventory("Coffee", 110);
//    	ivt.updateInventory("Coffee", 10);
//    	ivt.updateInventory("Coffee", 10);
//    	ivt.updateInventory("Coffee", 10);
//
//
//
//        /* Save and retrieve again to update with DB */
//        inventoryService.save( ivt );
//
//        ivt = inventoryService.getInventory();
//
//        Assertions.assertEquals( 110, ivt.checkIngredient("Coffee"),
//                "Adding to the inventory should result in correctly-updated values for coffee" );
//        Assertions.assertEquals( 105, ivt.checkIngredient("Milk"),
//                "Adding to the inventory should result in correctly-updated values for milk" );
//        Assertions.assertEquals( 120, ivt.checkIngredient("Sugar"),
//                "Adding to the inventory should result in correctly-updated values sugar" );
//        Assertions.assertEquals( 101, ivt.checkIngredient("Chocolate"),
//                "Adding to the inventory should result in correctly-updated values chocolate" );
//        
        
    }

    @Test
    @Transactional
    public void testAddInventory2 () {

        try {
            localIvt.updateInventory("Chocolate", -10);
            Assertions.fail("Negative amount should throw IAE");
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 100, localIvt.checkIngredient("Coffee"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 100, localIvt.checkIngredient("Milk"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 100, localIvt.checkIngredient("Sugar"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 100, localIvt.checkIngredient("Chocolate"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate" );
        }
    }

    @Test
    @Transactional
    public void testAddInventory3 () {

        try {
        	localIvt.updateInventory("Milk", -100);
            Assertions.fail("Negative amount should throw IAE");
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 100, localIvt.checkIngredient("Coffee"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 100, localIvt.checkIngredient("Milk"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 100, localIvt.checkIngredient("Sugar"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 100, localIvt.checkIngredient("Chocolate"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate" );
        }
        
        try {
        	localIvt.updateInventory("Oat Milk", 10);
            Assertions.fail("Non-present ingredient should throw IAE but didn't");
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 100, localIvt.checkIngredient("Coffee"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 100, localIvt.checkIngredient("Milk"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 100, localIvt.checkIngredient("Sugar"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 100, localIvt.checkIngredient("Chocolate"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate" );
        }
    }



    
    /*
     * 
     * Checks if there is enough ingredient for recipe
     * 
     */
    @Test
    @Transactional
    public void testEnoughAndUseIngredient() {
    	
    	
    	final Recipe r = new Recipe();
    	
    	r.addIngredient("Coffee", 51, localIvt);
    	r.addIngredient("Milk", 3, localIvt);
    	r.addIngredient("Chocolate", 1, localIvt);
    	
    	Assertions.assertTrue(localIvt.enoughIngredients(r), "Should be enough ingredients");
    	
    	
    	Assertions.assertTrue(localIvt.useIngredients(r), "Should be able to use ingredients");
    	
    	Assertions.assertEquals(49, localIvt.checkIngredient("Coffee"));
    	Assertions.assertEquals(97, localIvt.checkIngredient("Milk"));
    	Assertions.assertEquals(99, localIvt.checkIngredient("Chocolate"));
    	

    	Assertions.assertFalse(localIvt.enoughIngredients(r), "Not enough coffee for after using 51");
    	Assertions.assertFalse(localIvt.useIngredients(r), "Not enough coffee for after using 51");
    	
    	/* Check unchanged inventory after unsuccessful add */
    	Assertions.assertEquals(49, localIvt.checkIngredient("Coffee"));
    	Assertions.assertEquals(97, localIvt.checkIngredient("Milk"));
    	Assertions.assertEquals(99, localIvt.checkIngredient("Chocolate"));
    	
    }
    

//    Don't know what will auto-generate
//    /**
//     * 
//     * Checks toString.
//     * 
//     * @author leonard
//     */
//    @Test
//    @Transactional
//    public void testToString() {
//        Inventory invt = new Inventory(10, 10, 10, 10);
//        
//
//        String expected = "Coffee: 10\nMilk: 10\nSugar: 10\nChocolate: 10\n";
//        
//        Assertions.assertEquals(expected, invt.toString());
//        
//        
//    }

}

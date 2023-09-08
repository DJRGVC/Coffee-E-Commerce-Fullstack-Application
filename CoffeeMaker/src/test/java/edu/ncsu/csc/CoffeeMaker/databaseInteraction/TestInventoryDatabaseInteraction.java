package edu.ncsu.csc.CoffeeMaker.databaseInteraction;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class TestInventoryDatabaseInteraction {

    @Autowired
    private InventoryService service;

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateInventory() {

        final Inventory ivt = new Inventory();

        // make some new ingredients, chocolate, sugar, milk
        final Ingredient chocolate = new Ingredient( "Chocolate", 100 );
        final Ingredient milk = new Ingredient( "Milk", 100 );
        final Ingredient sugar = new Ingredient( "Sugar", 100 );

        // add them to the inventory
        ivt.addIngredient( chocolate );
        ivt.addIngredient( milk );
        ivt.addIngredient( sugar );

        // save the inventory
        service.save( ivt );

        Assertions.assertEquals( 1, service.count(), "Creating one inventory should result in one inventory in the database");

        // now, create a new inventory and save

        final Inventory ivt2 = new Inventory();

        // make some new ingredients, matcha, beans, water
        final Ingredient matcha = new Ingredient( "Matcha", 100 );
        final Ingredient beans = new Ingredient( "Beans", 100 );
        final Ingredient water = new Ingredient( "Water", 100 );

        // add them to the inventory
        ivt2.addIngredient( matcha );
        ivt2.addIngredient( beans );
        ivt2.addIngredient( water );

        // save the inventory
        service.save( ivt2 );

        // make sure that there is still only ONE inventory, and that it is the most recent one
        Assertions.assertEquals( 1, service.count(), "Creating a second inventory should not result in a second inventory in the database" );
        Assertions.assertEquals( ivt2.getIngredients(), service.getInventory().getIngredients());
    }

    // new test for testing if we can add ingredients to the inventory
    @Test
    @Transactional
    public void testAddIngredients() {

        final Inventory ivt = new Inventory();

        // make some new ingredients, chocolate, sugar, milk
        final Ingredient chocolate = new Ingredient( "Chocolate", 100 );
        final Ingredient milk = new Ingredient( "Milk", 100 );
        final Ingredient sugar = new Ingredient( "Sugar", 100 );

        // add them to the inventory
        ivt.addIngredient( chocolate );
        ivt.addIngredient( milk );
        ivt.addIngredient( sugar );

        // save the inventory
        service.save( ivt );

        // check that the inventory has the ingredients
        Assertions.assertEquals( 3, service.getInventory().getIngredients().size(), "Inventory should have 3 ingredients" );


        // now, get inventory and add more ingredients
        final Inventory ivt2 = service.getInventory();

        // make some new ingredients, matcha, beans, water
        final Ingredient matcha = new Ingredient( "Matcha", 100 );
        final Ingredient beans = new Ingredient( "Beans", 100 );
        final Ingredient water = new Ingredient( "Water", 100 );

        // add them to the inventory
        ivt2.addIngredient( matcha );
        ivt2.addIngredient( beans );
        ivt2.addIngredient( water );

        // save the inventory
        service.save( ivt2 );

        // make sure that there is still only ONE inventory, and that it is the most recent one
        Assertions.assertEquals( 6, service.getInventory().getIngredients().size(), "Inventory should have 6 ingredients" );
    }


    // new test for testing if we can remove ingredients from the inventory
    @Test
    @Transactional
    public void testRemoveIngredients() {

        final Inventory ivt = new Inventory();

        // make some new ingredients, chocolate, sugar, milk
        final Ingredient chocolate = new Ingredient( "Chocolate", 100 );
        final Ingredient milk = new Ingredient( "Milk", 100 );
        final Ingredient sugar = new Ingredient( "Sugar", 100 );

        // add them to the inventory
        ivt.addIngredient( chocolate );
        ivt.addIngredient( milk );
        ivt.addIngredient( sugar );

        // save the inventory
        service.save( ivt );

        // check that the inventory has the ingredients
        Assertions.assertEquals( 3, service.getInventory().getIngredients().size(), "Inventory should have 3 ingredients" );

        // now, save the inventory again, but with one less ingredient
        final Inventory ivt2 = new Inventory();
        
        // remove chocolate
        ivt2.addIngredient( milk );
        ivt2.addIngredient( sugar );

        // save the inventory
        service.save( ivt2 );

        // make sure that there is still only ONE inventory, and that it is the most recent one, with one less ingredient
        Assertions.assertEquals( 2, service.getInventory().getIngredients().size(), "Inventory should have 2 ingredients" );
    }
}

package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.repositories.InventoryRepository;

/**
 * The InventoryService is used to handle CRUD operations on the Inventory
 * model. In addition to all functionality in `Service`, we also manage the
 * Inventory singleton.
 *
 * @author Kai Presler-Marshall
 *
 */
@Component
@Transactional
public class InventoryService extends Service<Inventory, Long> {

    /**
     * InventoryRepository, to be autowired in by Spring and provide CRUD
     * operations on Inventory model.
     */
    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * Returns the appropriate JpaRepository for this service.
     */
    @Override
    protected JpaRepository<Inventory, Long> getRepository () {
        return inventoryRepository;
    }

    /**
     * Retrieves the singleton Inventory instance from the database, creating it
     * if it does not exist.
     *
     * @return the Inventory, either new or fetched
     */
    public synchronized Inventory getInventory () {
        final List<Inventory> inventoryList = findAll();
        if ( inventoryList != null && inventoryList.size() == 1 ) {
            return inventoryList.get( 0 );
        }
        else {
            // initialize the inventory with 0 of everything
            final Inventory i = new Inventory();
            save( i );
            return i;
        }
    }
    
    /**
     * Overrides the save() method to only save one inventory and override the current
     * inventory when saving.
     *
     * @param inventory the inventory to be saved
     */
    @Override
    public synchronized void save(Inventory inventory) {
        final List<Inventory> inventoryList = findAll();
        if (inventoryList != null && inventoryList.size() == 1) {
            final Inventory existingInventory = inventoryList.get(0);
            existingInventory.updateIngredientList(inventory);
            super.save(existingInventory);
        } else {
            super.save(inventory);
        }
    }


}

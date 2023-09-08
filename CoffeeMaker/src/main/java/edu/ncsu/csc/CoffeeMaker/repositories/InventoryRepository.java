package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Inventory;

/**
 * InventoryRepository is used to provide CRUD operations for the Inventory
 * model. Spring will generate appropriate code with JPA.
 *
 * @author Kai Presler-Marshall
 *
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}

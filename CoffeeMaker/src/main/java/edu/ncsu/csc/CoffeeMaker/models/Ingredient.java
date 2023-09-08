package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * Ingredient for the coffee maker. Ingredient is tied to the database using
 */
@Entity
public class Ingredient extends DomainObject {

	/**
	 * id for ingredient entry
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * name of the ingredient
	 */
	private String name;

	/**
	 * amount of the ingredient
	 */
	@Min(0)
	private int amount;

	/**
	 * Empty constructor for Hibernate
	 */
	public Ingredient() {
	}

	/**
	 * Constructor for ingredient
	 * @param ingredient name of the ingredient
	 * @param amount amount of the ingredient
	 */
	public Ingredient(final String ingredient, final int amount) {
		super();
		this.name = ingredient;
		this.amount = amount;
	}

	/**
	 * Returns the ID of the entry in the DB
	 * @return long
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the ID of the Inventory (Used by Hibernate)
	 * @return id the ID
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * set id 
	 * @param id the id
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * set ingredient
	 * @param name the ingredient's name
	 */
	public void setIngredient(final String name) {
		this.name = name;
	}

	/**
	 * set amount
	 * @param amount the amount
	 */
	public void setAmount(final int amount) {
		this.amount = amount;
	}

	/**
	 * to get id 
	 * @return the id
	 */
	@Override
	public Serializable getId() {
		return id;
	}

	/**
	 * hashcode function
	 * @return the hashcode
	 */
	@Override
	public int hashCode() {
		return Objects.hash(amount, id, name);
	}

	/**
	 * equals method
	 * @param obj the object
	 * @return true if equal, false if not
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Ingredient other = (Ingredient) obj;
		return amount == other.amount && Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

	/**
	 * to string method
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Ingredient [id=" + id + ", name=" + name + ", amount=" + amount + "]";
	}

}

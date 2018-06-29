package com.seeu.common;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by thomasfouan on 15/05/2018.
 *
 * Abstract class that provide template for all entities in the application.
 * An entity has a unique id, and a common key for temporary storage (shared preferences, intent).
 */
@Getter
@Setter
public abstract class Entity implements Serializable {

	protected Long id;

	@Override
	public String toString() {
		return "Entity " + id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Entity entity = (Entity) o;
		return id.equals(entity.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}

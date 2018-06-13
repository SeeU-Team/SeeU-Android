package com.seeu.common;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Entity implements Serializable {

	/**
	 * Key used when the entity is passed in an intent or store in the sharedPreferences.
	 */
	public static String STORAGE_KEY;

	protected long id;

	protected Entity(String keyName) {
		STORAGE_KEY = keyName;
	}

	@Override
	public String toString() {
		return "Entity " + id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Entity entity = (Entity) o;
		return id == entity.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}

package com.seeu.common;

import java.io.Serializable;
import java.util.Objects;

public abstract class Entity implements Serializable {

	public static String INTENT_EXTRA_KEY;

	protected long id;

	protected Entity(String keyName) {
		INTENT_EXTRA_KEY = keyName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

package com.seeu.team;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by thomasfouan on 07/05/2018.
 *
 * Entity that represents a team's description.
 * A description can be something that the team is or has (food, swimming pool, sports, etc.).
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Asset implements Serializable {

	private Long id;
	private String name;
	private String imageLight;
	private String imageDark;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Asset asset = (Asset) o;
		return Objects.equals(id, asset.id);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id);
	}
}

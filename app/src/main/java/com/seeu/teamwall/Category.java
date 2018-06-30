package com.seeu.teamwall;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by thomasfouan on 30/04/2018.
 *
 * Entity that represents the team category.
 * It can be Barbecue, Indoor, Outdoor, Gaming, Dancing, etc.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {

	private Long id;
	private String name;

	@Override
	public String toString() {
		return "Category " + name;
	}
}

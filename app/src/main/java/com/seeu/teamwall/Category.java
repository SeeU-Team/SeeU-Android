package com.seeu.teamwall;

import com.seeu.common.Entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by thomasfouan on 30/04/2018.
 *
 * Entity that represents the team category.
 * It can be Barbecue, Indoor, Outdoor, Gaming, Dancing, etc.
 */
@Getter
@Setter
public class Category extends Entity {

	private String name;

	public Category() {
	}

	@Override
	public String toString() {
		return "Category " + name;
	}
}

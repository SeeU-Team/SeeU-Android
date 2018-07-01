package com.seeu.team.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by thomasfouan on 01/07/2018.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Merge {

	private Long idMerge;
	private Long idFirst;
	private Long idSecond;
}

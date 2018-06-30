package com.seeu.team.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by thomasfouan on 29/06/2018.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Like {

	private Long idTeamUp;
	private Long idLike;
	private Long idLiked;
}

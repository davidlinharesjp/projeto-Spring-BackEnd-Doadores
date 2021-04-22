package com.davidlinhares.medicalcheck.entity.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.davidlinhares.medicalcheck.entity.Donor;
import com.davidlinhares.medicalcheck.entity.enumeration.StateEnum;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class StateDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	@Getter	@Setter	private Long total;
	@Getter	@Setter	private List<Donor> donors = new ArrayList<>();
	private String state;

	public StateDTO(Long total, List<Donor> donors, StateEnum state) {
		super();
		this.total = total;
		this.donors = donors;
		setState(state);
	}

	public StateEnum getState() {
		return state != null ? StateEnum.fromString(state) : null;
	}

	public void setState(StateEnum state) {
		this.state = state != null ? state.getValeu() : null;
	}

}

package com.davidlinhares.medicalcheck.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.CalendarDeserializer;
import com.fasterxml.jackson.databind.ser.std.CalendarSerializer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString(exclude = "id")
@NoArgsConstructor
@JsonDeserialize
@EqualsAndHashCode
@Valid
public class Donor implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, updatable = false, unique = true)
	@Getter
	private UUID id;
	@Getter
	@Setter
	private String nome;
	@Getter
	@Setter
	@Column(unique = true)
	@CPF(message = "Cpf Invalido")
	private String cpf;
	@Getter
	@Setter
	private String rg;
	@JsonDeserialize(using = CalendarDeserializer.class)
	@JsonSerialize(using = CalendarSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@Getter
	@Setter
	private Calendar data_nasc;
	@Getter
	@Setter
	private String mae;
	@Getter
	@Setter
	private String pai;
	@Getter
	@Setter
	private String email;
	@Getter
	@Setter
	private String cep;
	@Getter
	@Setter
	private String endereco;
	@Getter
	@Setter
	private Long numero;
	@Getter
	@Setter
	private String bairro;
	@Getter
	@Setter
	private String cidade;
	@Getter
	@Setter
	private String telefone_fixo;
	@Getter
	@Setter
	private String celular;
	@Getter
	@Setter
	@NotNull(message = "Altura não pode estar em branco")
	private Double altura;
	@Getter
	@Setter
	@NotNull(message = "Peso esta em brando")
	private Double peso;
	@Getter
	@Setter
	@NotNull(message = "Tipo senguineo esta em branco")
	private String tipo_sanguineo;
	@Getter
	@Setter
	@NotNull(message = "Sexo não pode estar em Branco")
	private String sexo;
	@Getter
	@Setter
	@NotNull(message = "Estado residente se encontra em branco")
	private String estado;
	@Column(insertable = true, updatable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date lastUpdate;

	@PreUpdate
	public void onUpdate() {
		this.lastUpdate = new Date();
	}

	@PrePersist
	public void onInsert() {
		this.lastUpdate = new Date();
	}

	@JsonIgnore
	public Boolean isEligibleForDonation() {
		Integer idade = getAge();
		return idade != null && idade > 16 && idade < 69 && this.peso > 50.0 && !isObese() ? Boolean.TRUE
				: Boolean.FALSE;

	}

	public Integer getAge() {
		Calendar today = Calendar.getInstance();
		return this.data_nasc != null ? today.get(Calendar.YEAR) - this.data_nasc.get(Calendar.YEAR) : null;

	}

	public Double getIMC() {
		if (this.peso != null && this.altura != null) {
			BigDecimal bigPeso = new BigDecimal(this.peso.toString());
			BigDecimal bigAltura = new BigDecimal(this.altura.toString());
			bigAltura = bigAltura.pow(2);
			return bigPeso.divide(bigAltura, 2, RoundingMode.HALF_UP).doubleValue();
		}
		return null;
	}

	@JsonIgnore
	public Boolean isObese() {
		Double imc = getIMC();
		return imc != null && imc > 30.0 ? Boolean.TRUE : Boolean.FALSE;
	}

}

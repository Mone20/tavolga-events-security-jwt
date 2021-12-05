package com.tavolgaevents.backend.models;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(	name = "users", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email") 
		})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(Views.Public.class)
	private Long id;

	@NotBlank
	@Size(max = 20)
	@JsonView(Views.Public.class)
	private String username;

	@NotBlank
	@JsonView(Views.Public.class)
	private String firstName;

	@NotBlank
	@JsonView(Views.Public.class)
	private String lastName;

	@NotBlank
	@JsonView(Views.Public.class)
	private String middleName;

	@JsonView(Views.Public.class)
	public String phone;

	@OneToOne
	File avatar;

	@NotBlank
	@Size(max = 50)
	@Email
	@JsonView(Views.Public.class)
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonView(Views.Public.class)
	private Role role;

	@JsonView(Views.Private.class)
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Nomination> nominations;

	@JsonView(Views.Private.class)
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Nomination> rateNominations;

	@JsonView(Views.Private.class)
	@OneToMany(fetch = FetchType.LAZY)
	private List<Assessment> participantAssessment;

	@JsonView(Views.Private.class)
	@OneToMany(mappedBy = "assessor",fetch = FetchType.LAZY)
	private List<Assessment> assessorAssessments;



	public User() {
	}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}


	public List<Nomination> getRateNominations() {
		return rateNominations;
	}

	public void setRateNominations(List<Nomination> rateNominations) {
		this.rateNominations = rateNominations;
	}

	public List<Assessment> getParticipantAssessment() {
		return participantAssessment;
	}

	public void setParticipantAssessment(List<Assessment> participantAssessment) {
		this.participantAssessment = participantAssessment;
	}

	public List<Assessment> getAssessorAssessments() {
		return assessorAssessments;
	}

	public void setAssessorAssessments(List<Assessment> assessorAssessments) {
		this.assessorAssessments = assessorAssessments;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public File getAvatar() {
		return avatar;
	}

	public void setAvatar(File file) {
		this.avatar = file;
	}

	public List<Nomination> getNominations() {
		return nominations;
	}

	public void setNominations(List<Nomination> nominations) {
		this.nominations = nominations;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String fullName) {
		this.firstName = fullName;
	}

	public Role getRole() {
		return role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public void setRole(Role role) {
		this.role = role;
	}
}
